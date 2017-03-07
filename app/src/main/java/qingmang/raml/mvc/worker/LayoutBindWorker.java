package qingmang.raml.mvc.worker;

import android.view.View;

import qingmang.raml.mvc.ViewHolder;
import qingmang.raml.mvc.binder.IBinder;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public abstract class LayoutBindWorker<T> implements IBinder<T> {

  protected ViewHolder holder;

  public void attachViewHolder(ViewHolder holder) {
    this.holder = holder;
  }

  protected final void show(int id) {
    View view = holder.getView(id);
    if (view != null) {
      view.setVisibility(View.VISIBLE);
    }
  }

  protected final void hide(int id) {
    View view = holder.getView(id);
    if (view != null) {
      view.setVisibility(View.GONE);
    }
  }

  protected final void invisible(int id) {
    View view = holder.getView(id);
    if (view != null) {
      view.setVisibility(View.INVISIBLE);
    }
  }

  protected final <T extends View> T view(int id) {
    return (T) holder.getView(id);
  }

  protected final void bindClick(int id, View.OnClickListener listener) {
    View view = holder.getView(id);
    if (view == null) {
      return;
    }
    view.setOnClickListener(listener);
  }

}
