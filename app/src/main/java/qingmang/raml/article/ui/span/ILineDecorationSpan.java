package qingmang.raml.article.ui.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Spanned;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public interface ILineDecorationSpan {
  void drawLineBackground(Canvas canvas, Paint paint, Spanned spanned, int spanStart,
      int spanEnd, Rect rect, int baseline);
}
