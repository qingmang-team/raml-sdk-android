package qingmang.raml.article.ui.span;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.Spanned;

import qingmang.raml.article.ui.UITheme;

/**
 * layer type = soft ware 时，性能会有大幅下降，所以先画实线了.
 *
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class DashUnderlineSpan implements ILineDecorationSpan {

  private final int color;
  private final int dashWidth;
  private final int dashGap;

  public DashUnderlineSpan(int color, int dashWidth, int dashGap) {
    this.color = color;
    this.dashWidth = dashWidth;
    this.dashGap = dashGap;
  }

  @Override
  public void drawLineBackground(Canvas canvas, Paint paint, Spanned spanned, int spanStart,
      int spanEnd, Rect rect, int baseline) {

    // float textSize = paint.getTextSize();
    // int bottom = (int) (rect.bottom - textSize / 8);
    Paint.FontMetrics metrics = paint.getFontMetrics();
    int bottom = Math.round(baseline + metrics.bottom + UITheme.UNDERLINE_MARGIN);
    Paint backgroundPaint = new Paint(paint);
    backgroundPaint.setColor(color);
    backgroundPaint.setStyle(Paint.Style.STROKE);
    backgroundPaint.setPathEffect(new DashPathEffect(new float[] {dashWidth, dashGap}, 0));
    backgroundPaint.setStrokeWidth(2);
    backgroundPaint.setAntiAlias(true);
    backgroundPaint.setDither(true);
    Path path = new Path();
    path.moveTo(rect.left, bottom);
    path.lineTo(rect.right, bottom);
    canvas.drawPath(path, backgroundPaint);
  }
}
