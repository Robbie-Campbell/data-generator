# Generate Data With Rab!

:shipit: creates data for a mysql database :shipit:

## A non-user friendly project which generates data using optional function parameters
##### Has the ability to keep track of all of your primary keys to be referenced later, as opposed to other software which simply generates static data where __YOU__ must keep a track of the foreign keys.

# THIS IS NOT EASY TO USE, BUT IF YOU WORK IT OUT IT'S PRETTY POWERFUL!

![image](https://user-images.githubusercontent.com/56073739/101159307-1f321d80-3625-11eb-8d5d-49e7b090ad39.png)

# Documentation

## Set up
> The Service utilises a mysql database to keep a track of the foreign keys. In the private static final parameters "user", "dbName" and "pass". You should insert your username, database name and password for the database here. (The database name is where the primary keys will be stored)

> You must also set your database to be on non-safe mode, how to do this is documented [here](https://stackoverflow.com/questions/11448068/mysql-error-code-1175-during-update-in-mysql-workbench)

## Using the application

##### An example of create table function
System.out.println(start.generateTable("Staff", true, new String[]{},  true, true,
                new ExtraColumn[]{
                new ExtraColumn("role", "VARCHAR", "10", false),
                new ExtraColumn("gross_salary_pm", "DECIMAL", "6,2", false),
                new ExtraColumn("is_absent", "TINYINT", "1", false)}));
                
_This will return the script to generate a staff table and will create a table ready for the PK's in the database_

### You must first create an instance of the DataGenerator class to run these functions, also it must be encaplsuated inside of a System.out.println() to return the script.

> In the __CREATE ALL OF THE TABLES__ Section, this is where you should write your code to create tables. The generateTable parameters are as follows:
1. The name of the Table (String)
2. Does it need a Primary Key (Boolean)
3. Whether this table references any other tables, do you have any foreign keys? (String Array)
4. Enter first name, last name and email columns automatically? (Boolean)
5. Enter Contact number, date of birth and address information automatically? (Boolean)
6. Do you need to enter any extra columns? details of this are specified below. (ExtraColumn Array)

> The ExtraColumn array must be instantiated, and the children inside are further instances of individual instantiated ExtraColumns. The parameters are as follows:
1. The name of the new column (String)
2. The Datatype can be "VARCHAR", "TEXT", "INT", "DECIMAL", "TINYINT" or "DATE" (String)
3. The size of the datatype date requires an empty string, decimal can accept a comma seperated value (String)
4. Can the extra column be nullable? (Boolean)

### If you want to implement any triggers in your script, here is the place to do so as when running in mysql you can simply run everything at once to create a database

##### An example of insert statements function
for (int i = 0; i < 5; i++)
      {
          start.insertStatements("Staff", true, new String[]{}, false,  0, true, true,
                  new ExtraColumn[]{
                  new ExtraColumn("role", "VARCHAR", "10", false, start.getRandomRole(new String[ {"HOD","Teacher"})),
                  new ExtraColumn("gross_salary_pm", "DECIMAL", "6,2", false, start.generateMoneyValue(100, 1000)),
                  new ExtraColumn("is_absent", "TINYINT", "1", false, "0")});
        }

> In the __GENERATE ALL DATA__ Section, each insert statement should be encapsulated in a for loop for how many data points you want to create. The parameters for the insert statements are the same as the create table statement except for the statement after the foreign key array which accepts a boolean and returns either all foreign keys from a table (false) or random foreign keys from a table (true). the final (optional) generate data parameter which will populate your table with data.

### List of generate data functions as of 04/12/2020
1. DataGenerator.generateMoneyValue Accepts a minimum and maximum value as parameters and returns a random money value (int maxValue, int minValue)
2. DataGenerator.getRandomValueFromArray Accepts a String array of values and returns a random one
3. DataGenerator.getRandomTinyInt returns 1 or 0 for a true or false value
