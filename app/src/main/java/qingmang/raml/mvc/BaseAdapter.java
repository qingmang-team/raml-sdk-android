package qingmang.raml.mvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import qingmang.raml.mvc.binder.GroupBinder;
import qingmang.raml.utils.CollectionUtils;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

  protected final Set<ViewHolder> viewHolders;
  protected List<T> data;

  public BaseAdapter() {
    viewHolders = new HashSet<>();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new BaseAdapter.ViewHolder(onCreateViewBinder(parent, viewType));
  }

  @Override
  public void onViewDetachedFromWindow(ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
  }

  @Override
  public void onViewAttachedToWindow(ViewHolder holder) {
    super.onViewAttachedToWindow(holder);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    viewHolders.add(holder);

    T model = data.get(position);
    holder.binder.bind(model);
  }

  @Override
  public int getItemCount() {
    return data == null ? 0 : data.size();
  }

  /**
   * 回收所有的 View, 在销毁界面时调用这个方法.
   */
  public void recycleAllViews() {
    Log.d("SubList", "Destroy view holders " + this + ", size is " + viewHolders.size());
    for (ViewHolder holder : viewHolders) {
      onViewRecycled(holder);
    }
    viewHolders.clear();
  }

  @Override
  public void onViewRecycled(ViewHolder holder) {
    super.onViewRecycled(holder);
    if (holder != null && holder.binder != null) {
      holder.binder.unbind();
    }
  }

  public boolean setData(List<T> newData) {
    // make copy
    if (newData == null) {
      this.data = new ArrayList<>();
    } else {
      this.data = new ArrayList(newData);
    }
    notifyDataSetChanged();
    return true;
  }

  public boolean appendData(List<T> newData) {
    return insertData(data == null ? 0 : data.size(), newData);
  }

  public final boolean insertData(int position, T item) {
    if (item == null) {
      return false;
    }
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    if (position >= 0 && position <= this.data.size()) {
      this.data.add(position, item);
      notifyItemInserted(mapPosition(position));
    }
    return false;
  }

  public final boolean insertData(int position, List<T> newData) {
    /**
     * https://code.google.com/p/android/issues/detail?id=77846
     * notifyItemInserted() without actual inserting items may crashes RecyclerView.
     */
    if (CollectionUtils.isEmpty(newData)) {
      return false;
    }
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    if (position >= 0 && position <= this.data.size()) {
      this.data.addAll(position, newData);
      notifyItemRangeInserted(mapPosition(position), newData.size());
      return true;
    }
    return false;
  }

  public final boolean removeData(T item) {
    if (this.data != null) {
      int position = this.data.indexOf(item);
      if (position >= 0 && position < this.data.size()) {
        this.data.remove(position);
        notifyItemRemoved(mapPosition(position));
        return true;
      }
    }
    return false;
  }

  public final boolean removeRangeData(int position, int count) {
    if (this.data != null) {
      if (position >= 0 && position + count <= this.data.size()) {
        List<T> before = this.data.subList(0, position);
        List<T> after = this.data.subList(position + count, this.data.size());
        this.data = new ArrayList<>(before);
        this.data.addAll(after);

        notifyItemRangeRemoved(position, count);
        return true;
      }
    }
    return false;
  }

  public final boolean updateData(int position, T item) {
    if (this.data != null) {
      if (position >= 0 && position < this.data.size()) {
        if (this.data.get(position) != item) {
          this.data.set(position, item);
        }
        notifyItemChanged(mapPosition(position));
        return true;
      }
    }
    return false;
  }

  public final boolean updateDataRange(int position, List<T> newData) {
    if (this.data != null) {
      if (position >= 0 && position + newData.size() < this.data.size()) {
        for (int i = 0; i < newData.size(); i++) {
          if (this.data.get(position + i) != newData.get(i)) {
            this.data.set(position + i, newData.get(i));
          }
        }
        notifyItemRangeChanged(mapPosition(position), newData.size());
        return true;
      }
    }
    return false;
  }

  protected int mapPosition(int position) {
    return position;
  }

  public List<T> getData() {
    return data;
  }

  /**
   * 创建ItemView对应的ViewGroupPresenter。
   * 需要铭记的是，ViewGroupPresenter是于这个ItemView完全对应的，不会被用在其它View上。
   *
   * @param viewType itemView 对应的 viewType
   * @return ViewGroupPresenter。
   */
  protected abstract GroupBinder onCreateViewBinder(ViewGroup parent, int viewType);

  /**
   * itemView 的 viewHolder，可以通过这个 viewHolder 获得 presenter 和 view
   */
  public static class ViewHolder extends RecyclerView.ViewHolder {
    public final GroupBinder binder;

    public ViewHolder(GroupBinder binder) {
      super(binder.view());
      this.binder = binder;
    }
  }

}
