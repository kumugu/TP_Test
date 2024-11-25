package ui;

import javax.swing.*;
import java.awt.*;

public class ProductManagementPanel extends JPanel {
    public ProductManagementPanel() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("상품 조회", new ProductViewPanel());
        tabbedPane.addTab("상품 등록", new ProductAddPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
