package sync.tobiasheine.eu.datasync.persistence.user;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Arrays;
import java.util.HashSet;

import sync.tobiasheine.eu.datasync.persistence.DataSource;
import sync.tobiasheine.eu.datasync.persistence.SQLHelper;

public class UserDataSource extends DataSource<UserEntity> implements IUserDataSource {

    public UserDataSource(SQLHelper dbHelper) {
        super(dbHelper);
    }

    public UserEntity createUser(final String name) {
        final ContentValues values = new ContentValues();
        values.put(UserTable.NAME, name);
        return createEntity(values);
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
        String[] available = UserTable.COLUMNS;

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
        return UserTable.TABLE_NAME;
    }

    @Override
    protected String getUriPath() {
        return PATH;
    }

    @Override
    protected String[] getAllColumns() {
        return UserTable.COLUMNS;
    }
}
