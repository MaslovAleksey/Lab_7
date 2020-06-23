package ru.lab.server.support.hashGenerator;

import ru.lab.server.support.hashGenerator.exceptions.HashGeneratorException;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SHA1Generator extends HashGenerator {
  @Override
  protected String generateHash(String string) throws HashGeneratorException {
    String sha1;

    try {
      MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
      msdDigest.update(string.getBytes(StandardCharsets.UTF_8), 0, string.length());
      sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
    } catch (NoSuchAlgorithmException e) {
      throw new HashGeneratorException(e);
    }

    return sha1;
  }
}
