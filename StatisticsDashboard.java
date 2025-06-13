package 학습통계뷰어.viewer;

import 학습통계뷰어.panel.SubjectTabPanel;
import 학습통계뷰어.panel.MonthlyTabPanel;
import 학습통계뷰어.panel.DailyTabPanel;

import javax.swing.*;

public class StatisticsDashboard extends JFrame {

    public StatisticsDashboard(String userId) {
        setTitle("📊 학습 통계 대시보드");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        // 과목별, 월별, 날짜별 패널 추가
        tabbedPane.addTab("📚 과목별", new SubjectTabPanel(userId));
        tabbedPane.addTab("🗓 월별", new MonthlyTabPanel(userId));
        tabbedPane.addTab("📆 날짜별", new DailyTabPanel(userId));

        add(tabbedPane);
        setVisible(true);
    }

    // 테스트용 메인
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StatisticsDashboard("testuser").setVisible(true));
    }
}
