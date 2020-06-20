package pers.msm.log.handler;

import pers.msm.log.factory.InputFormatter;

import java.util.logging.*;

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
   * Create a <tt>ConsoleHandler</tt> for <tt>System.err</tt>.
   * <p>
   * The <tt>ConsoleHandler</tt> is configured based on
   * <tt>LogManager</tt> properties (or their default values).
   */
  public InputConsoleHandler() {
    configure();
    setOutputStream(System.out);
  }

  /**
   * Publish a <tt>LogRecord</tt>.
   * <p>
   * The logging request was made initially to a <tt>Logger</tt> object,
   * which initialized the <tt>LogRecord</tt> and forwarded it here.
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
   * Override <tt>StreamHandler.close</tt> to do a flush but not
   * to close the output stream.  That is, we do <b>not</b>
   * close <tt>System.err</tt>.
   */
  @Override
  public void close() {
    flush();
  }
}
