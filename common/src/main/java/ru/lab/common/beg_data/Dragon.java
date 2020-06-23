package ru.lab.common.beg_data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/** Класс, экзэмпляры которого хранятся в коллекции */
public class Dragon implements Comparable<Dragon>, Serializable {
  static final long serialVersionUID = -4862926644813433707L;
  // 1) Начальные поля
  private Integer
      id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно
  // быть уникальным, Значение этого поля должно генерироваться автоматически
  private int ownerId;
  private String name; // Поле не может быть null, Строка не может быть пустой
  private Coordinates coordinates; // Поле не может быть null
  private LocalDateTime
      creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
  // автоматически
  private Long age; // Значение поля должно быть больше 0, Поле не может быть null
  private boolean speaking;
  private Color color; // Поле может быть null
  private DragonType type; // Поле может быть null
  private DragonCave cave; // Поле не может быть null

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public void setCoordinates(Double x, int y) {
    coordinates = new Coordinates();
    coordinates.setX(x);
    coordinates.setY(y);
  }

  public void setDragonCave(Integer depth, Float numberOfTreasures) {
    cave = new DragonCave();
    cave.setDepth(depth);
    cave.setNumberOfTreasures(numberOfTreasures);
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public int getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }

  public boolean isSpeaking() {
    return speaking;
  }

  public DragonType getType() {
    return type;
  }

  public void setType(DragonType type) {
    this.type = type;
  }

  public DragonCave getCave() {
    return cave;
  }

  public void setCave(DragonCave cave) {
    this.cave = cave;
  }

  /** Метод, устанавливающий время */
  public void setCreationDate() {
    creationDate = LocalDateTime.now();
  }

  // 3) Доступ к значениям полей
  public String getName() {
    return name;
  }

  // 2.1) Установка значений в поля
  public void setName(String name) {
    this.name = name;
  }

  public Long getAge() {
    return age;
  }

  public void setAge(Long age) {
    this.age = age;
  }

  // 2.2) Генерация значений отдельных полей

  public Double getX() {
    return coordinates.getX();
  }

  public int getY() {
    return coordinates.getY();
  }

  public String getColor() {
    if (color == null) return null;
    else return color.toString();
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public boolean getSpeaking() {
    return speaking;
  }

  public void setSpeaking(boolean speaking) {
    this.speaking = speaking;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public Integer getDepth() {
    return cave.getDepth();
  }

  public Float getNumberOfTreasures() {
    return cave.getNumberOfTreasures();
  }

  public String getDragonType() {
    if (type == null) return null;
    else return type.toString();
  }

  public void setDragonType(DragonType type) {
    this.type = type;
  }

  public String getCoordinatesString() {
    Double x = coordinates.getX();
    Integer y = coordinates.getY();

    String xString = "null";
    if (x != null) {
      xString = x.toString();
    }

    String yString = "null";
    if (y != null) {
      yString = y.toString();
    }

    return "X = " + xString + ", Y = " + yString;
  }

  public String getDragonCaveString() {
    Integer d = cave.getDepth();
    Float n = cave.getNumberOfTreasures();

    String dString = "null";
    if (d != null) {
      dString = d.toString();
    }

    String nString = "null";
    if (n != null) {
      nString = n.toString();
    }

    return "depth = " + dString + ", number_of_treasures = " + nString;
  }

  public DragonCave getDragonCave() {
    return cave;
  }

  // 4.) Реализация интерфейса Comparable

  /**
   * Реализация интерфейса Comparable
   *
   * @param other Объект класса Dragon для сравнения
   * @return Результат сравнения
   */
  public int compareTo(Dragon other) {
    if (getClass() != other.getClass())
      throw new ClassCastException(); // проверка на то, чтобы метод не был вызван у наследника
    // Dragon
    return Long.compare(getAge(), other.getAge()); // по возрастанию возраста
  }

  // 5.) Переопределение toString();
  @Override
  public String toString() {
    return "name = "
        + name
        + "; age = "
        + age
        + "; color =  "
        + color
        + "; Coordinates: "
        + getCoordinatesString()
        + "; speaking = "
        + speaking
        + "; Cave: "
        + getDragonCaveString()
        + "; Dragon_type = "
        + getDragonType()
        + "; id = "
        + id
        + "; creation_Date = "
        + getCreationDate();
  }

  // 6.) Переопределение equals()
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Dragon dragon = (Dragon) o;
    return speaking == dragon.speaking
        && Objects.equals(id, dragon.id)
        && Objects.equals(name, dragon.name)
        && Objects.equals(coordinates, dragon.coordinates)
        && Objects.equals(creationDate, dragon.creationDate)
        && Objects.equals(age, dragon.age)
        && color == dragon.color
        && type == dragon.type
        && Objects.equals(cave, dragon.cave);
  }

  // 7.) Переопределение hashCode()
  @Override
  public int hashCode() {
    double result =
        (name.length() * Math.random() * 7)
            + (id * 5)
            + (coordinates.getX() * 11)
            + (coordinates.getY() * 13)
            + (age * 17)
            + (cave.getNumberOfTreasures() * 6);
    if (speaking) result *= 3;
    else result *= 2;
    if (color != null) result += getColor().length() * Math.random() * 37;
    if (type != null) result += getDragonType().length() * Math.random() * 25;
    if (cave.getDepth() != null) result += cave.getDepth() * 19;
    return (int) result;
  }
}
