package qingmang.raml.activity;

import qingmang.raml.R;
import qingmang.raml.article.fragment.ArticleFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

/**
 * @author yangfan@wandoujia.com (Yang Fan)
 */

public class MenuActivity extends Activity {

  private static final int CODE_FILE_SELECT = 0;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);
    View select = findViewById(R.id.select);
    View sample = findViewById(R.id.sample);
    select.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Context context = MenuActivity.this;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
          startActivityForResult(
              Intent.createChooser(intent, "选择 RAML 文件"), CODE_FILE_SELECT);
        } catch (android.content.ActivityNotFoundException ex) {
          // Potentially direct the user to the Market with a Dialog
          Toast.makeText(context, "未安装 FileManager", Toast.LENGTH_SHORT).show();
        }
      }
    });
    sample.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Context context = MenuActivity.this;
        Intent intent = new Intent(context, ArticleActivity.class);
        context.startActivity(intent);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case CODE_FILE_SELECT:
          Uri uri = data.getData();
          Context context = MenuActivity.this;
          Intent intent = new Intent(context, ArticleActivity.class);
          intent.putExtra(ArticleFragment.EXTRA_URI, uri);
          context.startActivity(intent);
          break;
      }
    }
  }

}
