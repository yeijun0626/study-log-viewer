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
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Map;

public class DailyTabPanel extends JPanel {
    private String userId;
    private JLabel dateLabel;
    private JPanel chartPanelContainer;
    private LocalDate currentDate;

    public DailyTabPanel(String userId) {
        this.userId = userId;
        this.currentDate = LocalDate.now();

        setLayout(new BorderLayout());

        // 상단 날짜 선택 UI
        JPanel topPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("←");
        JButton nextButton = new JButton("→");
        dateLabel = new JLabel();
        updateDateLabel();

        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        DatePicker datePicker = new DatePicker(settings);
        datePicker.setDate(currentDate);

        datePicker.addDateChangeListener(e -> {
            LocalDate date = e.getNewDate();
            if (date != null) {
                currentDate = date;
                updateDateLabel();
                updateChart();
            }
        });

        prevButton.addActionListener(e -> {
            currentDate = currentDate.minusDays(1);
            datePicker.setDate(currentDate);
        });

        nextButton.addActionListener(e -> {
            currentDate = currentDate.plusDays(1);
            datePicker.setDate(currentDate);
        });

        topPanel.add(prevButton);
        topPanel.add(dateLabel);
        topPanel.add(nextButton);
        topPanel.add(datePicker);
        add(topPanel, BorderLayout.NORTH);

        // 차트 표시 패널
        chartPanelContainer = new JPanel(new BorderLayout());
        add(chartPanelContainer, BorderLayout.CENTER);

        updateChart();
    }

    private void updateDateLabel() {
        dateLabel.setText(currentDate.toString());
    }

    private void updateChart() {
        chartPanelContainer.removeAll();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            StudyLogDAO dao = new StudyLogDAO();
            Map<String, Integer> data = dao.getSubjectTimesByDate(userId, currentDate);

            if (data.isEmpty()) {
                chartPanelContainer.add(new JLabel("📭 선택한 날짜에 학습 기록이 없습니다."), BorderLayout.CENTER);
            } else {
                for (Map.Entry<String, Integer> entry : data.entrySet()) {
                    dataset.addValue(entry.getValue(), "학습 시간", entry.getKey());
                }

                JFreeChart chart = ChartFactory.createBarChart(
                        "일자별 과목별 학습 시간",
                        "과목",
                        "시간",
                        dataset
                );

                Font font = new Font("맑은 고딕", Font.PLAIN, 12);
                chart.setTitle(new TextTitle("일자별 과목별 학습 시간", font));
                if (chart.getLegend() != null) chart.getLegend().setItemFont(font);

                CategoryPlot plot = chart.getCategoryPlot();
                CategoryAxis domainAxis = plot.getDomainAxis();
                domainAxis.setLabelFont(font);
                domainAxis.setTickLabelFont(font);

                NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
                rangeAxis.setLabelFont(font);
                rangeAxis.setTickLabelFont(font);

                ChartPanel chartPanel = new ChartPanel(chart);
                chartPanel.setToolTipText("막대에 마우스를 올려 정보를 확인하세요.");
                chartPanelContainer.add(chartPanel, BorderLayout.CENTER);
            }

        } catch (Exception e) {
            chartPanelContainer.add(new JLabel("데이터를 불러오는 중 오류 발생: " + e.getMessage()), BorderLayout.CENTER);
        }

        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }
}
