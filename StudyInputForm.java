package 학습통계뷰어.viewer;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import 학습통계뷰어.dao.StudyLogDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class StudyInputForm extends JFrame {
    private String userId; // 사용자 ID 필드 추가
    private JTextField subjectField;
    private JTextField timeField;
    private DatePicker datePicker;
    private JButton saveButton;

    public StudyInputForm(String userId) {
        this.userId = userId; // 생성자로부터 전달받음
        setTitle("학습 기록 입력");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel dateLabel = new JLabel("날짜:");
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");
        datePicker = new DatePicker(settings);

        JLabel subjectLabel = new JLabel("과목:");
        subjectField = new JTextField();

        JLabel timeLabel = new JLabel("학습 시간 (시간 단위):");
        timeField = new JTextField();

        saveButton = new JButton("저장하기");

        panel.add(dateLabel);
        panel.add(datePicker);
        panel.add(subjectLabel);
        panel.add(subjectField);
        panel.add(timeLabel);
        panel.add(timeField);
        panel.add(new JLabel());
        panel.add(saveButton);

        add(panel);

        // 엔터 키로 저장하기
        ActionListener saveAction = evt -> saveButton.doClick();
        subjectField.addActionListener(saveAction);
        timeField.addActionListener(saveAction);
        datePicker.getComponentDateTextField().addActionListener(saveAction);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subject = subjectField.getText().trim();
                String timeText = timeField.getText().trim();
                LocalDate date = datePicker.getDate();

                try {
                    if (subject.isEmpty() || timeText.isEmpty() || date == null) {
                        JOptionPane.showMessageDialog(StudyInputForm.this,
                                "모든 항목을 입력해 주세요.",
                                "입력 오류", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    int time = Integer.parseInt(timeText);

                    StudyLogDAO dao = new StudyLogDAO();
                    boolean success = dao.insertStudyLog(userId, date, subject, time); // userId 포함

                    if (success) {
                        JOptionPane.showMessageDialog(StudyInputForm.this,
                                "저장되었습니다.",
                                "성공", JOptionPane.INFORMATION_MESSAGE);
                        subjectField.setText("");
                        timeField.setText("");
                        datePicker.clear();
                    } else {
                        JOptionPane.showMessageDialog(StudyInputForm.this,
                                "저장에 실패했습니다.",
                                "오류", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(StudyInputForm.this,
                            "학습 시간은 숫자만 입력 가능합니다.",
                            "입력 오류", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // 메인 테스트용 - 실제 프로그램에서는 사용하지 않음
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudyInputForm("testuser").setVisible(true));
    }
}
