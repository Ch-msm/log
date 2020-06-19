package log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 日志工厂类
 * time: 2020/6/19 15:19
 *
 * @author msm
 */
public class LogFactory {

  /**
   * 全局Log的名称
   */
  public static final String LOG_NAME = "Global";

  /**
   * 静态变量globalLog
   */
  private static final Logger globalLog;

  static {
    globalLog = initGlobalLog();
  }

  /**
   * 初始化全局Logger
   *
   * @return log对象
   */
  public static Logger initGlobalLog() {

    // 获取Log
    Logger log = Logger.getLogger(LOG_NAME);

    // 为log设置全局等级
    log.setLevel(Level.ALL);

    // 添加控制台handler
    LogUtil.addConsoleHandler(log, Level.ALL);

    // 添加文件输出handler
    LogUtil.addFileHandler(log, Level.ALL, LogConfig.OUTPUT_FOLDER.getAbsolutePath());

    // 设置不适用父类的handlers，这样不会在控制台重复输出信息
    log.setUseParentHandlers(false);

    return log;
  }

  /**
   * 全局日志
   *
   * @return 日志对象
   */
  public static Logger getGlobalLog() {
    return globalLog;
  }

  /**
   * 日志
   *
   * @param name 日志名称
   * @return 当前日志
   */
  public static Logger getLog(String name) {
    Logger log = Logger.getLogger(name);
    // 为log设置全局等级
    log.setLevel(globalLog.getLevel());


    for (Handler handler : globalLog.getHandlers()) {
      log.addHandler(handler);
    }
    log.setUseParentHandlers(false);
    return log;
  }
}
