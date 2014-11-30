package sync.tobiasheine.eu.datasync.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sync.tobiasheine.eu.datasync.persistence.user.UserTable;

public class SQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "datasync.db";
    private static final int DATABASE_VERSION = 1;

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UserTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nop
    }


}
