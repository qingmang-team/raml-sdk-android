package qingmang.raml.article.ui.span;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class LineBumpSpan extends MetricAffectingSpan {
  private final float amount;

  public LineBumpSpan(float i) {
    amount = i;
  }

  public void updateDrawState(TextPaint textpaint) {
    textpaint.baselineShift += amount;
  }

  public void updateMeasureState(TextPaint textpaint) {
    textpaint.baselineShift += amount;
  }

}
