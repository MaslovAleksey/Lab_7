package ru.lab.common.response;

import java.io.Serializable;

public class AvgAgeResponse implements Serializable {
  double averageAge;

  public AvgAgeResponse(double averageAge) {
    this.averageAge = averageAge;
  }

  public double getAverageAge() {
    return averageAge;
  }

  public void setAverageAge(double averageAge) {
    this.averageAge = averageAge;
  }
}
