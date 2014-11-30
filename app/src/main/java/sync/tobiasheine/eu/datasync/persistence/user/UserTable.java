package sync.tobiasheine.eu.datasync.persistence.user;

import android.database.sqlite.SQLiteDatabase;

public class UserTable {
    public static final String NAME = "users";

    public enum Column {
        USER_ID,
        NAME;
    }

    public static String[] getAllColumns() {
        final String[] columns = new String[Column.values().length];

        for (int i = 0; i < Column.values().length; i++) {
            columns[i] = Column.values()[i].name();
        }

        return columns;
    }

    public static void createTable(final SQLiteDatabase sqLiteDatabase) {
        final String statement = "create table " + UserTable.NAME + "(" + UserTable.Column.USER_ID + " integer primary key autoincrement, " + UserTable.Column.NAME
                + " text not null);";
        sqLiteDatabase.execSQL(statement);
    }

}
