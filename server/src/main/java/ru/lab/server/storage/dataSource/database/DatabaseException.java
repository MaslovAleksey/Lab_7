package ru.lab.server.storage.dataSource.database;

import ru.lab.server.storage.dataSource.DataSourceException;

public class DatabaseException extends DataSourceException {
  public DatabaseException() {
    super();
  }

  public DatabaseException(String message) {
    super(message);
  }

  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public DatabaseException(Throwable cause) {
    super(cause);
  }
}
