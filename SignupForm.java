package 학습통계뷰어.viewer;

import 학습통계뷰어.dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SignupForm extends JFrame {
    private JTextField idField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signupButton;
    private UserDAO dao;
    private JFrame parent;

    public SignupForm(JFrame parent) {
        this.parent = parent;
        this.dao = new UserDAO();

        setTitle("회원가입");
        setSize(350, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel idLabel = new JLabel("아이디:");
        JLabel pwLabel = new JLabel("비밀번호:");
        JLabel confirmLabel = new JLabel("비밀번호 확인:");
        idField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        signupButton = new JButton("가입하기");

        add(idLabel); add(idField);
        add(pwLabel); add(passwordField);
        add(confirmLabel); add(confirmPasswordField);
        add(new JLabel()); add(signupButton);

        // 가입하기 버튼 클릭
        signupButton.addActionListener(this::handleSignup);

        // 엔터 키로 가입
        KeyAdapter enterListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleSignup(null);
                }
            }
        };
        idField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
        confirmPasswordField.addKeyListener(enterListener);
    }

    private void handleSignup(ActionEvent e) {
        String id = idField.getText().trim();
        String pw = new String(passwordField.getPassword());
        String pwConfirm = new String(confirmPasswordField.getPassword());

        if (id.isEmpty() || pw.isEmpty() || pwConfirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 항목을 입력해주세요.");
            return;
        }

        if (!pw.equals(pwConfirm)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
            return;
        }

        if (dao.exists(id)) {
            JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
            return;
        }

        boolean success = dao.insertUser(id, pw);
        if (success) {
            JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
            if (parent != null) parent.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "회원가입에 실패했습니다.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignupForm(null).setVisible(true));
    }
}
