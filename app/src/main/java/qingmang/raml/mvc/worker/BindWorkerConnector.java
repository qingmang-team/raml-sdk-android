package qingmang.raml.mvc.worker;

import android.view.View;

import qingmang.raml.mvc.binder.IBinder;

/**
 * Created by yingyixu on 3/10/16.
 */
public class BindWorkerConnector<T> implements qingmang.raml.mvc.binder.IBinder<T> {

  private final IBinder worker1;
  private final IBinder worker2;

  private BindWorkerConnector(IBinder worker1, IBinder worker2) {
    this.worker1 = worker1;
    this.worker2 = worker2;
  }

  @Override
  public void bind(View view, T model) {
    worker1.bind(view, model);
    worker2.bind(view, model);
  }

  @Override
  public void unbind(View view) {
    worker2.unbind(view);
    worker1.unbind(view);
  }

  public static IBinder make(IBinder... workers) {
    if (workers == null) {
      return null;
    }
    if (workers.length == 1) {
      return workers[0];
    }
    IBinder worker = new BindWorkerConnector(workers[0], workers[1]);
    for (int i = 2; i < workers.length; i++) {
      worker = new BindWorkerConnector(worker, workers[i]);
    }
    return worker;
  }
}
