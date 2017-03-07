package qingmang.raml.article.model;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class Li {
  /**
   * 0 表示没有，1 表示 ul, 2 表示 ol, 3 表示 tab
   */
  public Type type;
  /**
   * start from 1
   */
  public int level;

  /**
   * start from 1
   */
  public int order;

  public boolean hasValue() {
    return type != null && level >= 0 && order > 0;
  }

  public enum Type {
    ol,
    ul,
    tab,
  }
}
