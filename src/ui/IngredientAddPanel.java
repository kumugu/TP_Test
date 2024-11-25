package ui;

import javax.swing.*;
import java.awt.*;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class IngredientAddPanel extends JPanel {
    public IngredientAddPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 입력 필드 생성
        JLabel nameLabel = new JLabel("재료명:");
        JTextField nameField = new JTextField(15);

        JLabel categoryLabel = new JLabel("카테고리:");
        JTextField categoryField = new JTextField(15);

        JLabel quantityLabel = new JLabel("수량:");
        JTextField quantityField = new JTextField(15);

        JLabel unitPriceLabel = new JLabel("단가:");
        JTextField unitPriceField = new JTextField(15);

        JButton addButton = new JButton("추가");

        // 레이아웃 구성
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(categoryLabel, gbc);
        gbc.gridx = 1;
        add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(quantityLabel, gbc);
        gbc.gridx = 1;
        add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(unitPriceLabel, gbc);
        gbc.gridx = 1;
        add(unitPriceField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        add(addButton, gbc);

        // 추가 버튼 이벤트
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String category = categoryField.getText();
            String quantity = quantityField.getText();
            String unitPrice = unitPriceField.getText();

            if (name.isEmpty() || category.isEmpty() || quantity.isEmpty() || unitPrice.isEmpty()) {
                JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            } else {
                try (Connection conn = DBConnection.getConnection()) {

                    // 카테고리 중복 확인 및 삽입
                    try (PreparedStatement checkCategoryStmt = conn.prepareStatement(
                            "SELECT COUNT(*) FROM CATEGORIES WHERE NAME = ? AND TYPE = '재료'")) {
                        checkCategoryStmt.setString(1, category);

                        try (ResultSet rs = checkCategoryStmt.executeQuery()) {
                            if (rs.next() && rs.getInt(1) == 0) { // 카테고리가 없으면 삽입
                                try (PreparedStatement insertCategoryStmt = conn.prepareStatement(
                                        "INSERT INTO CATEGORIES (ID, NAME, TYPE) VALUES (CATEGORY_SEQ.NEXTVAL, ?, '재료')")) {
                                    insertCategoryStmt.setString(1, category);
                                    insertCategoryStmt.executeUpdate();
                                }
                            }
                        }
                    }

                    // 재료 정보 삽입
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO INGREDIENTS (ID, NAME, CATEGORY, STOCK, UNIT_PRICE) VALUES (INGREDIENT_SEQ.NEXTVAL, ?, ?, ?, ?)")) {
                        insertStmt.setString(1, name);
                        insertStmt.setString(2, category);
                        insertStmt.setInt(3, Integer.parseInt(quantity));
                        insertStmt.setInt(4, Integer.parseInt(unitPrice));

                        insertStmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "재료가 추가되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);

                        // 필드 초기화
                        nameField.setText("");
                        categoryField.setText("");
                        quantityField.setText("");
                        unitPriceField.setText("");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "재료 추가에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



    }
}
