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
import java.time.YearMonth;
import java.util.Map;

public class MonthlyTabPanel extends JPanel {
    private String userId;
    private JLabel monthLabel;
    private JPanel chartPanelContainer;
    private YearMonth currentMonth;

    public MonthlyTabPanel(String userId) {
        this.userId = userId;
        this.currentMonth = YearMonth.now();

        setLayout(new BorderLayout());

        // 상단 컨트롤 영역
        JPanel topPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("←");
        JButton nextButton = new JButton("→");
        monthLabel = new JLabel();
        updateMonthLabel();

        // 달력
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM");
        DatePicker datePicker = new DatePicker(settings);
        datePicker.setDateToToday();

        datePicker.addDateChangeListener(e -> {
            LocalDate date = e.getNewDate();
            if (date != null) {
                currentMonth = YearMonth.from(date);
                updateMonthLabel();
                updateChart();
            }
        });

        prevButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            datePicker.setDate(currentMonth.atDay(1));
        });

        nextButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            datePicker.setDate(currentMonth.atDay(1));
        });

        topPanel.add(prevButton);
        topPanel.add(monthLabel);
        topPanel.add(nextButton);
        topPanel.add(datePicker);
        add(topPanel, BorderLayout.NORTH);

        // 차트 영역
        chartPanelContainer = new JPanel(new BorderLayout());
        add(chartPanelContainer, BorderLayout.CENTER);

        updateChart();
    }

    private void updateMonthLabel() {
        monthLabel.setText(currentMonth.getYear() + "년 " + currentMonth.getMonthValue() + "월");
    }

    private void updateChart() {
        chartPanelContainer.removeAll();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            StudyLogDAO dao = new StudyLogDAO();
            Map<String, Integer> data = dao.getSubjectTimesByMonth(userId, currentMonth);

            if (data.isEmpty()) {
                chartPanelContainer.add(new JLabel("📭 이 달에는 학습 기록이 없습니다."), BorderLayout.CENTER);
            } else {
                for (Map.Entry<String, Integer> entry : data.entrySet()) {
                    dataset.addValue(entry.getValue(), "학습 시간", entry.getKey());
                }

                JFreeChart chart = ChartFactory.createBarChart(
                        "월별 과목별 학습 시간",
                        "과목",
                        "시간",
                        dataset
                );

                Font font = new Font("맑은 고딕", Font.PLAIN, 12);
                chart.setTitle(new TextTitle("월별 과목별 학습 시간", font));
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
