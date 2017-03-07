package qingmang.raml.mvc.binder;

import android.view.View;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class EmptyBinder<T> extends GroupBinder<T> {

  public EmptyBinder(View view) {
    super(view);
  }

  @Override
  public void bind(View view, T model) {}

  @Override
  public void unbind(View view) {}
}
