package ru.lab.server.support;

import ru.lab.common.beg_data.Dragon;
import ru.lab.common.support.ObjToServer;
import ru.lab.server.support.hashGenerator.HashGenerator;
import ru.lab.server.support.hashGenerator.SHA1Generator;
import ru.lab.server.storage.dao.DAOException;
import ru.lab.server.storage.dao.daos.DragonDAO;
import ru.lab.server.storage.dao.daos.UserDAO;
import ru.lab.server.storage.dataSource.DataSource;
import ru.lab.server.storage.dataSource.database.Database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class UserDragServer {
  private final DragonDAO dragonDAO;
  private final UserDAO userDAO;
  private final HashGenerator hashGenerator = new SHA1Generator();
  private final Executor processExecutor = Executors.newFixedThreadPool(2);
  private final Executor sendExecutor = ForkJoinPool.commonPool();
  /** Коллекция для хранения элементов */
  private final Map<Integer, Dragon> dragonHashtable = new ConcurrentHashMap<>();
  /** Переменная, хранящая список запросов от клиента */
  LinkedList<String> commandsHistory = new LinkedList<>();
  /** Класс, обрабатывающий запрос клиента */
  ProvideResult provideResult = new ProvideResult();

  /** Значение порта по-умолчанию */
  private Integer port = 8734;
  /**
   * Переменная, содержащая в себе целочисленной значение из строки в случае успешного
   * преобразования
   */
  private Integer k;

  public UserDragServer(String dbUser, String dbPassword) {
    DataSource dataSource = new Database(dbUser, dbPassword);

    this.dragonDAO = new DragonDAO(dataSource);
    this.userDAO = new UserDAO(dataSource);
  }

  // -----------------------------------------------------------------------------------------------------------------
  // -----------------------------------------------------------------------------------------------------------------
  // 1.) Метод, реализующий работу приложения
  /** Метод, реализующий работу приложения */
  public void process() {
    Scanner in = new Scanner(System.in);
    try {
      System.out.println(
          "Завершение работы серверного приложения осуществляется вводом ключевого слова 'exit' или сочетанием клавиш <ctrl> + <d>");
      loadData();
      receivePort(in);
      waiting();
    } catch (NoSuchElementException e) {
      System.out.println("Поступила команда завершения работы серверного приложения");
      save();
      System.exit(0);
    }
  }
  // ---------------------------------------------------------------------------------------
  // 2.) Метод, считывающий данные из базы данных и заполняющий хеш-таблицу
  /**
   * Метод для считывания из базы данных и записи в хеш-таблицу
   *
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   */
  private void loadData() throws NoSuchElementException {
    try {
      dragonHashtable.putAll(dragonDAO.getAll());
      System.out.println("Данные успешно загружены");
    } catch (DAOException e) {
      throw new NoSuchElementException();
    }
  }
  // ------------------------------------------------------------------------------------------------------------------
  // 3.) Метод, осуществляющий взаимодействие с клиентом
  /** Метод, осуществляющий взаимодействие с клиентом */
  private void waiting() throws NoSuchElementException {
    try (ServerSocket serverSocket = new ServerSocket(port)) // Установка сокета на стороне сервера
    {
      while (true) {
        System.out.println("\nОжидание запроса от клиента (время ожидания - 5 минут)");
        serverSocket.setSoTimeout(
            500000); // Установка времени ожидания запроса от клиента - 5 минут
        Socket server =
            serverSocket
                .accept(); // Ожидание подключения клиентов к серверу и воссоздания клиентского
        // сокета на стороне сервера

        ObjectInputStream serverIn =
            new ObjectInputStream(new BufferedInputStream(server.getInputStream()));
        ObjectOutputStream serverOut =
            new ObjectOutputStream(new BufferedOutputStream(server.getOutputStream()));

        new Thread(
                () -> {
                  try {
                    ObjToServer request = (ObjToServer) serverIn.readObject();

                    processExecutor.execute(
                        () -> {
                          Serializable result = null;
                          try {
                            result =
                                provideResult.provideResult(
                                    request,
                                    dragonHashtable,
                                    dragonDAO,
                                    userDAO,
                                    hashGenerator,
                                    commandsHistory);
                          } catch (DAOException | NullPointerException e) {
                            System.out.println("Не удалось обработать запрос: " + e.getMessage());
                          }

                          Serializable finalResult = result;
                          sendExecutor.execute(
                              () -> {
                                try {
                                  serverOut.writeObject(finalResult);
                                  serverOut.flush();

                                  if (request.getCommand().trim().equals("save")) save();

                                  if (request.getCommand().trim().equals("exit"))
                                    System.out.println("\nКлиент отключен");

                                  System.out.println("\nЗапрос клиента обработан");
                                } catch (IOException e) {
                                  System.out.println("Не удалось установить соединение с клиентом");
                                }
                              });
                        });

                  } catch (IOException e) {
                    System.out.println("Не удалось установить соединение с клиентом");
                  } catch (ClassNotFoundException e) {
                    System.out.println("Клиент отправил пустой запрос");
                    save();
                    System.exit(0);
                  }
                })
            .start();
      }
    } catch (IOException e1) {
      System.out.println("Не удалось установить соединение с клиентом");
      save();
      System.exit(0);
    }
  }
  // ----------------------------------------------------------------------------------------------------------------
  // 4.) Метод, сохраняющий коллекцию в файл
  /** Метод, сохраняющий коллекцию в файл */
  private void save() {
    for (Map.Entry<Integer, Dragon> dragonEntry : dragonHashtable.entrySet()) {
      try {
        dragonDAO.update(dragonEntry.getKey(), dragonEntry.getValue());
      } catch (DAOException e) {
        System.out.println("Невозможно сохранить коллекцию в базу данных");
      }
    }
  }
  // -------------------------------------------------------------------------------------------------
  // 5.) Метод, запрашивающий номер порта
  /**
   * Метод, запрашивающий номер порта
   *
   * @param inPort порт
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   */
  private void receivePort(Scanner inPort) throws NoSuchElementException {
    String portMy;
    System.out.println(
        "\nВведите значение порта для установления связи с клиентом (целочисленное значение [1024-65535])\n"
            + "(для использования данных по умолчанию оставьте поля пустыми");
    while (true) {
      System.out.print("\nПорт - ");
      portMy = inPort.nextLine().trim();
      if (portMy.length() == 0) {
        System.out.println("Порт - " + port);
        break;
      } else {
        if ((isInteger(portMy) && (k <= 65535) && (k >= 1024))) {
          port = k;
          break;
        } else
          System.out.println(
              "Введите корректное значение порта (целочисленное значение [1024-65535])");
      }
    }
  }
  // ------------------------------------------------------------------------------------------------------------------
  // 6.) Метод, проверяющий строку на целочисленной значение
  /**
   * Метод, проверяющий строку на целочисленной значение
   *
   * @param st Строка для проверки
   * @return Успешно ли выполнена провека (true/false)
   */
  private boolean isInteger(String st) {
    try {
      k = Integer.parseInt(st);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }
}
