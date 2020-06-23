package ru.lab.client.support;

import org.json.simple.JSONObject;
import ru.lab.common.beg_data.Color;
import ru.lab.common.beg_data.DragonType;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Класс, генерирующий объект класса Json на основании введённых данных */
public class Creator {
  /**
   * Метод, генерирующий объект класса Json на основании введённых данных
   *
   * @throws NoSuchElementException Отслеживание команды завершения пользовательского ввода
   * @return Сгенерированный объект класса Json
   */
  public JSONObject createJson() throws NoSuchElementException {
    JSONObject dragonJson = new JSONObject();
    JSONObject coordinatesJson = new JSONObject();
    JSONObject caveJson = new JSONObject();
    Scanner inScaner = new Scanner(System.in);

    /*    System.out.print(
            "Введите имя объекта (возможно только использование символов, длина имени > 0) - ");
    dragonJson.put("name", "asd");

    System.out.print("Введите  координату X объекта (X > -704) - ");
    coordinatesJson.put("x", "20");
    System.out.print("Введите  координату Y объекта (Y <= 28) - ");
    coordinatesJson.put("y", "20");
    dragonJson.put("coordinates", coordinatesJson);

    System.out.print("Введите возраст объекта (age > 0) - ");
    dragonJson.put("age", "20");

    System.out.print("Укажите, имеет ли объект способность говорить (true or false) - ");
    dragonJson.put("speaking", "true");

    System.out.print("Введите цвет объекта: " + Arrays.toString((Color.values())) + " - ");
    dragonJson.put("color", "BROWN");

    System.out.print("Введите тип объекта: " + Arrays.toString((DragonType.values())) + " - ");
    dragonJson.put("type", "AIR");

    System.out.print("Введите значение поля depth объекта (depth must be number) - ");
    caveJson.put("depth", "20");
    System.out.print("Введите значение поля numberOfTreasures объекта (numberOfTreasures > 0) - ");
    caveJson.put("numberOfTreasures", "20");
    dragonJson.put("cave", caveJson);*/

    System.out.print(
        "Введите имя объекта (возможно только использование символов, длина имени > 0) - ");
    dragonJson.put("name", inScaner.nextLine().trim());

    System.out.print("Введите  координату X объекта (X > -704) - ");
    coordinatesJson.put("x", inScaner.nextLine().trim());
    System.out.print("Введите  координату Y объекта (Y <= 28) - ");
    coordinatesJson.put("y", inScaner.nextLine().trim());
    dragonJson.put("coordinates", coordinatesJson);

    System.out.print("Введите возраст объекта (age > 0) - ");
    dragonJson.put("age", inScaner.nextLine().trim());

    System.out.print("Укажите, имеет ли объект способность говорить (true or false) - ");
    dragonJson.put("speaking", inScaner.nextLine().trim());

    System.out.print("Введите цвет объекта: " + Arrays.toString((Color.values())) + " - ");
    dragonJson.put("color", inScaner.nextLine().trim());

    System.out.print("Введите тип объекта: " + Arrays.toString((DragonType.values())) + " - ");
    dragonJson.put("type", inScaner.nextLine().trim());

    System.out.print("Введите значение поля depth объекта (depth must be number) - ");
    caveJson.put("depth", inScaner.nextLine().trim());
    System.out.print("Введите значение поля numberOfTreasures объекта (numberOfTreasures > 0) - ");
    caveJson.put("numberOfTreasures", inScaner.nextLine().trim());
    dragonJson.put("cave", caveJson);

    System.out.println();
    return dragonJson;
  }
}
