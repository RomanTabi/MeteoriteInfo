package sk.meteoriteinfo.Presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sk.meteoriteinfo.Fragments.MeteoritesFragment;
import sk.meteoriteinfo.Helpers.MeteoriteHelper;
import sk.meteoriteinfo.Models.Meteorite;

public class MeteoritesPresenterImpl implements MeteoritesPresenter {

  private WeakReference<MeteoritesFragment> mMeteoritesFragment;

  private Subscription mGetAllSubscription;
  private Subscription mDownloadAndStoreSubscription;
  private Context mContext;

  public MeteoritesPresenterImpl(MeteoritesFragment fragment) {
    mMeteoritesFragment = new WeakReference<>(fragment);
    mContext = fragment.getContext();
  }

  @Override
  public void bind() {
    if (mGetAllSubscription == null || mGetAllSubscription.isUnsubscribed()) {
      mGetAllSubscription = MeteoriteHelper.getInstance(mContext)
          .getMeteorites()
          .subscribe(
              new Observer<List<Meteorite>>() {

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                  throwable.printStackTrace();
                }

                @Override
                public void onNext(List<Meteorite> meteorites) {
                  receiveData(meteorites);
                }
              });
    }
    if (mDownloadAndStoreSubscription == null || mDownloadAndStoreSubscription.isUnsubscribed()) {
      final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
      String lastSyncDate = sharedPreferences.getString("last_sync", "");

      final String currentDate = getCurrentDate();

      if (!lastSyncDate.equals(currentDate)) {
        mDownloadAndStoreSubscription = MeteoriteHelper.getInstance(mContext)
            .downloadAndStoreData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<String>() {
              @Override
              public void onCompleted() {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("last_sync", currentDate);
                editor.apply();
              }

              @Override
              public void onError(Throwable e) {
                e.printStackTrace();
              }

              @Override
              public void onNext(String s) {

              }
            });
      }
    }
  }

  private String getCurrentDate() {
    Calendar c = Calendar.getInstance();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
    final String currentDate = simpleDateFormat.format(c.getTime());

    return currentDate;
  }

  private void receiveData(List<Meteorite> meteorites) {
    mMeteoritesFragment.get().showData(meteorites);
  }

  @Override
  public void unBind() {
    if (mGetAllSubscription != null && !mGetAllSubscription.isUnsubscribed()) {
      mGetAllSubscription.unsubscribe();
    }
    if (mDownloadAndStoreSubscription != null && !mDownloadAndStoreSubscription.isUnsubscribed()) {
      mDownloadAndStoreSubscription.unsubscribe();
    }
  }

  @Override
  public void itemClick(String id) {
    mMeteoritesFragment.get().showDetail(id);
  }

}