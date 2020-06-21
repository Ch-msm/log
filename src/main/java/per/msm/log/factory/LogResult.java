package per.msm.log.factory;

import java.util.Map;
import java.util.logging.LogRecord;

/**
 * 自定义日志返回结果
 * Date  2020/6/21 13:36
 *
 * @author msm
 */
public class LogResult {
  /**
   * 自定义的日志属性
   */
  private Map<String, String> map;
  /**
   * 日志记录器
   */
  private LogRecord logRecord;

  public Map<String, String> getMap() {
    return map;
  }

  public void setMap(Map<String, String> map) {
    this.map = map;
  }

  public LogRecord getLogRecord() {
    return logRecord;
  }

  public void setLogRecord(LogRecord logRecord) {
    this.logRecord = logRecord;
  }
}
