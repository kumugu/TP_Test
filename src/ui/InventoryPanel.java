package ui;

import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends JPanel {
    public InventoryPanel() {
        setLayout(new BorderLayout());

        // JTabbedPane 생성
        JTabbedPane tabbedPane = new JTabbedPane();

        // 재고 조회 탭 추가
        tabbedPane.addTab("재고 조회", new InventoryViewPanel());

        // 재료 등록 탭 추가
        tabbedPane.addTab("재료 등록", new IngredientAddPanel());

        // 탭을 중앙에 배치
        add(tabbedPane, BorderLayout.CENTER);
    }
}
