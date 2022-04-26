package mandelbrot;

import mandelbrot.Complex;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.function.Function;
import java.util.ArrayList;
import java.util.HashMap;



public class MandelFractal /*implements ComplexDrawable*/{
  private int H,W;
  private final static int MAXITER=360;
  public double R;
  public double xmin,xmax,ymin,ymax;
  private Color color;
  private int cur_n;
  private double cur_r;
  private String sColor="HSB";
  public HashMap<String,Function<Integer,Color>> fnColor;
  MandelFractal(){
    R=2;
    color=Color.color(0,0,0);
    W=0;H=0;
    xmin=0;xmax=0;ymin=0;ymax=0;
    fnColor=new HashMap<>();
    fnColor.put( sColor , (Integer n)->Color.hsb(n,1.0,(n<MAXITER?1.0:0)) );
    fnColor.put( "Blue" , (Integer n)->Color.rgb(0,0,(int)((Math.abs((Math.sin(n)))+4)/5*255)) );
    fnColor.put( "Black&White" , (Integer n)->Color.rgb(n%255,n%255,n%255) );
  }
  public void draw(PixelWriter pw,Complex a,Complex b, int w,int h){
    H=h;
    W=w;
    xmin=Math.min(a.r,b.r);
    xmax=Math.max(a.r,b.r);
    ymin=Math.min(a.i,b.i);
    ymax=Math.max(a.i,b.i);
    double t=0;
    String cl="";
    Complex tmp=new Complex();
    int n;
    for( int x=0;x<W;x++ ){
      for( int y=0;y<H;y++ ){
        translate(tmp,x,y);
        n=iterI(tmp);
        if( n<MAXITER ){
          color=fnColor.get(sColor).apply(n);
        }else{
          color=Color.rgb(0,0,0);
        }
        pw.setColor(x,y,color);
      }
    }

  }
  public void translate(Complex t, int x, int y){
    double rx=Math.abs(xmin-xmax)/W;
    double ry=Math.abs(ymin-ymax)/H;
    t.setVal(xmin+rx*x,ymin+ry*y);
  }
  
  public int iterI(Complex c){
    Complex t=new Complex(0,0);
    int n=0;
    while( n<=MAXITER && t.sqrAbs()<R*R){
      n++;
      t=t.mul(t).add(c);
    }
    cur_n=n;
    cur_r=t.sqrAbs();
    return n-1;
  }
  public void setColor(String n){
    //if( n<0 || n>fnColor.size() ) return;
    sColor=n;
  }
}