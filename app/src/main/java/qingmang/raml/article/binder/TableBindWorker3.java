package qingmang.raml.article.binder;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import qingmang.raml.R;
import qingmang.raml.article.model.HElement;
import qingmang.raml.article.model.HText;
import qingmang.raml.mvc.BaseAdapter;
import qingmang.raml.mvc.binder.GroupBinder;
import qingmang.raml.mvc.binder.IBinder;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class TableBindWorker3 extends ArticleBindWorker {

  private BaseAdapter<HText> adapter;

  public TableBindWorker3() {
    adapter = new BaseAdapter<HText>() {
      @Override
      protected GroupBinder onCreateViewBinder(ViewGroup parent, int viewType) {
        return new GroupBinder(parent, R.layout.article_table_element)
            .addBinder(0, new IBinder<HText>() {
              @Override
              public void bind(View view, HText model) {
                ((TextView) view).setText(model.spannableString);
              }

              @Override
              public void unbind(View view) {}
            });
      }
    };
  }

  @Override
  public void bind(View view, HElement model) {
    RecyclerView recyclerView = (RecyclerView) view;
    recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), model.table.colCount));
    recyclerView.setAdapter(adapter);
    List<HText> items = new ArrayList<>();
    for (List<HText> row : model.table.items) {
      items.addAll(row);
    }
    adapter.setData(items);
  }

  @Override
  public void unbind(View view) {

  }
}
