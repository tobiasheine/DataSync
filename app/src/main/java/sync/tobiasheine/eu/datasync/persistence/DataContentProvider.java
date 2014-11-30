package sync.tobiasheine.eu.datasync.persistence;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import sync.tobiasheine.eu.datasync.persistence.user.IUserDataSource;
import sync.tobiasheine.eu.datasync.persistence.user.UserDataSource;

import static sync.tobiasheine.eu.datasync.persistence.user.UserDataSource.URI_CODE_USERS;
import static sync.tobiasheine.eu.datasync.persistence.user.UserDataSource.URI_CODE_USERS_ID;

public class DataContentProvider extends ContentProvider {

    public static final String AUTHORITY = "eu.tobiasheine.datasync.contentprovider";

    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + IUserDataSource.PATH);

    private static final UriMatcher URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URIMatcher.addURI(AUTHORITY, UserDataSource.PATH, URI_CODE_USERS);
        URIMatcher.addURI(AUTHORITY, UserDataSource.PATH + "/#", URI_CODE_USERS_ID);
    }

    private IUserDataSource userDataSource;

    @Override
    public boolean onCreate() {
        userDataSource = new UserDataSource(new SQLHelper(getContext()));
        userDataSource.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final Cursor cursor;

        int uriType = URIMatcher.match(uri);
        switch (uriType) {
            case URI_CODE_USERS:
                cursor = userDataSource.cursorOnAllEntities(projection, selection, selectionArgs, sortOrder);
                break;
            case URI_CODE_USERS_ID:
                cursor = userDataSource.cursorOnEntityById(uri.getLastPathSegment(), projection);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = URIMatcher.match(uri);

        Uri resultUri;
        switch (uriType) {
            case URI_CODE_USERS:
                resultUri = userDataSource.insertEntity(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;

        int uriType = URIMatcher.match(uri);
        switch (uriType) {
            case URI_CODE_USERS:
                rowsDeleted = userDataSource.deleteAll(selection, selectionArgs);
                break;
            case URI_CODE_USERS_ID:
                rowsDeleted = userDataSource.deleteEntity(uri.getLastPathSegment(), selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated;

        int uriType = URIMatcher.match(uri);
        switch (uriType) {
            case URI_CODE_USERS:
                rowsUpdated = userDataSource.deleteAll(selection, selectionArgs);
                break;
            case URI_CODE_USERS_ID:
                String id = uri.getLastPathSegment();
                rowsUpdated = userDataSource.deleteEntity(id, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
