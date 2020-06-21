import per.msm.log.factory.LogResult;

import java.util.function.Function;

/**
 * 简单的Function接口实现
 * Date  2020/6/21 13:53
 *
 * @author msm
 */
public class SimpleFunction implements Function<LogResult, Object> {
  @Override
  public Object apply(LogResult o) {
    System.out.println(o.getMap());
    return o;
  }
}
