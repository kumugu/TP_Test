package ui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class MainFrame extends JFrame {
    public static final String LOGIN_PANEL = "로그인 화면";
    public static final String LOBBY_PANEL = "로비 화면";
    public static final String PRODUCT_PANEL = "상품 관리";
    public static final String INVENTORY_PANEL = "재고 관리";
    public static final String SALES_PANEL = "판매";
    public static final String REPORT_PANEL = "보고서";

    private JPanel centerPanel; // 메인 화면 전환 패널
    private ProductAddPanel productAddPanel; // 상품 등록 패널

    public MainFrame() {
        // 기본 설정
        setTitle("매장 관리 프로그램");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // 메뉴바 생성
        createMenuBar();

        // 메인 패널 생성 및 레이아웃 설정
        centerPanel = new JPanel(new CardLayout());
        add(centerPanel, BorderLayout.CENTER);

        // 화면 추가
        initializePanels();

        // 초기 화면 설정
        showPanel(LOGIN_PANEL);

        setVisible(true);
    }
    // 상품 데이터를 로드
    private void initializePanels() {
        Map<String, List<String>> productData = loadProductData();
        centerPanel.add(new LoginPanel(this), LOGIN_PANEL);
        centerPanel.add(new LobbyPanel(this), LOBBY_PANEL);
        centerPanel.add(new ProductManagementPanel(), PRODUCT_PANEL);
        centerPanel.add(new InventoryPanel(), INVENTORY_PANEL);
        centerPanel.add(new SalesPanel(productData), SALES_PANEL);
        centerPanel.add(new ReportPanel(), REPORT_PANEL);
    }

    /**
     * 상품 데이터를 로드하는 메서드
     */
    private Map<String, List<String>> loadProductData() {
        Map<String, List<String>> productData = new HashMap<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT NAME, CATEGORY FROM PRODUCTS");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("NAME");
                String category = rs.getString("CATEGORY");

                productData.computeIfAbsent(category, k -> new ArrayList<>()).add(name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "상품 데이터를 불러오는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }

        return productData;
    }


    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // "✨홈" 메뉴 추가
        JMenu homeMenu = new JMenu("✨홈");
        homeMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showPanel(LOBBY_PANEL); // 홈 클릭 시 로비 화면으로 이동
            }
        });

        menuBar.add(homeMenu);
        setJMenuBar(menuBar);
    }

    public void showPanel(String panelName) {
        CardLayout cl = (CardLayout) centerPanel.getLayout();
        cl.show(centerPanel, panelName);

        // 상품 등록 패널로 전환될 때 재료 목록 갱신
        if (panelName.equals(PRODUCT_PANEL)) {
            if (productAddPanel != null) {
                productAddPanel.loadIngredients();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
