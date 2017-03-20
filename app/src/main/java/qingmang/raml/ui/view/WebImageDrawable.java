package qingmang.raml.ui.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import qingmang.raml.R;
import qingmang.raml.RamlApplication;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */

public class WebImageDrawable extends Drawable {

  private String imageUrl;
  private Resources resources;
  private BitmapDrawable drawable;

  public WebImageDrawable(Resources resources, String imageUrl) {
    this.resources = resources;
    this.imageUrl = imageUrl;
  }

  /**
   * 开始展示图片.
   *
   * @return
   */
  public void load() {
    Bitmap bitmap = getCacheImage(imageUrl);
    if (bitmap != null) {
      this.drawable = new BitmapDrawable(resources, Bitmap.createBitmap(bitmap));
      this.drawable.setBounds(getBounds());
    } else {
      prefetch(imageUrl);
    }
  }

  @Override
  public void draw(Canvas canvas) {
    // 这里绕开了回调，使用预取 + 绘制前加载的方式，来实现图片的异步加载. 它的好处是实现简单，而且可以减少界面
    // 的重绘. 缺点就是不能 100% 可靠，可能某个表情没加载出来就没加载出来. 回头可以根据效果做优化，考虑是否需要
    // 使用预取 + 绘制前加载 + 如果绘制前加载失败就异步刷新 的策略.
    if (drawable == null) {
      load();
    }
    if (drawable != null) {
      drawable.draw(canvas);
    }
  }

  @Override
  public void setAlpha(int i) {
    if (drawable != null) {
      drawable.setAlpha(i);
    }
  }

  @Override
  public void setColorFilter(ColorFilter colorFilter) {
    if (drawable != null) {
      drawable.setColorFilter(colorFilter);
    }
  }

  @Override
  public int getOpacity() {
    if (drawable != null) {
      return drawable.getOpacity();
    }
    return 0;
  }

  /**
   * 同步返回 cache image，如果没有 cache，返回 null
   * 有个问题，当图片是 gif 的时候，imageReference 一直返回 null，还没有查是什么原因
   *
   * @param url
   * @return
   */
  public static Bitmap getCacheImage(String url) {
    if (TextUtils.isEmpty(url)) {
      return null;
    }
    final ImagePipeline imagePipeline = Fresco.getImagePipeline();
    ImageRequestBuilder imageRequestBuilder =
        ImageRequestBuilder.newBuilderWithSource(Uri.parse(url));
    final ImageRequest imageRequest = imageRequestBuilder.build();
    DataSource<CloseableReference<CloseableImage>> dataSource =
        imagePipeline.fetchImageFromBitmapCache(imageRequest, null);
    try {
      CloseableReference<CloseableImage> imageReference = dataSource.getResult();
      if (imageReference != null) {
        try {
          CloseableImage image = imageReference.get();
          if (image instanceof CloseableBitmap) {
            return ((CloseableBitmap) image).getUnderlyingBitmap();
          } else {
            return null;
          }
          // do something with the image
        } finally {
          CloseableReference.closeSafely(imageReference);
        }
      }
    } finally {
      dataSource.close();
    }
    return null;
  }

  /**
   * 预取图片.
   *
   * @param imageUrl
   */
  public static void prefetch(String imageUrl) {
    final ImagePipeline imagePipeline = Fresco.getImagePipeline();
    final ImageRequest imageRequest =
        ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl)).build();
    imagePipeline.prefetchToBitmapCache(imageRequest, null);
  }
}
