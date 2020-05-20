package model;

import java.io.Serializable;

public class sanpham implements Serializable {
    public int Id;
    public String Tensanpham;
    public Integer Giasanpham;
    public String Hinhanhsanpham;
    public String mota;
    public int Idsanpham;
    public String Diachi;

    public sanpham(int id, String tensanpham, Integer giasanpham, String hinhanhsanpham, String mota, int idsanpham, String diachi) {
        Id = id;
        Tensanpham = tensanpham;
        Giasanpham = giasanpham;
        Hinhanhsanpham = hinhanhsanpham;
        this.mota = mota;
        Idsanpham = idsanpham;
        Diachi = diachi;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTensanpham() {
        return Tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        Tensanpham = tensanpham;
    }

    public Integer getGiasanpham() {
        return Giasanpham;
    }

    public void setGiasanpham(Integer giasanpham) {
        Giasanpham = giasanpham;
    }

    public String getHinhanhsanpham() {
        return Hinhanhsanpham;
    }

    public void setHinhanhsanpham(String hinhanhsanpham) {
        Hinhanhsanpham = hinhanhsanpham;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getIdsanpham() {
        return Idsanpham;
    }

    public void setIdsanpham(int idsanpham) {
        Idsanpham = idsanpham;
    }

    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String diachi) {
        Diachi = diachi;
    }
}
