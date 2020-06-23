package ru.lab.client.support;

import org.json.simple.JSONObject;
import ru.lab.common.beg_data.Dragon;
import ru.lab.common.support.MyPars;
import ru.lab.common.support.ObjToServer;
import ru.lab.common.support.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.UnresolvedAddressException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Класс, реализующий работу клиентского модуля */
public class UserDragClient {
  private final User user = new User();
  /** Класс, позволяющий создать Json объект на основании введённых данных */
  private Creator creator = new Creator(); // нет собственных переменных
  /** Класс, преобразующий объект класс Json в объект класса Dragon */
  private ru.lab.common.support.MyPars myPars = new MyPars();
  /** Класс, осуществляющий серриализацию объекта и взаимодействие с сервером */
  private ConnectToServer connection = new ConnectToServer();

  private String host;
  private Integer port = 8734;
  /**
   * Переменная, содержащая в себе целочисленной значение из строки в случае успешного
   * преобразования
   */
  private Integer k;
  // -----------------------------------------------------------------------------------------------------
  // 1.) Метод, реализующий работу клиентского приложения
  /** Метод, реализующий работу клиентского приложения */
  public void process() {
    Scanner in = new Scanner(System.in);
    try {
      receiveAddress(in);
      while (true) {
        System.out.println(
            "\nВведите очередную команду из списка:\n"
                + "[help, info, show, insert_key {element}, update_id {element}, remove_key {key}, clear,\n"
                + "execute_script {file_name}, save, exit, history, replace_if_lowe_key {element}, remove_greater_key {key},\n"
                + "average_of_age, count_less_then_cave {cave}, print_ascending, login, register <ctrl> + <d>]\n");
        perform(in);
      }
    } catch (NoSuchElementException e) {
      System.out.println(
          "Поступила команда завершения пользовательского ввода, для продолжения работы перезапустите приложение");
      System.exit(0);
    } catch (IOException e) {
      System.out.println(
          "Потеряна связь с сервером, для продолжения работы перезапустите приложение");
      System.exit(0);
    } catch (UnresolvedAddressException e) {
      System.out.println(
          "Не удалось установить соединение с сервером, проверьте данные для подключения и перезапустите приложение");
      System.exit(0);
    }
  }
  // ---------------------------------------------------------------------------------------------------------------------------------
  // 2.) Метод, осуществялющий передачу команды на выполнение
  /**
   * Метод, осуществялющий передачу команды на выполнение
   *
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   * @param scan_ Содержит команды для выполнения
   */
  private void perform(Scanner scan_)
      throws NoSuchElementException, IOException, UnresolvedAddressException {
    boolean com = true; // Проверка соотвествия команды
    boolean needToExit = false;
    String command = scan_.next();
    ObjToServer objectToServer = new ObjToServer(command);
    Dragon dragonObject;
    JSONObject jsonObject;
    switch (command) {
      case "help":
      case "info":
      case "show":
      case "save":
      case "clear":
      case "history":
      case "average_of_age":
      case "print_ascending":
        break;

      case "update_id":
        if ((isInteger(scan_.nextLine().trim())) && (k > 0)) {
          objectToServer.setValue(k);
          jsonObject = creator.createJson();
          dragonObject = myPars.convertor(jsonObject);
          objectToServer.setDragon(dragonObject);
        } else {
          System.out.println("Некорректное значение id ((id must be Integer) and (id > 0))");
          com = false;
        }
        break;

      case "insert_key":
        if (isInteger(scan_.nextLine().trim())) {
          objectToServer.setValue(k);
          jsonObject = creator.createJson();
          dragonObject = myPars.convertor(jsonObject);
          objectToServer.setDragon(dragonObject);
        } else {
          System.out.println("Некорректное значение ключа (key must be Integer)");
          com = false;
        }
        break;

      case "remove_key":
        if (isInteger(scan_.nextLine().trim())) {
          objectToServer.setValue(k);
        } else {
          System.out.println("Некорректное значение ключа (key must be Integer)");
          com = false;
        }
        break;

      case "replace_if_lowe_key":
        if (isInteger(scan_.nextLine().trim())) {
          objectToServer.setValue(k);
          jsonObject = creator.createJson();
          dragonObject = myPars.convertor(jsonObject);
          objectToServer.setDragon(dragonObject);
        } else {
          System.out.println("Некорректное значение ключа (key must be Integer)");
          com = false;
        }
        break;

      case "remove_greater_key":
        if (isInteger(scan_.nextLine().trim())) {
          objectToServer.setValue(k);
        } else {
          System.out.println("Некорректное значение ключа (key must be Integer)");
          com = false;
        }
        break;

      case "count_less_then_cave":
        if (MyPars.isNumber(scan_.nextLine().trim())) {
          objectToServer.setCave(MyPars.d);
        } else {
          System.out.println("Некорректное значение поля cave (cave must be Number)");
          com = false;
        }
        break;

      case "execute_script":
        try {
          FileReader reader = new FileReader(new File(scan_.nextLine().trim()));
          Scanner scanScript = new Scanner(reader);
          while (scanScript.hasNext()) {
            perform(scanScript);
          }
        } catch (FileNotFoundException ex) {
          System.out.println("Ошибка считывания файла");
        }
        com = false; // команда не отправлется на сервер
        break;

      case "login":
      case "register":
        Scanner inScanner = new Scanner(System.in);
        System.out.print("Введите логин: ");
        user.setLogin(inScanner.nextLine());
        System.out.print("Введите пароль: ");
        user.setPassword(inScanner.nextLine());
        System.out.println("Данные пользователя сохранены");
        break;

      case "exit":
        needToExit = true;
        break;

      default:
        System.out.println("Некорректная команда");
        com = false;
    }
    objectToServer.setUser(user);
    if (com) connection.connect(objectToServer, host, port, needToExit);
  }
  // ------------------------------------------------------------------------------------------------------------------
  // 3.) Метод, считывающий значение хоста и порта
  /** Метод, считывающий значение хоста и порта */
  private void receiveAddress(Scanner in) throws UnknownHostException {

    String hostSt;
    String portSt;
    System.out.println(
        "\nВведите данные для соединения с сервером\n"
            + "(для использования данных по умолчанию оставьте поля пустыми");

    System.out.print("\nХост (доменное имя или IP) - ");
    hostSt = in.nextLine().trim();
    if (hostSt.length() == 0) {
      host = InetAddress.getLocalHost().getHostName();
      System.out.println("Доменное имя - " + host);
      System.out.println("IP адрес - " + InetAddress.getLocalHost().getHostAddress());
    } else host = hostSt;

    while (true) {
      System.out.print("\nПорт (целочисленное значение [1024-65535]) - ");
      portSt = in.nextLine().trim();
      if (portSt.length() == 0) {
        System.out.println("Порт - " + port);
        break;
      } else {
        if ((isInteger(portSt)) && (k <= 65535) && (k >= 1024)) {
          port = k;
          break;
        } else
          System.out.println(
              "Введите корректное значение порта (целочисленное значение [1024-65535])");
      }
    }
  }
  // ------------------------------------------------------------------------------------------------------------------
  // 4.) Метод, проверяющий строку на целочисленной значение
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
