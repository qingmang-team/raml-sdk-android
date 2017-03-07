package qingmang.raml.mvc;

import java.util.HashMap;
import java.util.Map;

import android.view.View;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class ViewHolder {

  private final View root;
  private final Map<Integer, View> cache;

  public ViewHolder(View root) {
    this.cache = new HashMap<>();
    this.root = root;
  }

  public View getView(int id) {
    if (id == 0) {
      return root;
    }
    if (cache.containsKey(id)) {
      return cache.get(id);
    }
    View view = root.findViewById(id);
    cache.put(id, view);
    return view;
  }

  public View getRoot() {
    return root;
  }
}
