package qingmang.raml.article.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import qingmang.raml.R;
import qingmang.raml.article.model.HElement;
import qingmang.raml.article.model.Li;
import qingmang.raml.mvc.HeaderFooterAdapter;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class ArticleItemDecoration extends RecyclerView.ItemDecoration {

  private int vertMargin;
  private int horiMargin;
  private int tabSize;
  private int liHoriMargin;
  private int liVertMargin;

  public ArticleItemDecoration(Context context) {
    Resources res = context.getResources();
    vertMargin = res.getDimensionPixelSize(R.dimen.article_item_vert_margin);
    horiMargin = res.getDimensionPixelSize(R.dimen.article_item_hori_margin);
    tabSize = res.getDimensionPixelSize(R.dimen.article_item_tab_size);
    liVertMargin = res.getDimensionPixelSize(R.dimen.article_item_li_vert_margin);
    liHoriMargin = res.getDimensionPixelSize(R.dimen.article_item_li_hori_margin);
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
      state) {
    int position = parent.getChildAdapterPosition(view);
    if (position < 0 || position > parent.getAdapter().getItemCount()) {
      return;
    }
    int viewType = parent.getAdapter().getItemViewType(position);
    int top = position == 0 ? vertMargin : 0;
    switch (viewType) {
      case HElement.TYPE_BREAK:
      case HElement.TYPE_SEPARATOR:
        outRect.set(0, top, 0, 0);
        break;
      case HElement.TYPE_TEXT:
        HeaderFooterAdapter<HElement> adapter = (HeaderFooterAdapter<HElement>) parent.getAdapter();
        int left = horiMargin;
        int bottom = vertMargin;
        if (adapter != null && adapter.getData() != null) {
          HElement current = adapter.getItemWithRecyclerViewPosition(position);
          if (current == null || current.li == null) {
            outRect.set(left, top, horiMargin, bottom);
            break;
          }
          int tabCount = current.li.level > 1
              ? current.li.level - 1
              : 0;
          left += tabCount * tabSize;
          HElement next = adapter.getItemWithRecyclerViewPosition(position + 1);
          // 这样判断是有问题的，嗯，先取巧一下吧
          if (next == null || next.li == null) {
            // do nothing
          } else if (current.li.level < next.li.level
            || current.li.level == next.li.level && current.li.order < next.li.order) {
            bottom = liVertMargin;
          } else if (next.li.type == Li.Type.tab) {
            bottom = liVertMargin;
          }
        }
        outRect.set(left, top, horiMargin, bottom);
        break;
      case HElement.TYPE_TABLE:
        outRect.set(horiMargin, top, horiMargin, vertMargin);
        break;
      case HElement.TYPE_IMAGE:
      case HElement.TYPE_VIDEO:
      case HElement.TYPE_AUDIO:
        outRect.set(0, top, 0, vertMargin);
        break;
      default:
        outRect.set(0, top, 0, 0);
        break;
    }
  }
}
