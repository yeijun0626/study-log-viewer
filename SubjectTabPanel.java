package 학습통계뷰어.panel;

import 학습통계뷰어.dao.StudyLogDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

public class SubjectTabPanel extends JPanel {
    private String userId;

    public SubjectTabPanel(String userId) {
        this.userId = userId;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // 상단 통계 정보 영역
        JPanel statsPanel = new JPanel(new GridLayout(4, 1));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel totalLabel = new JLabel();
        JLabel avgLabel = new JLabel();
        JLabel maxLabel = new JLabel();
        JLabel minLabel = new JLabel();

        Font labelFont = new Font("맑은 고딕", Font.PLAIN, 14);
        totalLabel.setFont(labelFont);
        avgLabel.setFont(labelFont);
        maxLabel.setFont(labelFont);
        minLabel.setFont(labelFont);

        try {
            StudyLogDAO dao = new StudyLogDAO();
            int total = dao.getTotalTime(userId);
            double avg = dao.getAverageTime(userId);
            String max = dao.getMaxStudyDay(userId);
            String min = dao.getMinStudyDay(userId);

            totalLabel.setText("총 학습 시간: " + total + "시간");
            avgLabel.setText("평균 학습 시간: " + String.format("%.2f", avg) + "시간");
            maxLabel.setText("가장 많이 공부한 날: " + max);
            minLabel.setText("가장 적게 공부한 날: " + min);
        } catch (SQLException e) {
            totalLabel.setText("통계 정보를 불러오는 중 오류 발생");
            e.printStackTrace();
        }

        statsPanel.add(totalLabel);
        statsPanel.add(avgLabel);
        statsPanel.add(maxLabel);
        statsPanel.add(minLabel);
        add(statsPanel, BorderLayout.NORTH);

        // 하단 그래프 영역
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            StudyLogDAO dao = new StudyLogDAO();
            Map<String, Integer> subjectTotals = dao.getSubjectTotalTimes(userId);

            for (String subject : subjectTotals.keySet()) {
                dataset.addValue(subjectTotals.get(subject), "학습 시간", subject);
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "과목별 총 학습 시간",
                    "과목",
                    "시간",
                    dataset
            );

            Font font = new Font("맑은 고딕", Font.PLAIN, 12);
            chart.setTitle(new TextTitle("과목별 총 학습 시간", font));
            if (chart.getLegend() != null) {
                chart.getLegend().setItemFont(font);
            }

            CategoryPlot plot = chart.getCategoryPlot();
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setLabelFont(font);
            domainAxis.setTickLabelFont(font);

            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setLabelFont(font);
            rangeAxis.setTickLabelFont(font);

            ChartPanel chartPanel = new ChartPanel(chart);
            add(chartPanel, BorderLayout.CENTER);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "과목별 통계 데이터를 불러오는 데 실패했습니다.\n" + e.getMessage(),
                    "DB 오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
