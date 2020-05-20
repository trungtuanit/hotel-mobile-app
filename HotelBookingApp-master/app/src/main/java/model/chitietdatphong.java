package model;

import java.io.Serializable;

public class chitietdatphong implements Serializable {
    public Integer Id;
    public Integer madonhang;
    public Integer IdKhachSan;
    public String tenkhachsan;
    public int maphong;
    public String tenphong;
    public Integer giaphong;
    public int slphong;
    public int sodem;
    public String ngaynhanphong;
    public String ngaytraphong;
    public String dichvu;
    public int trangthai;
    public String hinhanh;

    public chitietdatphong(Integer id, Integer madonhang, Integer idKhachSan, String tenkhachsan, int maphong, String tenphong, Integer giaphong, int slphong, int sodem, String ngaynhanphong, String ngaytraphong, String dichvu, int trangthai, String hinhanh) {
        Id = id;
        this.madonhang = madonhang;
        IdKhachSan = idKhachSan;
        this.tenkhachsan = tenkhachsan;
        this.maphong = maphong;
        this.tenphong = tenphong;
        this.giaphong = giaphong;
        this.slphong = slphong;
        this.sodem = sodem;
        this.ngaynhanphong = ngaynhanphong;
        this.ngaytraphong = ngaytraphong;
        this.dichvu = dichvu;
        this.trangthai = trangthai;
        this.hinhanh = hinhanh;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getMadonhang() {
        return madonhang;
    }

    public void setMadonhang(Integer madonhang) {
        this.madonhang = madonhang;
    }

    public Integer getIdKhachSan() {
        return IdKhachSan;
    }

    public void setIdKhachSan(Integer idKhachSan) {
        IdKhachSan = idKhachSan;
    }

    public String getTenkhachsan() {
        return tenkhachsan;
    }

    public void setTenkhachsan(String tenkhachsan) {
        this.tenkhachsan = tenkhachsan;
    }

    public int getMaphong() {
        return maphong;
    }

    public void setMaphong(int maphong) {
        this.maphong = maphong;
    }

    public String getTenphong() {
        return tenphong;
    }

    public void setTenphong(String tenphong) {
        this.tenphong = tenphong;
    }

    public Integer getGiaphong() {
        return giaphong;
    }

    public void setGiaphong(Integer giaphong) {
        this.giaphong = giaphong;
    }

    public int getSlphong() {
        return slphong;
    }

    public void setSlphong(int slphong) {
        this.slphong = slphong;
    }

    public int getSodem() {
        return sodem;
    }

    public void setSodem(int sodem) {
        this.sodem = sodem;
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

    public String getDichvu() {
        return dichvu;
    }

    public void setDichvu(String dichvu) {
        this.dichvu = dichvu;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
