package sync.tobiasheine.eu.datasync.persistence.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import sync.tobiasheine.eu.datasync.persistence.DataSource;
import sync.tobiasheine.eu.datasync.persistence.SQLHelper;

public class UserDataSource extends DataSource<UserEntity> implements IUserDataSource {

    public UserDataSource(SQLHelper dbHelper) {
        super(dbHelper);
    }

    public UserEntity createUser(final String name) {
        final ContentValues values = new ContentValues();
        values.put(UserTable.Column.NAME.name(), name);
        return createEntity(values);
    }

    @Override
    public Cursor cursorOnEntityById(final String userId, final String[] projection) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UserTable.NAME);
        queryBuilder.appendWhere(UserTable.Column.USER_ID + "=" + userId);

        return queryBuilder.query(sqLiteDatabase, projection, null,
                null, null, null, null);
    }


    @Override
    public List<UserEntity> getAllEntities(final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        Cursor cursor = cursorOnAllEntities(projection, selection, selectionArgs, sortOrder);

        List<UserEntity> users = new ArrayList<UserEntity>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            UserEntity userEntity = cursorToEntity(cursor);
            users.add(userEntity);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return users;
    }

    @Override
    public Cursor cursorOnAllEntities(final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        checkColumns(projection);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UserTable.NAME);

        return queryBuilder.query(sqLiteDatabase, projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insertEntity(ContentValues values) {
        long id = getIdForNewEntity(values);

        return Uri.parse(USER_PATH + "/" + id);
    }

    @Override
    public int deleteEntity(final String id, final String selection, final String[] selectionArgs) {
        int rowsDeleted;
        if (TextUtils.isEmpty(selection)) {
            rowsDeleted = sqLiteDatabase.delete(UserTable.NAME,
                    UserTable.Column.USER_ID + "=" + id,
                    null);
        } else {
            rowsDeleted = sqLiteDatabase.delete(UserTable.NAME,
                    UserTable.Column.USER_ID + "=" + id
                            + " and " + selection,
                    selectionArgs);
        }

        return rowsDeleted;
    }

    @Override
    public int deleteAll(String selection, String[] selectionArgs) {
        return sqLiteDatabase.delete(UserTable.NAME, selection, selectionArgs);
    }

    @Override
    public int updateEntity(String id, ContentValues values, final String selection, final String[] selectionArgs) {
        int rowsUpdated;
        if (TextUtils.isEmpty(selection)) {
            rowsUpdated = sqLiteDatabase.update(UserTable.NAME,
                    values,
                    UserTable.Column.USER_ID + "=" + id,
                    null);
        } else {
            rowsUpdated = sqLiteDatabase.update(UserTable.NAME,
                    values,
                    UserTable.Column.USER_ID + "=" + id
                            + " and "
                            + selection,
                    selectionArgs);
        }
        return rowsUpdated;
    }

    @Override
    public int updateAll(ContentValues values, String selection, String[] selectionArgs) {
        return sqLiteDatabase.update(UserTable.NAME,
                values,
                selection,
                selectionArgs);
    }

    private long getIdForNewEntity(final ContentValues contentValues) {
        return sqLiteDatabase.insert(UserTable.NAME, null, contentValues);
    }

    @Override
    protected UserEntity createEntity(ContentValues contentValues) {
        long insertId = getIdForNewEntity(contentValues);

        Cursor cursor = sqLiteDatabase.query(UserTable.NAME,
                UserTable.getAllColumns(), UserTable.Column.USER_ID.name() + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();

        UserEntity user = cursorToEntity(cursor);
        cursor.close();

        return user;
    }

    @Override
    protected UserEntity cursorToEntity(Cursor cursor) {
        UserEntity user = new UserEntity();
        user.setUserId(cursor.getLong(0));
        user.setName(cursor.getString(1));
        return user;
    }

    @Override
    protected void checkColumns(String[] projection) {
        String[] available = UserTable.getAllColumns();

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    @Override
    protected String getTableName() {
        return UserTable.NAME;
    }

    @Override
    protected String[] getAllColumns() {
        return UserTable.getAllColumns();
    }
}
