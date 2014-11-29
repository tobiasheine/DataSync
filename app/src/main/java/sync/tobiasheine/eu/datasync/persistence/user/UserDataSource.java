package sync.tobiasheine.eu.datasync.persistence.user;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
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
    public List<UserEntity> getAll() {
        List<UserEntity> users = new ArrayList<UserEntity>();

        Cursor cursor = sqLiteDatabase.query(UserTable.NAME,
                UserTable.getAllColumns(), null, null, null, null, null);

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
    protected UserEntity createEntity(ContentValues contentValues) {
        long insertId = sqLiteDatabase.insert(UserTable.NAME, null, contentValues);

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
}
