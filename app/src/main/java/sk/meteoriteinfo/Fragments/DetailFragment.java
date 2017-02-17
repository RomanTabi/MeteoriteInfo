package sk.meteoriteinfo.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import sk.meteoriteinfo.Models.Meteorite;
import sk.meteoriteinfo.Presenters.DetailPresenterImpl;
import sk.meteoriteinfo.R;
import sk.meteoriteinfo.Views.DetailView;

public class DetailFragment extends Fragment implements DetailView, OnMapReadyCallback {

  private GoogleMap mGoogleMap;
  private MapView mMapView;
  private DetailPresenterImpl mDetailPresenter;
  private TextView mNameView;
  private TextView mMassView;
  private TextView mFallView;
  private TextView mYearView;
  private TextView mLatView;
  private TextView mLngView;

  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

    mMapView = (MapView) rootView.findViewById(R.id.map_view);
    mMapView.onCreate(savedInstanceState);

    mMapView.getMapAsync(this);

    mNameView = (TextView) rootView.findViewById(R.id.name);
    mMassView = (TextView) rootView.findViewById(R.id.mass);
    mFallView = (TextView) rootView.findViewById(R.id.fall);
    mYearView = (TextView) rootView.findViewById(R.id.year);
    mLatView = (TextView) rootView.findViewById(R.id.lat);
    mLngView = (TextView) rootView.findViewById(R.id.lng);

    mDetailPresenter = new DetailPresenterImpl(this);

    return rootView;
  }

  @Override
  public void loadObject(String id) {
    mDetailPresenter.getObject(id);
  }

  @Override
  public void showObject(Meteorite meteorite) {
    if (mGoogleMap == null) {
      return;
    }
    mGoogleMap.clear();

    LatLng latLng = new LatLng(Double.valueOf(meteorite.mRecLat), Double.valueOf(meteorite.mRecLong));
    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);

    mGoogleMap.addMarker(
        new MarkerOptions()
            .position(latLng));

    mGoogleMap.animateCamera(cameraUpdate);

    mNameView.setText(meteorite.mName);
    mMassView.setText(String.valueOf(meteorite.mMass));
    mFallView.setText(meteorite.mFall);
    mYearView.setText(String.valueOf(meteorite.mYear));
    mLatView.setText(meteorite.mRecLat);
    mLngView.setText(meteorite.mRecLong);
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    if (googleMap != null) {
      mGoogleMap = googleMap;
    }
  }

  @Override
  public void onStart() {
    mMapView.onStart();
    super.onStart();
  }

  @Override
  public void onResume() {
    mMapView.onResume();
    super.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mMapView.onPause();
  }

  @Override
  public void onStop() {
    mMapView.onStop();
    super.onStop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mMapView.onDestroy();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mMapView.onSaveInstanceState(outState);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mMapView.onLowMemory();
  }
}
