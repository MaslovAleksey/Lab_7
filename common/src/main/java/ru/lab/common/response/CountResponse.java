package ru.lab.common.response;

import java.io.Serializable;

public class CountResponse implements Serializable {
  long count;

  public CountResponse(long count) {
    this.count = count;
  }

  public long getCount() {
    return count;
  }
}
