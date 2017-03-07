package qingmang.raml.activity;

import qingmang.raml.article.fragment.ArticleFragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */
public class ArticleActivity extends FragmentActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ArticleFragment fragment = ArticleFragment.newInstance(
        (Uri) getIntent().getParcelableExtra(ArticleFragment.EXTRA_URI));
    getSupportFragmentManager().beginTransaction()
        .replace(android.R.id.content, fragment)
        .commitAllowingStateLoss();
  }
}
