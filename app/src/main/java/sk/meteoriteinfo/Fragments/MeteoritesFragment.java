package sk.meteoriteinfo.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sk.meteoriteinfo.Adapters.MeteoritesAdapter;
import sk.meteoriteinfo.Models.Meteorite;
import sk.meteoriteinfo.Presenters.MeteoritesPresenterImpl;
import sk.meteoriteinfo.R;
import sk.meteoriteinfo.Views.MeteoritesView;

public class MeteoritesFragment extends Fragment implements MeteoritesView, MeteoritesAdapter.OnItemClickCallback {


  private MeteoritesPresenterImpl mMeteoritesPresenter;

  private RecyclerView mRecyclerView;
  private MeteoritesAdapter mAdapter;
  private MeteoritesFragmentCallbacks mActivityCallbacks;

  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_meteorites, container, false);

    mActivityCallbacks = (MeteoritesFragmentCallbacks) getActivity();

    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
    mRecyclerView.setHasFixedSize(true);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
    mRecyclerView.setLayoutManager(layoutManager);

    mMeteoritesPresenter = new MeteoritesPresenterImpl(this);
    mMeteoritesPresenter.bind();

    return rootView;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();

    mMeteoritesPresenter.unBind();
  }

  @Override
  public void showData(List<Meteorite> data) {
    if (mAdapter == null) {
      mAdapter = new MeteoritesAdapter(data, this);
      mRecyclerView.setAdapter(mAdapter);
    } else {
      mAdapter.setData(data);
    }
  }

  @Override
  public void onItemClick(View view, int p) {
    mMeteoritesPresenter.itemClick(mAdapter.getItem(p).mId);
  }

  @Override
  public void showDetail(String id) {
    mActivityCallbacks.showDetail(id);
  }

  public interface MeteoritesFragmentCallbacks {
    void showDetail(String id);
  }
}
