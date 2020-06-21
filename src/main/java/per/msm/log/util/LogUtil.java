package per.msm.log.util;

import per.msm.log.LogConfig;
import per.msm.log.handler.CustomHandler;
import per.msm.log.handler.FileStreamHandler;
import per.msm.log.handler.InputConsoleHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    InputConsoleHandler consoleHandler = new InputConsoleHandler();
    // 设置控制台输出的等级（如果ConsoleHandler的等级高于或者等于log的level，则按照FileHandler的level输出到控制台，如果低于，则按照Log等级输出）
    consoleHandler.setLevel(level);
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
    FileStreamHandler fileHandler;
    try {
      fileHandler = new FileStreamHandler(filePath, 1024 * 1024 * LogConfig.LOG_SIZE, LogConfig.DATELINE, LogConfig.APPEND);
      // 设置输出文件的等级（如果FileHandler的等级高于或者等于log的level，则按照FileHandler的level输出到文件，如果低于，则按照Log等级输出）;
      fileHandler.setEncoding("UTF-8");
      fileHandler.setLevel(level);
      // 添加输出文件handler
      log.addHandler(fileHandler);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 为log添加自定义输出Handler
   *
   * @param log   log
   * @param level log输出等级
   */
  @SuppressWarnings("unchecked")
  public static void addCustomHandler(Logger log, Level level) {
    CustomHandler fileHandler;
    try {
      Object fun = Class.forName(LogConfig.CUSTOM_LOG_FUN).newInstance();
      //noinspection rawtypes
      fileHandler = new CustomHandler((Function) fun);
      // 设置输出文件的等级（如果FileHandler的等级高于或者等于log的level，则按照FileHandler的level输出到文件，如果低于，则按照Log等级输出）;
      fileHandler.setLevel(level);
      // 添加输出文件handler
      log.addHandler(fileHandler);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取当前时间
   *
   * @param pattern 字符串格式
   * @return 当前时间字符串
   */
  public static String getCurrentDateStr(String pattern) {
    return new SimpleDateFormat(pattern).format(new Date());
  }
}
