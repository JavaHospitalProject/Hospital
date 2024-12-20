package com.hospital.gui.components;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SearchPanel extends JPanel {
    private final JTextField searchField;
    
    public SearchPanel(String labelText, DocumentListener searchListener) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new JLabel(labelText));
        
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(searchListener);
        add(searchField);
    }
    
    public String getSearchText() {
        return searchField.getText();
    }
}