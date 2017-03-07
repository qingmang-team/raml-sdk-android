package qingmang.raml.ui.controller;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import qingmang.raml.RamlApplication;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class ImageController {
  private final String source;
  private int width;
  private int height;
  private boolean adjustBounds;

  public ImageController(String source) {
    super();
    this.source = source;
    this.adjustBounds = false;
  }

  public ImageController adjustBounds(boolean adjustBounds, int width, int height) {
    this.adjustBounds = adjustBounds;
    this.width = width;
    this.height = height;
    return this;
  }

  public void bind(View view) {
    String url = source;
    if (!(view instanceof SimpleDraweeView)) {
      return;
    }
    final SimpleDraweeView draweeView = (SimpleDraweeView) view;
    if (TextUtils.isEmpty(url)) {
      draweeView.setImageURI("");
      return;
    }
    if (width > 0 && width < 300) {
      float density = RamlApplication.getInstance().getResources().getDisplayMetrics().density;
      draweeView.getLayoutParams().width = (int) (width * density);
    } else {
      draweeView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }
    if (!adjustBounds) {
      DraweeController controller = Fresco.newDraweeControllerBuilder()
          .setAutoPlayAnimations(true)
          .setUri(Uri.parse(url))
          .build();
      draweeView.setController(controller);
      return;
    }
    if (width > 0 && height > 0) {
      draweeView.setAspectRatio((float) width / height);
      DraweeController controller = Fresco.newDraweeControllerBuilder()
          .setAutoPlayAnimations(true)
          .setUri(Uri.parse(url))
          .build();
      draweeView.setController(controller);
      return;
    }
    draweeView.setAspectRatio(2.0f); // 先设置一个值
    DraweeController controller = Fresco.newDraweeControllerBuilder()
        .setAutoPlayAnimations(true)
        .setControllerListener(new BaseControllerListener<ImageInfo>() {
          @Override
          public void onFinalImageSet(String iid, @Nullable ImageInfo info,
              @Nullable Animatable anim) {
            if (info != null) {
              draweeView.setAspectRatio((float) info.getWidth() / info.getHeight());
            }
          }
        })
        .setUri(Uri.parse(url))
        .build();
    draweeView.setController(controller);
  }
}
