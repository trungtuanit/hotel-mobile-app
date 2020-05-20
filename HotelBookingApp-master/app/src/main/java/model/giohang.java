package model;

public class giohang {
    public int idphong;
    public String tenloaiphong;
    public String sogiuong;
    public int sokhach;
    public String hinhanh;
    public int giaphong;
    public int idkhachsan;
    public String tenkhachsan;
    public int soluong;
    public int sodem;
    public String ngaynhanphong;
    public String ngaytraphong;
    public String dichvu;

    public giohang(int idphong, String tenloaiphong, String sogiuong, int sokhach, String hinhanh, int giaphong,
                   int idkhachsan, String tenkhachsan, int soluong, int sodem, String ngaynhanphong, String ngaytraphong, String dichvu) {
        this.idphong = idphong;
        this.tenloaiphong = tenloaiphong;
        this.sogiuong = sogiuong;
        this.sokhach = sokhach;
        this.hinhanh = hinhanh;
        this.giaphong = giaphong;
        this.idkhachsan = idkhachsan;
        this.tenkhachsan = tenkhachsan;
        this.soluong = soluong;
        this.sodem = sodem;
        this.ngaynhanphong = ngaynhanphong;
        this.ngaytraphong = ngaytraphong;
        this.dichvu = dichvu;
    }

    public String getDichvu() {
        return dichvu;
    }

    public void setDichvu(String dichvu) {
        this.dichvu = dichvu;
    }

    public String getNgaynhanphong() {
        return ngaynhanphong;
    }

    public void setNgaynhanphong(String ngaynhanphong) {
        this.ngaynhanphong = ngaynhanphong;
    }

    public String getNgaytraphong() {
        return ngaytraphong;
    }

    public void setNgaytraphong(String ngaytraphong) {
        this.ngaytraphong = ngaytraphong;
    }

    public String getTenkhachsan() {
        return tenkhachsan;
    }

    public void setTenkhachsan(String tenkhachsan) {
        this.tenkhachsan = tenkhachsan;
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

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getSodem() {
        return sodem;
    }

    public void setSodem(int sodem) {
        this.sodem = sodem;
    }
}
