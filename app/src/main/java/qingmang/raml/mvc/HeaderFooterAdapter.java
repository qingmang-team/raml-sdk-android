package qingmang.raml.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import qingmang.raml.mvc.binder.EmptyBinder;
import qingmang.raml.mvc.binder.GroupBinder;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public abstract class HeaderFooterAdapter<T> extends BaseAdapter<T> {

  /**
   * 定义了 Header 和 Footer 的 ViewType
   * 其中 Header 是从 65536 开始到 131071, 通过 当前位置加上 {@link #VIEW_TYPE_HEADER_FLAG} 的值，作为 ViewType
   * Footer 从 131072 开始到 , 通过 当前位置加上 {@link #VIEW_TYPE_FOOTER_FLAG} 的值，作为 ViewType
   */

  private static final int VIEW_TYPE_FILTER = 0xffff;
  private static final int FLAG_FILTER = 0x3 << 16;
  private static final int VIEW_TYPE_HEADER_FLAG = 0x2 << 16;
  private static final int VIEW_TYPE_FOOTER_FLAG = 0x1 << 16;
  /**
   * Data adapter do not offset with flag.
   */
  private static final int VIEW_TYPE_DATA_FLAG = 0x0;

  private ViewAdapter headerAdapter;
  private ViewAdapter footerAdapter;

  private interface PositionMapping {
    int mapPosition(int position);
  }

  private class HeaderFooterAdapterDataObserver extends RecyclerView.AdapterDataObserver {
    private PositionMapping positionMapping;

    public HeaderFooterAdapterDataObserver(PositionMapping positionMapping) {
      this.positionMapping = positionMapping;
    }

    @Override
    public void onChanged() {
      notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
      notifyItemRangeChanged(positionMapping.mapPosition(positionStart), itemCount);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
      notifyItemRangeInserted(positionMapping.mapPosition(positionStart), itemCount);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
      notifyItemRangeRemoved(positionMapping.mapPosition(positionStart), itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
      notifyItemMoved(positionMapping.mapPosition(fromPosition),
          positionMapping.mapPosition(toPosition));
    }
  }

  public HeaderFooterAdapter() {
    this.headerAdapter = new ViewAdapter();
    this.footerAdapter = new ViewAdapter();
    this.headerAdapter.registerAdapterDataObserver(
        new HeaderFooterAdapterDataObserver(
            new PositionMapping() {
              @Override
              public int mapPosition(int position) {
                return position;
              }
            }));
    this.footerAdapter.registerAdapterDataObserver(
        new HeaderFooterAdapterDataObserver(
            new PositionMapping() {
              @Override
              public int mapPosition(int position) {
                return position + getHeaderCount() + getDataCount();
              }
            }));
  }

  /**
   * 添加 Header.
   *
   * @param view
   */
  @UiThread
  public void addHeader(@NonNull View view) {
    addHeader(new EmptyViewData(view));
  }

  /**
   * 添加 Header.
   *
   * @param header
   */
  public void addHeader(ViewData header) {
    headerAdapter.addData(header);
  }

  public void clearHeader() {
    headerAdapter.clearData();
  }

  /**
   * 添加 Footer.
   *
   * @param view
   */
  @UiThread
  public void addFooter(@NonNull View view) {
    addFooter(new EmptyViewData(view));
  }

  /**
   * 添加 Footer.
   *
   * @param footer
   */
  @UiThread
  public void addFooter(ViewData footer) {
    footerAdapter.addData(footer);
  }

  @UiThread
  public void addFooter(int footerPosition, ViewData footer) {
    footerAdapter.addData(footerPosition, footer);
  }

  public int getHeaderCount() {
    return headerAdapter.getItemCount();
  }

  public int getFooterCount() {
    return footerAdapter.getItemCount();
  }

  public int getDataCount() {
    return data == null ? 0 : data.size();
  }

  public T getItemWithRecyclerViewPosition(int position) {
    return getItem(positionData(position));
  }

  public T getItem(int position) {
    return data != null && position < data.size() ? data.get(position) : null;
  }

  @Override
  public int getItemCount() {
    return getHeaderCount() + getFooterCount() + getDataCount();
  }

  @Override
  protected int mapPosition(int position) {
    return position + getHeaderCount();
  }

  @Override
  public void onViewAttachedToWindow(ViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    final int position = holder.getLayoutPosition();
    if (isHeader(position)) {
      headerAdapter.onViewAttachedToWindow(positionHeader(position));
    } else if (isFooter(position)) {
      footerAdapter.onViewAttachedToWindow(positionFooter(position));
    }
  }

  @Override
  public void onViewDetachedFromWindow(ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    final int position = holder.getLayoutPosition();
    if (isHeader(position)) {
      headerAdapter.onViewDetachedFromWindow(positionHeader(position));
    } else if (isFooter(position)) {
      footerAdapter.onViewDetachedFromWindow(positionFooter(position));
    }
  }

  @Override
  protected final GroupBinder onCreateViewBinder(ViewGroup parent, int viewType) {
    final int innerViewType = viewType & VIEW_TYPE_FILTER;
    final int flag = viewType & FLAG_FILTER;
    if (flag == VIEW_TYPE_HEADER_FLAG) {
      return headerAdapter.onCreateViewBinder(parent, innerViewType);
    } else if (flag == VIEW_TYPE_DATA_FLAG || flag == FLAG_FILTER) {
      return onCreateDataViewPresenter(parent, viewType);
    } else {
      return footerAdapter.onCreateViewBinder(parent, innerViewType);
    }
  }

  @Override
  public final void onBindViewHolder(ViewHolder holder, int position) {
    if (isHeader(position)) {
      headerAdapter.onBindViewHolder(holder, positionHeader(position));
    } else if (isData(position)) {
      onBindDataViewHolder(holder, positionData(position));
    } else {
      footerAdapter.onBindViewHolder(holder, positionFooter(position));
    }
  }

  @Override
  public final int getItemViewType(int position) {
    if (isHeader(position)) {
      return VIEW_TYPE_HEADER_FLAG | headerAdapter.getItemViewType(positionHeader(position));
    } else if (isData(position)) {
      int type = getDataItemViewType(positionData(position));
      if (type > VIEW_TYPE_FILTER) {
        throw new IllegalStateException("item view type can not exceed 0xffff");
      }
      return VIEW_TYPE_DATA_FLAG | type;
    } else {
      return VIEW_TYPE_FOOTER_FLAG | footerAdapter.getItemViewType(positionFooter(position));
    }
  }

  protected static boolean isDataViewType(int viewType) {
    return (viewType & FLAG_FILTER) == VIEW_TYPE_DATA_FLAG;
  }

  public static boolean isHeaderViewType(int viewType) {
    return (viewType & FLAG_FILTER) == VIEW_TYPE_HEADER_FLAG;
  }

  public static boolean isFooterViewType(int viewType) {
    return (viewType & FLAG_FILTER) == VIEW_TYPE_FOOTER_FLAG;
  }

  protected final boolean isHeader(int position) {
    final int finalPosition = positionHeader(position);
    return finalPosition >= 0 && finalPosition < getHeaderCount();
  }

  public final boolean isData(int position) {
    final int finalPosition = positionData(position);
    return finalPosition >= 0 && finalPosition < getDataCount();
  }

  protected final boolean isFooter(int position) {
    final int finalPosition = positionFooter(position);
    return finalPosition >= 0 && finalPosition < getFooterCount();
  }

  protected final int positionHeader(int position) {
    return position;
  }

  public final int positionData(int position) {
    return position - getHeaderCount();
  }

  public final int positionFooter(int position) {
    return position - getHeaderCount() - getDataCount();
  }

  private void onBindDataViewHolder(ViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
  }

  protected abstract GroupBinder<T> onCreateDataViewPresenter(ViewGroup parent, int viewType);

  protected abstract int getDataItemViewType(int position);

  private interface ViewDataObserver {
    void onDataChanged(ViewData viewData);

    void onVisibilityChanged(ViewData viewData);
  }

  /**
   * Data of Header and Footer view.
   */
  public abstract static class ViewData {

    private ViewDataObserver viewDataObserver;
    private boolean visible;
    private Object data;
    // position 目前是稳定的，用 position 做 viewType
    private int viewType;
    private boolean attached;
    private List<VisibilityChangedListener> visibilityChangedListeners = new ArrayList<>();

    public interface VisibilityChangedListener {
      void onVisibilityChanged(ViewData viewData);
    }

    public abstract GroupBinder<?> createGroupBinder(ViewGroup parent);

    public void onAttachedToWindow() {
      attached = true;
    }

    public void onDetachedFromWindow() {
      attached = false;
    }

    private void setViewDataObserver(ViewDataObserver viewDataObserver) {
      this.viewDataObserver = viewDataObserver;
    }

    public void addVisibilityChangedListener(VisibilityChangedListener listener) {
      if (!visibilityChangedListeners.contains(listener)) {
        visibilityChangedListeners.add(listener);
      }
    }

    public void removeVisibilityChangedListener(VisibilityChangedListener listener) {
      if (visibilityChangedListeners.contains(listener)) {
        visibilityChangedListeners.remove(listener);
      }
    }

    public ViewData() {
      this(false);
    }

    public ViewData(boolean visible) {
      this.visible = visible;
    }

    public void setData(Object data) {
      if (this.data != data) {
        this.data = data;
        if (viewDataObserver != null) {
          viewDataObserver.onDataChanged(this);
        }
      }
    }

    public void forceRefresh() {
      if (viewDataObserver != null) {
        viewDataObserver.onDataChanged(this);
      }
    }

    public void setVisible(boolean visible) {
      if (this.visible != visible) {
        this.visible = visible;
        if (viewDataObserver != null) {
          viewDataObserver.onVisibilityChanged(this);
        }
      }
    }

    public boolean isAttached() {
      return attached;
    }

    public boolean isVisible() {
      return visible;
    }
  }

  /**
   * Adapter for Header and Footer.
   */
  private static final class ViewAdapter extends BaseAdapter<ViewData>
      implements ViewDataObserver {

    private Map<Integer, ViewData> viewCreatorMap;
    private List<ViewData> allData;

    public ViewAdapter() {
      allData = new ArrayList<>();
      viewCreatorMap = new HashMap<>();
    }

    public void onViewAttachedToWindow(int position) {
      data.get(position).onAttachedToWindow();
    }

    public void onViewDetachedFromWindow(int position) {
      data.get(position).onDetachedFromWindow();
    }

    @Override
    public boolean setData(List<ViewData> data) {
      allData.clear();
      allData.addAll(data);
      viewCreatorMap.clear();
      List<ViewData> visibleData = new ArrayList<ViewData>();
      for (int i = 0; i < allData.size(); ++i) {
        ViewData view = allData.get(i);
        view.viewType = i;
        view.setViewDataObserver(this);
        if (view.visible) {
          visibleData.add(view);
        }
      }
      for (ViewData view : data) {
        viewCreatorMap.put(view.viewType, view);
      }
      return super.setData(visibleData);
    }

    public void addData(int position, ViewData view) {
      List<ViewData> newData = new ArrayList<>(allData);
      newData.add(position, view);
      setData(newData);
    }

    public void addData(ViewData view) {
      view.viewType = allData.size();
      viewCreatorMap.put(view.viewType, view);
      view.setViewDataObserver(this);
      allData.add(view);
      if (view.visible) {
        List<ViewData> visibleData = new ArrayList<>();
        visibleData.add(view);
        super.appendData(visibleData);
      }
    }

    public void clearData() {
      for (ViewData viewData : allData) {
        super.removeData(viewData);
      }
      allData.clear();
      viewCreatorMap.clear();
    }

    @Override
    protected GroupBinder<ViewData> onCreateViewBinder(ViewGroup parent, int viewType) {
      ViewData viewData = viewCreatorMap.get(viewType);
      GroupBinder binder = viewData.createGroupBinder(parent);
      return new ViewDataBinderWrapper(binder);
    }

    @Override
    public int getItemViewType(int position) {
      List<ViewData> data = getData();
      if (data != null && data.get(position) != null) {
        return data.get(position).viewType;
      } else {
        return 0;
      }
    }

    @Override
    public void onDataChanged(final ViewData viewData) {
      for (int i = 0; i < getItemCount(); ++i) {
        if (data.get(i).viewType == viewData.viewType) {
          notifyItemChanged(i);
        }
      }
    }

    @Override
    public void onVisibilityChanged(final ViewData viewData) {
      if (viewData.visible) {
        int index = 0;
        for (int i = 0; i < allData.size(); ++i) {
          if (allData.get(i).viewType == viewData.viewType) {
            break;
          }
          if (allData.get(i).visible) {
            index++;
          }
        }
        insertData(index, viewData);
      } else {
        removeData(viewData);
      }
      for (ViewData.VisibilityChangedListener listener : viewData.visibilityChangedListeners) {
        listener.onVisibilityChanged(viewData);
      }
    }

  }

  private static class ViewDataBinderWrapper extends GroupBinder<ViewData> {
    private final GroupBinder<Object> realBinder;

    public ViewDataBinderWrapper(GroupBinder<Object> realBinder) {
      super(realBinder.view());
      this.realBinder = realBinder;
    }

    @Override
    public void bind(View view, ViewData model) {
      realBinder.bind(view, model.data);
    }

    @Override
    public void unbind(View view) {
      realBinder.unbind(view);
    }
  }

  private class EmptyViewData extends ViewData {

    private View view;

    public EmptyViewData(View view) {
      this(view, true);
    }

    public EmptyViewData(View view, boolean visible) {
      super(visible);
      this.view = view;
    }

    @Override
    public GroupBinder createGroupBinder(ViewGroup parent) {
      return new EmptyBinder(view);
    }
  }
}
