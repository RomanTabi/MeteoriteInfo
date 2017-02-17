package sk.meteoriteinfo.Presenters;

import android.content.Context;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import rx.Subscriber;
import sk.meteoriteinfo.Fragments.DetailFragment;
import sk.meteoriteinfo.Helpers.MeteoriteHelper;
import sk.meteoriteinfo.Models.Meteorite;

public class DetailPresenterImpl implements DetailPresenter {

  private final WeakReference<DetailFragment> mDetailFragment;
  private final Context mContext;

  public DetailPresenterImpl(DetailFragment fragment) {
    mDetailFragment = new WeakReference<>(fragment);
    mContext = fragment.getContext();
  }

  @Override
  public void getObject(String id) {
    MeteoriteHelper.getInstance(mContext)
        .getMeteorite(id)
        .take(1)
        .subscribe(new Subscriber<Meteorite>() {
          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {
            e.printStackTrace();
          }

          @Override
          public void onNext(Meteorite meteorite) {
            receiveObject(meteorite);
          }
        });
  }

  @Override
  public void receiveObject(Meteorite meteorite) {
    mDetailFragment.get().showObject(meteorite);
  }
}
