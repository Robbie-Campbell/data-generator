/*
I realise now i am trying to achieve too many things at once, this class will
just create statements, without trying to link to a mysql db, that can come later.
 */

import java.util.ArrayList;
import java.util.Arrays;

public class NoDB {

    private final String[] firstNameList, lastNameList, addressLine1, roadTitle, addressLine2;
    private final String PKName;
    private int index = 0;
    private ArrayList<String> values;

    public NoDB() {

        // Create 2 arrays -- one for first names and one for last names
        this.firstNameList = new String[]{"Marco", "John", "Martin", "Melvin", "Alvin", "Benjamin", "Craig", "Daniel", "Eggbert",
                "Farquart", "Graham", "Hewey", "Inyego", "Jake", "Kevin", "Lebron", "Mickey", "Nicholas", "Oscar", "Peter",
                "Quincey", "Raymond", "Stephen", "Terry", "Ulysses", "Vincent", "Winston", "Yolanda", "Anabelle", "Brie",
                "Chrissy", "Danielle", "Evelyne", "Farah", "Gene", "Holly", "India", "Janine", "Kelly", "Linda", "Molly",
                "Nina", "Priscilla", "Gael", "Ludwig", "Zagreus", "Cameroono", "Natalie"};

        this.lastNameList = new String[]{"Arnold", "Brigham", "Campbell", "Donaldson", "Eggleton", "Farish", "Granada", "Hilbert",
                "Ilverez", "Johnson", "Kepler", "Linson", "Montana", "Nishelson", "Organson", "Peterson", "Raymondson",
                "Stephenson", "Teirson", "Vienna", "Waterson", "Baggins", "ofRivia", "Wynne"};

        // Create address arrays
        this.addressLine1 = new String[]{"Water", "Cribbage", "Richmond", "Stavingo", "Bolinko", "Crischenko", "Hearty",
                "Gastavern", "Fishbrook", "Lincoln", "Ringston", "Crippsortion", "Margo", "Lorem", "Ipsum", "David"};

        this.roadTitle = new String[]{"Provost", "Drive", "Close", "Road", "Avenue", "Street", "Gardens", "Grove", "Lane"};

        this.addressLine2 = new String[]{"Dorset", "Somerset", "Heartfordshire", "Essex"};

        // Create a sample variable
        this.PKName = "Academic_Staff";
    }

    // Create a random email
    public String generateEmail(String first, String last, String id)
    {
        return first + last + id + "@email.com";
    }

    // Assign a random contact number
    public String generateContactNo()
    {
        return "07" + (int) (100000000 + Math.random() * 100000000);
    }

    public String generateDOB()
    {
        return  (int) (1920 + Math.random() * 85) + "-" + (int) (1 + Math.random() * 12) + "-" +
                (int) (1 + Math.random() * 28);
    }

    // User key function
    private String generateKey()
    {
        // Conditions to make the length of the PK consistent
        if (this.index < 10)
            return this.getIDName() + "0000" + this.index;
        else if (this.index < 100)
            return this.getIDName() + "000" + this.index;
        else if (this.index < 1000)
            return this.getIDName() + "00" + this.index;
        else if (this.index < 10000)
            return this.getIDName() + "0" + this.index;
        else
            return this.getIDName() + this.index;
    }

    // Create all primary keys values
    public String getIDName()
    {
        String[] pkLetters = PKName.split("_");
        String id;
        if (pkLetters.length > 1)
            id = String.valueOf(pkLetters[0].charAt(0)) + pkLetters[1].charAt(0);
        else {
            id = String.valueOf(pkLetters[0].charAt(0));
        }

        return id;
    }

    // Generate a random first name
    public String getFirstName()
    {
        return firstNameList[(int) (Math.random() * firstNameList.length)];
    }

    // Makes a random address
    public String generateAddress()
    {
        String address1 = addressLine1[(int) (Math.random() * addressLine1.length)] + " "
                + roadTitle[(int) (Math.random() * roadTitle.length)];
        String address2 = addressLine2[(int) (Math.random() * addressLine2.length)];
        String postcode = Character.toString(address1.charAt(0)) + address1.charAt(1) + (int) (1 + Math.random() * 28) + " " +
                (int) (1 + Math.random() * 28) + address1.charAt(2) + address1.charAt(3);
        return String.format("'%s', '%s', '%s'", address1, address2, postcode.toUpperCase());
    }

    // Generate a random last name
    public String getLastName()
    {
        return lastNameList[(int) (Math.random() * lastNameList.length)];
    }

    // Creates a table with listed parameters
    public String createTable(String tableName, Boolean name, Boolean personalInfo)
    {
        StringBuilder statement = new StringBuilder(String.format("CREATE TABLE %s (%s", tableName.toLowerCase(),
                tableName.toLowerCase() + "_id VARCHAR(7) PRIMARY KEY NOT NULL,\n"));
        if (name)
        {
            statement.append("first_name VARCHAR(20) NOT NULL, last_name VARCHAR(20) NOT NULL, email VARCHAR(50) NOT NULL,\n");
        }
        if (personalInfo)
        {
            statement.append("contact_no VARCHAR(11) NOT NULL, DOB DATE NOT NULL, address_line_1 VARCHAR(50) NOT NULL, \n" +
                    "address_line_2 VARCHAR(50) NOT NULL, postcode VARCHAR(9) NOT NULL");
        }
        statement.append(");");
        return statement.toString();
    }

    // Execute the program, pass in params to decide which parts of the statement you want to include
    public void insertStatements(String tableTitle, Boolean name, Boolean personalInfo)
    {
        System.out.println(this.createTable(tableTitle, name, personalInfo));
        for (int i = 0; i < 5; i++)
        {
            this.index++;
            String firstName = this.getFirstName();
            String lastName = this.getLastName();
            String email = generateEmail(firstName, lastName, this.generateKey());
            String insertStatement = String.format("INSERT INTO %s VALUES ('%s'", tableTitle,
                    this.generateKey());
            if (name)
            {
                insertStatement += String.format(", '%s', '%s', '%s'", firstName, lastName, email);
            }
            if (personalInfo)
            {
                insertStatement += String.format(",'%s', DATE '%s', %s", this.generateContactNo(),
                        this.generateDOB(), this.generateAddress());
            }
            insertStatement += ");";
            System.out.println(insertStatement);
        }
    }

    public static void main(String[] args)
    {
        NoDB start = new NoDB();
        start.insertStatements("Student", false, true);
    }
}
