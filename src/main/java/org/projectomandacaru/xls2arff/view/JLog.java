package org.projectomandacaru.xls2arff.view;

import org.projectomandacaru.xls2arff.view.utils.TextAreaOutputStream;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class JLog extends JFrame {
    public JLog() {
        this.add(new JLabel("LOG"), BorderLayout.NORTH);

        JTextArea ta = new   JTextArea();
        TextAreaOutputStream taos = new TextAreaOutputStream(ta, 60);
        PrintStream ps = new PrintStream(taos);
        System.setOut(ps);
        System.setErr(ps);
        this.add(new JScrollPane(ta));

        this.pack();
        this.setVisible(true);
        this.setSize(800, 600);
    }

}
