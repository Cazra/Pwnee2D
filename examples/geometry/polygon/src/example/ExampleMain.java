package example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** The main window for this Pwnee game example. */
public class ExampleMain extends JFrame implements WindowListener, WindowFocusListener {

  public static ExampleMain instance;
  public ExamplePanel examplePanel;
  
  private ExampleMain() {
    super("Polygon Maker");
  }
  
  
  private void init() {
    int screenX = 800;    
    int screenY = 600;
    this.setSize(screenX,screenY);
    
    this.addWindowListener(this);
    this.addWindowFocusListener(this);
    
    // Create main window panel and add it into the window.
    examplePanel = new ExamplePanel();
    examplePanel.start(60);
    this.add(examplePanel);
    
    // finishing touches on Game window
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  
  
  
  public static ExampleMain launchInstance() {
    instance = new ExampleMain();
    instance.init();
    return instance;
  }
  
  
  
  
  // WindowListener interface stuff
  
  public void windowActivated(WindowEvent e) {
      System.err.println("Window Activated");
  }
  
  public void windowClosed(WindowEvent e)  {
      System.err.println("program terminated. Restoring original display settings.");
  }
  
  public void    windowClosing(WindowEvent e) {
      System.err.println("Window closing");
  }
  
  public void windowDeactivated(WindowEvent e) {
      System.err.println("Window deactivated");
  }
  
   public void windowDeiconified(WindowEvent e) {
      System.err.println("Window Deiconified");
      examplePanel.requestFocusInWindow();
  }
  
   public void windowIconified(WindowEvent e) {
      System.err.println("Window Iconified");
  }
  
   public void windowOpened(WindowEvent e) {
      System.err.println("Window opened");
  }
  
  public void windowGainedFocus(WindowEvent e) {
      System.err.println("Window gained focus");
      if(examplePanel != null)
          examplePanel.requestFocusInWindow();
  }
  
  public void windowLostFocus(WindowEvent e)  {
      System.err.println("Window lost focus");
  }
  
  
  
  
  /** Runs the GUI application. */
  public static void main(String[] args) {
      ExampleMain window = ExampleMain.launchInstance();
  }

}