package ui;

import javax.swing.*;
import java.awt.*;

public class LobbyPanel extends JPanel {
    public LobbyPanel(MainFrame frame) {
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        JButton menuManageButton = new JButton("상품 관리 🍔");
        JButton inventoryManageButton = new JButton("재고 관리 📦");
        JButton salesButton = new JButton("판매 💰");
        JButton reportButton = new JButton("보고서 📊");

        centerPanel.add(menuManageButton);
        centerPanel.add(inventoryManageButton);
        centerPanel.add(salesButton);
        centerPanel.add(reportButton);

        add(centerPanel, BorderLayout.CENTER);

        // 버튼 클릭 시 화면 전환
        menuManageButton.addActionListener(e -> frame.showPanel(MainFrame.PRODUCT_PANEL));
        inventoryManageButton.addActionListener(e -> frame.showPanel(MainFrame.INVENTORY_PANEL));
        salesButton.addActionListener(e -> frame.showPanel(MainFrame.SALES_PANEL));
        reportButton.addActionListener(e -> frame.showPanel(MainFrame.REPORT_PANEL));
    }
}
