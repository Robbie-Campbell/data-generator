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
    private final String username = "root";
    private final String pass = "";
    private ArrayList<String> values;

    PKGenerator()
    {
        // Define Vars
        this.id = "";
        this.index = 1;

        //
        System.out.println("Do you need to make keys (y/n): ");
        Scanner keyMaker = new Scanner(System.in);
        String answer = keyMaker.nextLine();


        if (answer.equals("y")) {
            // Ask the how many keys they want
            System.out.println("How many keys do you want to make?: ");
            Scanner howManyKeys = new Scanner(System.in);
            int value = howManyKeys.nextInt();

            // Create a name for the table
            System.out.println("Please create your table name using This_Naming_Convention\n\n");
            System.out.println("Name your primary key column: ");
            Scanner columnTitle = new Scanner(System.in);
            String title = columnTitle.nextLine();

            // Create primary keys
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
            this.createTable(title);
            this.insertIds(title, this.values);
        }
        else
        {
            this.getAllIds();
        }
    }

    // User key function
    public String generateKeys()
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

    // Creates the user table defined by their name
    public void createTable (String tableName)
    {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", username, pass);
            String createTable = String.format("CREATE TABLE %s (keyList VARCHAR(7) PRIMARY KEY)", tableName);
            PreparedStatement stat = conn.prepareStatement(createTable);
            stat.executeUpdate(createTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Insert values into a table names after users wish
    public void insertIds (String tableName, ArrayList<String> ids)
    {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", username, pass);
            for (String item: ids)
            {
                PreparedStatement stat = conn.prepareStatement(String.format("INSERT INTO %s VALUES(?)", tableName));
                stat.setString(1, item);
                stat.executeUpdate();
            }
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Retrieve all of the ID's specified by user input
    public void getAllIds ()
    {
        System.out.println("Which table do you want to retrieve id's from?: ");
        Scanner getIds = new Scanner(System.in);
        String value = getIds.nextLine();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", username, pass);
            PreparedStatement stat = conn.prepareStatement(String.format("SELECT * FROM %s", value));
            stat.executeQuery();
            ResultSet rs = stat.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getString("keyList"));
            }
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
    // Testing
    public static void main(String[] args)
    {
        PKGenerator start = new PKGenerator();
    }

}