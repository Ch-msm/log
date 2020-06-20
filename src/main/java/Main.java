import pers.msm.log.factory.CustomLevel;
import pers.msm.log.factory.LogFactory;

import java.util.logging.Logger;

/**
 * time: 2020/6/19 17:51
 * author:msm
 */
public class Main {
  private static final Logger log = LogFactory.getLog("测试模块");

  public static void main(String[] args) {

    for (int i = 0; i < 1000; i++) {
      log.severe("severe-->   this is severe!");
      log.warning("warning-->   this is warning!");
      log.info("测试日志功能");
      log.log(CustomLevel.DEBUG,"测试日志功能");
      log.log(CustomLevel.ERROR,"测试日志功能");
    }
  }
}
