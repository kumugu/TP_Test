package ui;

import javax.swing.*;
import java.awt.*;

public class SignUpPanel extends JPanel {
    public SignUpPanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 레이블 및 텍스트 필드
        JLabel nameLabel = new JLabel("이름:");
        JTextField nameField = new JTextField(20);

        JLabel emailLabel = new JLabel("이메일:");
        JTextField emailField = new JTextField(20);

        JLabel phoneLabel = new JLabel("전화번호:");
        JTextField phoneField = new JTextField(20);

        JLabel idLabel = new JLabel("아이디:");
        JTextField idField = new JTextField(20);

        JLabel passwordLabel = new JLabel("비밀번호:");
        JPasswordField passwordField = new JPasswordField(20);

        JLabel groupLabel = new JLabel("그룹:");
        JComboBox<String> groupComboBox = new JComboBox<>(new String[]{"관리자", "주방", "카운터", "알바생"});

        JLabel positionLabel = new JLabel("직책:");
        JComboBox<String> positionComboBox = new JComboBox<>(new String[]{"점장", "매니저", "오전 알바", "오후 알바", "사장"});

        // 버튼
        JButton signUpButton = new JButton("회원가입");
        JButton cancelButton = new JButton("취소");

        // 이벤트 리스너 추가
        signUpButton.addActionListener(e -> {
            // 데이터 유효성 검사
            if (nameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                    phoneField.getText().isEmpty() || idField.getText().isEmpty() ||
                    new String(passwordField.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 데이터 저장 로직
            // Database interaction 코드 작성 필요
            JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
            frame.showPanel(MainFrame.LOGIN_PANEL);
        });

        cancelButton.addActionListener(e -> frame.showPanel(MainFrame.LOGIN_PANEL));

        // 레이아웃 구성
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(phoneLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(idLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        add(groupLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(groupComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        add(positionLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(positionComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(signUpButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, gbc);
    }
}
