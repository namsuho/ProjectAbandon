package main.java.com.keduit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AbandonSql {

    private Connection conn;
    private Scanner scan;

    // MySQL 연결 메소드
    public AbandonSql() throws ClassNotFoundException, SQLException {
        String userid = "root";
        String url = "jdbc:mysql://localhost:3306/madang";
        String driver = "com.mysql.cj.jdbc.Driver";
        String pwd = "1234";
        Class.forName(driver); // JDBC 드라이버 로드
        conn = DriverManager.getConnection(url, userid, pwd); // 데이터베이스 연결
        scan = new Scanner(System.in); // Scanner 객체 초기화
    }

    // 테이블 존재 여부 확인 메소드
    public void checkTable() {

        try (Statement stmt = conn.createStatement(); // try-with-resources 사용하여 객체들의 리소스를 자동으로 관리
             ResultSet rs = stmt.executeQuery(
                     "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'madang' AND table_name = 'abandonall'")) {

            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("테이블이 존재합니다.");
                } else {
                    System.out.println("테이블이 존재하지 않습니다.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 테이블 생성 메소드
    public void createTable() {

        String sql = "CREATE TABLE  abandonall(" + "id varchar(40) primary Key," + "happenPlace varchar(40) not null,"
                + "kindCd varchar(20) not null," + "colorCd varchar(10)," + "processState varchar(10),"
                + "sexCd varchar(3)," + "neuterYn varchar(3)," + "careNm varchar(20) not null," + "careTel varchar(20),"
                + "orgNm varchar(20))";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("테이블 생성 성공");
        } catch (SQLException e) {
            System.out.println("테이블 생성 불가");
            System.out.println("테이블 존재 여부 확인");
            checkTable();
        }

    }

    // 유기동물 정보 삽입 메소드
    public void insertAbandon(Abandon abandon) {
        String sql = "INSERT INTO abandonall(id,happenPlace,kindCd,colorCd,processState,sexCd,neuterYn,careNm,careTel,orgNm) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, abandon.getId());
            pstmt.setString(2, abandon.getPlace());
            pstmt.setString(3, abandon.getKind());
            pstmt.setString(4, abandon.getColor());
            pstmt.setString(5, abandon.getPs());
            pstmt.setString(6, abandon.getSex());
            pstmt.setString(7, abandon.getNeuter());
            pstmt.setString(8, abandon.getCplace());
            pstmt.setString(9, abandon.getCtel());
            pstmt.setString(10, abandon.getOrg());
            int result = pstmt.executeUpdate();
            if (result == 1) {
                System.out.println(abandon.getId() + " 레코드 추가 성공");
            } else {
                System.out.println(abandon.getId() + " 레코드 추가 실패");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 테이블 행의 개수 확인 메소드
    public void checkRow() throws SQLException {
        String sql = "SELECT COUNT(*) FROM abandonall";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int rowCount = rs.getInt(1);
                System.out.println("테이블 행의 갯수: " + rowCount);
            }
        } catch (SQLException e) {
            System.out.println("테이블이 존재 하지 않습니다. 메뉴로 돌아갑니다.");
        }
    }

    // 모든 데이터 조회 메소드
    public void selectAll() throws SQLException {
        String sql = "select * from abandonall";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println(
                    "	    ─유기동물id─                 ─유기발생장소─           ─[축종]품종─      ─색상─     ─보호상태─   ─성별─  ─중성화─    ─보호소명─    ─ 연락처 ─    ─ 지역 ─");
            while (rs.next()) {
                System.out.print("\t" + rs.getString(1));
                System.out.print("\t" + rs.getString(2));
                System.out.print("\t" + rs.getString(3));
                System.out.print("\t" + rs.getString(4));
                System.out.print("\t" + rs.getString(5));
                System.out.print("\t" + rs.getString(6));
                System.out.print("\t" + rs.getString(7));
                System.out.print("\t" + rs.getString(8));
                System.out.print("\t" + rs.getString(9));
                System.out.print("\t" + rs.getString(10));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("데이터 불러오기 실패");
            System.out.println("다음 작업을 추천합니다. 1.테이블 존재여부 확인 2.테이블 안의 데이터 확인");
            int num = scan.nextInt();
            if (num == 1) {
                checkTable();
            } else {
                checkRow();
            }

        }
    }

    // 특정 장소와 품종으로 데이터 조회 메소드
    public void selectPlaceWithKind() throws Exception {

        System.out.println("잃어버린 장소를 입력하세요:");
        String place = scan.nextLine();
        System.out.println("품종을 입력하세요:");
        String kind = scan.nextLine();
        String sql = "select * from abandonall where happenPlace like ? AND kindCd like ? ";
        PreparedStatement pstmt;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + place + "%");
        pstmt.setString(2, "%" + kind + "%");
        ResultSet rs = pstmt.executeQuery();
        try {
            while (rs.next()) {
                System.out.print("\t" + rs.getString(1));
                System.out.print("\t" + rs.getString(2));
                System.out.print("\t" + rs.getString(3));
                System.out.print("\t" + rs.getString(4));
                System.out.print("\t" + rs.getString(5));
                System.out.print("\t" + rs.getString(6));
                System.out.print("\t" + rs.getString(7));
                System.out.print("\t" + rs.getString(8));
                System.out.print("\t" + rs.getString(9));
                System.out.print("\t" + rs.getString(10));
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        pstmt.close();

    }

    // 보호 상태로 데이터 조회 메소드
    public void selectState() throws SQLException {
        Scanner scan = new Scanner(System.in);
        System.out.println("검색할 보호 상태를 입력하세요: \n ────────────보기─────────────\n   보호중 ,  반환,  안락사,  방사");
        String state = scan.nextLine();
        String sql = "SELECT * FROM abandonall WHERE processState LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + state + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.print("\t" + rs.getString(1));
                    System.out.print("\t" + rs.getString(2));
                    System.out.print("\t" + rs.getString(3));
                    System.out.print("\t" + rs.getString(4));
                    System.out.print("\t" + rs.getString(5));
                    System.out.print("\t" + rs.getString(6));
                    System.out.print("\t" + rs.getString(7));
                    System.out.print("\t" + rs.getString(8));
                    System.out.print("\t" + rs.getString(9));
                    System.out.print("\t" + rs.getString(10));
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 보호 상태 업데이트 메소드
    public void updateState() throws SQLException {

        System.out.println("유기동물의 id를 입력하세요:");
        String id = scan.next();

        if (id == null || id.trim().isEmpty()) {
            System.out.println("유기동물 id는 필수 입력 항목입니다.");
            return;
        }

        System.out.println("변경할 상태를 고르세요 .(1:반환,2:안락사)");
        int num = scan.nextInt();
        String state;
        if (num == 1) {
            state = "반환";
        } else if (num == 2) {
            state = "안락사";
        } else {
            System.out.println("잘못된 입력입니다.");
            return;
        }

        String sql = "UPDATE abandonall SET processState = concat('종료(', ?) WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, state);
            pstmt.setString(2, id);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                System.out.println("해당 id의 유기동물이 없습니다.");
            } else {
                System.out.println("변경 성공");
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    // 데이터 삭제 메소드
    public void delete() {

        System.out.println("삭제할 유기동물의 id를 입력하세요:");
        String id = scan.next();
        String sql = "DELETE FROM abandonall WHERE id = ?";
        PreparedStatement pstmt;
        int rows = 0;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rows = pstmt.executeUpdate();
            pstmt.close();

            if (rows == 0) {
                System.out.println("해당 id의 유기동물이 없습니다.");
            } else {
                System.out.println("삭제 성공");
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    // 프로그램 실행 메소드
    public void program() throws Exception {
        AbandonAPI abandonApi = new AbandonAPI();

        System.out.println(
                "──────────────────────────────────────────────────────[        유기동물    조회    서비스        ]────────────"
                        + "────────────────────────────────────────────────────────────");
        while (true) {
            System.out.println();
            System.out.println("사용자 정보를 입력하세요:\n 1.관리자          2.일반");
            int user = scan.nextInt();

            if (user == 1) {
                System.out.println("비밀번호를 입력하세요: ");
                scan.nextLine();
                String pwd = scan.nextLine();
                if (pwd.equals("1234")) {
                    System.out.println("관리자 메뉴를 불러옵니다.");

                    while (true) {
                        System.out
                                .println("─────────────────────────────────────────────────────────────────────────────"
                                        + "────────────────────────────────────────────────────────────────────────────────────────");

                        System.out.println("     [1] 테이블 생성 및 api 에서 가져온 데이터 추가   [2] 테이블 조회   [3] 유기장소와 품종을 통해 조회"
                                + "    [4] 보호상태 기준 조회   [5] 보호상태 수정   [6] 특정 유기동물id 데이터 삭제  [7] 프로그램 종료\n"
                                + "────────────────────────────────────────────────────────────────────────────"
                                + "─────────────────────────────────────────────────────────────────────────────────────────");

                        int a = scan.nextInt();
                        scan.nextLine(); // 줄바꿈 : 입력 오류를 방지
                        if (a == 1) {

                            createTable();
                            abandonApi.jsonParser();

                        }
                        if (a == 2) {

                            selectAll();

                        }
                        if (a == 3) {
                            selectPlaceWithKind();
                            System.out.println("값이 없을 경우 일치하는 항목이 존재하지 않음");

                        }
                        if (a == 4) {
                            selectState();
                        }
                        if (a == 5) {
                            updateState();

                        }
                        if (a == 6) {

                            delete();
                        }
                        if (a == 7) {
                            System.out.println("프로그램을 종료합니다.");
                            break;
                        }
                    }
                    break;
                } else {
                    System.out.println("비밀번호가 틀렸습니다. 사용자 선택 메뉴로 돌아갑니다.");
                }
            } else {
                while (true) {
                    System.out.println("─────────────────────────────────────────────────────────────────────────────"
                            + "────────────────────────────────────────────────────────────────────────────────");

                    System.out.println(
                            "                                                       [1] 전체 테이블 조회        [2] 유기장소와 품종으로 원하는 유기동물 조회"
                                    + "    [3] 프로그램 종료\n"
                                    + "────────────────────────────────────────────────────────────────────────────"
                                    + "─────────────────────────────────────────────────────────────────────────────────");
                    int a = scan.nextInt();
                    scan.nextLine();
                    if (a == 1) {
                        selectAll();
                    }
                    if (a == 2) {
                        selectPlaceWithKind();
                        System.out.println();
                        System.out.println("                   주의: 값이 없을 경우 일치하는 항목이 존재하지 않음");
                        System.out.println();
                    }
                    if (a == 3) {
                        System.out.println("프로그램을 종료합니다.");
                        break;
                    }
                }

                break;
            }
        }
        scan.close(); // Scanner 객체 닫기
    }

}
