package sql;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {

    public void createParser(String query) {

    }

    public Query selectParser (String queryString) {
        // select * from table_name;
        // select * from table_name where col1 = abc;
        // select col1, col2 from table_name where col1 = abc;
        Query queryObj = new Query();
        queryString = queryString.replace(";", "");
        Pattern pattern = Pattern.compile("select\\s+(.*)\\s+from\\s+(.*)");
        Pattern wherePattern = Pattern.compile("where\\s+(.*)");
        Matcher matcher = pattern.matcher(queryString);

        matcher.find();
        String columns = matcher.group(1).replaceAll("\\s+", "");

        String conditions="";
        String tableName = matcher.group(2);

        // if where clause exists
        Matcher whereMatcher = wherePattern.matcher(tableName);
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

    public void insertParser (String query) {

    }
    public void updateParser (String query) {

    }
    public void deleteParser (String query) {

    }
}
