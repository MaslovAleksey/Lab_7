package ru.lab.common.support;

import org.json.simple.JSONObject;
import ru.lab.common.beg_data.Color;
import ru.lab.common.beg_data.Dragon;
import ru.lab.common.beg_data.DragonType;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

// Класс преобразующий JSONObject в Dragon
/** Класс преобразующий JSONObject в Dragon */
public class MyPars {
  public static Double d;

  private boolean f = false;
  private Scanner inConv = new Scanner(System.in);
  private String nameSt;
  // ------------------------------------------------------------------------------------------
  // 1.) Метод, генерирующий объект Dragon на основании данных из JSON

  /**
   * Метод, проверяющий строку на численное значение
   *
   * @param st Строка для проверки
   * @return Успешно ли выполнена провека на (true/false)
   */
  public static boolean isNumber(String st) {
    try {
      d = Double.parseDouble(st);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }
  // -----------------------------------------------------------------------------------------------------------------
  // 2.) Метод, отвечающий за извлечение имени

  /**
   * Метод, генерирующий объект Dragon на основании данных из JSON
   *
   * @param dopMy Объект, требующий приведения к Dragon
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   * @return Элемент коллекции, полученный из JSON
   */
  public Dragon convertor(JSONObject dopMy) throws NoSuchElementException {
    Dragon dopDragon = new Dragon();
    // Извлечение значений из JSONObject
    extractName(dopMy.get("name"), dopDragon);
    extractAge(dopMy.get("age"), dopDragon);
    extractColor(dopMy.get("color"), dopDragon);
    extractCoordinates(dopMy.get("coordinates"), dopDragon);
    extractSpeaking(dopMy.get("speaking"), dopDragon);
    extractCave(dopMy.get("cave"), dopDragon);
    extractType(dopMy.get("type"), dopDragon);

    // Запонение остальных полей
    dopDragon.setCreationDate();

    return dopDragon;
  }
  // --------------------------------------------------------------------------------------------------
  // 3.)Метод, отвечающий за извлечение возраста

  /**
   * Метод, отвечающий за извлечение имени
   *
   * @param name_ob Имя объекта
   * @param dopMyDragon Элемент коллекции, генерируемый на основании данных из JSON
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   */
  private void extractName(Object name_ob, Dragon dopMyDragon) throws NoSuchElementException {
    boolean isName;

    while (true) {
      if (name_ob != null) {
        nameSt = name_ob.toString();
        isName = isNumber(nameSt);
        if ((!isName) && (!nameSt.isEmpty())) {
          dopMyDragon.setName(nameSt);
          break;
        } else {
          System.out.print(
              "Имя объекта не соответствует требованиям, задайте корректное имя (возможно только "
                  + "использование символов, длина имени > 0) - ");
          name_ob = Empty(inConv.nextLine().trim());
        }
      } else {
        System.out.print(
            "Имя объекта не соответствует требованиям, задайте корректное имя (возможно только "
                + "использование символов, длина имени > 0) - ");
        name_ob = Empty(inConv.nextLine().trim());
      }
    }
  }
  // ----------------------------------------------------------------------------------------
  // 4.) Метод, отвечающий за извлечение цвета

  /**
   * Метод, отвечающий за извлечение возраста
   *
   * @param age_ob Возраст объекта
   * @param dopMyDragon Элемент коллекции, генерируемый на основании данных из JSON
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   */
  private void extractAge(Object age_ob, Dragon dopMyDragon) throws NoSuchElementException {
    String ageSt;
    boolean isAge;

    while (true) {
      if (age_ob != null) {
        ageSt = age_ob.toString();
        isAge = isNumber(ageSt);
        if ((isAge) && (d > 0)) {
          dopMyDragon.setAge(d.longValue());
          break;
        } else {
          System.out.print(
              "Возраст объекта "
                  + nameSt
                  + "  не соответствует требованиям, "
                  + "задайте корректное значение возраста (age > 0) - ");
          age_ob = Empty(inConv.nextLine().trim());
        }
      } else {
        System.out.print(
            "Возраст объекта "
                + nameSt
                + "  не соответствует требованиям, "
                + "задайте корректное значение возраста (age > 0) - ");
        age_ob = Empty(inConv.nextLine().trim());
      }
    }
  }
  // -----------------------------------------------------------------------------------------
  // 5.) Метод, отвечающий за извлечение координат

  /**
   * Метод, отвечающий за извлечение цвета
   *
   * @param color_ob Цвет объекта
   * @param dopMyDragon Элемент коллекции, генерируемый на основании данных из JSON
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   */
  private void extractColor(Object color_ob, Dragon dopMyDragon) throws NoSuchElementException {
    String colorSt;
    boolean isColor;
    f = false;

    while (true) {
      if (color_ob != null) {
        colorSt = color_ob.toString();
        isColor = isNumber(colorSt);
        if (!isColor) {
          switch (colorSt) {
            case "GREEN":
              dopMyDragon.setColor(Color.GREEN);
              f = true;
              break;
            case "YELLOW":
              dopMyDragon.setColor(Color.YELLOW);
              f = true;
              break;
            case "ORANGE":
              dopMyDragon.setColor(Color.ORANGE);
              f = true;
              break;
            case "BROWN":
              dopMyDragon.setColor(Color.BROWN);
              f = true;
              break;
            case "":
              dopMyDragon.setColor(null);
              f = true;
              break;
            default:
              System.out.print(
                  "Цвет объекта "
                      + nameSt
                      + " не соответствует требованиям, "
                      + "задайте корректное значение цвета: "
                      + Arrays.toString((Color.values()))
                      + " - ");
              color_ob = Empty(inConv.nextLine().trim());
          }
          if (f) break;
        } else {
          System.out.print(
              "Цвет объекта "
                  + nameSt
                  + " не соответствует требованиям, "
                  + "задайте корректное значение цвета: "
                  + Arrays.toString((Color.values()))
                  + " - ");
          color_ob = Empty(inConv.nextLine().trim());
        }
      } else {
        dopMyDragon.setColor(null);
        break;
      }
    }
  }
  // ------------------------------------------------------------------------------------------------------------
  // 6.) Метод, отвечающий за извлечение способности говорить

  /**
   * Метод, отвечающий за извлечение координат
   *
   * @param coordinates_ob Координаты объекта
   * @param dopMyDragon Элемент коллекции, генерируемый на основании данных из JSON
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   */
  private void extractCoordinates(Object coordinates_ob, Dragon dopMyDragon)
      throws NoSuchElementException {
    JSONObject coordinates = (JSONObject) coordinates_ob;
    Object xOb;
    Object yOb;
    String xSt;
    String ySt;
    boolean isX;
    boolean isY;
    Double x;
    int y;

    xOb = coordinates.get("x");
    while (true) {
      if (xOb != null) {
        xSt = xOb.toString();
        isX = isNumber(xSt);
        if ((isX) && (d > -704)) {
          x = d;
          break;
        } else {
          System.out.print(
              "Значение координаты Х у объекта "
                  + nameSt
                  + " не соответствует требованиям,"
                  + " задайте корректное значение координаты Х (X > -704) - ");
          xOb = Empty(inConv.nextLine().trim());
        }
      } else {
        System.out.print(
            "Значение координаты Х у объекта "
                + nameSt
                + " не соответствует требованиям,"
                + " задайте корректное значение координаты Х (X > -704) - ");
        xOb = Empty(inConv.nextLine().trim());
      }
    }

    yOb = coordinates.get("y");
    while (true) {
      if (yOb != null) {
        ySt = yOb.toString();
        isY = isNumber(ySt);
        if ((isY) && (d <= 28)) {
          y = d.intValue();
          break;
        } else {
          System.out.print(
              "Значение координаты Y у объекта "
                  + nameSt
                  + " не соответствует требованиям,"
                  + " задайте корректное значение координаты (Y <= 28) - ");
          yOb = Empty(inConv.nextLine().trim());
        }
      } else {
        System.out.print(
            "Значение координаты Y у объекта "
                + nameSt
                + " не соответствует требованиям,"
                + " задайте корректное значение координаты (Y <= 28) - ");
        yOb = Empty(inConv.nextLine().trim());
      }
    }
    dopMyDragon.setCoordinates(x, y);
  }
  // -----------------------------------------------------------------------------------------------------------
  // 7.) Метод, отвечающий за извлечение данных о пещере

  /**
   * Метод, отвечающий за извлечение способности говорить
   *
   * @param speaking_ob Способность объекта говорить
   * @param dopMyDragon Элемент коллекции, генерируемый на основании данных из JSON
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   */
  private void extractSpeaking(Object speaking_ob, Dragon dopMyDragon)
      throws NoSuchElementException {
    String speakingSt;
    boolean isSpeaking;

    while (true) {
      if (speaking_ob != null) {
        speakingSt = speaking_ob.toString();
        isSpeaking = isNumber(speakingSt);
        if ((!isSpeaking) && ((speakingSt.equals("true")) || (speakingSt.equals("false")))) {
          dopMyDragon.setSpeaking(Boolean.parseBoolean(speakingSt));
          break;
        } else {
          System.out.print(
              "Способность разговаривать у объекта "
                  + nameSt
                  + "  не соответствует требованиям, "
                  + "задайте корректное значение поля (true/false) - ");
          speaking_ob = Empty(inConv.nextLine().trim());
        }
      } else {
        System.out.print(
            "Способность разговаривать у объекта "
                + nameSt
                + "  не соответствует требованиям, "
                + "задайте корректное значение поля (true/false) - ");
        speaking_ob = Empty(inConv.nextLine().trim());
      }
    }
  }
  // ------------------------------------------------------------------------------------------------------------
  // 8.) Метод, отвечающий за извлечение типа

  /**
   * Метод, отвечающий за извлечение данных о пещере
   *
   * @param cave_ob Инфлормациия о пещере
   * @param dopMyDragon Элемент коллекции, генерируемый на основании данных из JSON
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   */
  private void extractCave(Object cave_ob, Dragon dopMyDragon) throws NoSuchElementException {
    JSONObject cave = (JSONObject) cave_ob;
    Object depthOb;
    Object numberOfTreasuresOb;
    String depthSt;
    String numberOfTreasuresSt;
    boolean isDepth;
    boolean isNumberOfTreasures;
    Integer depth;
    Float numberOfTreasures;

    depthOb = cave.get("depth");
    while (true) {
      if ((depthOb != null) && (Empty(depthOb.toString()) != null)) {
        depthSt = depthOb.toString();
        isDepth = isNumber(depthSt);
        if (isDepth) {
          depth = d.intValue();
          break;
        } else {
          System.out.print(
              "Значение поля depth у объекта "
                  + nameSt
                  + " не соответствует требованиям,"
                  + " задайте корректное значение поля depth (depth must be number) - ");
          depthOb = Empty(inConv.nextLine().trim());
        }
      } else {
        depth = null;
        break;
      }
    }

    numberOfTreasuresOb = cave.get("numberOfTreasures");
    while (true) {
      if (numberOfTreasuresOb != null) {
        numberOfTreasuresSt = numberOfTreasuresOb.toString();
        isNumberOfTreasures = isNumber(numberOfTreasuresSt);
        if ((d > 0) && (isNumberOfTreasures)) {
          numberOfTreasures = d.floatValue();
          break;
        } else {
          System.out.print(
              "Значение поля numberOfTreasures у объекта "
                  + nameSt
                  + " не соответствует требованиям,"
                  + " задайте корректное значение поля numberOfTreasures "
                  + "(numberOfTreasures > 0) - ");
          numberOfTreasuresOb = Empty(inConv.nextLine().trim());
        }
      } else {
        System.out.print(
            "Значение поля numberOfTreasures у объекта "
                + nameSt
                + " не соответствует требованиям,"
                + " задайте корректное значение поля numberOfTreasures "
                + "(numberOfTreasures > 0) - ");
        numberOfTreasuresOb = Empty(inConv.nextLine().trim());
      }
    }
    dopMyDragon.setDragonCave(depth, numberOfTreasures);
  }
  // -----------------------------------------------------------------------------------------------------------------
  // 9.) Метод, проверяющий строку на число

  /**
   * Метод, отвечающий за извлечение типа
   *
   * @param type_ob Тип объекта
   * @param dopMyDragon Элемент коллекции, генерируемый на основании данных из JSON
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   */
  private void extractType(Object type_ob, Dragon dopMyDragon) throws NoSuchElementException {
    String typeSt;
    boolean isType;
    f = false;

    while (true) {
      if (type_ob != null) {
        typeSt = type_ob.toString();
        isType = isNumber(typeSt);
        if (!isType) {
          switch (typeSt) {
            case "WATER":
              dopMyDragon.setDragonType(DragonType.WATER);
              f = true;
              break;
            case "AIR":
              dopMyDragon.setDragonType(DragonType.AIR);
              f = true;
              break;
            case "FIRE":
              dopMyDragon.setDragonType(DragonType.FIRE);
              f = true;
              break;
            case "":
              dopMyDragon.setDragonType(null);
              f = true;
              break;
            default:
              System.out.print(
                  "Тип объекта "
                      + nameSt
                      + " не соответствует требованиям, "
                      + "задайте корректное значение цвета: "
                      + Arrays.toString((DragonType.values()))
                      + " - ");
              type_ob = Empty(inConv.nextLine().trim());
          }
          if (f) break;
        } else {
          System.out.print(
              "Тип объекта "
                  + nameSt
                  + " не соответствует требованиям, "
                  + "задайте корректное значение цвета: "
                  + Arrays.toString((DragonType.values()))
                  + " - ");
          type_ob = Empty(inConv.nextLine().trim());
        }
      } else {
        type_ob = null;
        break;
      }
    }
  }
  // ------------------------------------------------------------------------------------------------------------------
  // 10.) Метод, проверяющий строку на наличие символов

  /**
   * Метод, проверяющий строку на наличие символов
   *
   * @param st Строка для проверки
   * @return Успешно ли выполнена провека на (true/false)
   */
  private String Empty(String st) {
    if (st.isEmpty()) return null;
    else return st;
  }
}
