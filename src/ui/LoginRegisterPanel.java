package ui;

import javax.swing.*;
import java.awt.*;

public class LoginRegisterPanel extends JPanel {
    private CardLayout cardLayout = new CardLayout(); // CardLayout으로 화면 전환 구현
    private JPanel mainPanel = new JPanel(cardLayout); // 화면 전환을 위한 메인 패널

    public LoginRegisterPanel() {
        setLayout(new BorderLayout());

        // 로그인 패널과 회원가입 패널 생성
        JPanel loginPanel = createLoginPanel();
        JPanel registerPanel = createRegisterPanel();

        // 메인 패널에 두 패널 추가
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registerPanel, "REGISTER");

        // 기본 화면은 로그인 화면
        cardLayout.show(mainPanel, "LOGIN");

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * 로그인 화면 생성 메서드
     */
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("로그인");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));

        JLabel userIdLabel = new JLabel("아이디:");
        JTextField userIdField = new JTextField(20);

        JLabel passwordLabel = new JLabel("비밀번호:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("로그인");
        JButton registerButton = new JButton("회원가입");

        // 회원가입 버튼 클릭 시 회원가입 화면으로 전환
        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));

        // 레이아웃 배치
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(userIdLabel, gbc);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(loginButton, gbc);
        gbc.gridx = 1;
        panel.add(registerButton, gbc);

        return panel;
    }

    /**
     * 회원가입 화면 생성 메서드
     */
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("회원가입");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));

        JLabel nameLabel = new JLabel("이름:");
        JTextField nameField = new JTextField(20);

        JLabel emailLabel = new JLabel("이메일:");
        JTextField emailField = new JTextField(20);

        JLabel phoneLabel = new JLabel("전화번호:");
        JTextField phoneField = new JTextField(20);

        JLabel groupLabel = new JLabel("그룹:");
        JComboBox<String> groupComboBox = new JComboBox<>(new String[]{"관리자", "주방", "카운터", "알바생"});

        JLabel positionLabel = new JLabel("직책:");
        JComboBox<String> positionComboBox = new JComboBox<>(new String[]{"점장", "매니저", "오전 알바", "오후 알바", "사장"});

        JLabel userIdLabel = new JLabel("아이디:");
        JTextField userIdField = new JTextField(20);

        JLabel passwordLabel = new JLabel("비밀번호:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton registerButton = new JButton("회원가입");
        JButton cancelButton = new JButton("취소");

        // 취소 버튼 클릭 시 로그인 화면으로 전환
        cancelButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        // 레이아웃 배치
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(groupLabel, gbc);
        gbc.gridx = 1;
        panel.add(groupComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(positionLabel, gbc);
        gbc.gridx = 1;
        panel.add(positionComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(userIdLabel, gbc);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(registerButton, gbc);
        gbc.gridx = 1;
        panel.add(cancelButton, gbc);

        return panel;
    }
}
