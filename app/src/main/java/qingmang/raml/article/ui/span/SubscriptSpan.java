package qingmang.raml.article.ui.span;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class SubscriptSpan extends MetricAffectingSpan {
  @Override
  public void updateDrawState(TextPaint tp) {
    tp.baselineShift -= (int) (tp.ascent() / 2 + 6);
  }

  @Override
  public void updateMeasureState(TextPaint tp) {
    tp.baselineShift -= (int) (tp.ascent() / 2 + 6);
  }
}
