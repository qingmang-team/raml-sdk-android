package qingmang.raml.article.binder;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import qingmang.raml.R;
import qingmang.raml.article.model.HElement;
import qingmang.raml.article.model.HTable;
import qingmang.raml.article.model.HText;

/**
 * TODO 换个能复用的控件？
 * 
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class TableBindWorker extends ArticleBindWorker {

  @Override
  public void bind(View view, HElement model) {
    HTable table = model.table;
    final Context context = view.getContext();
    TableLayout tableLayout = (TableLayout) view;
    tableLayout.removeAllViews();
    tableLayout.setShrinkAllColumns(true);
    tableLayout.setStretchAllColumns(true);
    for (List<HText> rowItems : table.items) {
      TableRow row = new TableRow(context);
      for (HText text : rowItems) {
        TextView textView =
            (TextView) LayoutInflater.from(context).inflate(R.layout.article_table_element, row,
                false);
        textView.setText(text.spannableString);
        row.addView(textView, new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
      }
      tableLayout.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));
    }
  }

  @Override
  public void unbind(View view) {

  }
}
