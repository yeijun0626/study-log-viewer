package 학습통계뷰어.viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import 학습통계뷰어.dao.UserDAO;

public class LoginForm extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton;
    private UserDAO dao;

    public LoginForm() {
        setTitle("로그인");
        setSize(350, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        dao = new UserDAO();

        JLabel idLabel = new JLabel("아이디:");
        JLabel pwLabel = new JLabel("비밀번호:");
        idField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("로그인");
        signupButton = new JButton("회원가입");

        add(idLabel); add(idField);
        add(pwLabel); add(passwordField);
        add(loginButton); add(signupButton);

        // 엔터로 로그인
        ActionListener loginAction = e -> handleLogin();
        idField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);

        loginButton.addActionListener(loginAction);
        signupButton.addActionListener(e -> new SignupForm(this).setVisible(true));
    }

    private void handleLogin() {
        try {
            String id = idField.getText().trim();
            String pw = new String(passwordField.getPassword());

            if (id.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 모두 입력해주세요.");
                return;
            }

            if (!dao.exists(id)) {
                JOptionPane.showMessageDialog(this, "등록되지 않은 아이디입니다!");
                return;
            }

            if (dao.validateUser(id, pw)) {
                JOptionPane.showMessageDialog(this, "로그인 성공!");
                new MainMenu(id).setVisible(true);  // userId를 전달
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 잘못되었습니다!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "로그인 중 오류 발생: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
