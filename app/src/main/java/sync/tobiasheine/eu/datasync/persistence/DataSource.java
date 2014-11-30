package sync.tobiasheine.eu.datasync.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class DataSource<ENTITY> implements IDataSource<ENTITY> {

    private final SQLHelper dbHelper;

    protected SQLiteDatabase sqLiteDatabase;

    protected DataSource(SQLHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    protected abstract ENTITY cursorToEntity(final Cursor cursor);

    protected abstract void checkColumns(String[] projection);

    protected abstract String getTableName();

    protected abstract String getUriPath();

    @Override
    public List<ENTITY> getAllEntities(final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        Cursor cursor = cursorOnAllEntities(projection, selection, selectionArgs, sortOrder);

        List<ENTITY> entities = new ArrayList<ENTITY>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ENTITY entity = cursorToEntity(cursor);
            entities.add(entity);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return entities;
    }

    @Override
    public Cursor cursorOnAllEntities(final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        checkColumns(projection);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(getTableName());

        return queryBuilder.query(sqLiteDatabase, projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    @Override
    public Cursor cursorOnEntityById(final String id, final String[] projection) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(getTableName());
        queryBuilder.appendWhere(Table._ID + "=" + id);

        return queryBuilder.query(sqLiteDatabase, projection, null,
                null, null, null, null);
    }

    @Override
    public Uri insertEntity(ContentValues values) {
        long id = getIdForNewEntity(values);

        return Uri.parse(getUriPath() + "/" + id);
    }

    @Override
    public int deleteEntity(final String id, final String selection, final String[] selectionArgs) {
        int rowsDeleted;
        if (TextUtils.isEmpty(selection)) {
            rowsDeleted = sqLiteDatabase.delete(getTableName(),
                    Table._ID + "=" + id,
                    null);
        } else {
            rowsDeleted = sqLiteDatabase.delete(getTableName(),
                    Table._ID + "=" + id
                            + " and " + selection,
                    selectionArgs);
        }

        return rowsDeleted;
    }

    @Override
    public int deleteAll(String selection, String[] selectionArgs) {
        return sqLiteDatabase.delete(getTableName(), selection, selectionArgs);
    }

    @Override
    public int updateEntity(String id, ContentValues values, final String selection, final String[] selectionArgs) {
        int rowsUpdated;
        if (TextUtils.isEmpty(selection)) {
            rowsUpdated = sqLiteDatabase.update(getTableName(),
                    values,
                    Table._ID + "=" + id,
                    null);
        } else {
            rowsUpdated = sqLiteDatabase.update(getTableName(),
                    values,
                    Table._ID + "=" + id
                            + " and "
                            + selection,
                    selectionArgs);
        }
        return rowsUpdated;
    }

    @Override
    public int updateAll(ContentValues values, String selection, String[] selectionArgs) {
        return sqLiteDatabase.update(getTableName(),
                values,
                selection,
                selectionArgs);
    }

    @Override
    public ENTITY createEntity(ContentValues contentValues) {
        long insertId = getIdForNewEntity(contentValues);

        Cursor cursor = sqLiteDatabase.query(getTableName(),
                getAllColumns(), Table._ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        ENTITY entity = cursorToEntity(cursor);
        cursor.close();

        return entity;
    }

    private long getIdForNewEntity(final ContentValues contentValues) {
        return sqLiteDatabase.insert(getTableName(), null, contentValues);
    }

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

    protected abstract String[] getAllColumns();
}
