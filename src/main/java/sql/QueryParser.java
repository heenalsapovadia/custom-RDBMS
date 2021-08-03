package sql;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {

    public Query createParser(String query) {
        Query queryObj = new Query();
        query = query.replaceAll(";", "");
        query = query.replaceAll(",", " ");
        query = query.replaceAll("[^a-zA-Z ]", "");
        String[] sqlWords = query.split(" ");

        String action = sqlWords[0];
        String type = sqlWords[1];
        String name = sqlWords[2];
        String location = sqlWords[3];

        queryObj.setType(type);
        queryObj.setTableName(name);
        return queryObj;
    }

    public Query useParser(String queryString)
    {
        queryString = queryString.replaceAll(";", "");
        String[] sql = queryString.split(" ");

        //logger.info("Parsing Query:"+query);
        //String action = sql[0];
        String databaseName = sql[1];

        //logger.info("Converting SQL query to internal query form");
        Query query = new Query();
        // Query.set("action",action);
        query.setDatabaseName(databaseName);

        return query;
    }

    public Query selectParser (String queryString) {
        // select * from table_name;
        // select * from table_name where col1 = abc;
        // select col1, col2 from table_name where col1 = abc;
        Query queryObj = new Query();
        queryString = queryString.replace(";", "");
        Pattern pattern = Pattern.compile("select\\s+(.*)\\s+from\\s+(.*)");
        Pattern wherePart = Pattern.compile("where\\s+(.*)");
        Matcher matcher = pattern.matcher(queryString);

        matcher.find();
        String columns = matcher.group(1).replaceAll("\\s+", "");

        String conditions="";
        String tableName = matcher.group(2);

        // if where clause exists
        Matcher whereMatcher = wherePart.matcher(tableName);
        if (whereMatcher.find()) {
            conditions = whereMatcher.group(1);
            String[] group2 = tableName.split("where");
            tableName = group2[0].replaceAll("\\s+","");

            String[] conditionArray = conditions.split("and");
            Map<String, String> conditionMap = new HashMap<>();
            for (String condition : conditionArray) {
                condition = condition.replaceAll("\\s+", "");
                String[] conditionParts = condition.split("=");
                conditionMap.put(conditionParts[0], conditionParts[1].replaceAll("\\'", ""));
            }
            queryObj.setCondition(conditions);
            queryObj.setConditionMap(conditionMap);
        }

        queryObj.setTableName(tableName);
        queryObj.setColumns(columns);
        return queryObj;
    }

    public Query insertParser (String queryString) {

        Query queryObj = new Query();
        queryString = queryString.replace(";", "");
        Pattern pattern = Pattern.compile("insert into\\s+(.*?)\\s+\\((.*?)\\)\\s+values\\s+\\((.*?)\\)");
        Matcher matcher = pattern.matcher(queryString);

        matcher.find();


        String tableName = matcher.group(1);
        String[] columnName = matcher.group(2)
                .replaceAll("\\s+", "")
                .replaceAll("[\\[\\](){}]","")
                .split(",");

        String[] insertValues = matcher.group(3)
                .replaceAll("\\s+", "")
                .replaceAll("[\\[\\](){}]","")
                .replace("\"","")
                .replace("'","")
                .split(",");




        Map<String, String> valueMap=new HashMap<>();


        String[] Parts;
        for(int i=0;i<insertValues.length;i++){

            System.out.println(insertValues[i].replaceAll("\\'", ""));
            valueMap.put(columnName[i], insertValues[i].replaceAll("\\'", ""));
        }


        queryObj.setTableName(tableName);
        queryObj.setOptionMap(valueMap);
        //queryObj.setColumns(columnName[0]);
        //queryObj.setColumns(columnName.length,columnName);
        //queryObj.setValueMap(valueMap);

        return queryObj;


    }
    public Query updateParser (String queryString) {
        // update table_name set col1=abc, col2=12;
        // update table_name set col1=abc where col2 = c;

        Query queryObj = new Query();
        queryString = queryString.replace(";", "");
        Pattern pattern = Pattern.compile("update\\s+(.*)\\s+set\\s+(.*)");
        Pattern wherePart = Pattern.compile("where\\s+(.*)");
        Matcher matcher = pattern.matcher(queryString);

        matcher.find();
        String tableName = matcher.group(1);
        String optionsPart = matcher.group(2);

        String conditions = "";
        Matcher whereMatcher = wherePart.matcher(optionsPart);
        if (whereMatcher.find()) {
            conditions = whereMatcher.group(1);
            String[] group2 = optionsPart.split("where");
            optionsPart = group2[0].replaceAll("\\s+","");

            String[] conditionArray = conditions.split("and");
            Map<String, String> conditionMap = new HashMap<>();
            for (String condition : conditionArray) {
                condition = condition.replaceAll("\\s+", "");
                String[] conditionParts = condition.split("=");
                conditionMap.put(conditionParts[0], conditionParts[1].replaceAll("\\'", ""));
            }
            queryObj.setCondition(conditions);
            queryObj.setConditionMap(conditionMap);
        }

        optionsPart = optionsPart.replaceAll("\\s+", "");
        String[] optionsList = optionsPart.split(",");
        Map<String, String> optionsMap = new HashMap<>();
        for (String option : optionsList) {
            option = option.replaceAll("\\s+", "");
            String[] optParts = option.split("=");
            optionsMap.put(optParts[0], optParts[1].replaceAll("\\'", ""));
        }

        queryObj.setOptionMap(optionsMap);
        queryObj.setTableName(tableName);
        return queryObj;
    }

    public Query deleteParser (String query) {
        Query queryOBJ = new Query();
        String query_removed = query.replace(";", "");

        Pattern pattern = Pattern.compile("delete\\s+(.*)\\s+from\\s+(.*)");
        Pattern wherePart = Pattern.compile("where\\s+(.*)");

        Matcher matcher = pattern.matcher(query);

        matcher.find();
        String tableName = matcher.group(1);
        String optionsPart = matcher.group(2);

        String conditions = "";
        Matcher whereMatcher = wherePart.matcher(optionsPart);
        if (whereMatcher.find()) {
            conditions = whereMatcher.group(1);
            String[] group2 = optionsPart.split("where");
            optionsPart = group2[0].replaceAll("\\s+","");

            String[] conditionArray = conditions.split("and");
            Map<String, String> conditionMap = new HashMap<>();
            for (String condition : conditionArray) {
                condition = condition.replaceAll("\\s+", "");
                String[] conditionParts = condition.split("=");
                conditionMap.put(conditionParts[0], conditionParts[1].replaceAll("\\'", ""));
            }
            queryOBJ.setCondition(conditions);
            queryOBJ.setConditionMap(conditionMap);
        }

        optionsPart = optionsPart.replaceAll("\\s+", "");
        String[] optionsList = optionsPart.split(",");
        Map<String, String> optionsMap = new HashMap<>();
        for (String option : optionsList) {
            option = option.replaceAll("\\s+", "");
            String[] optParts = option.split("=");
            optionsMap.put(optParts[0], optParts[1].replaceAll("\\'", ""));
        }

        queryOBJ.setOptionMap(optionsMap);
        queryOBJ.setTableName(tableName);
        // BUG: How to pass the column name
        // TODO: PASS THE COLUMN NAME
        return  queryOBJ;
    }
}
