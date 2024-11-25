package ui;

import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JPanel {
    public ReportPanel() {
        setLayout(new BorderLayout());

        JTable reportTable = new JTable(new String[][]{}, new String[]{"항목", "값"});
        JScrollPane scrollPane = new JScrollPane(reportTable);

        add(new JLabel("보고서 화면", JLabel.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
