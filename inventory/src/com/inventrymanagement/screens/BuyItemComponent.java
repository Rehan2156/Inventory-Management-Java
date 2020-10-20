package com.inventrymanagement.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class BuyItemComponent {
    public JPanel buyItemPane;
    public ArrayList<OneItemToBuy> buyItems;
    public JButton buyingButton;

    public BuyItemComponent() {
        this.buyItemPane = new JPanel();
        this.buyItems = new ArrayList<>();
        JPanel innerPanel = new JPanel();
        BorderLayout borderLayout = new BorderLayout();
        GridLayout gridLayout = new GridLayout();

        try {
//            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306","laukik","laukik20");
            Statement smt = con.createStatement();
            smt.execute("USE Inventry;");
            ResultSet rs = smt.executeQuery("SELECT * FROM Items;");

            while(rs.next()) {
                int id = rs.getInt(1);
                String label = rs.getString(2);
                boolean check = false;
                int spinnerNum = rs.getInt(8);
                System.out.println(id + " " + label + " " + spinnerNum);
                JLabel tempL = new JLabel(label);

                SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel();
                spinnerNumberModel.setMaximum(spinnerNum);

                spinnerNumberModel.setMinimum(1);
                spinnerNumberModel.setStepSize(1);
                spinnerNumberModel.setValue(1);

                JSpinner tempSpi = new JSpinner();
                tempSpi.setModel(spinnerNumberModel);

                JCheckBox checkBox = new JCheckBox("Buy");
                checkBox.setSelected(check);
                tempSpi.setEnabled(check);

                tempL.setPreferredSize(new Dimension(-1, 100));
                tempL.setMinimumSize(new Dimension(-1, 70));
                tempL.setMaximumSize(new Dimension(-1, 100));

                checkBox.setPreferredSize(new Dimension(-1, 100));
                checkBox.setMinimumSize(new Dimension(-1, 70));
                checkBox.setMaximumSize(new Dimension(-1, 100));

                tempSpi.setPreferredSize(new Dimension(-1, 100));
                tempSpi.setMinimumSize(new Dimension(-1, 70));
                tempSpi.setMaximumSize(new Dimension(-1, 100));

                buyItems.add(new OneItemToBuy(tempL, checkBox, tempSpi, id));
            }

            if(buyItems.size() > 0) {
                gridLayout.setRows(buyItems.size());
                gridLayout.setColumns(3);
                innerPanel.setLayout(gridLayout);

                for(int i=0; i < buyItems.size(); i++) {
                    System.out.println(i + " " + buyItems.get(i).label);
                    innerPanel.add(buyItems.get(i).label, 0 + (3*i));
                    innerPanel.add(buyItems.get(i).checkBox, 1 + (3*i));
                    innerPanel.add(buyItems.get(i).spinner, 2 + (3*i));
                }
            }

            con.close();
        } catch (SQLException err) {
            System.out.println("If the app is running for the 1st time you can have some error but is ok! \uD83D\uDE01");
        }

        buyingButton = new JButton("Buy");
        buyingButton.setPreferredSize(new Dimension(-1, 100));
        buyingButton.setMaximumSize(new Dimension(-1, 100));
        buyingButton.setMinimumSize(new Dimension(-1, 70));

        buyingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buyItems.forEach(e -> {
                    if(e.checkBox.isSelected()) {
                        int num = Integer.parseInt(e.spinner.getValue().toString());
                        System.out.print("Id -" + e.id + " Num - " + num);
                        int maxNum = 0;
                        try {
//            Class.forName("com.mysql.jdbc.Driver");
                            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306","laukik","laukik20");
                            Statement smt = con.createStatement();
                            smt.execute("USE Inventry;");
                            PreparedStatement prtSmt = con.prepareStatement("SELECT num FROM Items where id = ?;");
                            prtSmt.setInt(1, e.id);
                            ResultSet rs = prtSmt.executeQuery();
                            while(rs.next()) {
                                maxNum = rs.getInt(1);
                            }

                            System.out.println("MaxNum = " + maxNum + " num = " + num);

                            if(num == maxNum) {
                                prtSmt = con.prepareStatement("DELETE FROM Items where id = ?;");
                                prtSmt.setInt(1, e.id);
                                prtSmt.executeUpdate();
                                JOptionPane.showMessageDialog(buyItemPane, "Your Sold Out " + e.label.getText() + " so we deleted it from table");
                            } else {
                                prtSmt = con.prepareStatement("UPDATE Items SET num = num-? Where id = ?;");
                                prtSmt.setInt(1, num);
                                prtSmt.setInt(2, e.id);
                                prtSmt.executeUpdate();
                                JOptionPane.showMessageDialog(buyItemPane, "We Updated data about " + e.label.getText() + "!!");
                            }

                            con.close();
                        } catch (SQLException err) {
                            System.out.println("Something is Wrong table can't be updated!");
                        } finally {
                            e.setToDefault();
                        }
                    }
                });
            }
        });

        this.buyItemPane.setLayout(borderLayout);
        this.buyItemPane.add(innerPanel, BorderLayout.CENTER);
        this.buyItemPane.add(buyingButton, BorderLayout.SOUTH);
        innerPanel.setVisible(true);
        this.buyItemPane.setVisible(true);
    }

}
