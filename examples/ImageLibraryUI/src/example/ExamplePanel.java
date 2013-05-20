package example;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

import pwnee.image.ImageLibrary;

public class ExamplePanel extends JPanel implements ActionListener {
  
  public ImageLibrary library;
  
  public JTable table;
  
  
  public JButton addBtn = new JButton("Add Image");
  
  public JButton removeBtn = new JButton("Remove Image");
  
  
  public DefaultTableModel tModel;
  
  /** Has this ImageLibrary been edited since it was last saved? */
  public boolean isEdited = false;
  
  /** The current save path of the ImageLibrary. */
  public String curFilePath = "";
  
  
  
  public ExamplePanel() {
    super(new BorderLayout());
    library = new ImageLibrary();
    
    init();
  }
  
  
  
  public void init() {
    Object[] colNames = {"Name in ImageLibrary", "Image"};
    tModel = new DefaultTableModel(colNames, 0);
    table = new JTable(tModel) {
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
            
            
            public boolean isCellEditable(int row, int column) {
              return false;
            }
        };
    
    JScrollPane scrollPane = new JScrollPane(table);
    this.add(scrollPane, BorderLayout.CENTER);
    
    JPanel northPanel = new JPanel();
    northPanel.add(addBtn);
    addBtn.addActionListener(this);
    northPanel.add(removeBtn);
    removeBtn.addActionListener(this);
    
    this.add(northPanel, BorderLayout.NORTH);
    
    updateTable();
  }
  
  
  
  public void updateTable() {
    clearTable();
    
    // Create an alphabetically sorted list of image keys.
    List<String> keys = new ArrayList<>(library.images.keySet());
    Collections.sort(keys);
    
    // Add a row for each image in the library.
    int rowIndex = 0;
    for(String key : keys) {
      ImageIcon icon = new ImageIcon(library.get(key));
      Object[] row = {key, icon};
      tModel.addRow(row);
      
      table.setRowHeight(rowIndex, Math.max(icon.getIconHeight() + 10, 16));
      
      rowIndex++;
    }
    
    table.updateUI();
  }
  
  
  
  /** Clears all the rows from the table model. */
  public void clearTable() {
    // Remove the last row until no rows are left.
    while(tModel.getRowCount() > 0) {
      tModel.removeRow(tModel.getRowCount() - 1);
    }
  }
  
  
  
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    
    if(source == addBtn) {
      addImage();
    }
    if(source == removeBtn) {
      removeSelectedImage();
    }
  }
  
  
  
  /** Resets the GUI with a blank image library. */
  public void newLib() {
    isEdited = false;
    curFilePath = "";
    library = new ImageLibrary();
    updateTable();
  }
  
  
  /** Loads an ImageLibrary from a file. */
  public void load(String path) {
    library = ImageLibrary.load(path);
    updateTable();
    curFilePath = path;
    isEdited = false;
  }
  
  
  /** Saves the current ImageLibrary to a file. */
  public void save(String path) {
    library.save(path);
    curFilePath = path;
    isEdited = false;
  }
  
  
  public void save() {
    save(curFilePath);
  }
  
  
  /** Prompts the user to add a new image to the library. */
  public void addImage() {
    Image img;
    String key;
    
    JFileChooser chooser = new JFileChooser(ExampleMain.instance.config.get("lastOpen"));
    chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("BMP, GIF, JPG, or PNG image files", "bmp", "gif", "jpg", "png"));
    
    int confirmVal = chooser.showOpenDialog(this);
    File selFile = chooser.getSelectedFile();
    if(confirmVal == JFileChooser.APPROVE_OPTION) {
      ExampleMain.instance.config.put("lastOpen", selFile.getParent());
      img = Toolkit.getDefaultToolkit().createImage(selFile.getPath());
      key = JOptionPane.showInputDialog(this, "Image name:", selFile.getPath());
      
      if(key.equals("")) {
        return;
      }
      
      library.put(key, img);
      
      isEdited = true;
      updateTable();
    }
  }
  
  
  /** Removes the image currently selected in the table from the library. */
  public void removeSelectedImage() {
    
    int row = table.getSelectedRow();
    if(row == -1) {
      return;
    }
    
    String key = (String) table.getValueAt(row, 1);
    
    library.remove(key);
    
    isEdited = true;
    updateTable();
  }
  
}
