package qingmang.raml;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class RamlApplication extends Application {

  private static RamlApplication instance;

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    Fresco.initialize(this);
  }

  public static RamlApplication getInstance() {
    return instance;
  }
}
