package sync.tobiasheine.eu.datasync.persistence.post;

import sync.tobiasheine.eu.datasync.persistence.IDataSource;
import sync.tobiasheine.eu.datasync.persistence.user.UserEntity;

public interface IPostDataSource extends IDataSource<PostEntity>{

    public static final String PATH = "posts";
    public static final int URI_CODE_USERS = 3;
    public static final int URI_CODE_USERS_ID = 4;

    PostEntity createPost(final long authorId, final String text);

}
