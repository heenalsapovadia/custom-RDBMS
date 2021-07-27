package sql;

import java.util.Set;

public class QueryValidator {

    Set<String> queryTypes = Set.of("create", "insert", "update", "delete", "select");

    public QueryValidator () {}

    public String getQueryType(String query) {
        String queryType = "";
        String[] queryParts = query.split("\\s");
        if(queryTypes.contains(queryParts[0]))
            return queryParts[0];
        return "INVALID";
    }
}
