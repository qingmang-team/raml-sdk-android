package qingmang.raml.article.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;


import qingmang.raml.RamlApplication;
import qingmang.raml.article.ui.UITheme;
import qingmang.raml.article.ui.span.DashUnderlineSpan;
import qingmang.raml.article.ui.span.LineBumpSpan;
import qingmang.raml.article.ui.span.SubscriptSpan;
import qingmang.raml.ui.view.WebImageDrawable;
import qingmang.raml.ui.view.WebImageSpan;
import qingmang.raml.utils.HClickableSpan;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class HText implements ITags {

  public String text;
  public List<Markup> markups;
  public TextlineType linetype;
  public String html;
  public String align;

  // Local
  public CharSequence spannableString;
  public float lineSpacingAdd;

  public void buildSpannable() {
    spannableString = buildSpannableString();
  }

  private CharSequence buildSpannableString() {
    final int aColor = UITheme.A_COLOR;
    final int emColor = UITheme.EM_COLOR;
    SpannableString string = new SpannableString(text);
    List<Object> textlineSpans = new ArrayList<>();
    lineSpacingAdd = UITheme.FONT_NORMAL_SPACING;
    textlineSpans.add(new LineBumpSpan(lineSpacingAdd));
    if (linetype != null) {
      switch (linetype) {
        case h1:
          // TODO line spacing..
          textlineSpans.add(new RelativeSizeSpan(1.4f));
          textlineSpans.add(new ForegroundColorSpan(emColor));
          break;
        case h2:
          textlineSpans.add(new RelativeSizeSpan(1.3f));
          textlineSpans.add(new ForegroundColorSpan(emColor));
          break;
        case h3:
          textlineSpans.add(new RelativeSizeSpan(1.2f));
          textlineSpans.add(new ForegroundColorSpan(emColor));
          break;
        case small:
          textlineSpans.add(new RelativeSizeSpan(0.8f));
          break;
        case big:
          textlineSpans.add(new RelativeSizeSpan(1.1f));
          break;
        default:
          break;
      }
    }

    int start = 0;
    int end = text.length();
    for (Object span : textlineSpans) {
      string.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    if (markups != null) {
      for (final Markup markup : markups) {
        start = Math.max(markup.start, 0);
        end = Math.min(markup.end, text.length());
        List<Object> spans = new ArrayList<>();
        if (markup.tag == null) {
          return string;
        }

        switch (markup.tag) {
          case a: {
            spans.add(new HClickableSpan(aColor) {
              @Override
              public void onClick(View widget) {
                if (!TextUtils.isEmpty(markup.source)) {
                  Intent intent = new Intent(Intent.ACTION_VIEW);
                  intent.setData(Uri.parse(markup.source));
                  widget.getContext().startActivity(intent);
                }
              }
            });
            spans.add(new DashUnderlineSpan(UITheme.TEXT_COLOR, UITheme.UNDERLINE_DASH_WIDTH,
                UITheme.UNDERLINE_DASH_GAP));
            break;
          }
          case em:
            spans.add(new ForegroundColorSpan(emColor));
            break;
          case strong:
            spans.add(new StyleSpan(Typeface.BOLD));
            spans.add(new ForegroundColorSpan(emColor));
            break;
          case u:
          case i:
          case b:
            break;
          case img:
            String src = markup.source;
            float d = RamlApplication.getInstance().getResources().getDisplayMetrics().density;
            WebImageDrawable imageDrawable = new WebImageDrawable(Resources.getSystem(), src);
            imageDrawable.setBounds(0, 0, (int) (d * markup.width), (int) (d * markup.height));
            imageDrawable.load();
            imageDrawable.getIntrinsicHeight();
            spans.add(new WebImageSpan(imageDrawable, src, WebImageSpan.ALIGN_CENTER));
            break;
          case sub:
            spans.add(new SubscriptSpan());
            spans.add(new RelativeSizeSpan(0.6f));
            break;
          case sup:
            spans.add(new SuperscriptSpan());
            spans.add(new RelativeSizeSpan(0.6f));
            break;
          default:
            break;
        }
        for (Object span : spans) {
          string.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
      }
    }

    return string;
  }
}
