package com.inventrymanagement.screens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class MainScreen {
    public JTabbedPane tabbedPane1;
    public JPanel mainScreen;
    public JTextField nameOfProduct;
    public JTextArea description;
    public JComboBox category;
    public JComboBox domDate;
    public JComboBox domMonth;
    public JComboBox domYear;
    public JComboBox doeDate;
    public JComboBox doeMonth;
    public JComboBox doeYear;
    public JTextField amount;
    public JTextField numInInventory;
    public JButton addBtn;
    private JTable table1;
    private JPanel buyPanel;
    private JComboBox idNNameBox;
    private JTextField numRefillTxt;
    private JButton setCountButton;
    private JButton addButton;
    private BuyItemComponent buyItemComponent;

    public MainScreen() {
        category.addItem("Groceries");
        category.addItem("Food Product");
        category.addItem("Toys");
        category.addItem("Other");

        for(int i=1 ; i<=31; i++) {
            domDate.addItem(i);
            doeDate.addItem(i);
        }

        for(int i=1 ; i<=12; i++) {
            domMonth.addItem(i);
            doeMonth.addItem(i);
        }

        for(int i=2000 ; i<=2030; i++) {
            domYear.addItem(i);
            doeYear.addItem(i);
        }

        try {
//            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306","laukik","laukik20");
            System.out.println("Database is creating");
            Statement smt = con.createStatement();
            smt.execute("CREATE DATABASE IF NOT EXISTS Inventry;");
            System.out.println("Database is created");
            smt.execute("USE Inventry;");
            System.out.println("Use inventry");
            smt.execute("CREATE TABLE IF NOT EXISTS Items(id int PRIMARY KEY AUTO_INCREMENT,name varchar(20), description varchar(50), category varchar(30), dom DATE, doe DATE, amount int, num int);");
            System.out.println("table is created");
            System.out.println("Table is selected");
            ResultSet rs = smt.executeQuery("SELECT id, name FROM Items;");
            while(rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                idNNameBox.addItem(new BoxString(id, name));
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!nameOfProduct.getText().isBlank() && !description.getText().isBlank() && !amount.getText().isBlank() && !numInInventory.getText().isBlank()) {
                    int am = Integer.parseInt(amount.getText());
                    int num = Integer.parseInt(numInInventory.getText());
                    String name = nameOfProduct.getText();
                    String des = description.getText();
                    String cat = category.getSelectedItem().toString();

                    System.out.print(domDate.getSelectedItem().toString() +"/"+ domMonth.getSelectedItem().toString() + "/" +domYear.getSelectedItem().toString());
                    System.out.print(doeDate.getSelectedItem().toString() +"/"+ doeMonth.getSelectedItem().toString() + "/" +doeYear.getSelectedItem().toString());

                    Date dom = new Date((Integer.parseInt( domYear.getSelectedItem().toString()) - 1900), (Integer.parseInt(domMonth.getSelectedItem().toString()) - 1), Integer.parseInt(domDate.getSelectedItem().toString()));
                    Date doe = new Date((Integer.parseInt( doeYear.getSelectedItem().toString()) - 1900), (Integer.parseInt(doeMonth.getSelectedItem().toString()) - 1), Integer.parseInt(doeDate.getSelectedItem().toString()));
                    System.out.println(name + " " + am + " " + des + " " + num + " " + cat + " " + dom.toString() + " " + doe.toString());
//                    int id = 0;
                    try {
//            Class.forName("com.mysql.jdbc.Driver");
                        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306","laukik","laukik20");
                        Statement smt = con.createStatement();
                        smt.execute("USE Inventry;");
                        PreparedStatement prtSmt = con.prepareStatement("INSERT INTO Items(name , description , category , dom , doe , amount , num) Values (?,?,?,?,?,?,?)");
                        prtSmt.setString(1, name);
                        prtSmt.setString(2, des);
                        prtSmt.setString(3, cat);
                        prtSmt.setDate(4, dom);
                        prtSmt.setDate(5, doe);
                        prtSmt.setInt(6, am);
                        prtSmt.setInt(7, num);

                        prtSmt.execute();
                        ResultSet rs = smt.executeQuery("SELECT * FROM Items;");
                        while (rs.next()) {
                            System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getDate(5) + " " + rs.getDate(6) + " " + rs.getInt(7) + " " + rs.getInt(8));
//                            id = rs.getInt(1);
                        }

                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    nameOfProduct.setText("");
                    description.setText("");
                    domDate.setSelectedIndex(0);
                    doeDate.setSelectedIndex(0);
                    domMonth.setSelectedIndex(0);
                    doeMonth.setSelectedIndex(0);
                    domYear.setSelectedIndex(0);
                    doeYear.setSelectedIndex(0);
                    category.setSelectedIndex(0);
                    amount.setText("");
                    numInInventory.setText("");

//                    idNNameBox.addItem(new BoxString(id, name));

                    JOptionPane.showMessageDialog(mainScreen, "Item is Added!!");

                } else {
                    JOptionPane.showMessageDialog(mainScreen, "Please Fill Every Information");
                }
            }
        });

        tabbedPane1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                buyPanel.remove(buyItemComponent.buyItemPane);
                buyItemComponent = new BuyItemComponent();
                buyPanel.add(buyItemComponent.buyItemPane, BorderLayout.CENTER);
                try {
//            Class.forName("com.mysql.jdbc.Driver");
                    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306","laukik","laukik20");
                    Statement smt = con.createStatement();
                    smt.execute("USE Inventry;");
                    ResultSet rs = smt.executeQuery("SELECT * FROM Items;");
                    table1.setModel(buildTableModel(rs));

                    rs = smt.executeQuery("SELECT id, name FROM Items;");
                    idNNameBox.removeAllItems();
                    while(rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        idNNameBox.addItem(new BoxString(id, name));
                    }

                    con.close();
                } catch (SQLException err) {
                    err.printStackTrace();
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                BoxString boxString = (BoxString) idNNameBox.getSelectedItem();
                int num = Integer.parseInt(numRefillTxt.getText());
                try {
//            Class.forName("com.mysql.jdbc.Driver");
                    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306","laukik","laukik20");
                    Statement smt = con.createStatement();
                    smt.execute("USE Inventry;");
                    PreparedStatement prtSmt = con.prepareStatement("UPDATE Items SET num = num+? Where id = ?;");
                    prtSmt.setInt(1, num);
                    prtSmt.setInt(2, boxString.id);
                    prtSmt.executeUpdate();
                    con.close();
                } catch (SQLException err) {
                    err.printStackTrace();
                } finally {
                    idNNameBox.setSelectedIndex(0);
                    numRefillTxt.setText("");
                }
            }
        });

        setCountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                BoxString boxString = (BoxString) idNNameBox.getSelectedItem();
                int num = Integer.parseInt(numRefillTxt.getText());
                boolean whanToDelete = (num == 0);

                try {
//            Class.forName("com.mysql.jdbc.Driver");
                    Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306","laukik","laukik20");
                    Statement smt = con.createStatement();
                    smt.execute("USE Inventry;");
                    if(whanToDelete) {
                        PreparedStatement prtSmt = con.prepareStatement("DELETE FROM Items where id = ?;");
                        prtSmt.setInt(1, boxString.id);
                        prtSmt.execute();
                        JOptionPane.showMessageDialog(mainScreen, "Item is Deleted !!");
                    } else {
                        PreparedStatement prtSmt = con.prepareStatement("UPDATE Items SET num = ? Where id = ?;");
                        prtSmt.setInt(1, num);
                        prtSmt.setInt(2, boxString.id);
                        prtSmt.executeUpdate();

                        JOptionPane.showMessageDialog(mainScreen, "Item is Updated !!");
                    }
                    con.close();
                } catch (SQLException err) {
                    err.printStackTrace();
                } finally {
                    idNNameBox.setSelectedIndex(0);
                    numRefillTxt.setText("");
                }
            }
        });
    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();

        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.buyPanel = new JPanel();

        this.buyPanel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                buyPanel.setLayout(new BorderLayout());
                buyItemComponent = new BuyItemComponent();
                buyPanel.add(buyItemComponent.buyItemPane, BorderLayout.CENTER);
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
            }
        });

        this.buyPanel.setLayout(new BorderLayout());
        buyItemComponent = new BuyItemComponent();
        this.buyPanel.add(buyItemComponent.buyItemPane, BorderLayout.CENTER);
    }
}
