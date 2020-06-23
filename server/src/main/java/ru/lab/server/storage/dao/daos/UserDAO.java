package ru.lab.server.storage.dao.daos;

import ru.lab.common.support.User;
import ru.lab.server.storage.dao.DAOException;
import ru.lab.server.storage.dataSource.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public final class UserDAO {
  private static final String SELECT_ALL = "SELECT * FROM users";
  private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";
  private static final String SELECT_BY_LOGIN = SELECT_ALL + " WHERE login = ?";
  private static final String INSERT = "INSERT INTO users (login, password) VALUES (?, ?)";
  private static final String UPDATE = "UPDATE users SET login = ?, password = ? WHERE id = ?";
  private static final String DELETE = "DELETE FROM users WHERE id  = ?";

  private final DataSource dataSource;

  public UserDAO(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  private void get(ResultSet resultSet, User user) throws SQLException {
    user.setId(resultSet.getLong(1));
    user.setLogin(resultSet.getString(2));
    user.setPassword(resultSet.getString(3));
  }

  public Set<User> getAll() throws DAOException {
    Set<User> users = new HashSet<>();
    PreparedStatement preparedStatement = dataSource.getPrepareStatement(SELECT_ALL);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        User user = new User();
        get(resultSet, user);
        users.add(user);
      }
    } catch (SQLException e) {
      throw new DAOException("Ошибка при получении всех пользователей из базы данных.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    return users;
  }

  public User getByLogin(String login) throws DAOException {
    User user = null;
    PreparedStatement preparedStatement = dataSource.getPrepareStatement(SELECT_BY_LOGIN);

    try {
      preparedStatement.setString(1, login);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        user = new User();
        get(resultSet, user);
      }
    } catch (SQLException e) {
      throw new DAOException("Ошибка при получении пользоватля по логину.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    return user;
  }

  public User getById(Long id) throws DAOException {
    User user = null;
    PreparedStatement preparedStatement = dataSource.getPrepareStatement(SELECT_BY_ID);

    try {
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        user = new User();
        get(resultSet, user);
      }
    } catch (SQLException e) {
      throw new DAOException("Ошибка при получении пользователя по id.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    return user;
  }

  public User insert(User user) throws DAOException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setString(1, user.getLogin());
      preparedStatement.setString(2, user.getPassword());

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        user.setId(generatedKeys.getLong(1));
      }
    } catch (SQLException e) {
      throw new DAOException("Ошибка при добавлении пользователя в базу данных.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    return user;
  }

  public void update(User user) throws DAOException {
    PreparedStatement preparedStatement = dataSource.getPrepareStatement(UPDATE);

    try {
      preparedStatement.setString(1, user.getLogin());
      preparedStatement.setString(2, user.getPassword());

      preparedStatement.setLong(3, user.getId());

      preparedStatement.execute();
    } catch (SQLException e) {
      throw new DAOException("Ошибка при обновлении пользователя.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }
  }

  public void delete(User user) throws DAOException {
    PreparedStatement preparedStatement = dataSource.getPrepareStatement(DELETE);

    try {
      preparedStatement.setLong(1, user.getId());

      preparedStatement.execute();
    } catch (SQLException e) {
      throw new DAOException("Ошибка при удалении пользователя.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }
  }
}
