package sk.meteoriteinfo.Helpers;

import android.content.Context;
import android.util.Log;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sk.meteoriteinfo.Models.Meteorite;
import sk.meteoriteinfo.Models.MeteoriteStorIOSQLiteDeleteResolver;
import sk.meteoriteinfo.Models.MeteoriteStorIOSQLiteGetResolver;
import sk.meteoriteinfo.Models.MeteoriteStorIOSQLitePutResolver;

public class MeteoriteHelper {

  private static final String DATA_URL = "https://data.nasa.gov/resource/y77d-th95.json?$where=year>=\"2011-01-01T00:00:00.000\"";
  private static final String TOKEN = "sFAZz940wn7aYOrxWyKm5kWnl";

  private static final String X_APP_TOKEN_KEY = "X-App-Token";
  private static final String NAME_KEY = "name";
  private static final String ID_KEY = "id";
  private static final String MASS_KEY = "mass";
  private static final String FALL_KEY = "fall";
  private static final String RECLAT_KEY = "reclat";
  private static final String RECLONG_KEY = "reclong";
  private static final String YEAR_KEY = "year";

  private static MeteoriteHelper INSTANCE;
  private static DefaultStorIOSQLite sStorIOSQLite;

  private MeteoriteHelper(Context context) {
    sStorIOSQLite =  DefaultStorIOSQLite.builder()
        .sqliteOpenHelper(DbHelper.getInstance(context))
        .addTypeMapping(Meteorite.class, SQLiteTypeMapping.<Meteorite>builder()
            .putResolver(new MeteoriteStorIOSQLitePutResolver())
            .getResolver(new MeteoriteStorIOSQLiteGetResolver())
            .deleteResolver(new MeteoriteStorIOSQLiteDeleteResolver())
            .build())
        .build();
  }

  public static synchronized MeteoriteHelper getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = new MeteoriteHelper(context);
    }
    return INSTANCE;
  }

  /**
   * Get all from Meteorites table as List.
   * @return Observable
   */
  public Observable<List<Meteorite>> getMeteorites() {
    return sStorIOSQLite
            .get()
            .listOfObjects(Meteorite.class)
            .withQuery(Query.builder()
                .table("meteorites")
                .orderBy("mass desc")
                .build())
            .prepare()
            .asRxObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
  }

  public Observable<Meteorite> getMeteorite(String id) {
    return sStorIOSQLite
        .get()
        .object(Meteorite.class)
        .withQuery(Query.builder()
            .table("meteorites")
            .where("id=" + id)
            .build())
        .prepare()
        .asRxObservable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }


  /**
   * Download data from URL and store them in DB.
   * @return Observable
   */
  public Observable<String> downloadAndStoreData() {
    Observable<String> observable = Observable.create(
        new Observable.OnSubscribe<String>() {
          @Override
          public void call(Subscriber<? super String> subscriber) {
            URL url;
            HttpsURLConnection urlConnection = null;
            try {
              url = new URL(DATA_URL);

              urlConnection = (HttpsURLConnection) url.openConnection();
              urlConnection.setRequestProperty(X_APP_TOKEN_KEY, TOKEN);

              Log.d("URLConnection", "Response code: " + urlConnection.getResponseCode());
              Log.d("URLConnection", "Response message: " + urlConnection.getResponseMessage());

              InputStream inputStream = urlConnection.getInputStream();
              BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

              StringBuffer buffer = new StringBuffer();
              String line = "";

              while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
              }
              JSONArray jsonArray = new JSONArray(String.valueOf(buffer));
              List<Meteorite> meteorites = new ArrayList<Meteorite>();

              for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                Meteorite newMeteorite = createMeteoriteFromJson(jsonObject);

                if (newMeteorite != null) {
                  meteorites.add(newMeteorite);
                }
              }

              PutResults<Meteorite> putResults = sStorIOSQLite
                  .put()
                  .objects(meteorites)
                  .prepare()
                  .executeAsBlocking();

              subscriber.onCompleted();
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
              if (urlConnection != null) {
                urlConnection.disconnect();
              }
            }
          }
        }
    );
    return observable;
  }

  private Meteorite createMeteoriteFromJson(JSONObject jsonObject) {
    Meteorite newMeteorite = null;
    String id;
    String name;
    long mass;
    String fall;
    int year;
    String reclat;
    String reclong;

    try {
      id = jsonObject.getString(ID_KEY);
      name = jsonObject.getString(NAME_KEY);
      mass = jsonObject.getLong(MASS_KEY);
      fall = jsonObject.getString(FALL_KEY);
      reclat = jsonObject.getString(RECLAT_KEY);
      reclong = jsonObject.getString(RECLONG_KEY);

      String date = jsonObject.getString(YEAR_KEY);
      year = Integer.valueOf(date.substring(0, 4));

      newMeteorite = new Meteorite(id, name, mass, fall, year, reclat, reclong);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return newMeteorite;
  }
}
