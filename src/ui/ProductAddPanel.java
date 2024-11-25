package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductAddPanel extends JPanel {
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> ingredientComboBox;
    private DefaultTableModel ingredientTableModel;

    public ProductAddPanel() {
        setPreferredSize(new Dimension(1000, 800)); // 패널 크기 고정
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 여백 설정
        gbc.fill = GridBagConstraints.HORIZONTAL; // 수평 방향으로 채우기

        // 상품명 입력
        JLabel nameLabel = new JLabel("상품명:");
        JTextField nameField = new JTextField(20); // 고정 크기 설정

        // 카테고리 선택
        JLabel categoryLabel = new JLabel("카테고리:");
        categoryComboBox = new JComboBox<>();
        loadCategories(); // 카테고리 로드

        // 가격 입력
        JLabel priceLabel = new JLabel("가격:");
        JTextField priceField = new JTextField(20); // 고정 크기 설정

        // 재료 추가 관련 컴포넌트
        JLabel ingredientLabel = new JLabel("재료:");
        ingredientComboBox = new JComboBox<>();
        loadIngredients(); // 재료 목록 로드
        JTextField quantityField = new JTextField(5); // 고정 크기 설정
        JButton addIngredientButton = new JButton("재료 추가");

        // 재료 테이블
        ingredientTableModel = new DefaultTableModel(new String[]{"재료명", "수량"}, 0);
        JTable ingredientTable = new JTable(ingredientTableModel);
        // 테이블 크기 조정
        ingredientTable.setPreferredScrollableViewportSize(new Dimension(400, 150)); // 너비 400, 높이 150으로 설정
        // 스크롤 추가
        JScrollPane ingredientScrollPane = new JScrollPane(ingredientTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 수직 스크롤 활성화, 수평 스크롤 비활성화
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH; // 테이블이 화면을 채울 수 있도록 설정
        add(ingredientScrollPane, gbc);




        JButton deleteIngredientButton = new JButton("재료 삭제");

        // 상품 등록 버튼
        JButton addButton = new JButton("상품 등록");

        // 레이아웃 구성
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(categoryLabel, gbc);
        gbc.gridx = 1;
        add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(priceLabel, gbc);
        gbc.gridx = 1;
        add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(ingredientLabel, gbc);
        gbc.gridx = 1;
        add(ingredientComboBox, gbc);

        gbc.gridx = 2;
        add(new JLabel("수량:"), gbc);
        gbc.gridx = 3;
        add(quantityField, gbc);
        gbc.gridx = 4;
        add(addIngredientButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH; // 테이블이 화면을 채우도록 설정
        add(ingredientScrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE; // 기본 설정으로 복원
        add(deleteIngredientButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        add(addButton, gbc);

        // 이벤트 핸들러
        addIngredientButton.addActionListener(e -> addIngredientToTable(quantityField));
        deleteIngredientButton.addActionListener(e -> deleteSelectedIngredient(ingredientTable));
        addButton.addActionListener(e -> registerProduct(nameField, categoryComboBox, priceField));

        // 초기 데이터 로드
        loadCategories();
        loadIngredients();
    }


    private void loadCategories() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT NAME FROM CATEGORIES WHERE TYPE = '상품'");
             ResultSet rs = stmt.executeQuery()) {

            categoryComboBox.removeAllItems();
            boolean hasData = false;

            while (rs.next()) {
                categoryComboBox.addItem(rs.getString("NAME"));
                hasData = true;
            }

            if (!hasData) { // 데이터가 없을 경우 기본값 추가
                categoryComboBox.addItem("햄버거");
                categoryComboBox.addItem("음료");
                categoryComboBox.addItem("사이드");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "카테고리를 불러오는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);

            // 오류 발생 시 기본값 추가
            categoryComboBox.addItem("햄버거");
            categoryComboBox.addItem("음료");
            categoryComboBox.addItem("사이드");
        }
    }

    // 텍스트 필드와 레이아웃 조정
    private void setupLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("상품명:");
        JTextField nameField = new JTextField(20); // 고정 크기 설정

        JLabel categoryLabel = new JLabel("카테고리:");
        JComboBox<String> categoryComboBox = new JComboBox<>();
        loadCategories();

        JLabel priceLabel = new JLabel("가격:");
        JTextField priceField = new JTextField(20); // 고정 크기 설정

        // 레이아웃 추가
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(categoryLabel, gbc);

        gbc.gridx = 1;
        add(categoryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(priceLabel, gbc);

        gbc.gridx = 1;
        add(priceField, gbc);
    }



    private void loadIngredients() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT NAME FROM INGREDIENTS");
             ResultSet rs = stmt.executeQuery()) {

            ingredientComboBox.removeAllItems();
            while (rs.next()) {
                ingredientComboBox.addItem(rs.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "재료 목록을 불러오는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addIngredientToTable(JTextField quantityField) {
        String ingredientName = (String) ingredientComboBox.getSelectedItem();
        String quantityText = quantityField.getText();

        if (ingredientName == null || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "재료와 수량을 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            ingredientTableModel.addRow(new Object[]{ingredientName, quantity});
            quantityField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "수량은 숫자로 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedIngredient(JTable ingredientTable) {
        int selectedRow = ingredientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 재료를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        } else {
            ingredientTableModel.removeRow(selectedRow);
        }
    }

    private void registerProduct(JTextField nameField, JComboBox<String> categoryComboBox, JTextField priceField) {
        String name = nameField.getText();
        String category = (String) categoryComboBox.getSelectedItem();
        String price = priceField.getText();

        if (name.isEmpty() || category == null || price.isEmpty() || ingredientTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "모든 필드를 입력하고 재료를 추가하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // PRODUCTS 테이블에 상품 삽입
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO PRODUCTS (ID, NAME, CATEGORY, PRICE) VALUES (PRODUCT_SEQ.NEXTVAL, ?, ?, ?)")) {
                stmt.setString(1, name);
                stmt.setString(2, category);
                stmt.setDouble(3, Double.parseDouble(price));
                stmt.executeUpdate();
            }

            // PRODUCT_INGREDIENTS 테이블에 재료 정보 삽입
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO PRODUCT_INGREDIENTS (PRODUCT_ID, INGREDIENT_ID, QUANTITY) VALUES (PRODUCT_SEQ.CURRVAL, (SELECT ID FROM INGREDIENTS WHERE NAME = ?), ?)")) {
                for (int i = 0; i < ingredientTableModel.getRowCount(); i++) {
                    stmt.setString(1, (String) ingredientTableModel.getValueAt(i, 0)); // 재료명
                    stmt.setInt(2, (int) ingredientTableModel.getValueAt(i, 1)); // 수량
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "상품이 등록되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);

            // 필드 초기화
            nameField.setText("");
            priceField.setText("");
            ingredientTableModel.setRowCount(0); // 테이블 초기화
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "상품 등록에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
