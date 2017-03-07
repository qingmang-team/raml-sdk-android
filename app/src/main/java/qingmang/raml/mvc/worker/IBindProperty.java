package qingmang.raml.mvc.worker;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public interface IBindProperty<T, K> {
  K getProperty(T t);
}
