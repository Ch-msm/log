package pers.msm.log.factory;

import java.util.logging.Level;

/**
 * Date  2020/6/20 14:35
 *
 * @author msm
 */
public class CustomLevel extends Level {
  private static final String DEFAULT_BUNDLE = "sun.util.logging.resources.logging";
  public static final Level DEBUG = new CustomLevel("DEBUG", 200, DEFAULT_BUNDLE);
  public static final Level ERROR = new CustomLevel("ERROR", 950, DEFAULT_BUNDLE);

  protected CustomLevel(String name, int value) {
    super(name, value);
  }

  protected CustomLevel(String name, int value, String resourceBundleName) {
    super(name, value, resourceBundleName);
  }
}
