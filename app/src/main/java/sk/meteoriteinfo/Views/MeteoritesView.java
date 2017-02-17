package sk.meteoriteinfo.Views;

import java.util.List;

import sk.meteoriteinfo.Models.Meteorite;

public interface MeteoritesView {
  void showData(List<Meteorite> data);
  void showDetail(String id);
}
