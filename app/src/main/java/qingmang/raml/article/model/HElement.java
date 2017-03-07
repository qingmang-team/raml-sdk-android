package qingmang.raml.article.model;

import java.io.Serializable;
import java.util.List;


/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class HElement implements ITags, Serializable {
  public String id;
  public int type;
  public HText text;
  public HImage image;
  public HMedia media;
  public HTable table;
  public List<HImage> images;
  public Li li;
  public int blockquote;

  public static final int TYPE_TEXT = 0;
  public static final int TYPE_IMAGE = 1;
  public static final int TYPE_VIDEO = 2;
  public static final int TYPE_AUDIO = 3;
  public static final int TYPE_TABLE = 4;
  public static final int TYPE_GALLERY = 5;
  public static final int TYPE_BREAK = 10;
  public static final int TYPE_SEPARATOR = 11;

  /**
   * 对 item 做一些处理来满足展示的需要，返回这个 item 是否是合法的，list 应该将不合法的 item 过滤.
   *
   * @return valid or not.
   */
  public boolean prepareAndCheckValid() {
    switch (type) {
      case TYPE_TEXT:
        text.buildSpannable();
        return text != null;
      case TYPE_IMAGE:
        return image != null;
      case TYPE_AUDIO:
      case TYPE_VIDEO:
        return media != null;
      case TYPE_BREAK:
      case TYPE_SEPARATOR:
        return true;
      case TYPE_TABLE:
        if (table == null || table.items == null || table.items.size() <= 0) {
          return false;
        }
        for (List<HText> row : table.items) {
          for (HText text : row) {
            text.buildSpannable();
          }
        }
        return true;
      default:
        return false;
    }
  }
}
