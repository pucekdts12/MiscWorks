package mandelbrot;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Complex{
    public double r, i;
    private final static int precision=2;
    Complex(){
      r=0;i=0;
    }
    Complex(double R){
      r=R;i=0;
    }
    Complex(double R, double I){
      setVal(R,I);
    }
    Complex(Complex a){
      r=a.r;
      i=a.i;
    }
    Complex(String str){
      Matcher m = Pattern.compile("(-?.*?)([+-].*?)i").matcher(str);
      if( m.matches() ){
        r=Double.parseDouble(m.group(1));
        i=Double.parseDouble(m.group(2));
      }else{
        r=Double.NaN;
        i=Double.NaN;
      }
    }
    public Complex add(Complex b){
      r=r+b.r;
      i=i+b.i;
      return this;
    }
    public Complex sub(Complex b){
      r=r-b.r;
      i=i-b.i;
      return this;
    }
    public Complex mul(Complex b){
      double new_r=r*b.r-i*b.i;
      double new_i=r*b.i+b.r*i;
      r=new_r;
      i=new_i;
      return this;
    }
    public Complex div(Complex b){
      double m=b.r*b.r - b.i*b.i;
      double new_r= r*b.r+i*b.i;
      double new_i= r*b.i + i*b.r;
      r=new_r/m;
      i=new_i/m;
      return this;

    }
    public double abs(){
      return Math.sqrt(r*r+i*i);
    }
    public double sqrAbs(){
      return r*r+i*i;
    }
    public double phase(){
      return Math.atan2(i,r);
    }
    public double re(){return r;}
    public double im(){return i;}

    static Complex add(Complex a, Complex b){
      //return a.add(b);
      return new Complex(a.r+b.r,a.i+b.i);
    }
    static Complex sub(Complex a, Complex b){
      //return a.sub(b);
      return new Complex(a.r-b.r,a.i-b.i);
    } 
    static Complex mul(Complex a, Complex b){
      //return a.mul(b);
      double new_r=a.r*b.r-a.i*b.i;
      double new_i=a.r*b.i+b.r*a.i;
      return new Complex(new_r,new_i);
    }
    static Complex div(Complex a, Complex b){
      double m=b.r*b.r+b.i*b.i;
      double new_r=a.r*b.r+a.i*b.i;
      double new_i=a.i*b.r-a.r*b.i;
      new_r=new_r/m;
      new_i=new_i/m;
      return new Complex(new_r,new_i);
    } 
    public static double abs(Complex a){
      return a.abs();
    }
    public static double phase(Complex a){
      return a.phase();
    }
    public static double sqrAbs(Complex a){
      return a.sqrAbs();
    }
    public static double re(Complex a){
      return a.re();
    }
    public static double im(Complex a){
      return a.im();
    }
    public String toString(){
      return ""+r+(i<0 ? "" : "+")+i+"i";
    }
    public static Complex valueOf(String str){
      return new Complex(str);
    }

    public void setRe(double R){
      r=R;
    }

    public void setIm(double I){
      i=I;
    }

    public void setVal(Complex a){
      r=a.r;
      i=a.i;
    }
    public void setVal(double R, double I){
      r=R;i=I;
    }
}