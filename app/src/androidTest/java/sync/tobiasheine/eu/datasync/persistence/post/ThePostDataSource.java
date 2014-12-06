package sync.tobiasheine.eu.datasync.persistence.post;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import sync.tobiasheine.eu.datasync.persistence.SQLHelper;
import sync.tobiasheine.eu.datasync.persistence.user.IUserDataSource;
import sync.tobiasheine.eu.datasync.persistence.user.UserDataSource;
import sync.tobiasheine.eu.datasync.persistence.user.UserTable;

public class ThePostDataSource extends AndroidTestCase {

    private IPostDataSource postDataSource;
    private SQLHelper dbHelper;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dbHelper = new SQLHelper(new RenamingDelegatingContext(getContext(), "test_"));
        postDataSource = new PostDataSource(dbHelper);
        postDataSource.open();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        postDataSource.close();
    }


    public void testGetPostWithUser() throws Exception {
        // given
        String userName = "Tobi";
        long userId = getUserIdForNewUser(userName);
        String postText = "test post";
        postDataSource.createPost(userId, postText);

        // when
        Cursor cursor = postDataSource.cursorOnEntityById("1", new String[]{PostTable.addPrefix(PostTable.TEXT), UserTable.addPrefix(UserTable.NAME)});

        // then
        assertEquals(1, cursor.getCount());
        cursor.moveToFirst();
        assertEquals(postText, cursor.getString(0));
        assertEquals(userName, cursor.getString(1));
    }

    private long getUserIdForNewUser(final String name) {
        final IUserDataSource userDataSource = new UserDataSource(dbHelper);
        userDataSource.open();
        userDataSource.createUser(name);

        Cursor cursor = userDataSource.cursorOnAllEntities(new String[]{UserTable._ID}, UserTable.NAME + "=?", new String[]{name}, null);
        cursor.moveToFirst();

        return cursor.getLong(0);
    }
}