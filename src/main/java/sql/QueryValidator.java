package sql;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryValidator {

    Set<String> queryTypes = Set.of("use", "create", "insert", "update", "delete", "select", "exit");

    public QueryValidator () {}

    public String getQueryType (String query) {
        String[] queryParts = query.split("\\s");
        if(queryTypes.contains(queryParts[0]))
            return queryParts[0];
        return "INVALID";
    }

    public boolean isInteger (String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException nfe) {
            return false;
        }
    }

    public boolean isVarchar (String value) {
        try {
            Pattern pattern = Pattern.compile("'(.*)'");
            Matcher matcher = pattern.matcher(value);
            matcher.find();
            System.out.println(matcher.group(1));
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
