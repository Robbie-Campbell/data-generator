
// Return a new column for a table and create new data for it
public class ExtraColumns {

    private final String columnName;
    private final String dataType;
    private final String dataSize;
    private final String insertDataType;
    private final String nullValue;

    public ExtraColumns(String columnName, String dataType, String dataSize, boolean nullable, String insertDataType)
    {
        this.columnName = columnName;
        this.dataType = dataType;
        this.dataSize = dataSize;
        this.insertDataType = insertDataType;
        if (!nullable)
        {
            this.nullValue = "NOT NULL";
        }
        else
        {
            this.nullValue = "NULL";
        }

    }

    public String getColumnName() {
        return this.columnName;
    }

    public String getDataType() {
        return this.dataType;
    }

    public String getDataSize() {
        return this.dataSize;
    }

    public String getNullValue() {
        return this.nullValue;
    }

    public String getInsertDataType() {
        return insertDataType;
    }
}
