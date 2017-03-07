package qingmang.raml.article.binder;

import android.view.View;
import android.view.ViewGroup;

import qingmang.raml.article.model.HElement;
import qingmang.raml.mvc.binder.GroupBinder;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class ArticleGroupBinder extends GroupBinder<HElement> {
  public ArticleGroupBinder(ViewGroup parent, int layoutResId) {
    super(parent, layoutResId);
  }

  public ArticleGroupBinder(View view) {
    super(view);
  }
}
