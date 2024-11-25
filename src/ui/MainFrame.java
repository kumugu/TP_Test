package ui;

import db.DBConnection;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.sql.*;
import java.util.*;
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
    public static final String SIGNUP_PANEL =  "회원 가입";

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
        centerPanel.add(new SignUpPanel(this), SIGNUP_PANEL);
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

        // 🏠 홈 메뉴
        JMenu homeMenu = new JMenu("🏠 홈");
        homeMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                showPanel(LOBBY_PANEL); // 로비 화면으로 이동
            }
            @Override
            public void menuDeselected(MenuEvent e) { }
            @Override
            public void menuCanceled(MenuEvent e) { }
        });

        // 파일 메뉴
        JMenu fileMenu = new JMenu("파일");
        JMenuItem printItem = new JMenuItem("인쇄");
        JMenuItem logoutItem = new JMenuItem("로그아웃");
        JMenuItem exitItem = new JMenuItem("종료");

        printItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "인쇄 기능은 구현 예정입니다."));
        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                showPanel(LOGIN_PANEL); // 로그인 화면으로 이동
            }
        });
        exitItem.addActionListener(e -> System.exit(0)); // 프로그램 종료

        fileMenu.add(printItem);
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // 업무 메뉴
        JMenu workMenu = new JMenu("업무");
        JMenuItem menuManageItem = new JMenuItem("메뉴 관리");
        JMenuItem inventoryManageItem = new JMenuItem("재고 관리");
        JMenuItem salesMenuItem = new JMenuItem("판매");
        JMenuItem reportItem = new JMenuItem("보고서");

        menuManageItem.addActionListener(e -> showPanel(PRODUCT_PANEL)); // 상품 관리 화면
        inventoryManageItem.addActionListener(e -> showPanel(INVENTORY_PANEL)); // 재고 관리 화면
        salesMenuItem.addActionListener(e -> showPanel(SALES_PANEL)); // 판매 화면으로 이동
        reportItem.addActionListener(e -> showPanel(REPORT_PANEL)); // 보고서 화면

        workMenu.add(menuManageItem);
        workMenu.add(inventoryManageItem);
        workMenu.add(salesMenuItem);
        workMenu.add(reportItem);

        // 도움말 메뉴
        JMenu helpMenu = new JMenu("도움말");
        JMenuItem infoItem = new JMenuItem("정보");
        infoItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "버전 1.0 - 매장 관리 시스템\n제작: TeamKYL", "정보", JOptionPane.INFORMATION_MESSAGE));

        helpMenu.add(infoItem);

        // 메뉴바에 메뉴 추가
        menuBar.add(homeMenu);
        menuBar.add(fileMenu);
        menuBar.add(workMenu);
        menuBar.add(helpMenu);

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
