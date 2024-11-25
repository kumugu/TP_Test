package main;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
        String[] tableStatements = {
                // PRODUCTS 테이블
                "CREATE TABLE PRODUCTS (ID NUMBER PRIMARY KEY, NAME VARCHAR2(100) NOT NULL, CATEGORY VARCHAR2(50), PRICE NUMBER NOT NULL)",
                "CREATE SEQUENCE PRODUCT_SEQ START WITH 1 INCREMENT BY 1 NOCACHE",

                // INGREDIENTS 테이블
                "CREATE TABLE INGREDIENTS (ID NUMBER PRIMARY KEY, NAME VARCHAR2(100) NOT NULL, CATEGORY VARCHAR2(50), STOCK NUMBER NOT NULL, UNIT_PRICE NUMBER NOT NULL)",
                "CREATE SEQUENCE INGREDIENT_SEQ START WITH 1 INCREMENT BY 1 NOCACHE",

                // PRODUCT_INGREDIENTS 테이블
                "CREATE TABLE PRODUCT_INGREDIENTS (PRODUCT_ID NUMBER REFERENCES PRODUCTS(ID) ON DELETE CASCADE, INGREDIENT_ID NUMBER REFERENCES INGREDIENTS(ID) ON DELETE CASCADE, QUANTITY NUMBER NOT NULL, PRIMARY KEY (PRODUCT_ID, INGREDIENT_ID))",

                // SALES 테이블
                "CREATE TABLE SALES (ID NUMBER PRIMARY KEY, PRODUCT_ID NUMBER REFERENCES PRODUCTS(ID), QUANTITY NUMBER NOT NULL, SALE_DATE DATE DEFAULT SYSDATE)",
                "CREATE SEQUENCE SALES_SEQ START WITH 1 INCREMENT BY 1 NOCACHE",

                // CATEGORIES 테이블
                "CREATE TABLE CATEGORIES (ID NUMBER PRIMARY KEY, NAME VARCHAR2(50) UNIQUE NOT NULL, TYPE VARCHAR2(20) NOT NULL)",
                "CREATE SEQUENCE CATEGORY_SEQ START WITH 1 INCREMENT BY 1 NOCACHE"
        };

        for (String sql : tableStatements) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            } catch (Exception e) {
                System.out.println("Error executing: " + sql);
                e.printStackTrace();
            }
        }
    }

    private static void insertSampleData(Connection conn) throws Exception {
        String[] sampleData = {
                // CATEGORIES 데이터
                "INSERT INTO CATEGORIES (ID, NAME, TYPE) VALUES (CATEGORY_SEQ.NEXTVAL, '햄버거', '상품')",
                "INSERT INTO CATEGORIES (ID, NAME, TYPE) VALUES (CATEGORY_SEQ.NEXTVAL, '음료', '상품')",
                "INSERT INTO CATEGORIES (ID, NAME, TYPE) VALUES (CATEGORY_SEQ.NEXTVAL, '사이드', '상품')",
                "INSERT INTO CATEGORIES (ID, NAME, TYPE) VALUES (CATEGORY_SEQ.NEXTVAL, '빵', '재료')",
                "INSERT INTO CATEGORIES (ID, NAME, TYPE) VALUES (CATEGORY_SEQ.NEXTVAL, '고기', '재료')",
                "INSERT INTO CATEGORIES (ID, NAME, TYPE) VALUES (CATEGORY_SEQ.NEXTVAL, '치즈', '재료')",

                // INGREDIENTS 데이터
                "INSERT INTO INGREDIENTS (ID, NAME, CATEGORY, STOCK, UNIT_PRICE) VALUES (INGREDIENT_SEQ.NEXTVAL, '빵', '빵', 100, 0.50)",
                "INSERT INTO INGREDIENTS (ID, NAME, CATEGORY, STOCK, UNIT_PRICE) VALUES (INGREDIENT_SEQ.NEXTVAL, '소고기', '고기', 50, 2.00)",
                "INSERT INTO INGREDIENTS (ID, NAME, CATEGORY, STOCK, UNIT_PRICE) VALUES (INGREDIENT_SEQ.NEXTVAL, '치즈', '치즈', 200, 0.75)",

                // PRODUCTS 데이터
                "INSERT INTO PRODUCTS (ID, NAME, CATEGORY, PRICE) VALUES (PRODUCT_SEQ.NEXTVAL, '치즈버거', '햄버거', 5.50)",
                "INSERT INTO PRODUCTS (ID, NAME, CATEGORY, PRICE) VALUES (PRODUCT_SEQ.NEXTVAL, '콜라', '음료', 1.50)",
                "INSERT INTO PRODUCTS (ID, NAME, CATEGORY, PRICE) VALUES (PRODUCT_SEQ.NEXTVAL, '감자튀김', '사이드', 2.00)",

                // PRODUCT_INGREDIENTS 데이터
                "INSERT INTO PRODUCT_INGREDIENTS (PRODUCT_ID, INGREDIENT_ID, QUANTITY) VALUES (1, 1, 2)", // 치즈버거에 빵 2개
                "INSERT INTO PRODUCT_INGREDIENTS (PRODUCT_ID, INGREDIENT_ID, QUANTITY) VALUES (1, 2, 1)", // 치즈버거에 소고기 1개
                "INSERT INTO PRODUCT_INGREDIENTS (PRODUCT_ID, INGREDIENT_ID, QUANTITY) VALUES (1, 3, 1)"  // 치즈버거에 치즈 1개
        };

        for (String sql : sampleData) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            } catch (Exception e) {
                System.out.println("Error executing: " + sql);
                e.printStackTrace();
            }
        }

        System.out.println("Sample data inserted.");
    }
}
