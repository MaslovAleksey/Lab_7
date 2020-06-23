package ru.lab.server.storage.dataSource;

import ru.lab.server.storage.dao.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class DataSource {
  protected final Connection connection;

  private final String url;
  private final String user;
  private final String password;

  public DataSource(String user, String password) throws DataSourceException {
    this.url = ResourceBundle.getBundle("config").getString("url");
    this.user = user;
    this.password = password;

    connection = setupConnection();
  }

  /**
   * Устанавливает соедниение с базой данных.
   *
   * @return соединение с базой данных
   * @throws DataSourceException - ошибка соединения
   */
  private Connection setupConnection() throws DataSourceException {
    Connection connection;

    try {
      connection = DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
      throw new DataSourceException(
          "Ошибка подключения к базе данных. Проверье URL, логин и пароль.", e);
    }

    return connection;
  }

  /**
   * Открывает запрос
   *
   * @param sqlStatement sql строка
   * @return запрос для заданной sql строки.
   * @throws DAOException - ошибка открытия запроса
   */
  public final PreparedStatement getPrepareStatement(String sqlStatement) throws DAOException {
    PreparedStatement preparedStatement;
    try {
      preparedStatement = connection.prepareStatement(sqlStatement);
    } catch (SQLException e) {
      throw new DAOException("Ошибка подготовки SQL выражения.");
    }

    return preparedStatement;
  }

  /**
   * Открывает запрос
   *
   * @param sqlStatement sql строка
   * @param statement параметр запроса
   * @return запрос для заданной sql строки.
   * @throws DAOException - ошибка открытия запроса
   */
  public final PreparedStatement getPrepareStatement(String sqlStatement, int statement)
      throws DAOException {
    PreparedStatement preparedStatement;
    try {
      preparedStatement = connection.prepareStatement(sqlStatement, statement);
    } catch (SQLException e) {
      throw new DAOException("Ошибка подготовки SQL выражения.");
    }

    return preparedStatement;
  }

  /**
   * Закрывает запрос.
   *
   * @param preparedStatement запрос
   * @throws DAOException - ошибка закрытия запроса
   */
  public final void closePrepareStatement(PreparedStatement preparedStatement) throws DAOException {
    if (preparedStatement != null) {
      try {
        preparedStatement.close();
      } catch (SQLException e) {
        throw new DAOException("Ошибка закрытия SQL выражения.");
      }
    }
  }

  /**
   * Закрывает соединение с базой данных.
   *
   * @throws DataSourceException - ошибка закрытия соединения
   */
  public final void closeConnection() throws DataSourceException {
    if (connection == null) return;
    try {
      connection.close();
    } catch (SQLException e) {
      throw new DataSourceException("Ошибка закрытия подключения с базой данных.");
    }
  }
}
