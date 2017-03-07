package qingmang.raml.article.binder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import qingmang.raml.R;
import qingmang.raml.article.model.HElement;
import qingmang.raml.article.ui.UITheme;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class DecorationBindWorker extends ArticleBindWorker {
  @Override
  public void bind(View view, HElement model) {

    if (model.blockquote > 0) {
      show(R.id.blockquote);
    } else {
      hide(R.id.blockquote);
    }
    TextView liView = view(R.id.li);
    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) liView.getLayoutParams();
    params.setMargins(0, Math.round(UITheme.FONT_NORMAL_SPACING), 0, 0);
    if (model.li != null && model.li.hasValue()) {
      liView.setVisibility(View.VISIBLE);
      switch (model.li.type) {
        case ul:
          if (model.li.level > 1) {
            liView.setText("◦");
          } else {
            liView.setText("•");
          }
          break;
        case ol:
          liView.setText(String.valueOf(model.li.order));
          break;
        case tab:
          liView.setText("");
          break;
      }
    } else {
      liView.setVisibility(View.GONE);
    }
  }

  @Override
  public void unbind(View view) {

  }
}
