package qingmang.raml.article.binder;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import qingmang.raml.article.fragment.TableFragment;
import qingmang.raml.article.model.HElement;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class TableLinkBindWorker extends ArticleBindWorker {
  @Override
  public void bind(View view, final HElement model) {
    if (model.table.showLink()) {
      view.setVisibility(View.VISIBLE);
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          TableFragment fragment = TableFragment.newInstance(model);
          FragmentActivity activity = (FragmentActivity) v.getContext();
          activity.getSupportFragmentManager().beginTransaction()
              .add(android.R.id.content, fragment)
              .commitAllowingStateLoss();
        }
      });
    } else {
      view.setVisibility(View.GONE);
    }
  }

  @Override
  public void unbind(View view) {

  }
}
