import sql.Query;
import sql.QueryParser;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test1 {
    public static void main(String args[]){
        Query queryOBJ = new Query();
        String query1 = "create table employee ( id int, name varchar, deptId int, primary key (id), foreign key (deptId) references department (id) )";
        query1 = query1.replaceAll(";", "");
        String[] queryList = query1.split(" ",4);
        queryOBJ.setType(queryList[1]);
        if(queryOBJ.getType().equals("table")) {
            queryOBJ.setTableName(queryList[2]);
        }
        if(queryOBJ.getType().equals("database")){
            queryOBJ.setDatabaseName(queryList[2]);
        }
        Map<String, String> optionMap = new HashMap<String, String>();
        String workingQuery = queryList[3];
        workingQuery = workingQuery.replaceAll(",", " ");
        workingQuery = workingQuery.replaceAll("[^a-zA-Z ]", "");
        System.out.println(workingQuery);
        String aftersplit1[] = workingQuery.split("primary");
        String firstpart1 = aftersplit1[0];
        System.out.println(firstpart1);
        firstpart1 = firstpart1.replaceAll("  ", " ");
        System.out.println(firstpart1);
        String[] workSql = firstpart1.split(" ");
//        System.out.println(workSql[1]);
        for (int i = 1; i< workSql.length-1; i=i+2){
            optionMap.put(workSql[i],workSql[i+1]);
        }
        queryOBJ.setOptionMap(optionMap);
        System.out.println(optionMap);
        if (query1.contains("primary")) {
            String aftersplit[] = query1.split("primary");
            String firstpart = aftersplit[0];
            String remaining = " primary" +aftersplit[1];
//            System.out.println(remaining);
            String remaining1 = remaining.replaceAll("[^a-zA-Z ]", "");
            String[] pkey = remaining1.split(" ");
            queryOBJ.setPrimarykey(pkey[3]);
//            System.out.println(pkey[3]);
            if (remaining.contains("foreign")) {
//                System.out.println(remaining);
                String afters[] = remaining.split("foreign");
                String rem = "foreign" + afters[1];
//                System.out.println(rem);
                Pattern pattern = Pattern.compile("foreign\\skey\\s+\\((.*)\\)\\s+references\\s+(.*)\\((.*)\\)\\s+");
                Matcher matcher = pattern.matcher(rem);
                matcher.find();
                Map<String,String> FKMap = new HashMap<>();
                FKMap.put("column", matcher.group(1));
                FKMap.put("refTable", matcher.group(2));
                FKMap.put("refTableColumn", matcher.group(3));
                queryOBJ.setFKMap(FKMap);
            }
        }
    }
}
