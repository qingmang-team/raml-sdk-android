package qingmang.raml.article.binder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import qingmang.raml.R;
import qingmang.raml.article.model.HElement;
import qingmang.raml.article.model.HImage;
import qingmang.raml.ui.controller.ImageController;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class ImageBindWorker extends ArticleBindWorker {
  @Override
  public void bind(View view, HElement model) {
    HImage image = model.image;
    if (image == null) {
      return;
    }
    new ImageController(image.source)
        .adjustBounds(true, image.width, image.height)
        .bind(view(R.id.image));
    TextView textView = view(R.id.title);
    if (TextUtils.isEmpty(image.title)) {
      textView.setVisibility(View.GONE);
    } else {
      textView.setVisibility(View.VISIBLE);
      textView.setText(image.title);
    }
  }

  @Override
  public void unbind(View view) {}
}
