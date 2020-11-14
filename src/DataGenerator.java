/*
I realise now i am trying to achieve too many things at once, this class will
just create statements, without trying to link to a mysql db, that can come later.
 */

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class DataGenerator {

    private final String[] firstNameList, lastNameList, addressLine1, roadTitle, addressLine2;
    private String PKName;
    private int index = 0;
    private ArrayList<String> values;

    public DataGenerator() {

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
        String[] pkLetters = this.PKName.split("_");
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

    // Create monetary values for tables
    public String generateMoneyValue(int minValue, int maxValue)
    {
        DecimalFormat formatToMoney = new DecimalFormat("0.00");
        double randomValue = minValue + Math.random() * maxValue;
        return formatToMoney.format(randomValue);
    }

    // Generate a random last name
    public String getLastName()
    {
        return lastNameList[(int) (Math.random() * lastNameList.length)];
    }

    // Creates a table with listed parameters
    public String createTable(String tableName, boolean pk, String[] fk, Boolean name, Boolean personalInfo, ExtraColumns[] moreValues)
    {
        StringBuilder statement = new StringBuilder(String.format("CREATE TABLE %s (", tableName.toLowerCase()));
        if (pk)
        {
            statement.append("\n").append(tableName.toLowerCase()).append("_id VARCHAR(7) PRIMARY KEY NOT NULL,\n");
        }
        if (fk.length > 0)
        {
            for (String key: fk)
                statement.append(key).append("_id VARCHAR(7) NOT NULL,\n");
        }
        if (name)
        {
            statement.append("first_name VARCHAR(20) NOT NULL,\nlast_name VARCHAR(20) NOT NULL,\nemail VARCHAR(50) NOT NULL,\n");
        }
        if (personalInfo)
        {
            statement.append("contact_no VARCHAR(11) NOT NULL,\nDOB DATE NOT NULL,\naddress_line_1 VARCHAR(50) NOT NULL,\n" +
                    "address_line_2 VARCHAR(50) NOT NULL,\npostcode VARCHAR(9) NOT NULL,\n");
        }
        if (moreValues.length > 0)
        {
            for (ExtraColumns value: moreValues)
            {
                statement.append(String.format("%s %s(%s) %s,\n", value.getColumnName(), value.getDataType(), value.getDataSize(), value.getNullValue()));
            }
        }
        if (fk.length > 0)
        {
            for (int i = 0; i < fk.length; ++i) {
                System.out.println(fk[i]);
                if (i != fk.length - 1) {
                    statement.append(String.format("FOREIGN KEY (%s_id) references %s(%s_id),\n", fk[i], fk[i], fk[i]));
                }
                else{
                    statement.append(String.format("FOREIGN KEY (%s_id) references %s(%s_id)", fk[i], fk[i], fk[i]));
                }
            }
        }
        statement.setLength(statement.length() - 2);
        statement.append(");");
        return statement.toString();
    }

    // Execute the program, pass in params to decide which parts of the statement you want to include, the default FK
    // value should be an empty string array thus returning no fks
    public void insertStatements(String tableTitle, boolean pk, String[] fk, Boolean nameAndEmail, Boolean personalInfo, ExtraColumns[] moreValues)
    {
        this.createTable(tableTitle);
        this.PKName = tableTitle;
        int numberOfRows = 3;
        System.out.println(this.createTable(tableTitle, pk, fk, nameAndEmail, personalInfo, moreValues));
        for (int i = 0; i < numberOfRows; i++)
        {
            this.index++;
            String id = this.generateKey();
            String firstName = this.getFirstName();
            String lastName = this.getLastName();
            String email = generateEmail(firstName, lastName, this.generateKey());
            StringBuilder insertStatement = new StringBuilder(String.format("INSERT INTO %s VALUES (", tableTitle));
            if (pk) {
                insertStatement.append("'id',");
            }
            if (fk.length > 0)
            {
                for (String key: fk)
                    insertStatement.append("'").append(this.getAllFKIDs(key).get(i)).append("',");
            }
            if (nameAndEmail)
            {
                insertStatement.append(String.format("'%s', '%s', '%s', ", firstName, lastName, email));
            }
            if (personalInfo)
            {
                insertStatement.append(String.format("'%s', DATE '%s', %s, ", this.generateContactNo(),
                        this.generateDOB(), this.generateAddress()));
            }
            if (moreValues.length > 0)
            {
                for (ExtraColumns value: moreValues)
                {
                    switch (value.getDataType()) {
                        case "VARCHAR":
                            insertStatement.append(String.format("'%s', ", value.getInsertDataType()));
                            break;
                        case "INT":
                        case "DECIMAL":
                            insertStatement.append(value.getInsertDataType()).append(", ");
                            break;
                        case "DATE":
                            insertStatement.append(String.format("DATE '%s, ", value.getInsertDataType()));
                            break;
                    }
                }
            }
            insertStatement.setLength(insertStatement.length() - 2);
            insertStatement.append(");");
            System.out.println(insertStatement);
            if (pk) {
                commitPKToDB(tableTitle, id);
            }
        }
        System.out.println("\n");
    }

    // Creates the table for all of the primary keys of this type.
    public void createTable (String tableName) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", "root", "");
            String createTable = String.format("CREATE TABLE %s (keyList VARCHAR(7) PRIMARY KEY);", tableName);
            PreparedStatement stat = conn.prepareStatement(createTable);
            stat.executeUpdate(createTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // This saves all of the primary keys generated in the create table function so they can be referenced as foreign keys
    // in other tables
    public void commitPKToDB(String tableName, String primaryKey)
    {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", "root", "");
            PreparedStatement stat = conn.prepareStatement(String.format("INSERT INTO %s VALUES(?)", tableName));
            stat.setString(1, primaryKey);
            stat.executeUpdate();
            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Retrieves all of the primary keys from a given table, this streamlines the production of foreign keys
    public ArrayList<String> getAllFKIDs(String tableName)
    {
        ArrayList<String> allIds = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", "root", "");
            PreparedStatement stat = conn.prepareStatement(String.format("SELECT * FROM %s", tableName));
            stat.executeQuery();
            ResultSet rs = stat.getResultSet();
            while (rs.next())
            {
                allIds.add(rs.getString("keyList"));
            }
            conn.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return allIds;
    }

    // Testing
    public static void main(String[] args)
    {
        DataGenerator start = new DataGenerator();
        start.insertStatements("Staff", false, new String[]{}, false, false,
                new ExtraColumns[]{new ExtraColumns("income", "DECIMAL", "7,2", true, start.generateMoneyValue(200, 1000))});
    }
}
