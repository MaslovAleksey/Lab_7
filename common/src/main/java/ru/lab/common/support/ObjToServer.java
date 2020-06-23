package ru.lab.common.support;

import ru.lab.common.beg_data.Dragon;

import java.io.Serializable;

/** Класс, содержащий информацию для передачи на сервер */
public class ObjToServer implements Serializable {
  private Dragon dragon;
  private Integer value;
  private String command;
  private String fileName;
  private Double cave;
  private User user;

  public ObjToServer(String command) {
    this.command = command;
  }

  public String getCommand() {
    return command;
  }

  public Dragon getDragon() {
    return dragon;
  }

  public void setDragon(Dragon dragon) {
    this.dragon = dragon;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Double getCave() {
    return cave;
  }

  public void setCave(Double cave) {
    this.cave = cave;
  }
}
