package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    // 화면 식별자 상수
    public static final String LOGIN_PANEL = "로그인 화면";
    public static final String LOBBY_PANEL = "로비 화면";
    public static final String PRODUCT_PANEL = "상품 관리";
    public static final String INVENTORY_PANEL = "재고 관리";
    public static final String SALES_PANEL = "판매";
    public static final String REPORT_PANEL = "보고서";

    private JPanel centerPanel; // 메인 화면을 전환할 패널

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

    /**
     * 각 화면을 메인 패널에 추가
     */
    private void initializePanels() {
        centerPanel.add(new LoginPanel(this), LOGIN_PANEL);
        centerPanel.add(new LobbyPanel(this), LOBBY_PANEL);
        centerPanel.add(new ProductManagementPanel(), PRODUCT_PANEL);
        centerPanel.add(new InventoryPanel(), INVENTORY_PANEL);
        centerPanel.add(new SalesPanel(), SALES_PANEL);
        centerPanel.add(new ReportPanel(), REPORT_PANEL);
    }

    /**
     * 메뉴바 생성
     */
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

    /**
     * 특정 화면으로 전환
     *
     * @param panelName 전환할 화면의 이름
     */
    public void showPanel(String panelName) {
        CardLayout cl = (CardLayout) centerPanel.getLayout();
        cl.show(centerPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
