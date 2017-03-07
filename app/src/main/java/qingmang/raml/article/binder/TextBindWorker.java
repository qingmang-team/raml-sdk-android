package qingmang.raml.article.binder;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import qingmang.raml.article.model.HElement;
import qingmang.raml.article.model.HText;
import qingmang.raml.article.model.TextlineType;
import qingmang.raml.article.ui.span.ILineDecorationSpan;
import qingmang.raml.article.ui.span.LineDecorationSpanShim;


/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class TextBindWorker extends ArticleBindWorker {

  private LineDecorationSpanShim span;
  private HElement model;

  @Override
  public void bind(View view, HElement model) {
    this.model = model;
    HText text = model.text;
    TextView textView = (TextView) view;

    applyTextStyle(textView, text);
    applySpannable(textView, text);
    bindText(textView, text);
  }

  @Override
  public void unbind(View view) {
    if (model != null) {
      removeSpan((SpannableString) model.text.spannableString);
      this.model = null;
    }
  }

  private void applyTextStyle(TextView textView, HText text) {
    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
    textView.setLineSpacing(text.lineSpacingAdd, 1);
    boolean center = "center".equals(text.align) || text.linetype == TextlineType.aside;
    textView.setGravity(center ? Gravity.CENTER : Gravity.LEFT);
    textView.setMovementMethod(LinkMovementMethod.getInstance());
  }

  private void applySpannable(TextView textView, HText text) {
    SpannableString spannable = (SpannableString) text.spannableString;
    removeSpan(spannable);
    ILineDecorationSpan[] spans =
        spannable.getSpans(0, spannable.length(), ILineDecorationSpan.class);
    if (spans != null && spans.length > 0) {
      span = new LineDecorationSpanShim(textView);
      spannable.setSpan(span, 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
  }

  private void bindText(TextView textView, HText text) {
    textView.setText(text.spannableString);
  }

  private void removeSpan(SpannableString spannable) {
    if (span != null) {
      spannable.removeSpan(span);
      span = null;
    }
  }

}
