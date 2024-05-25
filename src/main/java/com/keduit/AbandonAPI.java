package main.java.com.keduit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AbandonAPI {

    // API 호출에 필요한 인증키 및 요청 타입
    private static final String key = "UzuzkI%2BeC2bW8T5I7OzkOwVMvfBAtlVqRRRbNespLIO4MjRCzdn2XTfo1z6e%2FAlc5BQaew1ayWWVdn62Q%2BQsfQ%3D%3D";
    private static final String requestType = "json";

    // API 호출을 위한 URL 생성
    private static String buildAll(String key, int bgn, int end, String requestType) throws Exception {

        StringBuilder urlBuilder = new StringBuilder(
                "https://apis.data.go.kr/1543061/abandonmentPublicSrvc/abandonmentPublic");

        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + key);
        urlBuilder.append("&" + URLEncoder.encode("bgnde", "UTF-8") + "=" + bgn);
        urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + end);
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + 100);
        urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + requestType);

        return urlBuilder.toString();
    }

    // 주어진 URL 로 HTTP GET 요청을 보내고 응답을 문자열로 반환하는 메소드
    private static String requestHttp(String prevUrl) throws IOException {

        URL url = new URL(prevUrl);
        System.out.println(prevUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type    ", "application/json");
        BufferedReader bf;

        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            bf = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            System.out.println("HTTP요청 실패");
        }

        // 응답을 읽어서 문자열로 변환 후 Return
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            sb.append(line);
        }

        bf.close();
        conn.disconnect();

        return sb.toString();
    }

    // 사용자로부터 시작 날짜와 종료 날짜를 입력받아 API 호출 URL 을 생성하는 메소드
    static void jsonParser() throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("조회 시작 날짜를 입력하세요: YYYYMMDD");
        int date1 = scan.nextInt();
        System.out.println("조회 종료 날짜를 입력하세요: YYYYMMDD");
        int date2 = scan.nextInt();
        String allUrl = buildAll(key, date1, date2, requestType);

        // JSONParser 를 이용하여 API 응답을 파싱하고 필요한 데이터 추출
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = (JSONObject) parser.parse(requestHttp(allUrl));
        JSONObject response = (JSONObject) jsonObj.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");

        // 추출된 데이터에서 각 유기동물의 정보를 얻어옴
        for (int i = 0; i < item.size(); i++) {
            JSONObject aif = (JSONObject) item.get(i);
            String no = (String) aif.get("desertionNo");
            String happenPlace = (String) aif.get("happenPlace");
            String kindCd = (String) aif.get("kindCd");
            String colorCd = (String) aif.get("colorCd");
            String processState = (String) aif.get("processState");
            String sexCd = (String) aif.get("sexCd");
            String neuterYn = (String) aif.get("neuterYn");
            String careNm = (String) aif.get("careNm");
            String careTel = (String) aif.get("careTel");
            String orgNm = (String) aif.get("orgNm");

            // 추출한 데이터를 Abandon 객체로 생성하고, AbandonSql 객체를 통해 데이터베이스에 삽입
            Abandon abd = new Abandon(no, happenPlace, kindCd, colorCd, processState, sexCd, neuterYn, careNm,
                    careTel, orgNm);
            AbandonSql abd2 = new AbandonSql();
            abd2.insertAbandon(abd);
        }
    }
}
