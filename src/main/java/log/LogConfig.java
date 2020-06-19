package log;

import java.io.File;

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
  protected static final String LOG_FOLDER = "Log";
  /**
   * 日志输出时间 基于SimpleDateFormat对象的日期字符串字符串模板
   */
  protected static final String OUTPUT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.sss";

  /**
   * 日志文件夹
   */
  protected static final File OUTPUT_FOLDER;


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
