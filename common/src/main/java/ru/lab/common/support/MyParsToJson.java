package ru.lab.common.support;

import org.json.simple.JSONObject;
import ru.lab.common.beg_data.Dragon;

/** Класс реализующий преобразование объекта класса Dragon в объект класса Json */
public class MyParsToJson {
  /**
   * Метод, преобразующий объект класса Dragon в объект класса Json
   *
   * @param dragMy Элемент коллекции, преобразуемый в объект класса Json
   * @return Преобразованный элемент коллекции
   */
  public JSONObject ConvertorToJson(
      Dragon dragMy) // метод, переводящий объект класса Dragon в объект класса Json
      {
    JSONObject dragJson = new JSONObject();
    JSONObject coordinatesJson = new JSONObject();
    JSONObject caveJson = new JSONObject();

    dragJson.put("name", dragMy.getName());

    coordinatesJson.put("x", dragMy.getX());
    coordinatesJson.put("y", dragMy.getY());
    dragJson.put("coordinates", coordinatesJson);

    dragJson.put("age", dragMy.getAge());

    dragJson.put("speaking", dragMy.getSpeaking());

    dragJson.put("color", dragMy.getColor());

    dragJson.put("type", dragMy.getDragonType());

    caveJson.put("depth", dragMy.getDepth());
    caveJson.put("numberOfTreasures", dragMy.getNumberOfTreasures());
    dragJson.put("cave", caveJson);

    return dragJson;
  }
}
