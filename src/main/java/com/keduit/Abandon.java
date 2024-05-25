package main.java.com.keduit;

public class Abandon {

    private String id;
    private String place;
    private String kind;
    private String color;
    private String ps;
    private String sex;
    private String neuter; // 중성화 여부
    private String cplace; // 현재 보관중인 장소
    private String ctel; // 보관소 연락처
    private String org;

    public Abandon(String id, String place, String kind, String color, String ps, String sex, String neuter,
                   String cplace, String ctel, String org) {
        this.id = id;
        this.place = place;
        this.kind = kind;
        this.color = color;
        this.ps = ps;
        this.sex = sex;
        this.neuter = neuter;
        this.cplace = cplace;
        this.ctel = ctel;
        this.org = org;
    }

    public String getId() {
        return id;
    }

    public String getPlace() {
        return place;
    }

    public String getKind() {
        return kind;
    }

    public String getColor() {
        return color;
    }

    public String getPs() {
        return ps;
    }

    public String getSex() {
        return sex;
    }

    public String getNeuter() {
        return neuter;
    }

    public String getCplace() {
        return cplace;
    }

    public String getCtel() {
        return ctel;
    }

    public String getOrg() {
        return org;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setNeuter(String neuter) {
        this.neuter = neuter;
    }

    public void setCPlace(String cplace) {
        this.cplace = cplace;
    }

    public void setTel(String ctel) {
        this.ctel = ctel;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    @Override
    public String toString() {
        return "id: " + id + "발생 장소: " + place + "품종: " + kind + "색상: " + color + "현상황: " + ps + "성별: " + sex
                + "중성화 여부: " + neuter + "보호소명: " + cplace + "보호소 연락처: " + ctel + "지역: " + org;
    }

}
