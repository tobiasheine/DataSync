package sync.tobiasheine.eu.datasync.persistence.user;

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

    public void testCreatesAUser() throws Exception {
        // when
        UserEntity tobi = userDataSource.createUser("Tobi");

        // then
        assertEquals("Tobi", tobi.getName());
    }

    public void testReturnsAllUsers() throws Exception {
        // given
        userDataSource.createUser("Tobi");
        userDataSource.createUser("Tom");

        // when
        List<UserEntity> allUsers = userDataSource.getAll();

        // then
        assertEquals(2, allUsers.size());
    }
}