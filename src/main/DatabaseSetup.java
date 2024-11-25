package main;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseSetup {

    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Database connection established.");

            // 필요한 경우 샘플 데이터만 삽입
            insertSampleData(conn);

            System.out.println("Database setup complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertSampleData(Connection conn) throws Exception {
        // 샘플 재료 데이터 삽입
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO INGREDIENTS (ID, NAME, CATEGORY, STOCK, UNIT_PRICE) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setInt(1, 4); // 기존 데이터와 겹치지 않도록 ID 지정
            pstmt.setString(2, "Cheese");
            pstmt.setString(3, "Dairy");
            pstmt.setInt(4, 100);
            pstmt.setDouble(5, 0.75);
            pstmt.executeUpdate();
        }

        System.out.println("Sample data inserted.");
    }
}
