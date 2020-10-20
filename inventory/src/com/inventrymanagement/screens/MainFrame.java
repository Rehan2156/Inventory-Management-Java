package com.inventrymanagement.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JPanel loginPanel;
    private JButton goAHeadButton;
    private JTextField userName;
    private JPasswordField userPswd;

    public MainFrame() throws HeadlessException {
        this.setTitle("Inventry Managemnet");

        this.setContentPane(loginPanel);
        this.setSize(new Dimension(500, 300));
//        this.pack();
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocation(new Point(750, 200));
        this.setVisible(true);

        goAHeadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                System.out.println("Name - " + userName.getText() + " password - " + userPswd.getText());
                if(userName.getText().equals("Rahul") && userPswd.getText().equals("rahul123")) {
                    JOptionPane.showMessageDialog(loginPanel, "Your in!!");
                    setSize(new Dimension(900, 800));
                    setContentPane(new MainScreen().mainScreen);
                } else {
                    JOptionPane.showMessageDialog(loginPanel, "User id and password are worng please try correct credentials ");
                }
            }
        });
    }

}
