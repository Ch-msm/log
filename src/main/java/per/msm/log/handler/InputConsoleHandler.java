package per.msm.log.handler;

import per.msm.log.factory.InputFormatter;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * 控制台输出处理器
 *
 * @author msm
 * Date 2020/6/19 23:12
 */
public class InputConsoleHandler extends StreamHandler {
  // Private method to configure a ConsoleHandler from LogManager
  // properties and/or default values as specified in the class
  // javadoc.
  private void configure() {
    setLevel(Level.INFO);
    setFilter(null);
    setFormatter(new InputFormatter());
  }

  /**
   * Create a <span>ConsoleHandler</span> for <span>System.err</span>.
   * <p>
   * The <span>ConsoleHandler</span> is configured based on
   * <span>LogManager</span> properties (or their default values).
   */
  public InputConsoleHandler() {
    configure();
    setOutputStream(System.out);
  }

  /**
   * Publish a <span>LogRecord</span>.
   * <p>
   * The logging request was made initially to a <span>Logger</span> object,
   * which initialized the <span>LogRecord</span> and forwarded it here.
   * <p>
   *
   * @param record description of the pers.msm.log event. A null record is
   *               silently ignored and is not published
   */
  @Override
  public void publish(LogRecord record) {
    super.publish(record);
    flush();
  }

  /**
   * Override <span>StreamHandler.close</span> to do a flush but not
   * to close the output stream.  That is, we do <b>not</b>
   * close <span>System.err</span>.
   */
  @Override
  public void close() {
    flush();
  }
}
