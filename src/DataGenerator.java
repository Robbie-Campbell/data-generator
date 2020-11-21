/*
I realise now i am trying to achieve too many things at once, this class will
just create statements, without trying to link to a mysql db, that can come later.
 */

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DataGenerator {

    private final String[] firstNameList, lastNameList, addressLine1, roadTitle, addressLine2;
    private String PKName;
    private static int index;
    int current = 0;

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
        index = this.getCurrentIndex();
    }

    // Create a random email
    public String generateEmail(String first, String last, String id)
    {
        return first.toLowerCase() + last.toLowerCase() + id.toLowerCase() + "@email.com";
    }

    // Assign a random contact number
    public String generateContactNo()
    {
        return "07" + (int) (100000000 + Math.random() * 100000000);
    }

    // Generate a random date value with a lowest value for day month and year
    public String generateDate(int minYear, int minMonth, int minDay, int... maximum)
    {
        int maximumYear = maximum.length == 1 ? maximum[0] : 2020 - minYear;
        int maximumMonth = maximum.length == 2 ? maximum[1] : 12 - minMonth;
        int maximumDay = maximum.length == 3 ? maximum[2] : 12 - minDay;
        return  (int) (minYear + Math.random() * maximumYear) + "-" + (int) (minMonth + Math.random() * maximumMonth) + "-" +
                (int) (minDay + Math.random() * maximumDay);
    }

    // User key function
    private String generateKey()
    {
        index = index + 1;
        // Conditions to make the length of the PK consistent
        if (index < 10)
            return this.getIDName() + "0000" + index;
        else if (index < 100)
            return this.getIDName() + "000" + index;
        else if (index < 1000)
            return this.getIDName() + "00" + index;
        else if (index < 10000)
            return this.getIDName() + "0" + index;
        else
            return this.getIDName() + index;
    }

    // Create all primary keys values
    public String getIDName()
    {
        String[] pkLetters = this.PKName.split("_");
        String id;
        if (pkLetters.length > 1)
            id = String.valueOf(pkLetters[0].charAt(0)) + pkLetters[1].charAt(0);
        else {
            id = String.valueOf(pkLetters[0].charAt(0)) + pkLetters[0].charAt(1) + pkLetters[0].charAt(2);
        }

        return id.toUpperCase();
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
    public String generateTable(String tableName, boolean pk, String[] fk, Boolean name, Boolean personalInfo, ExtraColumn[] moreValues)
    {
        StringBuilder statement = new StringBuilder(String.format("CREATE TABLE %s (", tableName.toLowerCase()));
        if (pk)
        {
            statement.append("\n").append(tableName.toLowerCase()).append("_id VARCHAR(8) PRIMARY KEY NOT NULL,\n");
        }
        if (fk.length > 0)
        {
            for (String key: fk)
                statement.append(key).append("_id VARCHAR(8) NOT NULL,\n");
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
            for (ExtraColumn value: moreValues)
            {
                if (value.getDataType().equals("DATE"))
                    statement.append(String.format("%s %s %s %s,\n", value.getColumnName(), value.getDataType(), value.getDataSize(), value.getNullValue()));
                else
                    statement.append(String.format("%s %s (%s) %s,\n", value.getColumnName(), value.getDataType(), value.getDataSize(), value.getNullValue()));
            }
        }
        if (fk.length > 0)
        {
            for (String s : fk) {
                statement.append(String.format("FOREIGN KEY (%s_id) references %s(%s_id),\n", s, s, s));
            }
        }
        statement.setLength(statement.length() - 2);
        statement.append("\n);");
        commitTableToDatabase(tableName);
        return statement.toString();
    }

    // Returns the new index as the total number of rows in the information schema (avoids duplicate data)
    public int getCurrentIndex()
    {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", "root", "");
            PreparedStatement stat = conn.prepareStatement("SELECT SUM(TABLE_ROWS) as TOTAL FROM INFORMATION_SCHEMA.TABLES\n" +
                    "   WHERE TABLE_SCHEMA = 'data-generator';");
            stat.executeQuery();
            ResultSet rs = stat.getResultSet();
            rs.next();
            int total_rows = rs.getInt("TOTAL");
            rs.close();
            return total_rows;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    // Enter a set of values and a random value with be returned
    public String getRandomRole(String[] roles)
    {
        return roles[(int) (Math.random() * roles.length)];
    }

    // Execute the program, pass in params to decide which parts of the statement you want to include, the default FK
    // value should be an empty string array thus returning no fks
    public void insertStatements(String tableTitle, boolean pk, String[] fk, boolean getRandomForeignKeys, boolean nameAndEmail, boolean personalInfo, ExtraColumn[] moreValues)
    {
        this.PKName = tableTitle;
        index = this.getCurrentIndex();
        String id = this.generateKey();
        String firstName = this.getFirstName();
        String lastName = this.getLastName();
        String email = generateEmail(firstName, lastName, this.generateKey());
        StringBuilder insertStatement = new StringBuilder(String.format("INSERT INTO %s VALUES (", tableTitle));
        if (pk) {
            insertStatement.append(String.format("'%s',", id));
        }
        if (fk.length > 0 && !getRandomForeignKeys)
        {
            for (String key: fk) {
                insertStatement.append("'").append(this.getAllFKIDs(key).get(this.current)).append("',");
            }
            this.current ++;
        }
        else if (fk.length > 0){
            for (String key: fk) {
                    insertStatement.append("'").append(this.getAllFKIDs(key).get((int) (Math.random() * this.getAllFKIDs(key).size()))).append("', ");
            }
        }
        if (nameAndEmail)
        {
            insertStatement.append(String.format("'%s', '%s', '%s', ", firstName, lastName, email));
        }
        if (personalInfo)
        {
            insertStatement.append(String.format("'%s', DATE '%s', %s, ", this.generateContactNo(),
                    this.generateDate(1960, 1, 1), this.generateAddress()));
        }
        if (moreValues.length > 0)
        {
            for (ExtraColumn value: moreValues)
            {
                switch (value.getDataType()) {
                    case "VARCHAR":
                    case "TEXT":
                        insertStatement.append(String.format("'%s', ", value.getInsertDataType()));
                        break;
                    case "INT":
                    case "DECIMAL":
                    case "TINYINT":
                        insertStatement.append(value.getInsertDataType()).append(", ");
                        break;
                    case "DATE":
                        insertStatement.append(String.format("DATE '%s', ", value.getInsertDataType()));
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

    // Creates the table for all of the primary keys of this type.
    public void commitTableToDatabase (String tableName) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-generator", "root", "");
            String createTable = String.format("CREATE TABLE %s (keyList VARCHAR(8) PRIMARY KEY);", tableName);
            PreparedStatement stat = conn.prepareStatement(createTable);
            stat.executeUpdate(createTable);
            conn.close();
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

        //////////////////////////////////////////// CREATE ALL OF THE TABLES //////////////////////////////////////////

        // Staff table
        System.out.println(start.generateTable("Staff", true, new String[]{},  true, true,
                new ExtraColumn[]{
                new ExtraColumn("role", "VARCHAR", "10", false),
                new ExtraColumn("gross_salary_pm", "DECIMAL", "6,2", false),
                new ExtraColumn("is_absent", "TINYINT", "1", false)}));

        // Department table
        System.out.println(start.generateTable("Department", true, new String[]{}, false, false,
                new ExtraColumn[]{
                new ExtraColumn("department_name", "VARCHAR", "10", false ),
                new ExtraColumn("department_salary_pa", "DECIMAL", "7,2", false)}));

        // Non academic table
        System.out.println(start.generateTable("Non_Academic", false, new String[]{"Staff", "Department"}, false, false,
                new ExtraColumn[]{
                new ExtraColumn("skill_bonus", "DECIMAL", "6,2", true)}));

        // Staff income table
        System.out.println(start.generateTable("Staff_Wage", false, new String[]{"Staff"}, false, false,
                new ExtraColumn[]{
                new ExtraColumn("month", "DATE", "", false),
                new ExtraColumn("net_month_salary", "DECIMAL", "7,2", false),
                new ExtraColumn("absence_deduction", "DECIMAL", "6,2", false),
                new ExtraColumn("tax_for_month", "DECIMAL", "6,2", false),
                new ExtraColumn("meeting_bonus", "DECIMAL", "6,2", false)}));

        // Staff Absence table
        System.out.println(start.generateTable("Absence", false, new String[]{"Staff"}, false, false,
                new ExtraColumn[]{
                new ExtraColumn("date_of_leave", "DATE", "", false, start.generateDate(2019, 4, 1, 2020, 1, 3)),
                new ExtraColumn("date_of_return", "DATE", "", false, start.generateDate(2019, 4, 1, 2019, 1,3 )),
                new ExtraColumn("duration_of_absence", "INT", "2", false, "0"),
                new ExtraColumn("details_of_absence", "VARCHAR", "255", false, "Lorem Ipsum Dolor Dest")}));

        // Create the skills table
        System.out.println(start.generateTable("Skillset", true, new String[]{}, false, false,
                new ExtraColumn[]{
                new ExtraColumn("skill_worth", "DECIMAL", "6,2", false),
                new ExtraColumn("skill_name", "VARCHAR", "20", false)}));

        // Generate skills held values
        System.out.println(start.generateTable("Skills_Held", false, new String[]{"Skillset", "Staff"}, false, false, new ExtraColumn[]{}));

        // Generate academic staff table
        System.out.println(start.generateTable("Academic", false, new String[]{"Staff"}, false, false,
                new ExtraColumn[]{
                new ExtraColumn("curriculum_area", "VARCHAR", "20", false),
                new ExtraColumn("teacher_level", "VARCHAR", "10", false),
                new ExtraColumn("gross_salary_pa", "DECIMAL", "7,2", false)
            }));

        // Create any triggers for the tables
        System.out.println(
        "CREATE TRIGGER create_staff_pay\n" +
        "AFTER INSERT ON staff\n" +
                "FOR EACH ROW\n" +
        "INSERT\n" +
            "staff_wage\n" +
        "SET\n" +
            "staff_wage.staff_id = NEW.staff_id,\n" +
            "staff_wage.month = CURDATE(),\n" +
            "staff_wage.absence_deduction = 0,\n" +
            "staff_wage.tax_for_month = NEW.gross_salary_pm * 0.2,\n" +
            "staff_wage.meeting_bonus = (SELECT COUNT(*) from staff) * 5,\n" +
            "staff_wage.net_month_salary = (NEW.gross_salary_pm + staff_wage.meeting_bonus) - staff_wage.tax_for_month - staff_wage.absence_deduction;\n\n" +

        "CREATE TRIGGER update_absence_deduction\n" +
        "AFTER INSERT ON absence\n" +
        "FOR EACH ROW\n" +
        "UPDATE\n" +
            "staff, staff_wage\n" +
        "SET\n" +
            "staff_wage.absence_deduction = Round((staff.gross_salary_pm * 12) / 365 * (SELECT COUNT(duration_of_absence) FROM absence WHERE NEW.staff_id = staff.staff_id AND NEW.duration_of_absence > 2))\n\n" +
        "WHERE\n" +
            "staff.staff_id = staff_wage.staff_id\n" +
        "AND\n" +
            "staff.staff_id = NEW.staff_id;\n\n" +

        "CREATE TRIGGER update_skill_bonus\n" +
        "AFTER INSERT ON skills_held\n" +
        "FOR EACH ROW\n" +
        "UPDATE\n" +
            "non_academic, skillset, skills_held\n" +
        "SET\n" +
            "non_academic.skill_bonus = non_academic.skill_bonus + skillset.skill_worth\n" +
        "WHERE\n" +
            "NEW.skillset_id = skillset.skillset_id\n" +
                "AND\n" +
            "NEW.staff_id = non_academic.staff_id;\n\n" +

        "CREATE TRIGGER update_gross_monthly_salary\n" +
        "AFTER UPDATE ON non_academic\n" +
        "FOR EACH ROW\n" +
        "UPDATE\n" +
            "staff, department\n" +
        "SET\n" +
            "staff.gross_salary_pm = NEW.skill_bonus + Round(department.department_salary_pa / 12)\n" +
        "WHERE\n" +
            "NEW.department_id = department.department_id\n" +
        "AND\n" +
            "NEW.staff_id = staff.staff_id;\n\n"
        );


        ///////////////////////////////////////////////// GENERATE ALL DATA ////////////////////////////////////////////

        // Generate staff data
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Staff", true, new String[]{}, false,  true, true,
                    new ExtraColumn[]{
                    new ExtraColumn("role", "VARCHAR", "10", false, start.getRandomRole(new String[]{"HOD", "Teacher"})),
                    new ExtraColumn("gross_salary_pm", "DECIMAL", "6,2", false, start.generateMoneyValue(100, 1000)),
                    new ExtraColumn("is_absent", "TINYINT", "1", false, "0")});
        }

        // Generate department data
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Department", true, new String[]{}, false, false, false,
                    new ExtraColumn[]{
                    new ExtraColumn("department_name", "VARCHAR", "10", false, "Admin"),
                    new ExtraColumn("department_salary_pa", "DECIMAL", "7,2", false, start.generateMoneyValue(10000, 60000))});
        }

        // Generate non academic data data
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Non_Academic", false, new String[]{"Staff", "Department"}, false, false, false,
                    new ExtraColumn[]{
                    new ExtraColumn("skill_bonus", "DECIMAL", "6,2", true, "0")});
        }

        // Generate absence data
        start.current = 0;
        for (int i = 0; i < 10; i++){
            start.insertStatements("Absence", false, new String[]{"Staff"}, true, false, false,
                    new ExtraColumn[]{
                    new ExtraColumn("date_of_leave", "DATE", "", false, start.generateDate(2019, 4, 1, 2020, 1, 3)),
                    new ExtraColumn("date_of_return", "DATE", "", false, start.generateDate(2019, 4, 1, 2019, 1,3 )),
                    new ExtraColumn("duration_of_absence", "INT", "2", false, String.valueOf((int) (Math.random() * 5))),
                    new ExtraColumn("details_of_absence", "VARCHAR", "255", false, "Lorem Ipsum Dolor Dest")});
        }

        // Generate a skills table
        String[] skills = {"Typing", "Councilling", "Wrestling", "Fighting", "Gun"};
        for (int i = 0; i < 5; i++)
        {
            start.insertStatements("Skillset", true, new String[]{}, false, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("skill_worth", "DECIMAL", "6,2", false, start.generateMoneyValue(100,200)),
                            new ExtraColumn("skill_name", "VARCHAR", "20", false, skills[i])});
        }

        // Generate a skills held table
        start.current = 0;
        for (int i = 0; i < 10; i++)
        {
            start.insertStatements("Skills_Held", false, new String[]{"Skillset", "Staff"}, true, false, false,
                    new ExtraColumn[]{});
        }

        // Generate non academic data data
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Academic", false, new String[]{"Staff"}, true, false, false,
                    new ExtraColumn[]{
                    new ExtraColumn("curriculum_area", "VARCHAR", "20", false, start.getRandomRole(new String[]{"Computing", "English", "Maths"})),
                    new ExtraColumn("teacher_level", "VARCHAR", "10", false, start.getRandomRole(new String[]{"1", "2", "3"})),
                    new ExtraColumn("gross_salary_pa", "DECIMAL", "7,2", false, start.generateMoneyValue(10000, 20000))});
        }
    }
}

/*
drop trigger create_staff_pay;
drop trigger update_absence_deduction;
drop trigger update_skill_bonus;
 */
