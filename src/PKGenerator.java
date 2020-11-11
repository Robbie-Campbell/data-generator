/*
Name:Robbie Campbell
Date:09/11/2020
Description: This class generates primary keys for SQL insert statements and saves to a database.
The purpose of this class is to make readable primary keys for sql, it allows the user to pass in a string which
contains the first characters of the PK and then it will return a number this.index afterwards as the return value.

It then saves into a table (with a name of the users choosing) so it can viewed later.

This will then be saved into a readable database which can show the user all of the primary keys made so they can be
referenced in other data generator instances.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PKGenerator
{
    private String id;
    private int index;
    public ArrayList<String> values;
    public int value;
    public String title;

    PKGenerator()
    {
        // Define Vars
        this.id = "";
        this.index = 1;
    }

    public void createTables()
    {
        // Create a name for the table
        System.out.println("Please create your table name using This_Naming_Convention\n\n");
        System.out.println("Name your table: ");
        Scanner columnTitle = new Scanner(System.in);
        this.title = columnTitle.nextLine();
    }

    public void setValueMax()
    {
        // Ask the how many keys they want
        System.out.println("How many keys do you want to make?: ");
        Scanner howManyKeys = new Scanner(System.in);
        value = howManyKeys.nextInt();
    }

    // User key function
    private String generateKeys()
    {
        
        // Conditions to make the length of the PK consistent
        if (this.index < 10)
            return this.id + "000" + this.index;
        else if (this.index < 100)
            return this.id + "00" + this.index;
        else if (this.index < 1000)
            return this.id + "0" + this.index;
        else
            return this.id + this.index;
    }

    // Create all primary keys values
    public void createAllKeys()
    {
        String[] pkLetters = title.split("_");
        if (pkLetters.length > 1)
            this.id = String.valueOf(pkLetters[0].charAt(0)) + pkLetters[1].charAt(0);
        else
            this.id = String.valueOf(pkLetters[0].charAt(0));

        this.values = new ArrayList<>();

        // Create the user Keys
        for (; this.index < value + 1; this.index++) {
            this.values.add(this.generateKeys());
        }
    }

    // Retrieve all of the ID's from a table specified by user input
    public ArrayList<String> getAllIds ()
    {

        // Select table to retrieve data from
        System.out.println("Which table would you like to retrieve data from?: ");
        Scanner getRequest = new Scanner(System.in);
        String selectFrom = getRequest.nextLine();
        ArrayList<String> keylist = new ArrayList<>();

        // Add all of the keys from a table into an array list
        try {
            String username = "root";
            String pass = "";
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", username, pass);
            PreparedStatement stat = conn.prepareStatement(String.format("SELECT keyList FROM %s", selectFrom));
            stat.executeQuery();
            ResultSet rs = stat.getResultSet();
            while (rs.next()) {
                keylist.add(rs.getString("keyList"));
            }
            conn.close();

        } catch (SQLException throwables) {
            System.out.println("Table does not exist");
        }
        return keylist;
    }

}