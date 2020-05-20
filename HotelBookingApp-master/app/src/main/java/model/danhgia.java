package model;

public class danhgia {
    public int id;
    public String username;
    public String detail;
    public  String header;
    public int mark;
    public String hinhanhdiadiem;
    public danhgia(int id, String username, String detail, String header, int mark,String hinhanhdiadiem) {
        this.id = id;
        this.username = username;
        this.detail = detail;
        this.header = header;
        this.mark = mark;
        this.hinhanhdiadiem = hinhanhdiadiem;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
    public  String getHinhanhdiadiem() {
        return hinhanhdiadiem;
    }

    public void setHinhanhdiadiem(String hinhanhdiadiem) {
        this.hinhanhdiadiem = hinhanhdiadiem;
    }
}
