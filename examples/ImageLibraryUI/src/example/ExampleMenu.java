package example;

import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class ExampleMenu extends JMenuBar implements ActionListener {
  
  public JMenu fileMenu = new JMenu("File");
    public JMenuItem newItem = new JMenuItem("New");
    public JMenuItem openItem = new JMenuItem("Open");
    public JMenuItem saveItem = new JMenuItem("Save");
    public JMenuItem saveAsItem = new JMenuItem("Save As");
    public JMenuItem exitItem = new JMenuItem("Exit");
    
  
  public ExampleMenu() {
    super();
    
    init();
  }
  
  
  public void init() {
    this.add(fileMenu);
      fileMenu.add(newItem);
      newItem.addActionListener(this);
      newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
      
      fileMenu.add(openItem);
      openItem.addActionListener(this);
      openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
      
      fileMenu.add(saveItem);
      saveItem.addActionListener(this);
      saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
      
      fileMenu.add(saveAsItem);
      saveAsItem.addActionListener(this);
      saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.ALT_MASK));
      
      fileMenu.add(exitItem);
      exitItem.addActionListener(this);
      exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
  }
  
  
  
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    
    ExamplePanel examplePanel = ExampleMain.instance.examplePanel;
    
    if(source == newItem) {
      newOption();
    }
    if(source == openItem) {
      openOption();
    }
    if(source == saveItem) {
      saveOption(false);
    }
    if(source == saveAsItem) {
      saveOption(true);
    }
    if(source == exitItem) {
      exitOption();
    }
  }
  
  
  public void newOption() {
    ExamplePanel examplePanel = ExampleMain.instance.examplePanel;
    
    if(checkNeedsSave() != JOptionPane.CANCEL_OPTION) {
      examplePanel.newLib();
    }
  }
  
  
  /**
   * Loads the ImageLibrary from a file.
   */
  public void openOption() {
    ExamplePanel examplePanel = ExampleMain.instance.examplePanel;
    
    if(checkNeedsSave() != JOptionPane.CANCEL_OPTION) {
      JFileChooser chooser = new JFileChooser(ExampleMain.instance.config.get("lastOpen"));
      chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(".ILIB Image Library file", "ilib"));
      
      int confirmVal = chooser.showOpenDialog(this);
      File selFile = chooser.getSelectedFile();
      if(confirmVal == JFileChooser.APPROVE_OPTION) {
        ExampleMain.instance.config.put("lastOpen", selFile.getParent());
        examplePanel.load(selFile.getPath());
      }
    }
  }
  
  
  /**
   * Saves the ImageLibrary using a save dialog.
   */
  public int saveOption(boolean saveAs) {
    ExamplePanel examplePanel = ExampleMain.instance.examplePanel;
    
    if(examplePanel.curFilePath.equals("") || saveAs) {
      JFileChooser chooser = new JFileChooser(ExampleMain.instance.config.get("lastOpen"));
      chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(".ILIB Image Library file", "ilib"));
      
      int confirmVal = chooser.showSaveDialog(this);
      File selFile = chooser.getSelectedFile();
      if(confirmVal == JFileChooser.APPROVE_OPTION && selFile != null) {
        ExampleMain.instance.config.put("lastOpen", selFile.getParent());
        
        // make sure the saved file ends with ".ilib".
        if(!selFile.getName().endsWith(".ilib")) {
          selFile = new File(selFile.getPath() + ".ilib");
        }
        
        examplePanel.save(selFile.getPath());
        return JFileChooser.APPROVE_OPTION;
      }
      return JFileChooser.CANCEL_OPTION;
    }
    else {
      examplePanel.save();
      return JFileChooser.APPROVE_OPTION;
    }
  }
  
  
  
  /**
   * Exits the application, but asks the user if they'd like to save first.
   */
  public void exitOption() {
    if(checkNeedsSave() != JOptionPane.CANCEL_OPTION) {
      ExampleMain.instance.config.save();
      System.exit(0);
    }
  }
  
  
  
  
  /**
   * If the ImageLibrary has been edited, prompt the user if they would 
   * like to save it before continuing.
   * @return  JOptionPane.YES_OPTION if the user chooses to save.
   *          JOptionPane.NO_OPTION if the user chooses not to save
   *            or if the ImageLibrary has not been edited.
   *          JOptionPane.CANCEL_OPTION if the save operation is canceled.
   */
  public int checkNeedsSave() {
    ExamplePanel examplePanel = ExampleMain.instance.examplePanel;
    
    if(examplePanel.isEdited) {
      int confirm = JOptionPane.showConfirmDialog(ExampleMain.instance, "You have unsaved changes to your ImageLibrary. Would you like to save before exiting?");
      
      if(confirm == JOptionPane.YES_OPTION) {
        int saveConfirm = saveOption(false);
        if(saveConfirm == JFileChooser.APPROVE_OPTION) {
          return JOptionPane.YES_OPTION;
        }
        else {
          return JOptionPane.CANCEL_OPTION;
        }
      }
      else if(confirm == JOptionPane.NO_OPTION) {
        return JOptionPane.NO_OPTION;
      }
      else {
        return JOptionPane.CANCEL_OPTION;
      }
    }
    else {
      return JOptionPane.NO_OPTION;
    }
  }
}
