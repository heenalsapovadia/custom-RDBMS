package sql;

import java.util.Set;

public class QueryValidator {

    Set<String> queryTypes = Set.of("use", "create", "insert", "update", "delete", "select", "exit");

    public QueryValidator () {}

    public String getQueryType (String query) {
        String[] queryParts = query.split("\\s");
        if(queryTypes.contains(queryParts[0]))
            return queryParts[0];
        return "INVALID";
    }

//    public void parseQuery (String queryType, String query) {
//        QueryParser queryParser = new QueryParser();
//        Query queryObj = new Query();
//
//        switch (queryType) {
//            case "create" :
////                queryParser.createParser(query);
//            case "select" :
//                queryObj = queryParser.selectParser(query);
//
//            case "insert" :
//            case "update" :
//            case "delete" :
//        }
//    }
}
