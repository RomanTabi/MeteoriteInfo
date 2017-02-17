package sk.meteoriteinfo.Models;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import org.json.JSONException;
import org.json.JSONObject;

@StorIOSQLiteType(table = "meteorites")
public class Meteorite {

  @StorIOSQLiteColumn(name = "name")
  public String mName;
  @StorIOSQLiteColumn(name = "id", key = true)
  public String mId;
  @StorIOSQLiteColumn(name = "mass")
  public long mMass;
  @StorIOSQLiteColumn(name = "fall")
  public String mFall;
  @StorIOSQLiteColumn(name = "year")
  public int mYear;
  @StorIOSQLiteColumn(name = "reclat")
  public String mRecLat;
  @StorIOSQLiteColumn(name = "reclong")
  public String mRecLong;

  public Meteorite() {}

  public Meteorite(@NonNull String id, String name, long mass, String fall, int year, String reclat, String reclong) {
    mId = id;
    mName = name;
    mMass = mass;
    mFall = fall;
    mYear = year;
    mRecLat = reclat;
    mRecLong = reclong;
  }
}
