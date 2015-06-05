/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uitests;

/**
 *
 * @author Dionysios-Charalampos Vythoulkas <dcvythoulkas@gmail.com>
 */

import database.PostgresDBClient;
import database.ResultSetTableModel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


public class UI extends JFrame {
    private final GridBagLayout layout; // layout to use in panels
    private final GridBagConstraints constraints; // layout's constraints
    private final JPanel panel1; // panel for tab one
    private ResultSetTableModel resultSetTableModel;
    private JTable researchersTable;
    // set up GUI
    public UI() throws SQLException {
        // Frame wide config
        super("Akademia");        
        JTabbedPane tabbedPane =  new JTabbedPane(); // create JTabbedPane
        
        // panel1 config
        layout = new GridBagLayout();
        constraints = new GridBagConstraints();
        panel1 = new JPanel();        
        panel1.setLayout(layout);
        // creating ui objects
        // JLabels
        JLabel jlblAA = new JLabel("Α/Α", SwingConstants.RIGHT);
        JLabel jlblNameGr = new JLabel("Όνομα", SwingConstants.RIGHT);
        JLabel jlblSurNameGr = new JLabel("Επίθετο", SwingConstants.RIGHT);
        JLabel jlblName = new JLabel("Όνομα Αναζήτησης", SwingConstants.RIGHT);
        JLabel jlblSurName = new JLabel("Επίθετο Αναζήτησης", SwingConstants.RIGHT);
        JLabel jlblEmail = new JLabel("Διεύθυνση Ηλ.Ταχ.", SwingConstants.RIGHT);
        JLabel jlblDate = new JLabel("Ημ/νια Τελ. Ενημέρωσης", SwingConstants.RIGHT);
        // JTextFields
        JTextField jtxtAA = new JTextField(15);
        jtxtAA.setEditable(false);
        JTextField jtxtNameGr = new JTextField(15);
        JTextField jtxtSurNameGr = new JTextField(15);
        JTextField jtxtName = new JTextField(15);
        JTextField jtxtSurName = new JTextField(15);
        JTextField jtxtEmail = new JTextField(15);
        JTextField jtxtDate = new JTextField(15);
        jtxtDate.setEditable(false);
        // JButtons
        JButton jbClear = new JButton("Εκκαθάριση");
        jbClear.addActionListener(
            new ActionListener() { // anonymous inner class
                // handle JButton event and clear all textfields for new entry
                @Override
                public void actionPerformed(ActionEvent event) {
                    jtxtAA.setText("");
                    jtxtNameGr.setText("");
                    jtxtSurNameGr.setText("");
                    jtxtName.setText("");
                    jtxtSurName.setText("");
                    jtxtEmail.setText("");
                    jtxtDate.setText("");
                }
        });
        JButton jbInsert = new JButton("Εισαγωγή");
        jbInsert.addActionListener(
                new ActionListener() { // anonymous inner class
                    // handle JButton event and insert textfields' value in database
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        if (jtxtNameGr.getText().equals("") || jtxtSurNameGr.getText().equals("") ||
                                jtxtName.getText().equals("") || jtxtSurName.getText().equals("")) {
                            JOptionPane.showMessageDialog(null,
                                    "Τα πεδία 'Όνομα', 'Επίθετο', 'Όνoμα Αναζήτησης', 'Επίθετο Αναζήτησης' "
                                            + "είναι υποχρεωτικά.",
                                    "Μήνυμα", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            PostgresDBClient pDBC = new PostgresDBClient();
                            pDBC.insResearcher(
                                    jtxtNameGr.getText(),
                                    jtxtSurNameGr.getText(),
                                    jtxtName.getText(),
                                    jtxtSurName.getText(),
                                    jtxtEmail.getText()
                            );
                            jtxtAA.setText("");
                            jtxtNameGr.setText("");
                            jtxtSurNameGr.setText("");
                            jtxtName.setText("");
                            jtxtSurName.setText("");
                            jtxtEmail.setText("");
                            jtxtDate.setText("");
                            
                            try {
                                resultSetTableModel.updateView();
                            } catch (SQLException ex) {
                                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }        
                    }
                }
        );
        JButton jbEdit = new JButton("Επεξεργασία");
        jbEdit.addActionListener(
                new ActionListener() { // anonymous inner class
                    // handle JButton event and edit selected record from JTable
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        if (researchersTable.getSelectedRow() > -1) {
                            int row = researchersTable.convertRowIndexToModel(researchersTable.getSelectedRow());                        
                            jtxtAA.setText(researchersTable.getModel().getValueAt(row, 0).toString());
                            jtxtNameGr.setText(researchersTable.getModel().getValueAt(row, 1).toString());
                            jtxtSurNameGr.setText(researchersTable.getModel().getValueAt(row, 2).toString());
                            jtxtName.setText(researchersTable.getModel().getValueAt(row, 3).toString());
                            jtxtSurName.setText(researchersTable.getModel().getValueAt(row, 4).toString());
                            jtxtEmail.setText(researchersTable.getModel().getValueAt(row, 5).toString());
                            try {
                                jtxtDate.setText(researchersTable.getModel().getValueAt(row, 6).toString());
                            }
                            catch (Exception exception) {
                                jtxtDate.setText("");
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Επιλέξτε εγγραφή για επεξεργασία",
                                    "Μήνυμα",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
        );
        JButton jbSave = new JButton("Αποθήκευση");
        jbSave.addActionListener(
                new ActionListener() { // anonymous inner class
                    @Override
                    // handle JButton event and 
                    public void actionPerformed(ActionEvent event) {
                        if (jtxtAA.getText().equals("") || jtxtNameGr.getText().equals("") || jtxtSurNameGr.getText().equals("") ||
                                jtxtName.getText().equals("") || jtxtSurName.getText().equals("")) {
                            JOptionPane.showMessageDialog(null,
                                    "Επιλέξτε εγγραφή για επεξεργασία.",
                                    "Μήνυμα", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            PostgresDBClient pDBC = new PostgresDBClient();
                            pDBC.updResearcher(
                                    jtxtAA.getText(),
                                    jtxtNameGr.getText(),
                                    jtxtSurNameGr.getText(),
                                    jtxtName.getText(),
                                    jtxtSurName.getText(),
                                    jtxtEmail.getText()
                            );
                            jtxtAA.setText("");
                            jtxtNameGr.setText("");
                            jtxtSurNameGr.setText("");
                            jtxtName.setText("");
                            jtxtSurName.setText("");
                            jtxtEmail.setText("");
                            jtxtDate.setText("");

                            try {
                                resultSetTableModel.updateView();
                            } catch (SQLException ex) {
                                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }    
                    }
                }
        );
        JButton jbDelete = new JButton("Διαγραφή");
        jbDelete.addActionListener(
                new ActionListener() { // anonymous inner class
                    // handle JButton event and edit selected record from JTable
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        if (researchersTable.getSelectedRow() > -1) {
                            int row = researchersTable.convertRowIndexToModel(researchersTable.getSelectedRow());                        
                            String researcher_id = researchersTable.getModel().getValueAt(row, 0).toString();
                            String nameGr = researchersTable.getModel().getValueAt(row, 1).toString();
                            String surNameGr = researchersTable.getModel().getValueAt(row, 2).toString();
                            int response = JOptionPane.showConfirmDialog(null,
                                    "Θέλετε να διαγράψετε την εγγραφή '" + nameGr + " "
                                    + surNameGr + " και όλες τις σχετικές δημοσιεύσεις;",
                                    "Επιβεβαίωση διαγραφής",JOptionPane.OK_CANCEL_OPTION);
                            if (response==0) {
                                PostgresDBClient pDBC =  new PostgresDBClient();
                                pDBC.delResearcher(researcher_id);
                                
                                try {
                                    resultSetTableModel.updateView();
                                }
                                catch (SQLException ex) {
                                    Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        else 
                            JOptionPane.showMessageDialog(null, "Επιλέξτε εγγραφή για διαγραφή",
                                    "Μήνυμα", JOptionPane.PLAIN_MESSAGE);
                    }
                }
        );
        // JTable
        resultSetTableModel = new ResultSetTableModel();
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(resultSetTableModel);
        researchersTable = new JTable(resultSetTableModel);
        researchersTable.setRowSorter(sorter);
        // Position ui objects in the layout
        // weightx and weighty are 0 : the default
        constraints.fill = GridBagConstraints.HORIZONTAL;
        addComponent(jlblAA, 0, 1, 1, 1);
        addComponent(jlblNameGr, 1, 1, 1, 1);
        addComponent(jlblSurNameGr, 2, 1, 1, 1);
        addComponent(jlblName, 3, 1, 1, 1);
        addComponent(jlblSurName, 4, 1, 1, 1);
        addComponent(jlblEmail, 5, 1, 1, 1);
        addComponent(jlblDate, 6, 1, 1, 1);
        addComponent(jtxtAA, 0, 2, 1, 1);
        addComponent(jtxtNameGr, 1, 2, 1, 1);
        addComponent(jtxtSurNameGr, 2, 2, 1, 1);
        addComponent(jtxtName, 3, 2, 1, 1);
        addComponent(jtxtSurName, 4, 2, 1, 1);
        addComponent(jtxtEmail, 5, 2, 1, 1);
        addComponent(jtxtDate, 6, 2, 1, 1);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        addComponent(jbClear, 0, 4, 1, 1);
        addComponent(jbInsert, 1, 4, 1, 1);
        addComponent(jbEdit, 3, 4, 1, 1);
        addComponent(jbSave, 4, 4, 1, 1);
        addComponent(jbDelete, 6, 4, 1, 1);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1000;
        constraints.weighty = 1000;
        addComponent(new JScrollPane(researchersTable), 8, 0, 5,1);
        
        tabbedPane.addTab("Εισαγωγή", null, panel1, "Διαχείριση εισαγωγών");
        
        //////////////////////////   Panel 2  //////////////////////////////////////////
        // set up panel2 and add it to JTabbedPane
        JLabel label2 = new JLabel("Panel2", SwingConstants.CENTER);
        JPanel panel2 = new JPanel();
        panel2.add(label2);
        tabbedPane.addTab("Ενημέρωση", null, panel2, "Ενημέρωση δημοσιεύσεων");
        
        //////////////////////////  Panel 3  ///////////////////////////////////////////
        // set up pane3 and add it to JTabbedPane
        JLabel label3 = new JLabel("Panel3", SwingConstants.CENTER);
        JPanel panel3 = new JPanel();
        panel3.add(label3);
        tabbedPane.addTab("Προβολη", null, panel3, "Προβολή καταγεγραμμένων δημοσιεύσεων");
        
        
        add(tabbedPane); // add JTabbedPane to frame        
    }
    
    // Utility method that takes care of component place in the GridBagLayout
    // Courtesy of P & H Deitel
    private void addComponent(Component component, int row, int column, int width, int height) {
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        layout.setConstraints(component, constraints);
        panel1.add(component);
    }
    
} // end class UI 
