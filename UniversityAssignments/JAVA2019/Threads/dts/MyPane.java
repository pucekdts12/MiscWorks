package dts;
import javafx.scene.layout.Pane;

public class MyPane extends Pane{
  public void resize(double w,double h){
    setHeight(h);
    setWidth(w);
  }
  public void setHeight(double h){
    super.setHeight(h);
  }
  public void setWidth(double w){
    super.setWidth(w);
  }
}