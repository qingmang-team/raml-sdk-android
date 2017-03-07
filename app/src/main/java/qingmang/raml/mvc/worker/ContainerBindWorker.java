package qingmang.raml.mvc.worker;

import android.view.View;
import android.view.ViewGroup;


import qingmang.raml.mvc.binder.IBinder;
import qingmang.raml.utils.RecyclerPool;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public abstract class ContainerBindWorker<T> implements IBinder<T> {
  protected final RecyclerPool<View> recycler;
  private final int recyclerKey;

  public ContainerBindWorker(int recyclerKey, RecyclerPool<View> recycler) {
    this.recyclerKey = recyclerKey;
    this.recycler = recycler;
  }

  @Override
  public void bind(View view, T model) {
    ViewGroup container = (ViewGroup) view;
    int itemCount = getItemCount(model);
    int i;
    for (i = 0; i < itemCount; ++i) {
      View itemView;
      if (i < container.getChildCount()) {
        itemView = container.getChildAt(i);
      } else {
        itemView = createItemView(container);
        container.addView(itemView);
      }
      onBindItemView(itemView, i, model);
    }
    while (i < container.getChildCount()) {
      View reusedView = container.getChildAt(i);
      container.removeView(reusedView);
      recycler.putRecycledObject(recyclerKey, reusedView);
    }

  }

  @Override
  public void unbind(View view) {

  }

  private View createItemView(ViewGroup parent) {
    View itemView;
    itemView = recycler.getCachedObject(recyclerKey);
    if (itemView == null) {
      itemView = onCreateItemView(parent);
    }

    return itemView;
  }


  protected abstract int getItemCount(T model);

  protected abstract View onCreateItemView(ViewGroup parent);

  protected abstract void onBindItemView(View itemView, int i, T model);
}
