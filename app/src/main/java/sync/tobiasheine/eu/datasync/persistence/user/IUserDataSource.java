package sync.tobiasheine.eu.datasync.persistence.user;

import sync.tobiasheine.eu.datasync.persistence.IDataSource;

public interface IUserDataSource extends IDataSource<UserEntity>{

    public static final String PATH = "users";
    public static final int URI_CODE_USERS = 1;
    public static final int URI_CODE_USERS_ID = 2;

    UserEntity createUser(final String name);

}
