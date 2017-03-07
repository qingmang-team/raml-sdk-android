package qingmang.raml.ui.view;

import java.lang.ref.WeakReference;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

/**
 * 支持 align center
 *
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class WebImageSpan extends ImageSpan {

  /**
   * A constant indicating that the bottom of this span should be aligned
   * with the bottom of the surrounding text, i.e., at the same level as the
   * lowest descender in the text.
   */
  public static final int ALIGN_BOTTOM = 0;

  /**
   * A constant indicating that the bottom of this span should be aligned
   * with the baseline of the surrounding text.
   */
  public static final int ALIGN_BASELINE = 1;

  public static final int ALIGN_CENTER = 2;

  private WeakReference<Drawable> mDrawableRef;

  public WebImageSpan(final Drawable drawable) {
    super(drawable);
  }

  public WebImageSpan(final Drawable d, final int verticalAlignment) {
    super(d, verticalAlignment);
  }

  public WebImageSpan(Drawable d, String source, int verticalAlignment) {
    super(d, source, verticalAlignment);
  }

  @Override
  public int getSize(Paint paint, CharSequence text,
      int start, int end, Paint.FontMetricsInt fm) {
    Drawable d = getCachedDrawable();
    Rect rect = d.getBounds();

    if (fm != null) {
      fm.ascent = -rect.bottom;
      fm.descent = 0;

      fm.top = fm.ascent;
      fm.bottom = 0;
    }

    return rect.right;
  }

  @Override
  public void draw(@NonNull Canvas canvas, CharSequence text,
      int start, int end, float x,
      int top, int y, int bottom, @NonNull Paint paint) {

    Drawable d = getCachedDrawable();
    canvas.save();
    int transY;
    // 只是用来估算间距，见 rip_detail_article_text;
    int lineSpacing = (int) (paint.getFontSpacing() * 0.5);
    if (mVerticalAlignment == ALIGN_BOTTOM) {
      transY = bottom - d.getBounds().bottom;
    } else if (mVerticalAlignment == ALIGN_CENTER) {
      int fontTop = paint.getFontMetricsInt().top; // 注意 ascent, top 是负数
      int fontBottom = paint.getFontMetricsInt().bottom; // descent, bottom 为正
      int fontHeight = fontBottom - fontTop;
      if (fontHeight < d.getBounds().bottom - lineSpacing) {// 如果图片比行高
        transY = bottom - d.getBounds().bottom - lineSpacing;
      } else {
        transY = bottom - lineSpacing - d.getBounds().bottom / 2 // align bottom to bottom
            - fontHeight / 2; // align center to center
      }
    } else { // ALINE_BASELINE
      transY = bottom - d.getBounds().bottom - paint.getFontMetricsInt().descent;
    }

    canvas.translate(x, transY);
    d.draw(canvas);
    canvas.restore();
  }

  // Redefined locally because it is a private member from DynamicDrawableSpan
  private Drawable getCachedDrawable() {
    WeakReference<Drawable> wr = mDrawableRef;
    Drawable d = null;

    if (wr != null)
      d = wr.get();

    if (d == null) {
      d = getDrawable();
      mDrawableRef = new WeakReference<>(d);
    }

    return d;
  }
}
