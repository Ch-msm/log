package per.msm.log;

import per.msm.log.factory.CustomLevel;

import java.io.File;
import java.util.logging.Level;

/**
 * 日志配置
 * time: 2020/6/19 15:27
 *
 * @author msm
 */
public class LogConfig {
  /**
   * 日志文件夹 默认当前目录下
   */
  public static String LOG_FOLDER = "Log";
  /**
   * 日志输出时间 基于SimpleDateFormat对象的日期字符串字符串模板
   */
  public static String OUTPUT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.sss";
  /**
   * 日志文件夹
   */
  public static File OUTPUT_FOLDER;
  /**
   * 日志文件名称
   */
  public static String LOG_FILE_NAME = "log";
  /**
   * 日志文件大小 单位（M） 默认10 如果为0 不分片
   */
  public static int LOG_SIZE = 1;
  /**
   * 日志文件保留天数 为0时 不删除
   */
  public static int DATELINE = 5;
  /**
   * 日志输出是否追加在已有的文件最后
   */
  public static boolean APPEND = true;
  /**
   * 控制台输出等级
   */
  public static Level CONSOLE_LEVEL = CustomLevel.ALL;
  /**
   * 日志文件输出等级
   */
  public static Level FILE_LEVEL = CustomLevel.ALL;
  /**
   * 自定义日志输出等级
   */
  public static Level CUSTOM_LEVEL = CustomLevel.OFF;
  /**
   * 自定义日志处理函数
   */
  public static String CUSTOM_LOG_FUN = "per.msm.log.factory.SimpleFunction";

  /*
    初始化目录文件夹
   */
  static {
    OUTPUT_FOLDER = new File(LOG_FOLDER);
    if (!OUTPUT_FOLDER.exists()) {
      //noinspection ResultOfMethodCallIgnored
      OUTPUT_FOLDER.mkdirs();
    }
  }
}
