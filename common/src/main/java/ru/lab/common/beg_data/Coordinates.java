package ru.lab.common.beg_data;

import java.io.Serializable;

/** Класс, отвечающий за размещение объекта класса Dragon в пространстве */
public class Coordinates implements Serializable {
  // 1) Начальные поля
  private Double x; // Значение поля должно быть больше -704, Поле не может быть null
  private int y; // Максимальное значение поля: 28

  // 3) Доступ к значениям полей
  public Double getX() {
    return x;
  }

  // 2) Установка значений в полях
  public void setX(Double x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
}
