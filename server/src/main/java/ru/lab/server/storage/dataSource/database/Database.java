package ru.lab.server.storage.dataSource.database;

import ru.lab.server.storage.dao.DAOException;
import ru.lab.server.storage.dataSource.DataSource;
import ru.lab.server.storage.dataSource.DataSourceException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class Database extends DataSource {
  private final String CREATE_IF_NOT_EXISTS_DRAGONS_TABLE =
      "CREATE TABLE IF NOT EXISTS dragons ("
          + "id SERIAL NOT NULL PRIMARY KEY, "
          + "owner_id SERIAL NOT NULL, "
          + "key INT NOT NULL, "
          + "name VARCHAR NOT NULL CHECK(LENGTH(name)>0), "
          + "coordinates_x REAL NOT NULL CHECK(coordinates_x>-704), "
          + "coordinates_y INT NOT NULL CHECK(coordinates_y<=28), "
          + "creation_date DATE NOT NULL, "
          + "age REAL NOT NULL CHECK(age>0), "
          + "speaking BOOL NOT NULL, "
          + "color VARCHAR NULL, "
          + "dragon_type VARCHAR NULL, "
          + "depth INT NULL, "
          + "number_of_treasures REAL NOT NULL CHECK(number_of_treasures>0), "
          + "FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE)";

  private final String CREATE_IF_NOT_EXISTS_USERS_TABLE =
      "CREATE TABLE IF NOT EXISTS users ("
          + "id SERIAL NOT NULL PRIMARY KEY, "
          + "login VARCHAR UNIQUE NOT NULL, "
          + "password VARCHAR NOT NULL)";

  public Database(String user, String password) throws DataSourceException, DatabaseException {
    super(user, password);
    initUsersTable();
    initDragonsTable();
  }

  /**
   * Инициализирует таблицу пользователей. Если таблица не существует - создается новая.
   *
   * @throws DatabaseException - ошибка инициализации
   */
  private void initUsersTable() throws DatabaseException {
    PreparedStatement preparedStatement = getPrepareStatement(CREATE_IF_NOT_EXISTS_USERS_TABLE);

    try {
      preparedStatement.executeUpdate();
    } catch (SQLException | DAOException e) {
      throw new DatabaseException("Ошибка при создании таблицы пользователей.", e);
    } finally {
      closePrepareStatement(preparedStatement);
    }
  }

  /**
   * Инициализирует таблицу драконов. Если таблица не существует - создается новая.
   *
   * @throws DatabaseException - ошибка инициализации
   */
  private void initDragonsTable() throws DatabaseException {
    PreparedStatement preparedStatement = getPrepareStatement(CREATE_IF_NOT_EXISTS_DRAGONS_TABLE);

    try {
      preparedStatement.executeUpdate();
    } catch (SQLException | DAOException e) {
      throw new DatabaseException("Ошибка при создании таблицы драконов.", e);
    } finally {
      closePrepareStatement(preparedStatement);
    }
  }
}
