package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 *  Ver.0
 *  JDBC - DriverManger 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null; // SQL 문 파라미터 바인딩 용

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            // SQL 문 파라미터 바인딩
            // SQL Injection 막을 수 있다.
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate(); // 반환 값? 영향받은 테이블 내 row 수

            return  member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally { // 리소스 반환
            close(conn, pstmt, null);
        }
    }

    private void close(Connection conn, Statement stmt, ResultSet rs) {
        // 이미 닫을 때 예외가 터진거라 어떻게 더 처리 할 방법이 없음
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }

    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
