package qingmang.raml.article.fragment;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import qingmang.raml.R;
import qingmang.raml.article.binder.TableBindWorker2;
import qingmang.raml.article.model.HElement;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class TableFragment extends Fragment {

  public static TableFragment newInstance(HElement element) {
    TableFragment fragment = new TableFragment();
    Bundle bundle = new Bundle();
    bundle.putSerializable(EXTRA_ELEMENT, element);
    fragment.setArguments(bundle);
    return fragment;
  }

  public static TableFragment newInstance() {
    TableFragment fragment = new TableFragment();
    return fragment;
  }

  private static final String EXTRA_ELEMENT = "element";

  private TableLayout tableLayout;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    tableLayout = (TableLayout) inflater.inflate(R.layout.fragment_table, container, false);
    return tableLayout;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    HElement element = (HElement) getArguments().getSerializable(EXTRA_ELEMENT);
    if (element != null && element.table != null) {
      new TableBindWorker2().bind(tableLayout, element);
    }
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
  }

  @Override
  public void onDetach() {
    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    super.onDetach();
  }
}
