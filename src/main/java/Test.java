import sql.QueryParser;

import java.sql.Array;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Test {
        public static void main(String args[]){

            String query1 = "create table employee ( id int, name varchar, deptId int, primary key (id), foreign key (deptId) references department(id), foreign key (deptId) references department(id) )";
            query1 = query1.replaceAll(";", "");
            String[] queryList = query1.split(" ",4);

            String query = "( id int, name varchar, primary key (id) )";
            System.out.println(query.substring(2,query.length()-2));
            query = query.replaceAll(",", " ");
            query = query.replaceAll("[^a-zA-Z ]", "");
            System.out.println(query);
            if (query.contains("primary")) {
                String aftersplit[] = query.split("primary");
                String firstpart = aftersplit[0];
                String remaining = " primary" +aftersplit[1];
                // TODO: GET WITH 2
                System.out.println(firstpart);
                System.out.println(remaining);
                if(query.contains("foreigh")){
//                    after foreign split with ,
//                            array - split on rereferences
                    Pattern pattern = Pattern.compile("foreign\\skey\\s+\\((.*)\\)\\s+references\\s+(.*)\\((.*)\\)\\s+\"\n");
//                    colum name  = matcher.groupname(1)
//                    FKMap.put("column", parts[0]);
//                    FKMap.put("refTable", parts[1]);
//                    FKMap.put("refTableColumn", parts[2]);
                }

//                for (int i = 0; i < aftersplit.length; i++){
//                    System.out.println(aftersplit[i]);
//                }
//                String firstPart = query.substring( 0, query.indexOf("primary"));
//                System.out.println(firstPart);
//                String remaining = query - firstPart;
            }
//            String[] queryList = query.split(" ");
















            //            String query = "create table department ( id int, name varchar, primary key (id) )";
////            query.contains("primary"){
////                query.contains("foreign")
////            }
//            query = query.replaceAll(";", "");
////            query = query.replaceAll(",", " ");
////            query = query.replaceAll("[^a-zA-Z ]", "");
//            String[] queryList = query.split(" ",4);
//            for (int i = 0; i < queryList.length; i++){
//                System.out.println(queryList[i]);
//            }


//            String[] sqlWords = query.split(" ");
//
//            String type = sqlWords[1];
//            String table_name = sqlWords[2];
//            ArrayList<String> columnName = new ArrayList<String>();
//            ArrayList<String> columndataType = new ArrayList<String>();
//
//            for (int i=4; i<= sqlWords.length - 2 ; i=i+2) {
//                columnName.add(sqlWords[i]);
//                columndataType.add(sqlWords[i+1]);
//            }
//
//            System.out.println(columnName);
//            System.out.println(columndataType);
//




        }
}
