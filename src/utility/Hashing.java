package utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * This class is used to hash user passwords. It uses a combination of MD5 hashing and unique salt.
 * The hashed value has 128 bits.
 *
 * @author fpetek
 * @version 1.0
 */
public class Hashing {

  /**
   * Method to generate a salted hash value with MD5 hashing-algorithm and salt.
   *
   * @param password is user password.
   * @param salt a 16 byte salt -> please use generateSalt() method to get unique values.
   * @return Returns the hashed password with a pinch of salt.
   */
  public static String generateHash(String password, byte[] salt) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.reset();
      md.update(salt);
      byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
      return bytesToString(hash);
    } catch (NoSuchAlgorithmException notPossible) {
      return null;
    }
  }

  /**
   * Method to generate a unique salt value with 16 bytes to guarantee a collision free hash value.
   *
   * @return Returns the salt.
   */
  public static byte[] generateSalt() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[16];
    random.nextBytes(bytes);
    return bytes;
  }

  /**
   * Method to parse bytes to string in UTF_8 encoding.
   *
   * @param bytes are bytes to parse.
   * @return bytes as a String.
   */
  public static String bytesToString(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  /**
   * Method to parse String to byte array in UTF_8 encoding.
   *
   * @param string String to parse.
   * @return String as a byte array.
   */
  public static byte[] stringToBytes(String string) {
    return Base64.getDecoder().decode(string);
  }
}
