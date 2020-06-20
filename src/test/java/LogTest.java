import pers.msm.log.factory.Log;
import pers.msm.log.factory.LogFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 日志测试
 * time: 2020/6/19 15:45
 * author:msm
 */
public class LogTest {
  private static final Log log = LogFactory.getLog("测试模块");

  public static void main(String[] args) {
    Map<String, String> map = new LinkedHashMap<>();
    map.put("用户：", "梅思铭");
    map.put("IP:", "192.168.0.2");
    LogFactory.logThreadLocal.set(map);
    for (int i = 0; i < 10; i++) {
      log.debug("测试日志功能");
      log.info("测试日志功能");
      log.debug("测试日志功能:{},{}", "debug ", "debug");
      log.info("测试日志功能:{},{}", "info ", "123");
      log.warn("测试日志功能:{},{}", "info ", "123");
      log.error("123");
      log.error(new Exception("123"));
    }
  }
}
