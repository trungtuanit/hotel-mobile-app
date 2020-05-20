package model;

public class loaiphong {
    public int idphong;
    public String tenloaiphong;
    public String sogiuong;
    public int sokhach;
    public String hinhanh;
    public int giaphong;
    public int idkhachsan;
    public int soluong;

    public loaiphong(int idphong, String tenloaiphong, String sogiuong, int sokhach, String hinhanh, int giaphong, int idkhachsan, int soluong) {
        this.idphong = idphong;
        this.tenloaiphong = tenloaiphong;
        this.sogiuong = sogiuong;
        this.sokhach = sokhach;
        this.hinhanh = hinhanh;
        this.giaphong = giaphong;
        this.idkhachsan = idkhachsan;
        this.soluong = soluong;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getIdphong() {
        return idphong;
    }

    public void setIdphong(int idphong) {
        this.idphong = idphong;
    }

    public String getTenloaiphong() {
        return tenloaiphong;
    }

    public void setTenloaiphong(String tenloaiphong) {
        this.tenloaiphong = tenloaiphong;
    }

    public String getSogiuong() {
        return sogiuong;
    }

    public void setSogiuong(String sogiuong) {
        this.sogiuong = sogiuong;
    }

    public int getSokhach() {
        return sokhach;
    }

    public void setSokhach(int sokhach) {
        this.sokhach = sokhach;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public int getGiaphong() {
        return giaphong;
    }

    public void setGiaphong(int giaphong) {
        this.giaphong = giaphong;
    }

    public int getIdkhachsan() {
        return idkhachsan;
    }

    public void setIdkhachsan(int idkhachsan) {
        this.idkhachsan = idkhachsan;
    }
}
