package ru.lab.client.support;

import ru.lab.common.beg_data.Dragon;
import ru.lab.common.response.AvgAgeResponse;
import ru.lab.common.response.CountResponse;
import ru.lab.common.response.InfoResponse;
import ru.lab.common.support.ObjToServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.util.LinkedList;
import java.util.List;

/** Класс, осуществляющий серриализацию объекта и взаимодействие с сервером */
public class ConnectToServer {
  /** Список элементов коллекции, полученный от сервера */
  private List<Dragon> dragons = new LinkedList<>();

  /**
   * Метод, осуществляющий серриализацию объекта и взаимодействие с сервером
   *
   * @param inputData Объект для передачи на сервер
   * @param host Хост
   * @param port Порт
   * @param needExit завершение работы приложения
   * @throws IOException - ошибка ввода-вывода
   * @throws UnresolvedAddressException - неверный адресс
   */
  public void connect(ObjToServer inputData, String host, Integer port, boolean needExit)
      throws IOException, UnresolvedAddressException {
    SocketChannel client = SocketChannel.open(); // создание канала для связи с сервером
    client.connect(new InetSocketAddress(host, port)); // установление соединения
    client.configureBlocking(false); // Неблокирующий режим

    // Серриализация объекта
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

      objectOutputStream.writeObject(inputData);
      objectOutputStream.flush();

      ByteBuffer buffer =
          ByteBuffer.wrap(
              byteArrayOutputStream.toByteArray()); // Обёртывание массива байтов в буфер
      client.write(buffer); // Запись данных из буфера в канал

      ByteBuffer buf = ByteBuffer.allocate(10240);
      client.read(buf);

      if (needExit) {
        System.out.println("Завершение работы клиентского приложения");
        System.exit(0);
      }

      System.out.println("\nОжидание ответа от сервера");
      while (client.read(buf) == 0) {}

      ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
      Serializable result = (Serializable) ois.readObject();

      try {
        processResult(inputData, result);
      } catch (ClassCastException e) {
        System.out.println(result);
      }
    } catch (IOException | ClassNotFoundException | NullPointerException e) {
      System.out.println(e);
    }

    client.close(); // Отключение отсервера
  }

  /**
   * Метод, осуществляющий обработку результатов, полученных от сервера
   *
   * @param request Запрос, отправленный на сервер
   * @param result Ответ сервера
   */
  private void processResult(ObjToServer request, Serializable result) {
    switch (request.getCommand()) {
      case "info":
        InfoResponse r = (InfoResponse) result;
        System.out.printf(
            "Информация о коллекции:\nТип коллекции - %s\nКоличество элементов - %s\n",
            r.getCollectionType(), r.getSize());
        if (r.getSize() != 0)
          System.out.printf(
              "Тип элементов - %s\nДата создания - %s\n", r.getElementType(), r.getCreationDate());
        break;

      case "show":
        dragons = (LinkedList<Dragon>) result;
        System.out.println("Элементы коллекции в строковом представлении:");
        for (Dragon dr : dragons) System.out.println(dr.toString());
        break;

      case "help":
        System.out.println("Список доступных команд:");
        LinkedList<String> help = (LinkedList<String>) result;
        for (String command : help) System.out.println(command);
        break;

      case "clear":
        System.out.println("Коллекция очищена");
        break;

      case "save":
        System.out.println("Коллекция сохранена в указанный файл");
        break;

      case "history":
        LinkedList<String> history = (LinkedList<String>) result;
        System.out.println("Последние команды, вызванные пользователем(не более 6):");
        System.out.println(history.toString());
        break;

      case "print_ascending":
        System.out.println("Элементы коллекции в порядке возрастания (на основании id)");
        dragons = (LinkedList<Dragon>) result;
        for (Dragon dr : dragons) System.out.println(dr.toString());
        break;

      case "average_of_age":
        AvgAgeResponse avg = (AvgAgeResponse) result;
        System.out.println("Средний возраст элементов коллекции: " + avg.getAverageAge());
        break;

      case "remove_key":
        System.out.println(
            "Коллекция после попытки удаления элемента (key = " + request.getValue() + "):");
        dragons = (LinkedList<Dragon>) result;
        for (Dragon dr : dragons) System.out.println(dr.toString());
        break;

      case "remove_greater_key":
        System.out.println(
            "Коллекция после удаления элементов, ключ которых превышает заданный (key = "
                + request.getValue()
                + "):");
        dragons = (LinkedList<Dragon>) result;
        for (Dragon dr : dragons) System.out.println(dr.toString());
        break;

      case "replace_if_lowe_key":
        System.out.println(
            "Коллекция после попытки замены элемента (key = " + request.getValue() + "):");
        dragons = (LinkedList<Dragon>) result;
        for (Dragon dr : dragons) System.out.println(dr.toString());
        break;

      case "update_id":
        System.out.println(
            "Коллекция после попытки обновления элемента (id = " + request.getValue() + "):");
        dragons = (LinkedList<Dragon>) result;
        for (Dragon dr : dragons) System.out.println(dr.toString());
        break;

      case "insert_key":
        dragons = (LinkedList<Dragon>) result;
        System.out.println(
            "Дракон с заданным ключом - {" + request.getValue() + "} добавлен в коллекцию");
        System.out.println("Элементы коллекции в строковом представлении:");
        for (Dragon dr : dragons) System.out.println(dr.toString());
        break;

      case "count_less_then_cave":
        CountResponse count = (CountResponse) result;
        System.out.println(count.getCount() + " caves found");
        break;

      case "login":
        System.out.println("Информация об авторизации: ");
        System.out.println(result);
        break;

      case "register":
        System.out.println("Информация о регистрации: ");
        System.out.println(result);
        break;

      default:
        System.out.println("Response not supported");
    }
  }
}
