package qingmang.raml.article.ui.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class LineDecorationSpanShim implements LineBackgroundSpan {
  private final int horiGravityFlag = Gravity.LEFT | Gravity.RIGHT | Gravity.CENTER_HORIZONTAL;
  private final TextPaint workPaint;
  private final TextView textView;

  public LineDecorationSpanShim(TextView textView) {
    this.workPaint = new TextPaint();
    this.textView = textView;
  }

  @Override
  public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int
      bottom, CharSequence text, int start, int end, int lnum) {
    if (!(text instanceof Spanned)) {
      return;
    }
    Spanned spanned = (Spanned) text;
    /**
     * 为了处理 center align，在 draw span 的时候，paint 的 text align 并不是最终的 text align
     * 这时遇到的 text 已经划分为行了，所以不需要关心 vertical gravity.
     */
    Paint.Align align = p.getTextAlign();
    if ((horiGravityFlag & textView.getGravity()) == Gravity.CENTER_HORIZONTAL) {
      p.setTextAlign(Paint.Align.CENTER);
    }
    ILineDecorationSpan spans[] = spanned.getSpans(start, end, ILineDecorationSpan.class);
    for (int i = 0; i < spans.length; i++) {
      drawLineBackground(spans[i], c, p, left, right, top, baseline, bottom, text, start, end, lnum);
    }
    p.setTextAlign(align);
  }

  public void drawLineBackground(ILineDecorationSpan span, Canvas c, Paint p, int left, int right,
      int top, int baseline, int
      bottom, CharSequence text, int start, int end, int lnum) {
    Spanned spanned = (Spanned) text;
    int spanStart = Math.max(spanned.getSpanStart(span), start);
    int spanEnd = Math.min(spanned.getSpanEnd(span), end);
    if (spanStart > spanEnd) {
      return;
    }
    int leftMargin = left + getParagraphLeadingMargin(spanned, start, end, lnum);
    leftMargin =
        leftMargin + getAlignmentLeftMargin(p, leftMargin, right, spanned, start, end);
    float beforeSpan = measureWidth(p, spanned, start, spanStart);
    float inSpan = measureWidth(p, spanned, spanStart, spanEnd);
    span.drawLineBackground(c, p, spanned, spanStart, spanEnd, new Rect(leftMargin
        + (int) beforeSpan, top, leftMargin + (int) (beforeSpan + inSpan), bottom), baseline);
  }

  private int getParagraphLeadingMargin(Spanned spanned, int start, int end, int lnum) {
    LeadingMarginSpan spans[] = spanned.getSpans(start, end, LeadingMarginSpan.class);
    if (spans.length == 0) {
      return 0;
    }
    int margin = 0;
    boolean flag;
    int i;
    if (lnum == 0) {
      flag = true;
    } else {
      flag = false;
    }
    i = 0;
    while (i < spans.length) {
      LeadingMarginSpan span = spans[i];
      if (span instanceof LeadingMarginSpan.LeadingMarginSpan2) {
        if (lnum < ((LeadingMarginSpan.LeadingMarginSpan2) span).getLeadingMarginLineCount()) {
          flag |= true;
        } else {
          flag |= false;
        }
      }
      i++;
    }
    for (int j = 0; j < spans.length; j++) {
      margin += spans[j].getLeadingMargin(flag);
    }

    return margin;
  }

  private int getAlignmentLeftMargin(Paint paint, int left, int right, CharSequence text,
      int start, int end) {
    if (paint.getTextAlign() == Paint.Align.CENTER) {
      /**
       * paint.measureText 并不能准确的计算 spannable 的宽度.
       */
      // float textWidth = paint.measureText(text, start, end);
      workPaint.setTextSize(paint.getTextSize());
      // 不能取 subsequence，会循环调用... why?
      float textWidth = Layout.getDesiredWidth(text, start, end, workPaint);
      return Math.round(((float) (right - left) - textWidth) / 2.0f);
    } else {
      return 0;
    }
  }

  private float measureWidth(Paint paint, Spanned spanned, int start, int end) {
    float width = 0.0F;
    int i = start;
    while (i < end) {
      int offset = spanned.nextSpanTransition(i, end, Object.class);
      MetricAffectingSpan spans[] = spanned.getSpans(i, offset, MetricAffectingSpan.class);
      workPaint.set(paint);
      ReplacementSpan replacementSpan = null;
      int len = spans.length;
      for (int j = 0; j < len; j++) {
        MetricAffectingSpan metricAffectingSpan = spans[j];
        metricAffectingSpan.updateMeasureState(workPaint);
        if (metricAffectingSpan instanceof ReplacementSpan) {
          replacementSpan = (ReplacementSpan) metricAffectingSpan;
        }
      }

      if (replacementSpan == null) {
        workPaint.setTextSize(paint.getTextSize());
        width += Layout.getDesiredWidth(spanned, i, offset, workPaint);
//         width += workPaint.measureText(spanned, i, offset);
      } else {
        width +=
            replacementSpan.getSize(workPaint, spanned, i, offset, workPaint.getFontMetricsInt());
      }
      i = offset;
    }
    return width;
  }

}
