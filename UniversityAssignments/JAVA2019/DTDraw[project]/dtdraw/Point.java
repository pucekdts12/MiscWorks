package dtdraw;

import java.io.Serializable;

public class Point implements Serializable{
  public double x,y;
  Point(double X,double Y){
    x=X;y=Y;
  }
  Point(){
    x=0;y=0;
  }
}
