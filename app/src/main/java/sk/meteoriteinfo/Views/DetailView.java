package sk.meteoriteinfo.Views;

import sk.meteoriteinfo.Models.Meteorite;

public interface DetailView {
  void loadObject(String id);
  void showObject(Meteorite meteorite);
}
