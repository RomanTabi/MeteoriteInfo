package sk.meteoriteinfo.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import sk.meteoriteinfo.Fragments.DetailFragment;
import sk.meteoriteinfo.R;

public class DetailActivity extends AppCompatActivity {

  private DetailFragment mDetailFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      getSupportActionBar().setHomeButtonEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    mDetailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);

    if (getIntent().getExtras() != null && mDetailFragment != null) {
      String id = getIntent().getExtras().getString("object_id");
      mDetailFragment.loadObject(id);
    }
  }
}
