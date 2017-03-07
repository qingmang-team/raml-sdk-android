package qingmang.raml.article.binder;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import qingmang.raml.R;
import qingmang.raml.article.model.HElement;
import qingmang.raml.article.model.HText;
import qingmang.raml.mvc.worker.ContainerBindWorker;
import qingmang.raml.utils.RecyclerPool;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class TableBindWorker2 extends ContainerBindWorker<HElement> {

  public TableBindWorker2() {
    super(R.layout.article_table_item, new RecyclerPool<View>());
  }

  @Override
  public void bind(View view, HElement model) {
      TableLayout tableLayout = (TableLayout) view;
      tableLayout.setShrinkAllColumns(true);
      tableLayout.setStretchAllColumns(true);
      super.bind(view, model);
  }

  @Override
  protected int getItemCount(HElement model) {
    return model.table.rowCount;
  }

  @Override
  protected View onCreateItemView(ViewGroup parent) {
    TableRow row = new TableRow(parent.getContext());
    row.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
    return row;
  }

  @Override
  protected void onBindItemView(View itemView, int i, HElement model) {
    new RowBindWorker(recycler).bind(itemView, model.table.items.get(i));

  }

  private static class RowBindWorker extends ContainerBindWorker<List<HText>> {

    public RowBindWorker(RecyclerPool<View> recycler) {
      super(R.layout.article_table_element, recycler);
    }

    @Override
    protected int getItemCount(List<HText> model) {
      return model.size();
    }

    @Override
    protected View onCreateItemView(ViewGroup parent) {
      return LayoutInflater.from(parent.getContext()).inflate(R.layout.article_table_element, parent, false);
    }

    @Override
    protected void onBindItemView(View itemView, int i, List<HText> model) {
      TextView textView = (TextView) itemView;
      textView.setText(model.get(i).spannableString);
    }
  }

}
