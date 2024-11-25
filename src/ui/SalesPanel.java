package ui;

import javax.swing.*;
import java.awt.*;

public class SalesPanel extends JPanel {
    public SalesPanel() {
        setLayout(new BorderLayout());

        // 상단: 상품 선택
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel productLabel = new JLabel("상품:");
        JComboBox<String> productComboBox = new JComboBox<>(new String[]{"햄버거", "음료", "사이드"});
        topPanel.add(productLabel);
        topPanel.add(productComboBox);

        // 중앙: 수량 입력
        JPanel centerPanel = new JPanel(new FlowLayout());
        JLabel quantityLabel = new JLabel("수량:");
        JTextField quantityField = new JTextField(10);
        JButton sellButton = new JButton("판매");
        centerPanel.add(quantityLabel);
        centerPanel.add(quantityField);
        centerPanel.add(sellButton);

        // 하단: 판매 내역
        JTable salesTable = new JTable(new String[][]{}, new String[]{"상품명", "수량", "총액"});
        JScrollPane scrollPane = new JScrollPane(salesTable);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // 판매 버튼 이벤트
        sellButton.addActionListener(e -> {
            // 판매 내역 등록 로직 추가
        });
    }
}
