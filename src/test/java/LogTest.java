import pers.msm.log.factory.LogFactory;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * 日志测试
 * time: 2020/6/19 15:45
 * author:msm
 */
public class LogTest {
  private static final Logger log = LogFactory.getLog("测试模块");
  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

  @Test
  public void test() {

    long a = System.currentTimeMillis();
    for (int i = 0; i < 100000; i++) {
      String s = LocalDate.now().toString();

    }
    System.out.println(System.currentTimeMillis()-a);



  }

  public static void main(String[] args) {
//    pers.msm.log.severe("severe-->   this is severe!");
//    pers.msm.log.warning("warning-->   this is warning!");
//    pers.msm.log.info("info-->   this is info!");
//    pers.msm.log.config("config-->   this is config!");
//    pers.msm.log.fine("fine-->   this is fine!");
//    pers.msm.log.finer("finer-->   this is finer!");

  }
}
