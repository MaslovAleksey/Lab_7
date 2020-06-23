package ru.lab.server.support.hashGenerator;

import ru.lab.server.support.hashGenerator.exceptions.HashGeneratorException;

public abstract class HashGenerator {
  protected final String salt;
  protected final String pepper;

  protected HashGenerator() {
    salt = "!jdD*@;;:";
    pepper = "!@##!$!)";
  }

  /**
   * Генерирует хэш.
   *
   * @param string строка для генерации хэша
   * @return хэшированная строка
   * @throws HashGeneratorException - если алгоритм не был найден
   */
  protected abstract String generateHash(String string) throws HashGeneratorException;

  /**
   * Генерирует хэш с солью.
   *
   * @param string строка для генерации хэша
   * @return хэшированная строка
   * @throws HashGeneratorException - если алгоритм не был найден
   */
  public final String generateHashWithSalt(String string) throws HashGeneratorException {
    String stringWithSalt = string + salt;
    return generateHash(stringWithSalt);
  }

  /**
   * Генерирует хэш с солью и перцем.
   *
   * @param string строка для генерации хэша
   * @return хэшированная строка
   * @throws HashGeneratorException - если алгоритм не был найден
   */
  public final String generateHashWithPepperAndSalt(String string) throws HashGeneratorException {
    String stringWithPepperAndSalt = pepper + string + salt;
    return generateHash(stringWithPepperAndSalt);
  }
}
