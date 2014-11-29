package sync.tobiasheine.eu.datasync.persistence.user;

import sync.tobiasheine.eu.datasync.persistence.IDataSource;

public interface IUserDataSource extends IDataSource<UserEntity>{

    UserEntity createUser(final String name);
}
