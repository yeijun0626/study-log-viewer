package 학습통계뷰어.viewer;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private String userId;

    public MainMenu(String userId) {
        this.userId = userId;

        setTitle("📚 학습 통계 뷰어 메인 메뉴");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JButton inputButton = new JButton("📝 학습 기록 입력");
        JButton outputButton = new JButton("📊 학습 통계 보기");
        JButton exitButton = new JButton("❌ 종료");

        inputButton.addActionListener(e -> new StudyInputForm(userId).setVisible(true));
        outputButton.addActionListener(e -> new StatisticsDashboard(userId).setVisible(true));
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "정말 종료하시겠습니까?", "종료 확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        exitButton.addActionListener(e -> System.exit(0));

        add(inputButton);
        add(outputButton);
        add(exitButton);

        setLocationRelativeTo(null); // 화면 중앙에 위치
        setVisible(true);
    }
}
