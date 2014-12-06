package sync.tobiasheine.eu.datasync.persistence.user;

import android.database.sqlite.SQLiteDatabase;

import sync.tobiasheine.eu.datasync.persistence.Table;

public class UserTable extends Table {

    public static final String TABLE_NAME = "users";

    public static final String NAME = "name";
    public static final String[] COLUMNS = new String[]{_ID, NAME};
    public static final String ALIAS_JOINER = "_";


    public static void createTable(final SQLiteDatabase sqLiteDatabase) {
        final String statement = "create table " + UserTable.TABLE_NAME + "(" + Table._ID + " integer primary key autoincrement, " + UserTable.NAME
                + " text not null);";
        sqLiteDatabase.execSQL(statement);
    }

    public static String addPrefix(String column) {
        return TABLE_NAME + "." + column;
    }

}
