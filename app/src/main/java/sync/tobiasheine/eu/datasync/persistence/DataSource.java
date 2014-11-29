package sync.tobiasheine.eu.datasync.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public abstract class DataSource<ENTITY> implements IDataSource<ENTITY> {

    private final SQLHelper dbHelper;

    protected SQLiteDatabase sqLiteDatabase;

    protected DataSource(SQLHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    protected abstract ENTITY createEntity(final ContentValues contentValues);

    protected abstract ENTITY cursorToEntity(final Cursor cursor);

    @Override
    public void open() throws SQLException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    @Override
    public void close() {
        dbHelper.close();
    }
}
