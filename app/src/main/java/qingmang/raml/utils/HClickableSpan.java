package qingmang.raml.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public abstract class HClickableSpan extends ClickableSpan {

  int color;
  public HClickableSpan(int color) {
    this.color = color;
  }
  /**
   * Performs the click action associated with this span.
   */
  public abstract void onClick(View widget);

  /**
   * Makes the text underlined and in the link color.
   */
  @Override
  public void updateDrawState(TextPaint ds) {
//    ds.setColor(color);
  }
}
