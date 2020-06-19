package log;

import java.util.logging.Logger;

/**
 * time: 2020/6/19 17:51
 * author:msm
 */
public class Main {
  private static final Logger log = LogFactory.getLog("测试模块");

  public static void main(String[] args) {

    for (int i = 0; i < 10000; i++) {
      log.severe("severe-->   this is severe!");
      log.warning("warning-->   this is warning!");
      log.info("111");
    }
  }
}
