import log.LogFactory;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * 日志测试
 * time: 2020/6/19 15:45
 * author:msm
 */
public class LogTest {
  private static final Logger log = LogFactory.getLog("测试模块");

  @Test
  public void test() {
//    log.severe("severe-->   this is severe!");
//    log.warning("warning-->   this is warning!");
//    log.info("info-->   this is info!");
//    log.config("config-->   this is config!");
//    log.fine("fine-->   this is fine!");
//    log.finer("finer-->   this is finer!");
  }

  public static void main(String[] args) {
//    log.severe("severe-->   this is severe!");
//    log.warning("warning-->   this is warning!");
//    log.info("info-->   this is info!");
//    log.config("config-->   this is config!");
//    log.fine("fine-->   this is fine!");
//    log.finer("finer-->   this is finer!");
    System.out.println(123);
    System.out.println(123);
    System.out.println(123);
  }
}
