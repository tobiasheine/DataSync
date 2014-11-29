package sync.tobiasheine.eu.datasync.persistence;

import java.util.List;

public interface IDataSource<ENTITY> {
    List<ENTITY> getAll();

    void open();

    void close();
}
