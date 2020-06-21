import per.msm.log.LogConfig;
import per.msm.log.factory.Log;
import per.msm.log.factory.LogFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 日志测试
 * time: 2020/6/19 15:45
 * author:msm
 */
public class LogTest {
  static {
    LogConfig.CUSTOM_LOG_FUN = "SimpleFunction";
  }

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 1000; i++) {
      int finalI = i;
      new Thread(() -> {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("用户：", "梅思铭");
        map.put("IP:", "192.168.0." + finalI);
        LogFactory.logThreadLocal.set(map);
        Log log = LogFactory.getLog("测试模块" + finalI);
        log.info("测试日志功能:{},{}", "info ", "info");
      }).start();
      Thread.sleep(1000);
    }
  }
}
