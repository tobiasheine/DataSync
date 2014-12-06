package sync.tobiasheine.eu.datasync.persistence.post;

import android.database.sqlite.SQLiteDatabase;

import sync.tobiasheine.eu.datasync.persistence.Table;

public class PostTable extends Table {
    public static final String TABLE_NAME = "posts";

    public static final String TEXT = "text";
    public static final String AUTHOR_ID = "author_id";
    public static final String[] COLUMNS = new String[]{_ID, TEXT, AUTHOR_ID};

    public static void createTable(final SQLiteDatabase sqLiteDatabase) {
        final String statement = "create table " + PostTable.TABLE_NAME + "(" + Table._ID + " integer primary key autoincrement, "
                + PostTable.TEXT+ " text not null, "
                +PostTable.AUTHOR_ID+" integer not null);";
        sqLiteDatabase.execSQL(statement);
    }

    public static String addPrefix(String column) {
        return TABLE_NAME + "." + column;
    }
}
