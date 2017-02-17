package sk.meteoriteinfo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import sk.meteoriteinfo.Fragments.DetailFragment;
import sk.meteoriteinfo.Fragments.MeteoritesFragment;
import sk.meteoriteinfo.R;

public class MeteoritesActivity extends AppCompatActivity implements MeteoritesFragment.MeteoritesFragmentCallbacks {

  private MeteoritesFragment mMeteoritesFragment;
  private DetailFragment mDetailFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_meteorites);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
    }

    mMeteoritesFragment = (MeteoritesFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment);
    mDetailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
  }

  @Override
  public void showDetail(String id) {
    if (mDetailFragment == null) {
      Intent intent = new Intent(this, DetailActivity.class);
      intent.putExtra("object_id", id);
      startActivity(intent);
    } else {
      mDetailFragment.loadObject(id);
    }
  }
}
