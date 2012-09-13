package example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import pwnee.*;

/** The main window for this Pwnee game example. */
public class HelloMain extends JFrame implements WindowListener, WindowFocusListener {
    
    private GraphicsDevice grDev;
    private DisplayMode oldDisplay;
    
    public GamePanel helloPanel;
    public boolean fullscreen = false;
    
    public HelloMain(boolean fullscreen) {
        super("Hello World!");
        int screenX = 640;    
        int screenY = 480;
        this.setSize(screenX,screenY);
        
        this.addWindowListener(this);
        this.addWindowFocusListener(this);
        
        // Save our starting display mode.
        grDev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        oldDisplay = grDev.getDisplayMode();
        
        // setup the game for full-screen if requested.
        this.fullscreen = fullscreen;
        if(fullscreen)
            setFullscreen();

        // Create main window panel and add it into the window.
        helloPanel = new HelloPanel();
        this.add(helloPanel);
        
        // finishing touches on Game window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // Request focus so that we can poll keyboard input in our game.
        helloPanel.requestFocusInWindow();
        
        System.err.println("Game Window successfully created!!!");
    }
    
    
    public void setFullscreen() {
        System.err.println("Trying to start program in Fullscreen mode.");
            
        // makes sure fullscreen is supported before doing anything.
        if(grDev.isFullScreenSupported()) {
            System.err.println("FullScreen is supported");
            this.setUndecorated(true);
            
            // Create and apply a new DisplayMode with a 640 x 480 resolution.
            try {
                DisplayMode resChangeMode = new DisplayMode(640,480,32,DisplayMode.REFRESH_RATE_UNKNOWN); 
                grDev.setFullScreenWindow(this); 
                grDev.setDisplayMode(resChangeMode); 
                System.err.println("Change resolution: Success!");
            }
            catch(Exception e) {
                System.err.println("Change resolution: FAIL!");
            }
        }
        
        this.setExtendedState(MAXIMIZED_BOTH);
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
        if(fullscreen)
            grDev.setDisplayMode(oldDisplay);
    }
    
    public void windowDeactivated(WindowEvent e) {
        System.err.println("Window deactivated");
    }
    
     public void windowDeiconified(WindowEvent e) {
        System.err.println("Window Deiconified");
        helloPanel.requestFocusInWindow();
    }
    
     public void windowIconified(WindowEvent e) {
        System.err.println("Window Iconified");
    }
    
     public void windowOpened(WindowEvent e) {
        System.err.println("Window opened");
    }
    
    public void windowGainedFocus(WindowEvent e) {
        System.err.println("Window gained focus");
        helloPanel.requestFocusInWindow();
    }
    
    public void windowLostFocus(WindowEvent e)  {
        System.err.println("Window lost focus");
    }
    
    
    
    
    /** Creates the game window and makes it fullscreen if the user provided the argument "fullscreen". */
    public static void main(String[] args) {
        HelloMain window;
        
        for(String s : args)
            System.out.println(s);
        if(args.length == 0)
            window = new HelloMain(false);
        else if(args[0].equals("fullscreen"))
            window = new HelloMain(true);
        
    }

}