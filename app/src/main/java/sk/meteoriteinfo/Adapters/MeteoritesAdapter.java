package sk.meteoriteinfo.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sk.meteoriteinfo.Fragments.MeteoritesFragment;
import sk.meteoriteinfo.Models.Meteorite;
import sk.meteoriteinfo.R;

public class MeteoritesAdapter extends RecyclerView.Adapter<MeteoritesAdapter.ViewHolder> {


  private List<Meteorite> mData;
  private OnItemClickCallback mClickCallback;

  public MeteoritesAdapter(List<Meteorite> data, MeteoritesFragment fragment) {
    mData = data;
    mClickCallback = fragment;
  }

  @Override
  public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
    final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_meteorite, parent, false);

    return new ViewHolder(itemView, mClickCallback);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Meteorite meteorite = mData.get(position);

    holder.mNameView.setText(meteorite.mName);
    holder.mMassView.setText(String.valueOf(meteorite.mMass));
    holder.mYearView.setText(String.valueOf(meteorite.mYear));
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public Meteorite getItem(int p) {
    return mData.get(p);
  }

  public void setData(List<Meteorite> data) {
    mData = data;
    notifyDataSetChanged();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mNameView;
    public TextView mMassView;
    public TextView mYearView;
    private OnItemClickCallback mClickCallback;

    public ViewHolder(View view, OnItemClickCallback clickCallback) {
      super(view);

      view.setOnClickListener(this);

      mClickCallback = clickCallback;
      mNameView = (TextView) view.findViewById(R.id.name_view);
      mMassView = (TextView) view.findViewById(R.id.mass_view);
      mYearView = (TextView) view.findViewById(R.id.year_view);
    }

    @Override
    public void onClick(View view) {
      mClickCallback.onItemClick(view, getAdapterPosition());
    }
  }

  public interface OnItemClickCallback {
    void onItemClick(View view, int p);
  }
}
