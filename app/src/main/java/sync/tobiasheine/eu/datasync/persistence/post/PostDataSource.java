package sync.tobiasheine.eu.datasync.persistence.post;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.HashMap;

import sync.tobiasheine.eu.datasync.persistence.DataSource;
import sync.tobiasheine.eu.datasync.persistence.SQLHelper;
import sync.tobiasheine.eu.datasync.persistence.user.UserTable;

public class PostDataSource extends DataSource<PostEntity> implements IPostDataSource {

    protected PostDataSource(SQLHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    protected PostEntity cursorToEntity(Cursor cursor) {
        PostEntity postEntity = new PostEntity();

        postEntity.setPostId(cursor.getLong(cursor.getColumnIndex(PostTable._ID)));
        postEntity.setAuthorId(cursor.getLong(cursor.getColumnIndex(PostTable.AUTHOR_ID)));
        postEntity.setText(cursor.getString(cursor.getColumnIndex(PostTable.TEXT)));

        return postEntity;
    }


    @Override
    public Cursor cursorOnEntityById(String id, String[] projection) {
        //TODO: does not include user cols
        //checkColumns(getAllColumns());

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String table;

        StringBuilder sb = new StringBuilder();
        sb.append(PostTable.TABLE_NAME);
        sb.append(" LEFT OUTER JOIN ");
        sb.append(UserTable.TABLE_NAME);
        sb.append(" ON (");
        sb.append(PostTable.addPrefix(PostTable.AUTHOR_ID));
        sb.append(" = ");
        sb.append(UserTable.addPrefix(UserTable._ID));
        sb.append(")");
        table = sb.toString();

        queryBuilder.setTables(table);
        queryBuilder.setProjectionMap(buildColumnMap());

        return queryBuilder.query(sqLiteDatabase, projection, null,
                null, null, null, null);
    }

    @Override
    protected String getTableName() {
        return PostTable.TABLE_NAME;
    }

    @Override
    protected String getUriPath() {
        return IPostDataSource.PATH;
    }

    @Override
    protected String[] getAllColumns() {
        return PostTable.COLUMNS;
    }

    @Override
    public PostEntity createPost(long authorId, String text) {
        final ContentValues values = new ContentValues();
        values.put(PostTable.TEXT, text);
        values.put(PostTable.AUTHOR_ID, authorId);
        return createEntity(values);
    }

    /**
     * Because the tables we're joining have columns of the same name, we have to map column names to aliases.
     * The team table is the primary table here, so the alias is just the column name. For the sport table,
     * the alias is calculated by adding the table name plus "_", then the column name.
     *
     * https://github.com/thwick/android-provider-join/tree/master/joinexample
     *
     * @return
     */
    private static HashMap<String, String> buildColumnMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        String postProjection[] = PostTable.COLUMNS;
        for (String col : postProjection) {

            String qualifiedCol = PostTable.addPrefix(col);
            map.put(qualifiedCol, qualifiedCol + " as " + col);
        }

        String userProjection[] = UserTable.COLUMNS;
        for (String col : userProjection) {

            String qualifiedCol = UserTable.addPrefix(col);
            String alias = qualifiedCol.replace(".", UserTable.ALIAS_JOINER);
            map.put(qualifiedCol, qualifiedCol + " AS " + alias);
        }

        return map;
    }
}
