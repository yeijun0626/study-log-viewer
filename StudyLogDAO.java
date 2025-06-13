package 학습통계뷰어.dao;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class StudyLogDAO {

	public boolean insertStudyLog(String userId, LocalDate date, String subject, int time) {
	    String sql = "INSERT INTO study_log (user_id, study_date, subject, study_time) VALUES (?, ?, ?, ?)";

	    try (Connection conn = DBUtil.connect();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, userId);
	        pstmt.setDate(2, Date.valueOf(date));
	        pstmt.setString(3, subject);
	        pstmt.setInt(4, time);

	        pstmt.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


    public Map<String, Integer> getSubjectTotalTimes(String userId) throws SQLException {
        String sql = "SELECT subject, SUM(study_time) AS total_time " +
                     "FROM study_log WHERE user_id = ? GROUP BY subject ORDER BY subject";

        Map<String, Integer> result = new LinkedHashMap<>();

        try (Connection conn = DBUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("subject"), rs.getInt("total_time"));
                }
            }
        }

        return result;
    }



    public Map<String, Integer> getTimeBySubjectInDate(String userId, LocalDate date) throws SQLException {
        String sql = "SELECT subject, SUM(study_time) as total_time FROM study_log WHERE user_id = ? AND study_date = ? GROUP BY subject";
        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection conn = DBUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setDate(2, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("subject"), rs.getInt("total_time"));
            }
        }
        return result;
    }
    

    public Map<String, Integer> getSubjectTimesByMonth(String userId, YearMonth month) throws SQLException {
        String sql = "SELECT subject, SUM(study_time) AS total " +
                     "FROM study_log " +
                     "WHERE user_id = ? AND DATE_FORMAT(study_date, '%Y-%m') = ? " +
                     "GROUP BY subject";

        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection conn = DBUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, month.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("subject"), rs.getInt("total"));
                }
            }
        }
        return result;
    }
    public Map<String, Integer> getSubjectTimesByDate(String userId, LocalDate date) throws SQLException {
        String sql = "SELECT subject, SUM(study_time) AS total " +
                     "FROM study_log " +
                     "WHERE user_id = ? AND study_date = ? " +
                     "GROUP BY subject";

        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection conn = DBUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("subject"), rs.getInt("total"));
                }
            }
        }
        return result;
    }
 // 총 학습 시간 (사용자별)
    public int getTotalTime(String userId) throws SQLException {
        String sql = "SELECT SUM(study_time) FROM study_log WHERE user_id = ?";
        try (Connection conn = DBUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    // 평균 학습 시간 (사용자별)
    public double getAverageTime(String userId) throws SQLException {
        String sql = "SELECT AVG(study_time) FROM study_log WHERE user_id = ?";
        try (Connection conn = DBUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    // 가장 많이 공부한 날 (사용자별)
    public String getMaxStudyDay(String userId) throws SQLException {
        String sql = "SELECT study_date, study_time FROM study_log WHERE user_id = ? ORDER BY study_time DESC LIMIT 1";
        try (Connection conn = DBUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("study_date") + " (" + rs.getInt("study_time") + "시간)";
                }
            }
        }
        return "없음";
    }

    // 가장 적게 공부한 날 (사용자별)
    public String getMinStudyDay(String userId) throws SQLException {
        String sql = "SELECT study_date, study_time FROM study_log WHERE user_id = ? ORDER BY study_time ASC LIMIT 1";
        try (Connection conn = DBUtil.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("study_date") + " (" + rs.getInt("study_time") + "시간)";
                }
            }
        }
        return "없음";
    }


}
