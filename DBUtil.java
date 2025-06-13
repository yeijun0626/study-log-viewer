
package 학습통계뷰어.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	public static Connection connect() throws SQLException {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        throw new SQLException("MySQL 드라이버를 찾을 수 없습니다.");
	    }

	    String url = "jdbc:mysql://localhost:3306/study_log?serverTimezone=UTC";
	    String user = "root";
	    String password = "rootroot";
	    return DriverManager.getConnection(url, user, password);
	}

}
