package main;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DatabaseSetup {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Database connection established.");

            // 테이블 생성
            createTables(conn);

            // 샘플 데이터 삽입
            insertSampleData(conn);

            System.out.println("Database setup complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            // employee_groups 테이블 생성
            stmt.execute("""
            BEGIN
                EXECUTE IMMEDIATE '
                    CREATE TABLE employee_groups (
                        group_id INT PRIMARY KEY,
                        group_name VARCHAR(100) NOT NULL
                    )
                ';
            EXCEPTION
                WHEN OTHERS THEN
                    IF SQLCODE != -955 THEN RAISE; END IF;
            END;
            """);

            // employee_positions 테이블 생성
            stmt.execute("""
            BEGIN
                EXECUTE IMMEDIATE '
                    CREATE TABLE employee_positions (
                        position_id INT PRIMARY KEY,
                        position_name VARCHAR(100) NOT NULL
                    )
                ';
            EXCEPTION
                WHEN OTHERS THEN
                    IF SQLCODE != -955 THEN RAISE; END IF;
            END;
            """);

            // employees 테이블 생성
            stmt.execute("""
            BEGIN
                EXECUTE IMMEDIATE '
                    CREATE TABLE employees (
                        employee_id INT PRIMARY KEY,
                        employee_name VARCHAR(100) NOT NULL,
                        employee_email VARCHAR(100) NOT NULL,
                        employee_phone VARCHAR(15),
                        group_id INT,
                        position_id INT,
                        employee_id_value VARCHAR(100) UNIQUE,
                        employee_password VARCHAR(100),
                        FOREIGN KEY (group_id) REFERENCES employee_groups(group_id) ON DELETE SET NULL,
                        FOREIGN KEY (position_id) REFERENCES employee_positions(position_id) ON DELETE SET NULL
                    )
                ';
            EXCEPTION
                WHEN OTHERS THEN
                    IF SQLCODE != -955 THEN RAISE; END IF;
            END;
            """);

            // 시퀀스 및 트리거 설정
            stmt.execute("""
            BEGIN
                EXECUTE IMMEDIATE '
                    CREATE SEQUENCE employee_seq
                    START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE
                ';
            EXCEPTION
                WHEN OTHERS THEN
                    IF SQLCODE != -955 THEN RAISE; END IF;
            END;
            """);

            stmt.execute("""
            BEGIN
                EXECUTE IMMEDIATE '
                    CREATE OR REPLACE TRIGGER employee_id_trigger
                    BEFORE INSERT ON employees
                    FOR EACH ROW
                    BEGIN
                      SELECT employee_seq.NEXTVAL INTO :NEW.employee_id FROM dual;
                    END;
                ';
            EXCEPTION
                WHEN OTHERS THEN
                    IF SQLCODE != -955 THEN RAISE; END IF;
            END;
            """);
        }
    }


    private static void insertSampleData(Connection conn) throws Exception {
        // employee_groups 데이터 삽입
        try (PreparedStatement stmt = conn.prepareStatement(
                "MERGE INTO employee_groups g " +
                        "USING (SELECT ? AS group_id, ? AS group_name FROM dual) d " +
                        "ON (g.group_id = d.group_id) " +
                        "WHEN NOT MATCHED THEN " +
                        "INSERT (group_id, group_name) VALUES (d.group_id, d.group_name)")) {
            stmt.setInt(1, 1);
            stmt.setString(2, "관리자");
            stmt.executeUpdate();

            stmt.setInt(1, 2);
            stmt.setString(2, "주방");
            stmt.executeUpdate();

            stmt.setInt(1, 3);
            stmt.setString(2, "카운터");
            stmt.executeUpdate();

            stmt.setInt(1, 4);
            stmt.setString(2, "알바생");
            stmt.executeUpdate();
        }

        // employee_positions 데이터 삽입
        try (PreparedStatement stmt = conn.prepareStatement(
                "MERGE INTO employee_positions p " +
                        "USING (SELECT ? AS position_id, ? AS position_name FROM dual) d " +
                        "ON (p.position_id = d.position_id) " +
                        "WHEN NOT MATCHED THEN " +
                        "INSERT (position_id, position_name) VALUES (d.position_id, d.position_name)")) {
            stmt.setInt(1, 1);
            stmt.setString(2, "점장");
            stmt.executeUpdate();

            stmt.setInt(1, 2);
            stmt.setString(2, "매니저");
            stmt.executeUpdate();

            stmt.setInt(1, 3);
            stmt.setString(2, "오전 알바");
            stmt.executeUpdate();

            stmt.setInt(1, 4);
            stmt.setString(2, "오후 알바");
            stmt.executeUpdate();

            stmt.setInt(1, 5);
            stmt.setString(2, "사장");
            stmt.executeUpdate();
        }

        // employees 데이터 삽입
        try (PreparedStatement stmt = conn.prepareStatement(
                "MERGE INTO employees e " +
                        "USING (SELECT ? AS employee_id_value FROM dual) d " +
                        "ON (e.employee_id_value = d.employee_id_value) " +
                        "WHEN NOT MATCHED THEN " +
                        "INSERT (employee_name, employee_email, employee_phone, group_id, position_id, employee_id_value, employee_password) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, "hong123");
            stmt.setString(2, "홍길동");
            stmt.setString(3, "hong@burger.com");
            stmt.setString(4, "010-1234-5678");
            stmt.setInt(5, 1);
            stmt.setInt(6, 1);
            stmt.setString(7, "hong123");
            stmt.setString(8, "password123");
            stmt.executeUpdate();

            stmt.setString(1, "kim123");
            stmt.setString(2, "김영희");
            stmt.setString(3, "kim@burger.com");
            stmt.setString(4, "010-2345-6789");
            stmt.setInt(5, 2);
            stmt.setInt(6, 2);
            stmt.setString(7, "kim123");
            stmt.setString(8, "password456");
            stmt.executeUpdate();

            stmt.setString(1, "lee123");
            stmt.setString(2, "이민수");
            stmt.setString(3, "lee@burger.com");
            stmt.setString(4, "010-3456-7890");
            stmt.setInt(5, 3);
            stmt.setInt(6, 3);
            stmt.setString(7, "lee123");
            stmt.setString(8, "password789");
            stmt.executeUpdate();
        }
    }

}
