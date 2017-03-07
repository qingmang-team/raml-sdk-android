package qingmang.raml.utils;

import java.util.Collection;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class CollectionUtils {
  private CollectionUtils() {}

  public static boolean isEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }
}
