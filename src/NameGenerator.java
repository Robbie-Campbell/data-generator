/*
Generates a random first name, last name and email for input into a table
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class NameGenerator {
    private ArrayList<String> email;
    private final String[] firstNameList;
    private final String[] lastNameList;
    private final String username = "root";
    private final String pass = "";
    private String firstName;
    private String lastName;
    private PKGenerator makeTable;
    public LinkedHashMap<String, String> names;

    // An array of names for first and last
    public NameGenerator()
    {
        this.firstNameList = new String[]{"Marco", "John", "Martin", "Melvin", "Alvin", "Benjamin", "Craig", "Daniel", "Eggbert",
                "Farquart", "Graham", "Hewey", "Inyego", "Jake", "Kevin", "Lebron", "Mickey", "Nicholas", "Oscar", "Peter",
                "Quincey", "Raymond", "Stephen", "Terry", "Ulysses", "Vincent", "Winston", "Yolanda", "Anabelle", "Brie",
                "Chrissy", "Danielle", "Evelyne", "Farah", "Gene", "Holly", "India", "Janine", "Kelly", "Linda", "Molly",
                "Nina", "Priscilla", "Gael", "Ludwig", "Zagreus", "Cameroono", "Natalie"};

        this.lastNameList = new  String[]{"Arnold", "Brigham", "Campbell", "Donaldson", "Eggleton", "Farish", "Granada", "Hilbert",
                "Ilverez", "Johnson", "Kepler", "Linson", "Montana", "Nishelson", "Organson", "Peterson", "Raymondson",
                "Stephenson", "Teirson", "Vienna", "Waterson", "Baggins", "ofRivia", "Wynne"};
        this.email = new ArrayList<>();
        makeTable = new PKGenerator();
        this.createTable(makeTable.title);
        this.generateNames();
        this.insertIds(makeTable.title);
    }

    // Assigns random names to each person
    public void generateNames()
    {
        names = new LinkedHashMap<>();
        for (int i = 0; i <= makeTable.value + 1; i++) {
            this.firstName = firstNameList[(int) (Math.random() * firstNameList.length)];
            this.lastName = lastNameList[(int) (Math.random() * lastNameList.length)];
            this.names.put(this.firstName, this.lastName);
        }
    }

    // Creates the user table defined by their name
    public void createTable (String tableName)
    {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", username, pass);
            String createTable = String.format("CREATE TABLE %s (keyList VARCHAR(7) PRIMARY KEY," +
                    "first_name VARCHAR(20) NOT NULL," +
                    "last_name VARCHAR(20) NOT NULL," +
                    "email VARCHAR(50) NOT NULL)", tableName);
            PreparedStatement stat = conn.prepareStatement(createTable);
            stat.executeUpdate(createTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Insert values into a table names after users wish
    public void insertIds (String tableName)
    {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", username, pass);
            for (int i = 0; i  + 1 < makeTable.value; i++)
            {
                PreparedStatement stat = conn.prepareStatement(String.format("INSERT INTO %s VALUES(?, ?, ?, ?)", tableName));
                stat.setString(1, makeTable.values.get(i));
                String firstName = (String) this.names.keySet().toArray()[i];
                String lastName = (String) this.names.values().toArray()[i];
                stat.setString(2, firstName);
                stat.setString(3, lastName);
                stat.setString(4, firstName + lastName + "@" + "email.com");
                stat.executeUpdate();
            }
            System.out.println("Successfully Created Table!");
            conn.close();

        } catch (SQLException throwables) {
            System.out.println("Great Success!");
        }
    }

    // Testing
    public static void main(String[] args)
    {
//        NameGenerator start = new NameGenerator();
        PKGenerator ids = new PKGenerator();
        for (String id: ids.getAllIds("full_time"))
            System.out.println(id);
    }

}
