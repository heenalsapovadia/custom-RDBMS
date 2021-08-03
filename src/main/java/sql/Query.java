package sql;

import java.util.Map;

public class Query {
    private String databaseName;
    private String tableName;
    private String columns;
    private String condition;
    private String type;
    private Map<String, String> conditionMap;
    private Map<String, String> optionMap;
    private Map<String, String> valueMap;

    public String getType(){return type;}

    public void setType(String type) {
        this.type = type;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Map<String, String> getConditionMap() {
        return conditionMap;
    }

    public void setConditionMap(Map<String, String> conditionMap) {
        this.conditionMap = conditionMap;
    }

    public Map<String, String> getOptionMap() {
        return optionMap;
    }

    public void setOptionMap(Map<String, String> optionMap) {
        this.optionMap = optionMap;
    }

    public void setValueMap(Map<String,String> valueMap)
    {
        this.valueMap=valueMap;
    }
}
