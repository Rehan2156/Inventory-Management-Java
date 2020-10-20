package com.inventrymanagement.screens;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OneItemToBuy {
    public JLabel label;
    public JCheckBox checkBox;
    public JSpinner spinner;
    public int id;

    public OneItemToBuy(JLabel label, JCheckBox checkBox, JSpinner spinner, int id) {
        this.label = label;
        this.checkBox = checkBox;
        this.spinner = spinner;
        this.id = id;

        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                spinner.setEnabled(checkBox.isSelected());
            }
        });

    }

    public void setToDefault() {
        this.checkBox.setSelected(false);
        this.spinner.setValue(1);
        this.spinner.setEnabled(false);
    }
}
