package sync.tobiasheine.eu.datasync.persistence;

import android.database.sqlite.SQLiteDatabase;

public abstract class Table {

    public abstract String getName();
    public abstract String[] getAllColumns();
    public abstract void createTable(final SQLiteDatabase sqLiteDatabase);

}
