package ui;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#f5f5f5")); // 배경색 설정

        // 상단 패널: 로고 및 제목
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.decode("#ffffff"));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel logoLabel = new JLabel(new ImageIcon("path/to/logo.png")); // 로고 이미지
        JLabel titleLabel = new JLabel("매장 관리 시스템");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#333333"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(Box.createVerticalStrut(20)); // 상단 여백
        topPanel.add(logoLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(20));

        // 중앙 패널: 아이디 및 비밀번호 입력
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.decode("#f5f5f5"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userIdLabel = new JLabel("아이디:");
        userIdLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        JTextField userIdField = new JTextField(20);
        userIdField.setBorder(BorderFactory.createLineBorder(Color.decode("#cccccc")));

        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.decode("#cccccc")));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(userIdLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(passwordField, gbc);

        // 하단 패널: 버튼들
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.decode("#ffffff"));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JButton loginButton = new JButton("로그인");
        loginButton.setBackground(Color.decode("#4CAF50"));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> frame.showPanel(MainFrame.LOBBY_PANEL)); // 로비로 이동

        JButton signUpButton = new JButton("회원가입");
        signUpButton.setBackground(Color.decode("#2196F3"));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        signUpButton.setFocusPainted(false);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.addActionListener(e -> frame.showPanel(MainFrame.SIGNUP_PANEL)); // 회원가입 화면으로 이동

        bottomPanel.add(Box.createVerticalStrut(20));
        bottomPanel.add(loginButton);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(signUpButton);
        bottomPanel.add(Box.createVerticalStrut(20));

        // 전체 레이아웃 구성
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
