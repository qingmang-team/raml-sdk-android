package qingmang.raml.mvc.worker;

import android.view.View;

import qingmang.raml.mvc.binder.IBinder;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class PropertyBindWorker<T, K> implements IBinder<T> {
  IBinder<K> binder;
  IBindProperty<T, K> property;

  public PropertyBindWorker(IBinder<K> binder, IBindProperty<T, K> property) {
    super();
    this.binder = binder;
    this.property = property;
  }

  @Override
  public void bind(View view, T model) {
    binder.bind(view, property.getProperty(model));
  }

  @Override
  public void unbind(View view) {
    binder.unbind(view);
  }
}
