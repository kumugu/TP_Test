package ui;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 로그인 버튼
        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(e -> frame.showPanel(MainFrame.LOBBY_PANEL));

        // 회원가입 버튼
        JButton signUpButton = new JButton("회원가입");

        // 레이아웃 구성
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("로그인 화면"), gbc);

        gbc.gridy = 1;
        add(loginButton, gbc);

        gbc.gridy = 2;
        add(signUpButton, gbc);
    }
}
