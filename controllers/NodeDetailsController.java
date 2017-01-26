package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import dao.ElectricalDataDAO;

public class NodeDetailsController extends Controller {

  public static Result getNodeDetails() {
    ElectricalDataDAO dao = new ElectricalDataDAO();
    return ok(index.render(dao.getLatestElectricalData()));
  }

}
