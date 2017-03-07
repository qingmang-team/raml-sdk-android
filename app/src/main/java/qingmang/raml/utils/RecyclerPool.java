package qingmang.raml.utils;


import java.util.LinkedList;
import java.util.Queue;

import android.support.annotation.MainThread;
import android.util.SparseArray;

/**
 * 用于循环利用的对象. 内部是基于 cacheKey 的队列，单进单出.
 *
 * 用于管理 View 和 CardPresenter 的缓存，减少 inflate,
 * 针对的是不同的 Section 之间，或者不同的页面之间 Section 中间 的子 item 不同时，对 Section 的 Item 进行复用。
 *
 * RecyclerPool 是针对但线程使用，不可以用于多线程中.
 *
 * @author huwei@wandoujia.com
 */
public final class RecyclerPool<T> {

  private static final int MAX_RECYCLER_SIZE = 10;
  private final SparseArray<Queue<T>> cachedArray;
  private final int maxRecyclerSize;

  /**
   * 设置缓存池中，每个 type 最多缓存的个数.
   *
   * @param maxRecyclerSize 最多的缓存个数
   */
  public RecyclerPool(int maxRecyclerSize) {
    this.maxRecyclerSize = maxRecyclerSize;
    this.cachedArray = new SparseArray<>();
  }

  /**
   * 默认构造函数.
   */
  public RecyclerPool() {
    this(MAX_RECYCLER_SIZE);
  }

  /**
   * 通过 cacheKey 获取缓存的对象, 如果没有缓存，则返回 null.
   *
   * @param cacheKey 缓存的 key
   *
   * @Nullable
   */
  @MainThread
  public T getCachedObject(int cacheKey) {
    Queue<T> cachedQueue = cachedArray.get(cacheKey);
    if (cachedQueue != null) {
      T object = cachedQueue.poll();
      if (cachedQueue.isEmpty()) {
        cachedArray.remove(cacheKey);
      }
      return object;
    }
    return null;
  }

  /**
   * 当前的 Object 被回收后，将对象缓存，用于下次使用的时候，进行复用.
   *
   *
   * @param cacheKey
   * @param object
   */
  @MainThread
  public void putRecycledObject(int cacheKey, T object) {
    Queue<T> objects = getCachedListByKey(cacheKey);

    if (objects.size() < maxRecyclerSize) {
      objects.add(object);
    }
  }

  /**
   * 清除所有的缓存对象.
   *
   * 程序可以在系统内存不足的时候，调用清除缓存对象
   */
  @MainThread
  public void clear() {
    cachedArray.clear();
  }

  /**
   * 清除当前 cacheKey 的对象
   *
   * @param cacheKey
   */
  @MainThread
  public void clear(int cacheKey) {
    cachedArray.remove(cacheKey);
  }

  private Queue<T> getCachedListByKey(int cacheKey) {
    Queue<T> cachedQueue = cachedArray.get(cacheKey);
    if (cachedQueue == null) {
      cachedQueue = new LinkedList<>();
      cachedArray.put(cacheKey, cachedQueue);
    }
    return cachedQueue;
  }
}

