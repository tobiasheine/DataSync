package sync.tobiasheine.eu.datasync.persistence.user;

public class UserTable {
    public static final String NAME = "users";

    public enum Column {
        USER_ID,
        NAME;
    }

    public static String[] getAllColumns() {
        final String[] columns = new String[Column.values().length];

        for (int i = 0; i < Column.values().length; i++) {
            columns[i] = Column.values()[i].name();
        }

        return columns;
    }

}
