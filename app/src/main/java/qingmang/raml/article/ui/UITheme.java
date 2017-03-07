package qingmang.raml.article.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.text.TextPaint;

import qingmang.raml.R;
import qingmang.raml.RamlApplication;


/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class UITheme {

  public static final int EM_COLOR = resources().getColor(R.color.black);
  public static final int A_COLOR = resources().getColor(R.color.link_color);
  public static final int TEXT_COLOR = resources().getColor(R.color.text_body);

  public static final int LINE_HEIGHT_DP = 28;
  public static final float DENSITY = resources().getDisplayMetrics().density;
  public static final float FONT_NORMAL_SPACING = calculateSpacing(LINE_HEIGHT_DP, 16);
  public static final float UNDERLINE_MARGIN = DENSITY * 5;
  public static final int UNDERLINE_DASH_WIDTH = Math.round(DENSITY * 4);
  public static final int UNDERLINE_DASH_GAP = Math.round(DENSITY * 2);

  private static Context context() {
    return RamlApplication.getInstance();
  }

  private static Resources resources() {
    return context().getResources();
  }

  /**
   * @param lineHeight line height in dp
   * @param fontSize font size in dp
   * @return spacing / 2
   */
  private static float calculateSpacing(int lineHeight, int fontSize) {
    TextPaint paint = new TextPaint();
    paint.setTextSize(DENSITY * fontSize);
    Paint.FontMetrics fontMetrics = paint.getFontMetrics();
    float height = fontMetrics.descent - fontMetrics.ascent;
    return (DENSITY * lineHeight - height) / 2;
  }
}
