package Test;

import database.DatabaseAgent;

public class QueryMaxTIDTest {
    public static void main(String[] args) {
        System.out.println(new DatabaseAgent().getMaxTID());
    }
}
