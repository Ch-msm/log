package per.msm.log.factory;

import java.util.logging.Level;

/**
 * 自定义日志等级
 * Date  2020/6/20 14:35
 *
 * @author msm
 */
public class CustomLevel extends Level {
  private static final String DEFAULT_BUNDLE = "sun.util.logging.resources.logging";
  private static final Level DEBUG = new CustomLevel("DEBUG", 200, DEFAULT_BUNDLE);
  private static final Level ERROR = new CustomLevel("ERROR", 950, DEFAULT_BUNDLE);

  protected CustomLevel(String name, int value) {
    super(name, value);
  }

  protected CustomLevel(String name, int value, String resourceBundleName) {
    super(name, value, resourceBundleName);
  }

  public static Level parse(String name) {
    switch (name) {
      case "DEBUG": {
        return DEBUG;
      }
      case "ERROR": {
        return ERROR;
      }
      default:
        return Level.parse(name);
    }
  }
}
