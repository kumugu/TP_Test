package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.DBConnection;


public class InventoryViewPanel extends JPanel {
    private DefaultTableModel inventoryTableModel;

    public InventoryViewPanel() {
        setLayout(new BorderLayout());

        // 상단 검색 패널
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");
        JButton viewAllButton = new JButton("전체 조회");

        searchPanel.add(new JLabel("재료 검색:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(viewAllButton);

        // 재고 테이블 생성
        inventoryTableModel = new DefaultTableModel(new String[]{"ID", "재료명", "카테고리", "수량", "단가"}, 0);
        JTable inventoryTable = new JTable(inventoryTableModel);
        JScrollPane scrollPane = new JScrollPane(inventoryTable);

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

        // 버튼 이벤트 핸들러
        viewAllButton.addActionListener(e -> loadAllInventory());
        searchButton.addActionListener(e -> searchInventory(searchField.getText()));
        editButton.addActionListener(e -> editSelectedInventory(inventoryTable));
        deleteButton.addActionListener(e -> deleteSelectedInventory(inventoryTable));

        // 초기 데이터 로드
        loadAllInventory();
    }

    // 재고 데이터 로드 (DB 연결 코드 추가 필요)
    private void loadAllInventory() {
        inventoryTableModel.setRowCount(0); // 기존 데이터 초기화
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM INGREDIENTS ORDER BY ID");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                inventoryTableModel.addRow(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getString("CATEGORY"),
                        rs.getInt("STOCK"),
                        rs.getInt("UNIT_PRICE")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 검색
    private void searchInventory(String keyword) {
        inventoryTableModel.setRowCount(0); // 기존 데이터 초기화
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM INGREDIENTS WHERE NAME LIKE ? ORDER BY ID")) {
            stmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    inventoryTableModel.addRow(new Object[]{
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getString("CATEGORY"),
                            rs.getInt("STOCK"),
                            rs.getInt("UNIT_PRICE")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 선택된 재고 수정
    private void editSelectedInventory(JTable inventoryTable) {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "수정할 재고를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) inventoryTableModel.getValueAt(selectedRow, 0);
        String currentName = (String) inventoryTableModel.getValueAt(selectedRow, 1);
        String currentCategory = (String) inventoryTableModel.getValueAt(selectedRow, 2);
        int currentQuantity = (int) inventoryTableModel.getValueAt(selectedRow, 3);
        int currentUnitPrice = (int) inventoryTableModel.getValueAt(selectedRow, 4);

        JTextField nameField = new JTextField(currentName);
        JTextField categoryField = new JTextField(currentCategory);
        JTextField quantityField = new JTextField(String.valueOf(currentQuantity));
        JTextField unitPriceField = new JTextField(String.valueOf(currentUnitPrice));

        int result = JOptionPane.showConfirmDialog(this,
                new Object[]{"재료명:", nameField, "카테고리:", categoryField, "수량:", quantityField, "단가:", unitPriceField},
                "재고 수정", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE INGREDIENTS SET NAME = ?, CATEGORY = ?, STOCK = ?, UNIT_PRICE = ? WHERE ID = ?")) {

                stmt.setString(1, nameField.getText());
                stmt.setString(2, categoryField.getText());
                stmt.setInt(3, Integer.parseInt(quantityField.getText()));
                stmt.setInt(4, Integer.parseInt(unitPriceField.getText()));
                stmt.setInt(5, id);

                stmt.executeUpdate();
                loadAllInventory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 선택된 재고 삭제
    private void deleteSelectedInventory(JTable inventoryTable) {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 재고를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) inventoryTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "정말로 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM INGREDIENTS WHERE ID = ?")) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                loadAllInventory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
