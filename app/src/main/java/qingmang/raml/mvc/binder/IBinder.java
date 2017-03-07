package qingmang.raml.mvc.binder;

import android.view.View;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public interface IBinder<T> {
  void bind(View view, T model);
  void unbind(View view);
}
