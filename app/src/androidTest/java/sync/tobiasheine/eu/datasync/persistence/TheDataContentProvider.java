package sync.tobiasheine.eu.datasync.persistence;

import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.test.ProviderTestCase2;

import java.util.ArrayList;
import java.util.List;

import sync.tobiasheine.eu.datasync.persistence.user.UserTable;

import static java.util.Arrays.asList;

public class TheDataContentProvider extends ProviderTestCase2<DataContentProvider> {

    public TheDataContentProvider() {
        super(DataContentProvider.class, DataContentProvider.AUTHORITY);
    }

    public void testInsertUser() throws Exception {
        // when
        insertUsers(asList("tobi"));

        // then
        Cursor cursor = getMockContentResolver().query(DataContentProvider.USER_CONTENT_URI, UserTable.getAllColumns(), null, null, null);
        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex(UserTable.Column.NAME.name());
        assertEquals("tobi", cursor.getString(nameColumnIndex));
    }

    public void testQueryForUserById() throws Exception {
        // given
        String tobi = "tobi";
        String paul = "paul";

        insertUsers(asList(tobi, paul));

        // when
        Cursor cursor = getMockContentResolver().query(ContentUris.withAppendedId(DataContentProvider.USER_CONTENT_URI, 2), UserTable.getAllColumns(), null, null, null);
        cursor.moveToFirst();

        // then
        int nameColumnIndex = cursor.getColumnIndex(UserTable.Column.NAME.name());
        assertEquals("paul", cursor.getString(nameColumnIndex));
    }

    private void insertUsers(final List<String> userNames) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        for (String name : userNames) {
            ContentProviderOperation operation = ContentProviderOperation.newInsert(DataContentProvider.USER_CONTENT_URI).withValue(UserTable.Column.NAME.name(), name).build();
            operations.add(operation);
        }
        getMockContentResolver().applyBatch(DataContentProvider.AUTHORITY, operations);
    }
}