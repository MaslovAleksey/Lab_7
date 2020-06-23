package ru.lab.server;

import ru.lab.server.support.UserDragServer;

public class Server {
  /**
   * @param args Аргумент командной строки, принимающий логи и пароль базы данных файла, которая
   *     содержит элементы коллекции.
   */
  public static void main(String[] args) {
    try {
      if (args.length != 2) {
        System.err.println("Неверные аргументы. Необходимо ввести логин и пароль базы данных.");
        System.exit(1);
      }

      UserDragServer userServer = new UserDragServer(args[0], args[1]);
      userServer.process();
    } catch (Throwable e) {
      System.err.println("Фатальная работа при работе сервера: " + e.getMessage());
      System.err.println("Подробнее: " + e.getCause().getMessage());
    }
  }
}
