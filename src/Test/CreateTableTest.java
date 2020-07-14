package Test;

import database.DatabaseAgent;

public class CreateTableTest {
    public static void main(String[] args) {
        DatabaseAgent db = new DatabaseAgent();
        db.connect();
        db.createTable();
        db.close();
    }
}
