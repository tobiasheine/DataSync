package sync.tobiasheine.eu.datasync.persistence.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.List;

import sync.tobiasheine.eu.datasync.persistence.SQLHelper;

public class TheUserDataSource extends AndroidTestCase {

    private IUserDataSource userDataSource;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        userDataSource = new UserDataSource(new SQLHelper(new RenamingDelegatingContext(getContext(), "test_")));
        userDataSource.open();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        userDataSource.close();
    }

    public void testCreateUser() throws Exception {
        // when
        UserEntity tobi = userDataSource.createUser("Tobi");

        // then
        assertEquals("Tobi", tobi.getName());
    }

    public void testGetAllEntities() throws Exception {
        // given
        userDataSource.createUser("Tobi");
        userDataSource.createUser("Tom");

        // when
        List<UserEntity> allUsers = userDataSource.getAllEntities(UserTable.COLUMNS, null, null, null);

        // then
        assertEquals(2, allUsers.size());
    }

    public void testCursorOnUserById() throws Exception {
        // given
        userDataSource.createUser("Tobi");
        userDataSource.createUser("Tom");

        // when
        Cursor userById = userDataSource.cursorOnEntityById("2", new String[]{UserTable.NAME});
        userById.moveToFirst();

        // then
        String userName = userById.getString(0);
        assertEquals("Tom", userName);
    }

    public void testCursorOnAll() throws Exception {
        // given
        userDataSource.createUser("Tobi");
        userDataSource.createUser("Tom");

        // when
        Cursor list = userDataSource.cursorOnAllEntities(UserTable.COLUMNS, null, null, null);

        // then
        assertEquals(2, list.getCount());
    }

    public void testInsertEntity() throws Exception {
        // given
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.NAME, "Tobi");

        // when
        Uri uriToEntity = userDataSource.insertEntity(contentValues);

        // then
        assertEquals(Uri.parse(IUserDataSource.PATH + "/1"), uriToEntity);
    }

    public void testDeleteEntityById() throws Exception {
        // given
        userDataSource.createUser("Tobi");
        userDataSource.createUser("Tom");

        // when
        userDataSource.deleteEntity("1", null, null);

        // then
        Cursor cursor = userDataSource.cursorOnAllEntities(new String[]{UserTable.NAME}, null, null, null);
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        String userName = cursor.getString(0);
        assertEquals("Tom", userName);
    }

    public void testDeleteAll() throws Exception {
        // given
        userDataSource.createUser("Tobi");
        userDataSource.createUser("Tom");

        // when
        userDataSource.deleteAll(null, null);

        // then
        Cursor cursor = userDataSource.cursorOnAllEntities(UserTable.COLUMNS, null, null, null);
        assertTrue(cursor.isAfterLast());
    }

    public void testUpdateById() throws Exception {
        // given
        userDataSource.createUser("Tobi");
        userDataSource.createUser("Tom");

        // when
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.NAME, "Peter");
        userDataSource.updateEntity("2", contentValues, null, null);

        // then
        Cursor cursor = userDataSource.cursorOnEntityById("2", new String[]{UserTable.NAME});
        cursor.moveToFirst();
        assertEquals("Peter", cursor.getString(0));
    }
}