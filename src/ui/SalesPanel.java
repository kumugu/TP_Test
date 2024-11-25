package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesPanel extends JPanel {
    private Map<String, JPanel> categoryPanels; // 카테고리별 상품 패널
    private DefaultTableModel orderTableModel; // 주문 테이블 모델
    private JLabel totalLabel; // 총 금액 라벨
    private double totalAmount = 0; // 총 결제 금액
    private JTable orderTable; // 주문 테이블
    private JTable stockTable; // 재고 테이블
    private StringBuilder currentQuantityInput = new StringBuilder(); // 숫자 입력 관리

    public SalesPanel(Map<String, List<String>> productData) {
        setLayout(new BorderLayout());

        JTabbedPane categoryTabs = createCategoryTabs(productData);
        add(categoryTabs, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        // 초기 데이터 로드
        updateStockTable();
    }

    /**
     * 카테고리 탭 생성
     */
    private JTabbedPane createCategoryTabs(Map<String, List<String>> productData) {
        JTabbedPane categoryTabs = new JTabbedPane();
        categoryPanels = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : productData.entrySet()) {
            String category = entry.getKey();
            List<String> products = entry.getValue();

            JPanel productButtonPanel = new JPanel(new GridLayout(0, 5, 5, 5));
            JScrollPane scrollPane = new JScrollPane(productButtonPanel);
            categoryTabs.addTab(category, scrollPane);

            for (String product : products) {
                JButton productButton = new JButton(product);
                productButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
                productButton.setPreferredSize(new Dimension(150, 40));
                productButtonPanel.add(productButton);

                productButton.addActionListener(e -> addProductToOrder(product, 1, 1000)); // 임시 가격
            }
        }

        return categoryTabs;
    }

    /**
     * 하단 패널 생성 (재고 테이블, 주문 테이블, 숫자 패드 포함)
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // 왼쪽: 재고 테이블
        stockTable = new JTable(new DefaultTableModel(new Object[]{"재료명", "재고량"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 편집 불가
            }
        });
        JScrollPane stockScrollPane = new JScrollPane(stockTable);
        stockScrollPane.setPreferredSize(new Dimension(400, 150));

        JPanel stockPanel = new JPanel();
        stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.Y_AXIS));
        stockPanel.add(new JLabel("재고 리스트", JLabel.CENTER));
        stockPanel.add(stockScrollPane);

        // 중간: 주문 테이블
        orderTableModel = new DefaultTableModel(new Object[]{"상품명", "수량", "가격"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderTable = new JTable(orderTableModel);
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderScrollPane.setPreferredSize(new Dimension(400, 150));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(new JLabel("선택한 상품", JLabel.CENTER));
        centerPanel.add(orderScrollPane);

        // 오른쪽: 숫자 패드 및 결제 버튼
        JPanel rightPanel = createNumericPad();

        bottomPanel.add(stockPanel, BorderLayout.WEST);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
     * 숫자 패드 및 결제 버튼 생성
     */
    private JPanel createNumericPad() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel numericPadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        numericPadPanel.setPreferredSize(new Dimension(200, 200));

        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.addActionListener(this::handleNumericInput);
            numericPadPanel.add(button);
        }

        JButton button0 = new JButton("0");
        button0.addActionListener(this::handleNumericInput);
        numericPadPanel.add(button0);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearOrderList());
        numericPadPanel.add(clearButton);

        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(e -> applyQuantityInput());
        numericPadPanel.add(enterButton);

        totalLabel = new JLabel("총 결제 금액: 0원", JLabel.CENTER);
        totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));

        JButton payButton = new JButton("결제");
        payButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        payButton.addActionListener(e -> processPayment());

        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(new JLabel("수량 입력", JLabel.CENTER));
        rightPanel.add(numericPadPanel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(totalLabel);
        rightPanel.add(payButton);

        return rightPanel;
    }

    private void processPayment() {
    }

    /**
     * 주문 테이블에 상품 추가
     */
    private void addProductToOrder(String product, int quantity, int price) {
        for (int i = 0; i < orderTableModel.getRowCount(); i++) {
            if (orderTableModel.getValueAt(i, 0).equals(product)) {
                int currentQuantity = (int) orderTableModel.getValueAt(i, 1);
                orderTableModel.setValueAt(currentQuantity + quantity, i, 1);
                orderTableModel.setValueAt((currentQuantity + quantity) * price, i, 2);
                updateTotal();
                return;
            }
        }

        orderTableModel.addRow(new Object[]{product, quantity, quantity * price});
        updateTotal();
    }

    /**
     * 숫자 버튼 입력 처리
     */
    private void handleNumericInput(ActionEvent e) {
        currentQuantityInput.append(((JButton) e.getSource()).getText());
    }

    /**
     * 엔터 버튼으로 수량 변경 적용
     */
    private void applyQuantityInput() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1 || currentQuantityInput.length() == 0) {
            JOptionPane.showMessageDialog(this, "수량을 수정할 상품을 선택하거나 입력 값을 확인하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int newQuantity = Integer.parseInt(currentQuantityInput.toString());
            int pricePerUnit = (int) orderTableModel.getValueAt(selectedRow, 2) / (int) orderTableModel.getValueAt(selectedRow, 1);
            orderTableModel.setValueAt(newQuantity, selectedRow, 1);
            orderTableModel.setValueAt(newQuantity * pricePerUnit, selectedRow, 2);
            currentQuantityInput.setLength(0); // 입력 초기화
            updateTotal();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "유효한 숫자를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 총 결제 금액 업데이트
     */
    private void updateTotal() {
        totalAmount = 0;
        for (int i = 0; i < orderTableModel.getRowCount(); i++) {
            totalAmount += (int) orderTableModel.getValueAt(i, 2);
        }
        totalLabel.setText("총 결제 금액: " + totalAmount + "원");
    }

    /**
     * 주문 초기화
     */
    private void clearOrderList() {
        orderTableModel.setRowCount(0);
        totalAmount = 0;
        totalLabel.setText("총 결제 금액: 0원");
    }

    /**
     * 재고 테이블 갱신
     */
    private void updateStockTable() {
        // stockTable이 제대로 초기화되었는지 확인
        if (stockTable == null) {
            JOptionPane.showMessageDialog(this, "재고 테이블이 초기화되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel stockTableModel = (DefaultTableModel) stockTable.getModel();
        stockTableModel.setRowCount(0); // 기존 데이터 초기화

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT NAME, STOCK FROM INGREDIENTS");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                stockTableModel.addRow(new Object[]{rs.getString("NAME"), rs.getInt("STOCK")});
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "재고 데이터를 불러오는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
