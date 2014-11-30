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

    protected abstract void checkColumns(String[] projection);

    protected abstract String getTableName();

    protected abstract String[] getAllColumns();

    @Override
    public void open() throws SQLException {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = dbHelper.getWritableDatabase();
        }
    }

    @Override
    public void close() {
        dbHelper.close();
    }
}
