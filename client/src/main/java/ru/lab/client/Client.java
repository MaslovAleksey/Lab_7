package ru.lab.client;

import ru.lab.client.support.UserDragClient;

/**
 * Основной класс программы клиентского приложения
 *
 * @author Маслов Алексей и Третьяков Артур
 * @version 1.0
 */
public class Client {
  public static void main(String[] args) {
    try {
      UserDragClient userDragonClient = new UserDragClient();
      userDragonClient.process();
    } catch (Throwable e) {
      System.err.println("Фатальная работа при работе клиента: " + e.getMessage());
      System.err.println("Подробнее: " + e.getCause().getMessage());
    }
  }
}
