package qingmang.raml.article.model;

/**
 * text 的语义标签，如果不为空(默认)，那么应该忽略所有的 markups.
 *
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public enum TextlineType {
  // text type
  code,
  pre, // code 都当成 pre 处理
  // figcaption,
  h1,
  h2,
  h3,
  h4,
  h5,
  h6,
  // TODO 这两个 type 将会被其他有语义的替代
  big,
  small,
  aside;


  /**
   * TODO.！
   * 这里有点黑，在读取原始 tag 的时候，big small 并不是当做 text line type 来读取的
   * 需要在 build 的时候检测是否是整行的 big 和 small，然后设置为 text style
   * 其他的 textline style 一定是单独的一段，所以没有同样的问题.
   *
   * @param tag
   * @return
   */
  public static TextlineType parse(String tag) {
    if ("big".equals(tag) || "small".equals(tag)) {
      return null;
    }
    try {
      return TextlineType.valueOf(tag);
    } catch (Exception e) {
      return null;
    }
  }
}
