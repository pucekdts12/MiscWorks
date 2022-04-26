package dtdraw;

import javafx.scene.control.ToggleButton;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;


public class ToolButton extends ToggleButton{
  private StringProperty tool = new SimpleStringProperty(this,"tool","none");
  public void setTool(String s){
    tool.set(s);
  }
  public String getTool(){
    return tool.get();
  }  


  
  
}