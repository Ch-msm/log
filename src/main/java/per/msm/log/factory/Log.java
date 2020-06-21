package per.msm.log.factory;

import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * 基于Logger的简易封装
 * Date  2020/6/20 16:21
 *
 * @author msm
 */
public class Log {
  private final Logger logger;

  private Log(String name) {
    this.logger = LogFactory.getLogger(name);
  }

  /**
   * 获取日志实例
   *
   * @param name 日志记录器名称
   * @return 实例
   */
  public static Log getLog(String name) {
    return new Log(name);
  }

  /**
   * 获取日志调用方的类和方法名称
   *
   * @return 结果
   */
  private String[] getClassNameAndMethod(int i) {
    String[] array = new String[2];
    StackTraceElement[] stack = (new Throwable()).getStackTrace();
    StackTraceElement s = stack[i];
    array[0] = s.getClassName();
    array[1] = s.getMethodName();
    return array;
  }

  public void debug(String mes) {
    log("DEBUG", mes);
  }

  public void debug(String mes, Object... params) {
    log("DEBUG", formatMessage(mes, params));
  }

  public void info(String mes) {
    log("INFO", mes);
  }

  public void info(String mes, Object... params) {
    log("INFO", formatMessage(mes, params));
  }

  public void warn(String mes) {
    log("WARNING", mes);
  }

  public void warn(String mes, Object... params) {
    log("WARNING", formatMessage(mes, params));
  }

  public void catching(String mes) {
    log("ERROR", mes);
  }

  public void error(String mes) {
    log("ERROR", mes);
  }

  public void error(String mes, Object... params) {
    log("ERROR", formatMessage(mes, params));
  }

  public void error(Throwable t) {
    StringBuilder error = new StringBuilder(t.toString());
    error.append(t.toString());
    for (StackTraceElement s : t.getStackTrace()) {
      error.append("\n").append(s);
    }
    log("ERROR", error.toString());
  }

  public void error(String mes, Throwable t) {
    StringBuilder error = new StringBuilder(mes);
    if (!mes.isEmpty()) {
      error.append("\n");
    }
    error.append(t.toString());
    for (StackTraceElement s : t.getStackTrace()) {
      error.append("\n").append(s);
    }
    log("ERROR", error.toString());
  }

  private void log(String level, String mes) {
    String[] a = getClassNameAndMethod(3);
    logger.logp(CustomLevel.parse(level), a[0], a[1], mes);
  }

  private String formatMessage(String mes, Object... params) {
    for (int i = 0; i < params.length; i++) {
      mes = mes.replaceFirst("\\{}", "{" + i + "}");
    }
    return MessageFormat.format(mes, params);
  }
}
