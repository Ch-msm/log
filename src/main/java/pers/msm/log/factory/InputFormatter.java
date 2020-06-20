package pers.msm.log.factory;

import pers.msm.log.LogConfig;
import pers.msm.log.util.LogUtil;

import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author msm
 * Date 2020/6/19 23:22
 */
public class InputFormatter extends Formatter {
  @Override
  public String format(LogRecord record) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder
        .append(LogUtil.getCurrentDateStr(LogConfig.OUTPUT_DATE_PATTERN))
        .append(" ").append(record.getLevel().getName())
        .append(" ").append(" [").append(record.getLoggerName())
        .append("] ").append(record.getSourceClassName())
        .append(" ")
        .append(record.getSourceMethodName());
    Map<String, String> logMap = LogFactory.logThreadLocal.get();
    if (logMap != null && !logMap.isEmpty()) {
      stringBuilder.append(" ");
      logMap.keySet().forEach(
          o -> stringBuilder
              .append(o)
              .append(logMap.get(o))
              .append(" ")
      );
    }
    stringBuilder
        .append("\n")
        .append(record.getMessage())
        .append("\n");


    return stringBuilder.toString();
  }
}
