package ru.lab.common.beg_data;

import java.io.Serializable;

/** Класс, демонтсрирующий характеристики поля DragonCave объекта класса Dragon */
public class DragonCave implements Serializable {
  // 1) Начальные поля
  private Integer depth; // Поле может быть null
  private Float numberOfTreasures; // Поле не может быть null, Значение поля должно быть больше 0

  // 2) Доступ к значениям полей

  public Integer getDepth() {
    return depth;
  }

  public void setDepth(Integer depth) {
    this.depth = depth;
  }

  // 3) Установка значений в поля

  public Float getNumberOfTreasures() {
    return numberOfTreasures;
  }

  public void setNumberOfTreasures(Float numberOfTreasures) {
    this.numberOfTreasures = numberOfTreasures;
  }
}
