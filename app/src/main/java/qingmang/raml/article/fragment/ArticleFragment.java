package qingmang.raml.article.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import qingmang.raml.R;
import qingmang.raml.article.binder.ArticleGroupBinder;
import qingmang.raml.article.binder.AudioBindWorker;
import qingmang.raml.article.binder.DecorationBindWorker;
import qingmang.raml.article.binder.ImageBindWorker;
import qingmang.raml.article.binder.TableBindWorker2;
import qingmang.raml.article.binder.TextBindWorker;
import qingmang.raml.article.binder.VideoBindWorker;
import qingmang.raml.article.model.HElement;
import qingmang.raml.article.model.ITags;
import qingmang.raml.article.ui.ArticleItemDecoration;
import qingmang.raml.mvc.HeaderFooterAdapter;
import qingmang.raml.mvc.binder.GroupBinder;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class ArticleFragment extends Fragment implements ITags {

  public static final String EXTRA_URI = "uri";
  private RecyclerView recyclerView;
  private ArticleAdapter adapter;

  private Uri uri;

  public static ArticleFragment newInstance(Uri uri) {
    ArticleFragment fragment = new ArticleFragment();
    Bundle arguments = new Bundle();
    if (uri != null) {
      arguments.putParcelable(EXTRA_URI, uri);
    }
    fragment.setArguments(arguments);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.uri = getArguments().getParcelable(EXTRA_URI);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_article, container, false);
    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.addItemDecoration(new ArticleItemDecoration(getActivity()));
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    adapter = new ArticleAdapter();
    recyclerView.setAdapter(adapter);
    new AsyncTask<String, Void, List<HElement>>() {
      @Override
      protected List<HElement> doInBackground(String... params) {
        String json = debugRaml();
        if (json == null) {
          return new ArrayList<>();
        }
        List<HElement> elements =
            new Gson().fromJson(json, new TypeToken<List<HElement>>() {}.getType());
        List<HElement> items = new ArrayList<>();
        for (HElement element : elements) {
          if (element != null && element.prepareAndCheckValid()) {
            items.add(element);
          }
        }
        return items;
      }

      @Override
      protected void onPostExecute(List<HElement> items) {
        super.onPostExecute(items);
        adapter.setData(items);
        adapter.notifyDataSetChanged();
      }
    }.execute("");
  }

  private class ArticleAdapter extends HeaderFooterAdapter<HElement> {

    public ArticleAdapter() {}

    @Override
    protected GroupBinder<HElement> onCreateDataViewPresenter(ViewGroup parent, int viewType) {
      switch (viewType) {
        case HElement.TYPE_TEXT: {
          return new ArticleGroupBinder(parent, R.layout.article_text_item)
              .addBinder(0, new DecorationBindWorker())
              .addBinder(R.id.text, new TextBindWorker());
        }
        case HElement.TYPE_IMAGE: {
          return new ArticleGroupBinder(parent, R.layout.article_image_item)
              .addBinder(0, new ImageBindWorker());
        }
        case HElement.TYPE_VIDEO: {
          return new ArticleGroupBinder(parent, R.layout.article_video_item)
              .addBinder(0, new VideoBindWorker());
        }
        case HElement.TYPE_AUDIO: {
          return new ArticleGroupBinder(parent, R.layout.article_audio_item)
              .addBinder(0, new AudioBindWorker());
        }
        case HElement.TYPE_TABLE: {
          return new ArticleGroupBinder(parent, R.layout.article_table_item)
              .addBinder(R.id.table, new TableBindWorker2());
        }
        case HElement.TYPE_BREAK: {
          return new ArticleGroupBinder(parent, R.layout.article_break_item);
        }
        case HElement.TYPE_SEPARATOR: {
          return new ArticleGroupBinder(parent, R.layout.article_seperator_item);
        }
        default: {
          // never run into this case.
          return null;
        }
      }
    }

    @Override
    protected int getDataItemViewType(int position) {
      return getData().get(position).type;
    }
  }

  private String debugRaml() {
    final Context context = getActivity();
    InputStream inputStream = null;
    if (uri != null) {
      try {
        inputStream = context.getContentResolver().openInputStream(uri);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      inputStream = context.getResources().openRawResource(R.raw.raml_sample);
    }
    if (inputStream == null) {
      Log.e("RAML", "stream is null, uri is " + uri);
      return null;
    }
    InputStreamReader inputReader = new InputStreamReader(inputStream);
    BufferedReader bufferedReader = new BufferedReader(inputReader);
    String line;
    StringBuilder text = new StringBuilder();

    try {
      while ((line = bufferedReader.readLine()) != null) {
        text.append(line);
        text.append('\n');
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return text.toString();
  }
}
