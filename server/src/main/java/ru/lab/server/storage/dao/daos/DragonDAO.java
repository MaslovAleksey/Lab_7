package ru.lab.server.storage.dao.daos;

import ru.lab.common.beg_data.Color;
import ru.lab.common.beg_data.Dragon;
import ru.lab.common.beg_data.DragonType;
import ru.lab.server.storage.dao.DAOException;
import ru.lab.server.storage.dataSource.DataSource;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public final class DragonDAO {
  private static final String SELECT_ALL = "SELECT * FROM dragons";
  private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";
  private static final String INSERT =
      "INSERT INTO dragons (owner_id, key, name, coordinates_x, coordinates_y, creation_date, age, speaking, color, dragon_type, depth, number_of_treasures) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String UPDATE =
      "UPDATE dragons SET owner_id = ?, key = ?, name = ?, coordinates_x = ?, coordinates_y = ?, creation_date = ?, age = ?, speaking = ?, color = ?, dragon_type = ?, depth = ?, number_of_treasures = ? WHERE id = ?";
  private static final String DELETE = "DELETE FROM dragons WHERE id  = ?";

  private final DataSource dataSource;

  public DragonDAO(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  private Color getColorFromString(String colorString) {
    try {
      return Color.valueOf(colorString);
    } catch (IllegalArgumentException | NullPointerException e) {
      return null;
    }
  }

  private DragonType getDragonTypeFromString(String dragonTypeString) {
    try {
      return DragonType.valueOf(dragonTypeString);
    } catch (IllegalArgumentException | NullPointerException e) {
      return null;
    }
  }

  public Map<Integer, Dragon> getAll() throws DAOException {
    Map<Integer, Dragon> dragons = new HashMap<>();
    PreparedStatement preparedStatement = dataSource.getPrepareStatement(SELECT_ALL);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Dragon dragon = new Dragon();

        dragon.setId(resultSet.getInt(1));
        dragon.setOwnerId(resultSet.getInt(2));
        dragon.setName(resultSet.getString(4));
        dragon.setCoordinates(resultSet.getDouble(5), resultSet.getInt(6));
        dragon.setCreationDate(resultSet.getTimestamp(7).toLocalDateTime());
        dragon.setAge(resultSet.getLong(8));
        dragon.setSpeaking(resultSet.getBoolean(9));
        dragon.setColor(getColorFromString(resultSet.getString(10)));
        dragon.setDragonType(getDragonTypeFromString(resultSet.getString(11)));
        dragon.setDragonCave(resultSet.getInt(12), resultSet.getFloat(13));

        dragons.put(resultSet.getInt(3), dragon);
      }
    } catch (SQLException e) {
      throw new DAOException("Ошибка при получении всех драконов из базы данных.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    return dragons;
  }

  public Map<Integer, Dragon> getById(int id) throws DAOException {
    Map<Integer, Dragon> dragons = new HashMap<>();
    PreparedStatement preparedStatement = dataSource.getPrepareStatement(SELECT_BY_ID);

    try {
      preparedStatement.setInt(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Dragon dragon = new Dragon();

        dragon.setId(resultSet.getInt(1));
        dragon.setOwnerId(resultSet.getInt(2));
        dragon.setName(resultSet.getString(4));
        dragon.setCoordinates(resultSet.getDouble(5), resultSet.getInt(6));
        dragon.setCreationDate(resultSet.getTimestamp(7).toLocalDateTime());
        dragon.setAge(resultSet.getLong(8));
        dragon.setSpeaking(resultSet.getBoolean(9));
        dragon.setColor(getColorFromString(resultSet.getString(10)));
        dragon.setDragonType(getDragonTypeFromString(resultSet.getString(11)));
        dragon.setDragonCave(resultSet.getInt(12), resultSet.getFloat(13));

        dragons.put(resultSet.getInt(3), dragon);
      }
    } catch (SQLException e) {
      throw new DAOException("Ошибка при получении дракона по id из базы данных.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    return dragons;
  }

  public Dragon insert(int key, Dragon dragon) throws DAOException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setInt(1, dragon.getOwnerId());
      preparedStatement.setInt(2, key);
      preparedStatement.setString(3, dragon.getName());
      if (dragon.getCoordinates() == null) {
        preparedStatement.setNull(4, Types.DOUBLE);
        preparedStatement.setNull(5, Types.INTEGER);
      } else {
        preparedStatement.setDouble(4, dragon.getCoordinates().getX());
        preparedStatement.setInt(5, dragon.getCoordinates().getY());
      }
      preparedStatement.setTimestamp(6, Timestamp.valueOf(dragon.getCreationDate()));
      preparedStatement.setLong(7, dragon.getAge());
      preparedStatement.setBoolean(8, dragon.getSpeaking());
      preparedStatement.setString(9, dragon.getColor());
      preparedStatement.setString(10, dragon.getDragonType());
      if (dragon.getDragonCave() == null) {
        preparedStatement.setNull(11, Types.INTEGER);
        preparedStatement.setNull(12, Types.FLOAT);
      } else {
        if (dragon.getDragonCave().getDepth() != null) {
          preparedStatement.setInt(11, dragon.getDragonCave().getDepth());
        } else {
          preparedStatement.setNull(11, Types.INTEGER);
        }
        preparedStatement.setFloat(12, dragon.getDragonCave().getNumberOfTreasures());
      }

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        dragon.setId(generatedKeys.getInt(1));
      }
    } catch (SQLException e) {
      throw new DAOException("Ошибка при добавлении дракона в базу данных.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    return dragon;
  }

  public void update(int key, Dragon dragon) throws DAOException {
    PreparedStatement preparedStatement = dataSource.getPrepareStatement(UPDATE);

    try {
      preparedStatement.setInt(1, dragon.getOwnerId());
      preparedStatement.setInt(2, key);
      preparedStatement.setString(3, dragon.getName());
      if (dragon.getCoordinates() == null) {
        preparedStatement.setNull(4, Types.DOUBLE);
        preparedStatement.setNull(5, Types.INTEGER);
      } else {
        preparedStatement.setDouble(4, dragon.getCoordinates().getX());
        preparedStatement.setInt(5, dragon.getCoordinates().getY());
      }
      preparedStatement.setTimestamp(6, Timestamp.valueOf(dragon.getCreationDate()));
      preparedStatement.setLong(7, dragon.getAge());
      preparedStatement.setBoolean(8, dragon.getSpeaking());
      preparedStatement.setString(9, dragon.getColor());
      preparedStatement.setString(10, dragon.getDragonType());
      if (dragon.getDragonCave() == null) {
        preparedStatement.setNull(11, Types.INTEGER);
        preparedStatement.setNull(12, Types.FLOAT);
      } else {
        if (dragon.getDragonCave().getDepth() != null) {
          preparedStatement.setInt(11, dragon.getDragonCave().getDepth());
        } else {
          preparedStatement.setNull(11, Types.INTEGER);
        }
        preparedStatement.setFloat(12, dragon.getDragonCave().getNumberOfTreasures());
      }

      preparedStatement.setInt(13, dragon.getId());

      preparedStatement.execute();
    } catch (SQLException e) {
      throw new DAOException("Ошибка при обновлении дракона.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }
  }

  public void delete(Dragon dragon) throws DAOException {
    PreparedStatement preparedStatement = dataSource.getPrepareStatement(DELETE);

    try {
      preparedStatement.setLong(1, dragon.getId());

      preparedStatement.execute();
    } catch (SQLException e) {
      throw new DAOException("Ошибка при удалении дракона.");
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }
  }
}
