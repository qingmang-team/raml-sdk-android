package qingmang.raml.article.model;

import java.util.List;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class HTable {
  public int rowCount;
  public int colCount;
  public List<List<HText>> items;

  public boolean showLink() {
    return colCount > 3 || rowCount > 10;
  }
}
