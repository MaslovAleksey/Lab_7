package ru.lab.common.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public class InfoResponse implements Serializable {
  static final long serialVersionUID = -4862926644813433708L;
  String collectionType;
  int size;
  String elementType;
  LocalDateTime creationDate;

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public String getCollectionType() {
    return collectionType;
  }

  public void setCollectionType(String collectionType) {
    this.collectionType = collectionType;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getElementType() {
    return elementType;
  }

  public void setElementType(String elementType) {
    this.elementType = elementType;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }
}
