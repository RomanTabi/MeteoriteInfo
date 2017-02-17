package sk.meteoriteinfo.Presenters;

import sk.meteoriteinfo.Models.Meteorite;

public interface DetailPresenter {
  void receiveObject(Meteorite meteorite);
  void getObject(String id);
}
