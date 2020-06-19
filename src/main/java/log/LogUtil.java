package log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * 日志工具
 * time: 2020/6/19 15:16
 *
 * @author msm
 */
public class LogUtil {

  /**
   * 为log添加控制台handler
   *
   * @param log   要添加handler的log
   * @param level 控制台的输出等级
   */
  public static void addConsoleHandler(Logger log, Level level) {
    // 控制台输出的handler
    ConsoleHandler consoleHandler = new ConsoleHandler();
    // 设置控制台输出的等级（如果ConsoleHandler的等级高于或者等于log的level，则按照FileHandler的level输出到控制台，如果低于，则按照Log等级输出）
    consoleHandler.setLevel(level);
    consoleHandler.setFormatter(new Formatter() {
      @Override
      public String format(LogRecord record) {
        return logOutputFormat(record);
      }
    });
    // 添加控制台的handler
    log.addHandler(consoleHandler);
  }

  /**
   * 为log添加文件输出Handler
   *
   * @param log      要添加文件输出handler的log
   * @param level    log输出等级
   * @param filePath 指定文件全路径
   */
  public static void addFileHandler(Logger log, Level level, String filePath) {
    FileHandler fileHandler;
    try {
      fileHandler = new XLFileStreamHandler(filePath, 0, 1, true);
      // 设置输出文件的等级（如果FileHandler的等级高于或者等于log的level，则按照FileHandler的level输出到文件，如果低于，则按照Log等级输出）
      fileHandler.setLevel(level);

      // 添加输出文件handler
      log.addHandler(fileHandler);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param record 日志记录对象
   * @return 日志统一输出
   */
  protected static String logOutputFormat(LogRecord record) {
    @SuppressWarnings("StringBufferReplaceableByString")
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder
        .append(getCurrentDateStr(LogConfig.OUTPUT_DATE_PATTERN))
        .append(" ").append(record.getLevel().getName())
        .append(" ").append(" [").append(record.getLoggerName())
        .append("] ").append(record.getSourceClassName())
        .append(" ")
        .append(record.getSourceMethodName())
        .append("\n")
        .append(record.getMessage())
        .append("\n");
    return stringBuilder.toString();
  }

  /**
   * 获取当前时间
   *
   * @return 当前时间字符串
   */
  public static String getCurrentDateStr(String pattern) {
    return new SimpleDateFormat(pattern).format(new Date());
  }
}
