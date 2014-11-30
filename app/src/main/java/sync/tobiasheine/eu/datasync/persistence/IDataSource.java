package sync.tobiasheine.eu.datasync.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

public interface IDataSource<ENTITY> {
    List<ENTITY> getAllEntities(String[] projection, String selection, String[] selectionArgs, String sortOrder);

    Cursor cursorOnAllEntities(final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder);

    Cursor cursorOnEntityById(final String userId, final String[] projection);

    Uri insertEntity(final ContentValues values);

    int deleteEntity(final String id, final String selection, final String[] selectionArgs);

    int deleteAll(final String selection, final String[] selectionArgs);

    int updateEntity(final String id, final ContentValues values, final String selection, final String[] selectionArgs);

    int updateAll(final ContentValues values, final String selection, final String[] selectionArgs);

    void open();

    void close();
}
