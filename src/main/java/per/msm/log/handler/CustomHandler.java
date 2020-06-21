package per.msm.log.handler;

import per.msm.log.factory.LogFactory;
import per.msm.log.factory.LogResult;

import java.util.function.Function;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * 自定义日志处理器
 * Date  2020/6/21 13:20
 *
 * @author msm
 */
public class CustomHandler extends Handler {
  private final Function<LogResult, Object> f;

  public CustomHandler(Function<LogResult, Object> f) {
    this.f = f;
  }

  @Override
  public void publish(LogRecord record) {
    LogResult logResult = new LogResult();
    logResult.setLogRecord(record);
    logResult.setMap(LogFactory.logThreadLocal.get());
    f.apply(logResult);
  }

  @Override
  public void flush() {
    //不用实现
  }

  @Override
  public void close() throws SecurityException {
    //不用实现
  }
}
