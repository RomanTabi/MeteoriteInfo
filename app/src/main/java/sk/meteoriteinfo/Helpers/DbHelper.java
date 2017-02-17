package sk.meteoriteinfo.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

  private static final String METEORITES_TABLE_NAME = "meteorites";
  private static final String METEORITES_ID_COL = "id";
  private static final String METEORITES_NAME_COL = "name";
  private static final String METEORITES_MASS_COL = "mass";
  private static final String METEORITES_FALL_COL = "fall";
  private static final String METEORITES_RECLAT_COL = "reclat";
  private static final String METEORITES_RECLONG_COL = "reclong";
  private static final String METEORITES_YEAR_COL = "year";

  private static final String METEORITES_TABLE_CREATE =
      "CREATE TABLE IF NOT EXISTS " + METEORITES_TABLE_NAME + "(" +
          METEORITES_ID_COL + " TEXT, " +
          METEORITES_NAME_COL + " TEXT, " +
          METEORITES_MASS_COL + " INTEGER, " +
          METEORITES_FALL_COL + " TEXT, " +
          METEORITES_RECLAT_COL + " TEXT, " +
          METEORITES_RECLONG_COL + " TEXT, " +
          METEORITES_YEAR_COL + " INTEGER" +
          ");";

  private static DbHelper mInstance;

  public static synchronized DbHelper getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new DbHelper(context.getApplicationContext());
    }
    return mInstance;
  }

  private DbHelper(Context context) {
    super(context, "db", null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(METEORITES_TABLE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

  }
}
