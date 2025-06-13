package 학습통계뷰어.dao;

import java.sql.*;

public class UserDAO {
    private Connection conn;

    public UserDAO() {
        try {
            // DB 연결 설정
        	conn = DBUtil.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 회원가입: 사용자 추가
    public boolean insertUser(String id, String password) {
        String sql = "INSERT INTO users (id, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false; // 중복 ID 등
        }
    }

    // 로그인: 사용자 존재 여부 확인
    public boolean validateUser(String id, String password) {
        String sql = "SELECT * FROM users WHERE id = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 아이디 중복 체크
    public boolean exists(String id) {
        String sql = "SELECT id FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
