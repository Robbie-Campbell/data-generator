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
    private static int index;
    private static final String pass = "";
    private static final String user = "root";
    private static final String dbName = "data-generator";
    int current = 0;

    public DataGenerator() {

        // Create 2 arrays -- one for first names and one for last names
        this.firstNameList = new String[]{"Marco", "John", "Martin", "Melvin", "Alvin", "Benjamin", "Craig", "Daniel", "Eggbert",
                "Farquart", "Graham", "Hewey", "Inyego", "Jake", "Kevin", "Lebron", "Mickey", "Nicholas", "Oscar", "Peter",
                "Quincey", "Raymond", "Stephen", "Terry", "Ulysses", "Vincent", "Winston", "Yolanda", "Anabelle", "Brie",
                "Chrissy", "Danielle", "Evelyne", "Farah", "Gene", "Holly", "India", "Janine", "Kelly", "Linda", "Molly",
                "Nina", "Priscilla", "Gael", "Ludwig", "Zagreus", "Cameroono", "Natalie", "Geralt"};

        this.lastNameList = new String[]{"Arnold", "Brigham", "Campbell", "Donaldson", "Eggleton", "Farish", "Granada", "Hilbert",
                "Ilverez", "Johnson", "Kepler", "Linson", "Montana", "Nishelson", "Organson", "Peterson", "Raymondson",
                "Stephenson", "Teirson", "Vienna", "Waterson", "Baggins", "ofRivia", "Wynne"};

        // Create address arrays
        this.addressLine1 = new String[]{"Water", "Cribbage", "Richmond", "Stavingo", "Bolinko", "Crischenko", "Hearty",
                "Gastavern", "Fishbrook", "Lincoln", "Ringston", "Crippsortion", "Margo", "Lorem", "Ipsum", "David", "Cookie",
                "Biker", "Green", "Pantonomo", "Grove", "Lamplight"};

        this.roadTitle = new String[]{"Provost", "Drive", "Close", "Road", "Avenue", "Street", "Gardens", "Grove", "Lane"};

        this.addressLine2 = new String[]{"Dorset", "Somerset", "Heartfordshire", "Essex", "Upper London", "Riverland", "Cloud",
                "Novigrad", "Crally", "Wessex"};
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

    // Generate a random time
    public String generateTime()
    {
        int hour = (int) ( 8 + Math.random() * 6);
        int min = ((int) (Math.random() * 60));
        return String.format("%d:%d:00", hour, min);

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

    // generate a random full name
    public String getFullName()
    {
        return firstNameList[(int) (Math.random() * firstNameList.length)] + " " + lastNameList[(int) (Math.random() * lastNameList.length)];
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
    public String generateTable(String tableName, boolean pk, String[] fk, Boolean name, Boolean personalInfo, ExtraColumn[] moreValues, String... fkColumnNames)
    {
        StringBuilder statement = new StringBuilder(String.format("CREATE TABLE %s (", tableName.toLowerCase()));
        if (pk)
        {
            statement.append("\n").append(tableName.toLowerCase()).append("_id VARCHAR(8) PRIMARY KEY NOT NULL,\n");
        }
        if (fk.length > 0)
        {
            int fkIndex = 0;
            for (String key: fk)
            {
                if (fkColumnNames.length == 0)
                    statement.append(key).append("_id VARCHAR(8) NOT NULL,\n");
                else {
                    statement.append(fkColumnNames[fkIndex]).append("_id VARCHAR(8) NOT NULL,\n");
                    fkIndex++;
                }
            }
        }
        if (name)
        {
            statement.append("first_name VARCHAR(20) NOT NULL,\nlast_name VARCHAR(20) NOT NULL,\nemail VARCHAR(50) NOT NULL,\n");
        }
        if (personalInfo)
        {
            statement.append("contact_no VARCHAR(13) NOT NULL,\nDOB DATE NOT NULL,\naddress_line_1 VARCHAR(50) NOT NULL,\n" +
                    "address_line_2 VARCHAR(50) NOT NULL,\npostcode VARCHAR(9) NOT NULL,\n");
        }
        if (moreValues.length > 0)
        {
            for (ExtraColumn value: moreValues)
            {
                if (value.getDataType().equals("DATE") || value.getDataType().equals("TIME"))
                    statement.append(String.format("%s %s %s %s,\n", value.getColumnName(), value.getDataType(), value.getDataSize(), value.getNullValue()));
                else
                    statement.append(String.format("%s %s (%s) %s,\n", value.getColumnName(), value.getDataType(), value.getDataSize(), value.getNullValue()));
            }
        }
        if (fk.length > 0)
        {
            int fkIndex = 0;
            for (String s : fk) {
                if (fkColumnNames.length == 0)
                    statement.append(String.format("FOREIGN KEY (%s_id) references %s(%s_id),\n", s, s, s));
                else {
                    statement.append(String.format("FOREIGN KEY (%s_id) references %s(%s_id),\n", fkColumnNames[fkIndex], s, s));
                    fkIndex++;
                }
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
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + dbName, user, pass);
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
    public String getRandomValueFromArray(String[] roles)
    {
        return roles[(int) (Math.random() * roles.length)];
    }

    // Get a value of one or zero
    public String getRandomTinyInt () {return String.valueOf((int) (Math.random() * 2)); }

    // Execute the program, pass in params to decide which parts of the statement you want to include, the default FK
    // value should be an empty string array thus returning no fks
    public void insertStatements(String tableTitle, boolean pk, String[] fk, boolean getRandomForeignKeys, int startingFK,
                                 boolean nameAndEmail, boolean personalInfo, ExtraColumn[] moreValues)
    {
        this.PKName = tableTitle;
        index = this.getCurrentIndex();
        String firstName = this.getFirstName();
        String id = this.generateKey();
        String lastName = this.getLastName();
        String email = generateEmail(firstName, lastName, id);
        StringBuilder insertStatement = new StringBuilder(String.format("INSERT INTO %s VALUES (", tableTitle));
        if (pk) {
            insertStatement.append(String.format("'%s',", id));
        }
        if (fk.length > 0 && !getRandomForeignKeys)
        {
            for (String key: fk) {
                insertStatement.append("'").append(this.getAllFKIDs(key).get(this.current + startingFK)).append("',");
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
                    this.generateDate(1960, 1, 1, 40, 11, 28), this.generateAddress()));
        }
        if (moreValues.length > 0)
        {
            for (ExtraColumn value: moreValues)
            {
                switch (value.getDataType()) {
                    case "VARCHAR":
                    case "TEXT":
                    case "TIME":
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
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + dbName, user, pass);
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
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + dbName, user, pass);
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
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/" + dbName, user, pass);
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
    public static void main(String[] args) {
        DataGenerator start = new DataGenerator();

        //////////////////////////////////////////// CREATE ALL OF THE TABLES //////////////////////////////////////////

        // Staff table
        System.out.println(start.generateTable("Staff", true, new String[]{}, true, true,
                new ExtraColumn[]{
                        new ExtraColumn("role", "VARCHAR", "7", false),
                        new ExtraColumn("gross_salary_pm", "DECIMAL", "6,2", false),
                        new ExtraColumn("is_absent", "TINYINT", "1", false)}));

        // Manager table
        System.out.println(start.generateTable("Manager", false, new String[]{"Staff", "Staff"}, false, false, new ExtraColumn[]{},
                "Staff_member", "Manager"));

        // Department table
        System.out.println(start.generateTable("Department", true, new String[]{}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("department_name", "VARCHAR", "15", false),
                        new ExtraColumn("department_salary_pa", "DECIMAL", "8,2", false)}));

        // Non academic table
        System.out.println(start.generateTable("Non_Academic", false, new String[]{"Staff", "Department"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("skill_bonus", "DECIMAL", "6,2", false)}));

        // Staff income table
        System.out.println(start.generateTable("Staff_Wage", false, new String[]{"Staff"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("month", "DATE", "", false),
                        new ExtraColumn("net_month_salary", "DECIMAL", "6,2", false),
                        new ExtraColumn("absence_deduction", "DECIMAL", "6,2", false),
                        new ExtraColumn("tax_for_month", "DECIMAL", "6,2", false),
                        new ExtraColumn("meeting_bonus", "DECIMAL", "6,2", false)}));

        // Staff Absence table
        System.out.println(start.generateTable("Absence", false, new String[]{"Staff"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("date_of_leave", "DATE", "", false),
                        new ExtraColumn("date_of_return", "DATE", "", false),
                        new ExtraColumn("duration_of_absence", "INT", "2", false),
                        new ExtraColumn("details_of_absence", "TEXT", "255", false)}));

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
                        new ExtraColumn("gross_salary_pa", "DECIMAL", "8,2", false)
                }));

        // Create a curriculum Table
        System.out.println(start.generateTable("Curriculum", true, new String[]{"Staff"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("course_name", "VARCHAR", "20", false),
                        new ExtraColumn("course_level", "VARCHAR", "10", false),
                        new ExtraColumn("course_start_date", "DATE", "", false),
                        new ExtraColumn("course_end_date", "DATE", "", false)
                }));

        // Create a coursework Table
        System.out.println(start.generateTable("Coursework", true, new String[]{"Curriculum"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("set_date", "DATE", "", false),
                        new ExtraColumn("due_date", "DATE", "", false)
                }));

        // Create a subject table
        System.out.println(start.generateTable("Subject", true, new String[]{"Curriculum"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("subject_name", "VARCHAR", "12", false)}));

        // Create a programme Table
        System.out.println(start.generateTable("Programme", true, new String[]{"Subject", "Staff"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("programme_title", "VARCHAR", "20", false),
                        new ExtraColumn("programme_date", "DATE", "", false),
                        new ExtraColumn("programme_time", "TIME", "", false),
                        new ExtraColumn("programme_location", "VARCHAR", "20", false)
                }, "Subject", "Programme_Leader"));

        // Create a tuition table
        System.out.println(start.generateTable("Tuition", true, new String[]{}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("student_type", "VARCHAR", "20", false),
                        new ExtraColumn("loan_total", "DECIMAL", "7,2", false)}));

        // Create a student table (most complicated)
        System.out.println(start.generateTable("Student", true, new String[]{"Tuition", "Curriculum"}, true, true,
                new ExtraColumn[]{
                        new ExtraColumn("tuition_amount_paid", "DECIMAL", "7,2", false),
                        new ExtraColumn("emergency_contact", "VARCHAR", "40", false),
                        new ExtraColumn("emergency_phone_no", "VARCHAR", "20", false),
                        new ExtraColumn("emergency_email", "VARCHAR", "50", false)}));

        // Create a submission table
        System.out.println(start.generateTable("Submission_Status", false, new String[]{"Student", "Coursework"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("submitted", "TINYINT", "1", false)}));

        // Create a programme attendance table
        System.out.println(start.generateTable("Programme_Attendance", false, new String[]{"Programme", "Student"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("attendance_status", "VARCHAR", "1", false)}));

        // Create a Student meeting table
        System.out.println(start.generateTable("Student_Meeting", true, new String[]{"Staff", "Student"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("location_of_meeting", "VARCHAR", "20", false),
                        new ExtraColumn("date_of_meeting", "DATE", "", false),
                        new ExtraColumn("time_of_meeting", "TIME", "", false),
                        new ExtraColumn("subject_of_meeting", "VARCHAR", "50", false)}));

        // Create a meeting notes table
        // Create a programme attendance table
        System.out.println(start.generateTable("Meeting_notes", false, new String[]{"Student_Meeting"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("notes", "TEXT", "255", false)}));

        // Create a Locker table
        System.out.println(start.generateTable("Locker", true, new String[]{}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("location", "VARCHAR", "20", false),
                        new ExtraColumn("locker_owned", "TINYINT", "1", false)}));

        // Create a full_time student table
        System.out.println(start.generateTable("Full_Time_Student", false, new String[]{"Student", "Locker"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("deposit_paid", "TINYINT", "1", false)}));

        // Create a international student table
        System.out.println(start.generateTable("Distance_Student", false, new String[]{"Student", "Staff", "Staff"}, false, false,
                new ExtraColumn[]{}, "student", "academic_tutor", "non_academic_tutor"));

        // Create a distance student table
        System.out.println(start.generateTable("International_Student", false, new String[]{"Student"}, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("nationality", "VARCHAR", "20", false),
                        new ExtraColumn("visa_number", "VARCHAR", "12", false),
                        new ExtraColumn("passport_number", "VARCHAR", "12", false)}));

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
                        "staff_wage.meeting_bonus = 0,\n" +
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
                        "NEW.staff_id = staff.staff_id;\n\n" +

                        "CREATE TRIGGER update_staff_meeting_bonus\n" +
                        "AFTER INSERT ON student_meeting\n" +
                        "FOR EACH ROW\n" +
                        "UPDATE\n" +
                        "non_academic, staff_wage\n" +
                        "SET\n" +
                        "staff_wage.meeting_bonus = staff_wage.meeting_bonus + 5\n" +
                        "WHERE\n" +
                        "non_academic.staff_id = staff_wage.staff_id\n" +
                        "AND\n" +
                        "non_academic.staff_id = NEW.staff_id;\n" +

                        "CREATE TRIGGER update_locker_status\n" +
                        "AFTER INSERT ON Full_Time_Student\n" +
                        "FOR EACH ROW\n" +
                        "UPDATE\n" +
                        "Locker\n" +
                        "SET\n" +
                        "locker_owned = 1 WHERE NEW.locker_id = Locker.locker_id;"
        );


        ///////////////////////////////////////////////// GENERATE ALL DATA ////////////////////////////////////////////

        // Generate staff data
        for (int i = 0; i < 10; i++) {
            start.insertStatements("Staff", true, new String[]{}, false, 0, true, true,
                    new ExtraColumn[]{
                            new ExtraColumn("role", "VARCHAR", "10", false, start.getRandomValueFromArray(new String[]{"HOD", "Teacher"})),
                            new ExtraColumn("gross_salary_pm", "DECIMAL", "6,2", false, start.generateMoneyValue(100, 1000)),
                            new ExtraColumn("is_absent", "TINYINT", "1", false, "0")});
        }

        System.out.println(
                "INSERT INTO Manager VALUES ('STA00001', 'STA0003');\n" +
                "INSERT INTO Manager VALUES ('STA00002', 'STA0004');\n" +
                "INSERT INTO Manager VALUES ('STA00003', 'STA00005');\n" +
                "INSERT INTO Manager VALUES ('STA00004', 'STA00005');\n" +
                "INSERT INTO Manager VALUES ('STA00006', 'STA00007');\n" +
                "INSERT INTO Manager VALUES ('STA00007', 'STA00009');\n" +
                "INSERT INTO Manager VALUES ('STA00008', 'STA00010');\n" +
                "INSERT INTO Manager VALUES ('STA00009', 'STA00010');\n\n"
        );

        // Generate department data
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Department", true, new String[]{}, false, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("department_name", "VARCHAR", "10", false, new String[]{"Admin", "Janitorial", "Medical", "Councillor", "IT"}[i]),
                            new ExtraColumn("department_salary_pa", "DECIMAL", "7,2", false, start.generateMoneyValue(10000, 60000))});
        }

        // Generate non academic data data
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Non_Academic", false, new String[]{"Staff", "Department"}, false, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("skill_bonus", "DECIMAL", "6,2", false, "0")});
        }

        // Generate absence data
        start.current = 0;
        for (int i = 0; i < 10; i++) {
            start.insertStatements("Absence", false, new String[]{"Staff"}, true, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("date_of_leave", "DATE", "", false, start.generateDate(2019, 4, 1, 2020, 1, 3)),
                            new ExtraColumn("date_of_return", "DATE", "", false, start.generateDate(2019, 4, 1, 2019, 1, 3)),
                            new ExtraColumn("duration_of_absence", "INT", "2", false, String.valueOf((int) (Math.random() * 5))),
                            new ExtraColumn("details_of_absence", "VARCHAR", "255", false, "Lorem Ipsum Dolor Dest")});
        }

        // Generate a skills table
        String[] skills = {"Typing", "Counselling", "Office", "Language", "Cleaning"};
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Skillset", true, new String[]{}, false, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("skill_worth", "DECIMAL", "6,2", false, start.generateMoneyValue(100, 200)),
                            new ExtraColumn("skill_name", "VARCHAR", "20", false, skills[i])});
        }

        // Generate a skills held table
        start.current = 0;
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Skills_Held", false, new String[]{"Skillset", "Staff"}, true, 0, false, false,
                    new ExtraColumn[]{});
        }

        // Generate non academic data data
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Academic", false, new String[]{"Staff"}, false, 5, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("curriculum_area", "VARCHAR", "20", false, start.getRandomValueFromArray(new String[]{"Computing", "English", "Maths"})),
                            new ExtraColumn("teacher_level", "VARCHAR", "10", false, start.getRandomValueFromArray(new String[]{"1", "2", "3"})),
                            new ExtraColumn("gross_salary_pa", "DECIMAL", "7,2", false, start.generateMoneyValue(10000, 20000))});
        }

        // Generate curriculum data
        for (int i = 0; i < 3; i++)
        {
            start.insertStatements("Curriculum", true, new String[]{"Staff"}, true, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("course_name", "VARCHAR", "20", false, new String[]{"Computing", "English", "Maths"}[i]),
                            new ExtraColumn("course_level", "VARCHAR", "10", false, start.getRandomValueFromArray(new String[]{"1", "2", "3", "4"})),
                            new ExtraColumn("course_start_date", "DATE", "", false, start.generateDate(2019, 4, 1, 2019, 1, 3)),
                            new ExtraColumn("course_end_date", "DATE", "", false, start.generateDate(2020, 4, 1, 2020, 4, 10))
                    });
        }

        // Create a coursework Table
        start.current = 0;
        for (int i = 0; i < 6; i++)
        {
        start.insertStatements("Coursework", true, new String[]{"Curriculum"}, false, 0, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("set_date", "DATE", "", false, start.generateDate(2019, 4, 1, 2019, 1, 3)),
                        new ExtraColumn("due_date", "DATE", "", false, start.generateDate(2020, 4, 1, 2020, 4, 10))
                });
            if (i == 2)
            {
                start.current = 0;
            }
        }
        start.current=0;
        // Create subject data
        for (int i = 0; i < 6; i++)
        {
            start.insertStatements("Subject", true, new String[]{"Curriculum"}, false, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("subject_name", "VARCHAR", "6,2", false, new String[]{"Networking", "Shakespeare", "Algebra", "Programming", "Language", "Calculus"}[i])});
            if (i == 2)
            {
                start.current=0;
            }
        }

        // Create programme data
        for (int i = 0; i < 10; i++)
        {
            start.insertStatements("Programme", true, new String[]{"Subject", "Staff"}, true, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("programme_title", "VARCHAR", "20", false, start.getRandomValueFromArray(new String[]{"lorem", "ipsum", "dolor", "dest"})),
                            new ExtraColumn("programme_date", "DATE", "", false, start.generateDate(2019, 1, 1)),
                            new ExtraColumn("programme_time", "TIME", "", false, start.generateTime()),
                            new ExtraColumn("programme_location", "VARCHAR", "20", false, start.getRandomValueFromArray(new String[]{"lorem", "ipsum", "dolor", "dest"}))
                    });
        }

        // Create tuition data
        for (int i = 0; i< 4; i++) {
            start.insertStatements("Tuition", true, new String[]{}, false, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("student_type", "VARCHAR", "20", false, new String[]{"Full_Time", "Distance", "FT-International", "Distance-International"}[i]),
                            new ExtraColumn("loan_total", "DECIMAL", "7,2", false, new String[]{"6000.00", "3000.00", "9000.00", "6000.00"}[i])
                    });
        }

        // Create a student table (most complicated)
        for (int i = 0; i < 10; i++) {
            start.insertStatements("Student", true, new String[]{"Tuition", "Curriculum"}, true, 0, true, true,
                    new ExtraColumn[]{
                            new ExtraColumn("tuition_amount_paid", "DECIMAL", "7,2", false, start.generateMoneyValue(0, 9000)),
                            new ExtraColumn("emergency_contact", "VARCHAR", "12", false, start.getFullName()),
                            new ExtraColumn("emergency_phone_no", "VARCHAR", "20", false, start.generateContactNo()),
                            new ExtraColumn("emergency_email", "VARCHAR", "50", false, start.generateEmail(start.getFirstName(), start.getLastName(), "parent"))});
        }

        // Create submission status data
        for (int i = 0; i < 10; i++)
        {

            start.insertStatements("Submission_Status", false, new String[]{"Student", "Coursework"}, true, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("submission_status", "TINYINT", "1", false, start.getRandomTinyInt())});
        }

        // Create a programme attendance data
        for (int i = 0; i < 20; i++) {
            start.insertStatements("Programme_Attendance", false, new String[]{"Programme", "Student"}, true, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("attendance_status", "VARCHAR", "1", false, start.getRandomValueFromArray(new String[]{"P", "A", "L", "E", "Q"}))});
        }

        // Create Student meeting data
        for (int i = 0; i < 10; i++) {
            start.insertStatements("Student_Meeting", true, new String[]{"Staff", "Student"}, true, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("location_of_meeting", "VARCHAR", "20", false, start.getRandomValueFromArray(new String[]{"lorem", "ipsum", "dolor", "dest"})),
                            new ExtraColumn("date_of_meeting", "DATE", "", false, start.generateDate(2019, 1, 1)),
                            new ExtraColumn("time_of_meeting", "TIME", "", false, start.generateTime()),
                            new ExtraColumn("subject_of_meeting", "VARCHAR", "50", false, start.getRandomValueFromArray(new String[]{"Attendance", "Grades", "Wellbeing", "Catch-up"}))
                    });
        }

        start.current = 0;
        // Create meeting notes data
        for (int i = 0; i < 10; i++)
        {
            start.insertStatements("Meeting_notes", false, new String[]{"Student_Meeting"}, false, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("notes", "TEXT", "255", false, loremGenerator.generateFunLorem())});
        }

        // Create Locker data
        // Create a full_time student table
        for (int i = 0; i < 20; i++)
        {
        start.insertStatements("Locker", true, new String[]{}, false, 0, false, false,
                new ExtraColumn[]{
                        new ExtraColumn("location", "VARCHAR", "20", false, start.getRandomValueFromArray(new String[]{"lorem", "ipsum", "dolor", "dolor"})),
                        new ExtraColumn("locker_owned", "TINYINT", "1", false, "0")});
        }

        start.current = 0;
        // Create a full_time student table
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Full_Time_Student", false, new String[]{"Student", "Locker"}, false, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("deposit_paid", "TINYINT", "1", false, start.getRandomTinyInt())});
        }

        start.current = 0;
        // Create a international student table
        for (int i = 0; i < 5; i++) {
            start.insertStatements("Distance_Student", false, new String[]{"Student", "Staff", "Staff"}, true, 0, false, false,
                    new ExtraColumn[]{});
        }

        start.current = 0;
        // Create a distance student table
        for (int i = 0; i < 5; i++) {
            start.insertStatements("International_Student", false, new String[]{"Student"}, true, 0, false, false,
                    new ExtraColumn[]{
                            new ExtraColumn("nationality", "VARCHAR", "20", false, start.getRandomValueFromArray(new String[]{"Scotland", "Venezuela", "Zimbabwe", "Chad"})),
                            new ExtraColumn("visa_number", "VARCHAR", "12", false, start.generateContactNo()),
                            new ExtraColumn("passport_number", "VARCHAR", "12", false, start.generateContactNo())});
        }
    }
}
