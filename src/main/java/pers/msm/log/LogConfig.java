package pers.msm.log;

import pers.msm.log.factory.CustomLevel;

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
  public static final String LOG_FOLDER = "Log";
  /**
   * 日志输出时间 基于SimpleDateFormat对象的日期字符串字符串模板
   */
  public static final String OUTPUT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.sss";
  /**
   * 日志文件夹
   */
  public static final File OUTPUT_FOLDER;
  /**
   * 日志文件名称
   */
  public static final String LOG_FILE_NAME = "log";
  /**
   * 日志文件大小 单位（M） 默认10 如果为0 不分片
   */
  public static final int LOG_SIZE = 10;
  /**
   * 日志文件保留天数 为0时 不删除
   */
   public static final int DATELINE = 5;
  /**
   * 日志输出是否追加在已有的文件最后
   */
  public static final boolean APPEND = true;
  /**
   * 控制台输出等级
   */
  public static Level CONSOLE_LEVEL = CustomLevel.ALL;
  /**
   *  日志文件输出等级
   */
  public static Level FILE_LEVEL = CustomLevel.ALL;
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
