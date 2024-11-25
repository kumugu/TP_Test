package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductViewPanel extends JPanel {
    private DefaultTableModel productTableModel;

    public ProductViewPanel() {
        setLayout(new BorderLayout());

        // 상단 검색 패널
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");
        JButton viewAllButton = new JButton("전체 조회");

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(viewAllButton);

        // 상품 테이블 생성
        productTableModel = new DefaultTableModel(new String[]{"ID", "상품명", "카테고리", "가격", "상태"}, 0);
        JTable productTable = new JTable(productTableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        // 하단 버튼 패널
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton editButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");

        actionPanel.add(editButton);
        actionPanel.add(deleteButton);

        // 패널 구성
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        // 이벤트 핸들러
        viewAllButton.addActionListener(e -> loadAllProducts());
        searchButton.addActionListener(e -> searchProducts(searchField.getText()));
        editButton.addActionListener(e -> editSelectedProduct(productTable));
        deleteButton.addActionListener(e -> deleteSelectedProduct(productTable));

        // 초기 데이터 로드
        loadAllProducts();
    }

    // 테이블을 읽기전용으로
    private JPanel createProductViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 상단 검색 패널
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");
        JButton viewAllButton = new JButton("전체 조회");

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(viewAllButton);

        // 상품 테이블 생성
        productTableModel = new DefaultTableModel(new String[]{"ID", "상품명", "카테고리", "가격", "상태"}, 0);
        JTable productTable = new JTable(productTableModel);

        // 여기에서 읽기 전용 설정
        productTable.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(productTable);

        // 하단 버튼 패널
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton editButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");

        actionPanel.add(editButton);
        actionPanel.add(deleteButton);

        // 패널 구성
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        // 이벤트 핸들러
        viewAllButton.addActionListener(e -> loadAllProducts());
        searchButton.addActionListener(e -> searchProducts(searchField.getText()));
        editButton.addActionListener(e -> editSelectedProduct(productTable));
        deleteButton.addActionListener(e -> deleteSelectedProduct(productTable));

        // 초기 데이터 로드
        loadAllProducts();

        return panel;
    }



    private void loadAllProducts() {
        productTableModel.setRowCount(0); // 기존 데이터 초기화
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PRODUCTS ORDER BY ID ASC");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productTableModel.addRow(new Object[]{
                        String.valueOf(rs.getInt("ID")), // String으로 변환하여 테이블에 추가
                        rs.getString("NAME"),
                        rs.getString("CATEGORY"),
                        rs.getDouble("PRICE"),
                        rs.getString("STATUS")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  DB에서 보유 재료 가져오기
    private List<String> getIngredientNames() {
        List<String> ingredientNames = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT NAME FROM INGREDIENTS");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ingredientNames.add(rs.getString("NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ingredientNames;
    }


    private void searchProducts(String keyword) {
        productTableModel.setRowCount(0); // 기존 데이터 초기화
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PRODUCTS WHERE NAME LIKE ?")) {
            stmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productTableModel.addRow(new Object[]{
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getString("CATEGORY"),
                            rs.getDouble("PRICE"),
                            rs.getString("STATUS")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editSelectedProduct(JTable productTable) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "수정할 상품을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 테이블에서 상품 정보 가져오기
        int id = Integer.parseInt((String) productTableModel.getValueAt(selectedRow, 0));
        String currentName = (String) productTableModel.getValueAt(selectedRow, 1);
        String currentCategory = (String) productTableModel.getValueAt(selectedRow, 2);
        double currentPrice = (double) productTableModel.getValueAt(selectedRow, 3);

        // 수정 다이얼로그 생성
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "상품 수정", true);
        dialog.setSize(800, 600); // 창 크기 넓힘
        dialog.setLayout(new BorderLayout());

        // 상단 패널: 상품 정보
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 컴포넌트 여백 설정

        // 상품명 입력
        JLabel nameLabel = new JLabel("상품명:");
        JTextField nameField = new JTextField(currentName, 20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        topPanel.add(nameField, gbc);

        // 카테고리 선택
        JLabel categoryLabel = new JLabel("카테고리:");
        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"햄버거", "음료", "사이드"});
        categoryComboBox.setSelectedItem(currentCategory);

        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        topPanel.add(categoryComboBox, gbc);

        // 가격 입력
        JLabel priceLabel = new JLabel("가격:");
        JTextField priceField = new JTextField(String.valueOf(currentPrice), 10);

        gbc.gridx = 0;
        gbc.gridy = 2;
        topPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        topPanel.add(priceField, gbc);

        // 중앙 패널: 재료 테이블
        JPanel centerPanel = new JPanel(new BorderLayout());
        DefaultTableModel ingredientTableModel = new DefaultTableModel(new String[]{"재료명", "수량"}, 0);
        JTable ingredientTable = new JTable(ingredientTableModel);
        JScrollPane ingredientScrollPane = new JScrollPane(ingredientTable);
        ingredientScrollPane.setPreferredSize(new Dimension(600, 200));
        loadProductIngredients(id, ingredientTableModel); // 재료 로드

        centerPanel.add(ingredientScrollPane, BorderLayout.CENTER);

        // 하단 패널: 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addIngredientButton = new JButton("재료 추가");
        JButton deleteIngredientButton = new JButton("재료 삭제");
        JButton editIngredientButton = new JButton("재료 수정");
        JButton saveButton = new JButton("저장");

        bottomPanel.add(addIngredientButton);
        bottomPanel.add(deleteIngredientButton);
        bottomPanel.add(editIngredientButton);
        bottomPanel.add(saveButton);

        // 버튼 이벤트 핸들러
        addIngredientButton.addActionListener(e -> addIngredientToTable(ingredientTableModel));
        deleteIngredientButton.addActionListener(e -> deleteIngredientFromTable(ingredientTable));
        editIngredientButton.addActionListener(e -> editIngredientInTable(ingredientTable));
        saveButton.addActionListener(e -> {
            updateProduct(id, nameField.getText(), (String) categoryComboBox.getSelectedItem(),
                    Double.parseDouble(priceField.getText()), ingredientTableModel);
            dialog.dispose();
        });

        // 다이얼로그 구성
        dialog.add(topPanel, BorderLayout.NORTH);
        dialog.add(centerPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        // 다이얼로그 중앙 배치
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }





    // 재료 로드 메서드
    private void loadProductIngredients(int productId, DefaultTableModel ingredientTableModel) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT i.NAME, pi.QUANTITY " +
                             "FROM PRODUCT_INGREDIENTS pi " +
                             "JOIN INGREDIENTS i ON pi.INGREDIENT_ID = i.ID " +
                             "WHERE pi.PRODUCT_ID = ?")) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ingredientTableModel.addRow(new Object[]{
                            rs.getString("NAME"),
                            rs.getInt("QUANTITY")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 재료 추가 메서드
    private void addIngredientToTable(DefaultTableModel ingredientTableModel) {
        List<String> ingredientNames = getIngredientNames(); // DB에서 재료 목록 가져오기
        if (ingredientNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "보유한 재료가 없습니다. 재료를 먼저 등록하세요.", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 다이얼로그 생성
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "재료 추가", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 재료 선택 콤보박스
        JLabel ingredientLabel = new JLabel("재료:");
        JComboBox<String> ingredientComboBox = new JComboBox<>(ingredientNames.toArray(new String[0]));

        // 수량 입력
        JLabel quantityLabel = new JLabel("수량:");
        JTextField quantityField = new JTextField(10);

        // 확인 버튼
        JButton confirmButton = new JButton("확인");

        // 레이아웃 구성
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(ingredientLabel, gbc);
        gbc.gridx = 1;
        dialog.add(ingredientComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(quantityLabel, gbc);
        gbc.gridx = 1;
        dialog.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(confirmButton, gbc);

        // 확인 버튼 이벤트 핸들러
        confirmButton.addActionListener(e -> {
            String selectedIngredient = (String) ingredientComboBox.getSelectedItem();
            String quantityText = quantityField.getText();

            try {
                int quantity = Integer.parseInt(quantityText);
                ingredientTableModel.addRow(new Object[]{selectedIngredient, quantity});
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "수량은 숫자로 입력해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // 재료 수정 기능 추가
    private void editIngredientInTable(JTable ingredientTable) {
        int selectedRow = ingredientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "수정할 재료를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 기존 데이터 가져오기
        String currentIngredient = (String) ingredientTable.getValueAt(selectedRow, 0);
        int currentQuantity = (int) ingredientTable.getValueAt(selectedRow, 1);

        List<String> ingredientNames = getIngredientNames(); // DB에서 재료 목록 가져오기
        if (ingredientNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "보유한 재료가 없습니다. 재료를 먼저 등록하세요.", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 다이얼로그 생성
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "재료 수정", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 재료 선택 콤보박스
        JLabel ingredientLabel = new JLabel("재료:");
        JComboBox<String> ingredientComboBox = new JComboBox<>(ingredientNames.toArray(new String[0]));
        ingredientComboBox.setSelectedItem(currentIngredient);

        // 수량 입력
        JLabel quantityLabel = new JLabel("수량:");
        JTextField quantityField = new JTextField(String.valueOf(currentQuantity), 10);

        // 확인 버튼
        JButton confirmButton = new JButton("확인");

        // 레이아웃 구성
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(ingredientLabel, gbc);
        gbc.gridx = 1;
        dialog.add(ingredientComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(quantityLabel, gbc);
        gbc.gridx = 1;
        dialog.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(confirmButton, gbc);

        // 확인 버튼 이벤트 핸들러
        confirmButton.addActionListener(e -> {
            String selectedIngredient = (String) ingredientComboBox.getSelectedItem();
            String quantityText = quantityField.getText();

            try {
                int quantity = Integer.parseInt(quantityText);
                ingredientTable.setValueAt(selectedIngredient, selectedRow, 0); // 재료명 수정
                ingredientTable.setValueAt(quantity, selectedRow, 1); // 수량 수정
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "수량은 숫자로 입력해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }



    // 재료 삭제 메서드
    private void deleteIngredientFromTable(JTable ingredientTable) {
        int selectedRow = ingredientTable.getSelectedRow();
        if (selectedRow != -1) {
            ((DefaultTableModel) ingredientTable.getModel()).removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 재료를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 상품 및 재료 저장
    private void updateProduct(int productId, String name, String category, double price,
                               DefaultTableModel ingredientTableModel) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 상품 정보 업데이트
            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE PRODUCTS SET NAME = ?, CATEGORY = ?, PRICE = ? WHERE ID = ?")) {
                stmt.setString(1, name);
                stmt.setString(2, category);
                stmt.setDouble(3, price);
                stmt.setInt(4, productId);
                stmt.executeUpdate();
            }

            // 기존 재료 삭제
            try (PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM PRODUCT_INGREDIENTS WHERE PRODUCT_ID = ?")) {
                stmt.setInt(1, productId);
                stmt.executeUpdate();
            }

            // 새로운 재료 추가
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO PRODUCT_INGREDIENTS (PRODUCT_ID, INGREDIENT_ID, QUANTITY) " +
                            "VALUES (?, (SELECT ID FROM INGREDIENTS WHERE NAME = ?), ?)")) {
                for (int i = 0; i < ingredientTableModel.getRowCount(); i++) {
                    stmt.setInt(1, productId);
                    stmt.setString(2, (String) ingredientTableModel.getValueAt(i, 0));
                    stmt.setInt(3, (int) ingredientTableModel.getValueAt(i, 1));
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "상품이 수정되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            loadAllProducts();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "상품 수정에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteSelectedProduct(JTable productTable) {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 상품을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 수정: String으로 읽은 데이터를 Integer로 변환
        String idString = (String) productTableModel.getValueAt(selectedRow, 0);
        int id = Integer.parseInt(idString);

        int confirm = JOptionPane.showConfirmDialog(this,
                "선택한 상품을 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM PRODUCTS WHERE ID = ?")) {

                stmt.setInt(1, id);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "상품이 삭제되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                loadAllProducts();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "상품 삭제에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
