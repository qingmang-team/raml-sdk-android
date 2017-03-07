package qingmang.raml.mvc.binder;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import qingmang.raml.mvc.ViewHolder;
import qingmang.raml.mvc.worker.BindWorkerConnector;
import qingmang.raml.mvc.worker.IBindProperty;
import qingmang.raml.mvc.worker.LayoutBindWorker;
import qingmang.raml.mvc.worker.PropertyBindWorker;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class GroupBinder<T> implements IBinder<T> {
  private final ViewHolder holder;
  private final SparseArray<IBinder> binders;
  private T model;

  public GroupBinder(ViewGroup parent, int layoutResId) {
    this(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false));
  }

  public GroupBinder(View view) {
    super();
    holder = new ViewHolder(view);
    binders = new SparseArray<>();
  }

  public <K> GroupBinder<T> addBinder(int viewId, IBinder<K> worker) {
    attachViewHolderIfNeeded(worker);
    if (holder.getView(viewId) != null) {
      final IBinder oldBinder = binders.get(viewId);
      if (oldBinder != null) {
        binders.put(viewId, BindWorkerConnector.make(oldBinder, worker));
      } else {
        binders.put(viewId, worker);
      }
    }
    return this;
  }


  public <K> GroupBinder replaceBinder(int viewId, IBinder worker) {
    attachViewHolderIfNeeded(worker);
    binders.remove(viewId);
    addBinder(viewId, worker);
    return this;
  }

  private void attachViewHolderIfNeeded(IBinder binder) {
    if (binder instanceof LayoutBindWorker) {
      ((LayoutBindWorker) binder).attachViewHolder(holder);
    }
  }

  public void bind(T model) {
    bind(view(), model);
  }

  public void unbind() {
    unbind(view());
  }

  @Override
  public void bind(View view, T model) {
    this.model = model;
    for (int i = 0; i < binders.size(); ++i) {
      int viewId = binders.keyAt(i);
      IBinder worker = binders.get(viewId);
      View childView = holder.getView(viewId);
      if (childView != null) {
        worker.bind(childView, model);
      }
    }
  }

  @Override
  public void unbind(View view) {
    this.model = null;
    for (int i = 0; i < binders.size(); ++i) {
      int viewId = binders.keyAt(i);
      IBinder binder = binders.get(viewId);
      View childView = holder.getView(viewId);
      if (childView != null) {
        binder.unbind(childView);
      }
    }
  }

  protected final Context context() {
    View view = view();
    return view != null ? view.getContext() : null;
  }

  public final View view() {
    return holder.getRoot();
  }
}
