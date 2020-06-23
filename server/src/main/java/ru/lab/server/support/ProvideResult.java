package ru.lab.server.support;

import ru.lab.common.beg_data.Dragon;
import ru.lab.common.response.AvgAgeResponse;
import ru.lab.common.response.CountResponse;
import ru.lab.common.response.InfoResponse;
import ru.lab.common.support.ObjToServer;
import ru.lab.common.support.User;
import ru.lab.server.storage.dao.DAOException;
import ru.lab.server.storage.dao.daos.DragonDAO;
import ru.lab.server.storage.dao.daos.UserDAO;
import ru.lab.server.support.hashGenerator.HashGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ProvideResult {
  /** Поле для хранения элемента коллекции, переданного от клиента */
  private Dragon dr;

  private User user;

  private void authentication(HashGenerator hashGenerator, UserDAO userDAO, User user)
      throws NotAuthorizedException {
    if (user == null || user.getLogin() == null || user.getPassword() == null) {
      throw new NotAuthorizedException("Неверные данные пользователя");
    }

    User found = userDAO.getByLogin(user.getLogin());

    if (found == null) {
      throw new NotAuthorizedException("Пользователь не зарегестрирован");
    }

    if (!hashGenerator
        .generateHashWithPepperAndSalt(user.getPassword())
        .equals(found.getPassword())) {
      throw new NotAuthorizedException("Неверный пароль пользователя");
    }

    this.user = found;
  }

  public Serializable provideResult(
      ObjToServer objServer,
      Map<Integer, Dragon> dragons,
      DragonDAO dragonDAO,
      UserDAO userDAO,
      HashGenerator hashGenerator,
      List<String> commandsHistory)
      throws DAOException {
    if (!objServer.getCommand().equals("history")) commandsHistory.add(objServer.getCommand());

    if (!objServer.getCommand().equals("register")) {
      try {
        authentication(hashGenerator, userDAO, objServer.getUser());
      } catch (NotAuthorizedException e) {
        return "Ошибка авторизации: " + e.getMessage();
      }
    }

    String command = objServer.getCommand();
    switch (command) {
      case "info":
        return info(dragons);

      case "show":
        return sortByName(dragons);

      case "help":
        List<String> helpCommand = new LinkedList<String>() {};
        helpCommand.add("help - вывод справки по доступным командам");
        helpCommand.add(
            "info - вывод в стандартный поток вывода информации о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        helpCommand.add(
            "show - вывод в стандартный поток вывода всех элементы коллекции в строковом представлении");
        helpCommand.add("insert_key {element} - добавление нового элемента с заданным ключом");
        helpCommand.add(
            "update_id {element} - обновление значения элемента коллекции, id которого равен заданному");
        helpCommand.add("remove_key {key} - удаление элемента из коллекции по его ключу");
        helpCommand.add("clear - очищение коллекции");
        helpCommand.add(
            "execute_script {file_name} - считывание и исполнение скрипта из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        helpCommand.add("save - сохрание коллекции в файл\n");
        helpCommand.add("exit - завершение программы");
        helpCommand.add("history - вывод последних 6 команд (без их аргументов)");
        helpCommand.add(
            "replace_if_lowe_key {element} - замена значения по ключу, если новое значение меньше старого");
        helpCommand.add(
            "remove_greater_key {key} - удаление из коллекции всех элементов, ключ которых превышает заданный");
        helpCommand.add(
            "average_of_age - вывод среднего значения поля age для всех элементов коллекции");
        helpCommand.add(
            "count_less_then_cave {cave} - вывод количества элементов, значение поля cave которых меньше заданного");
        helpCommand.add("print_ascending - вывод элементов коллекции в порядке возрастания");
        helpCommand.add("login - войти в систему");
        helpCommand.add("register - зарегестрироавть нового пользователя");
        helpCommand.add(
            "<ctrl> + <d> - завершение пользовательского ввода(вызов возможен в произвольном месте при вводе данных )");
        return new LinkedList<>(helpCommand);
      case "clear":
        for (Map.Entry<Integer, Dragon> dragonEntry : dragons.entrySet()) {
          if (dragonEntry.getValue().getOwnerId() == user.getId()) {
            dragonDAO.delete(dragonEntry.getValue());
            dragons.remove(dragonEntry.getKey());
          }
        }
        return sortByName(dragons);

      case "history":
        long elementsToSkip = 0;
        if (commandsHistory.size() > 6) {
          elementsToSkip = commandsHistory.size() - 6;
        }
        LinkedList<String> resultList =
            commandsHistory.stream()
                .skip(elementsToSkip) // Пропускает первые элементы
                .collect(Collectors.toCollection(LinkedList::new));
        Collections.reverse(resultList); // чтобы последняя команда была сверху
        return resultList;

      case "average_of_age":
        double avg = dragons.values().stream().mapToLong(Dragon::getAge).average().orElse(0.0);
        return new AvgAgeResponse(avg);

      case "print_ascending":
        return dragons.values().stream().sorted().collect(Collectors.toCollection(LinkedList::new));

      case "remove_key":
        Dragon dragon = dragons.get(objServer.getValue());
        if (dragon == null) {
          return "Дракона с данным ключем не существует";
        }
        if (dragon.getOwnerId() == user.getId()) {
          dragonDAO.delete(dragon);
          dragons.remove(objServer.getValue());
        } else {
          return "Пользователь не владеет драконом с ключем: " + objServer.getValue();
        }
        return sortByName(dragons);

      case "remove_greater_key":
        int key_new = objServer.getValue();
        for (Map.Entry<Integer, Dragon> dragonEntry : dragons.entrySet()) {
          if (dragonEntry.getKey() > key_new) {
            if (dragonEntry.getValue().getOwnerId() == user.getId()) {
              dragons.remove(dragonEntry.getKey());
              dragonDAO.delete(dragonEntry.getValue());
            } else {
              return "Пользователь не владеет драконом с ключем: " + dragonEntry.getKey();
            }
          }
        }
        return sortByName(dragons);

      case "update_id":
        dr = objServer.getDragon();
        Map<Integer, Dragon> found = dragonDAO.getById(objServer.getValue());
        for (Map.Entry<Integer, Dragon> dragonEntry : found.entrySet()) {
          dr.setId(dragonEntry.getValue().getId());
          dr.setOwnerId(dragonEntry.getValue().getOwnerId());
          dr.setCreationDate(dragonEntry.getValue().getCreationDate());
          if (dr.getOwnerId() == user.getId()) {
            dragonDAO.update(dragonEntry.getKey(), dr);
            dragons.replace(dragonEntry.getKey(), dr);
          } else {
            return "Пользователь не владеет драконом с id: " + objServer.getValue();
          }
        }
        return sortByName(dragons);

      case "insert_key":
        if (dragons.containsKey(objServer.getValue())) {
          return "Дракон с данным ключем " + objServer.getValue() + " уже существует";
        }
        dr = objServer.getDragon();
        dr.setCreationDate();
        dr.setOwnerId((int) user.getId());
        Dragon inserted = dragonDAO.insert(objServer.getValue(), dr);
        dragons.put(objServer.getValue(), inserted);
        return sortByName(dragons);

      case "count_less_then_cave":
        long count =
            dragons.values().stream()
                .map(d -> d.getDepth() + d.getNumberOfTreasures())
                .filter(value -> value < objServer.getCave())
                .count();
        return new CountResponse(count);

      case "replace_if_lowe_key":
        Dragon lower = dragons.get(objServer.getValue());
        if (lower != null) {
          Dragon dragonClient = objServer.getDragon();
          dragonClient.setCreationDate();
          if (lower.compareTo(dragonClient) > 0) {
            dragonClient.setId(lower.getId());
            dragonClient.setOwnerId(lower.getOwnerId());
            dragonClient.setCreationDate(lower.getCreationDate());
            if (lower.getOwnerId() == user.getId()) {
              dragonDAO.update(objServer.getValue(), lower);
              dragons.replace(objServer.getValue(), lower);
            } else {
              return "Пользователь не владеет драконом с ключем: " + objServer.getValue();
            }
          }
        }
        return sortByName(dragons);

      case "login":
        return "Пользователь авторизован";

      case "register":
        User user = objServer.getUser();
        if (user == null
            || user.getLogin() == null
            || user.getPassword() == null
            || user.getLogin().length() <= 1
            || user.getPassword().length() <= 1) {
          return "Неверные данные пользователя";
        }

        User foundLogin = userDAO.getByLogin(user.getLogin());
        if (foundLogin != null) {
          return "Пользователь с данным логином уже зарегестрирован";
        }

        user.setPassword(hashGenerator.generateHashWithPepperAndSalt(user.getPassword()));
        userDAO.insert(user);
        return "Пользователь зарегестрирован";

      default:
        return sortByName(dragons);
    }
  }

  private LinkedList<Dragon> sortByName(Map<Integer, Dragon> dragons) {
    return dragons.values().stream()
        .sorted(Comparator.comparing(Dragon::getName))
        .collect(Collectors.toCollection(LinkedList::new));
  }

  private InfoResponse info(Map<Integer, Dragon> dragons) {
    InfoResponse response = new InfoResponse();
    response.setSize(dragons.size());
    response.setCollectionType(dragons.getClass().toString());
    response.setElementType(Dragon.class.toString());

    Optional<LocalDateTime> min =
        dragons.values().stream().map(Dragon::getCreationDate).min(LocalDateTime::compareTo);

    min.ifPresent(response::setCreationDate);

    return response;
  }
}
