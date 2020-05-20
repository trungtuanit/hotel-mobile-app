package model;

public class loaisp {
    public  int id;
    public String tenloaisp;
    public String hinhanhloaisp;

    public loaisp(int id, String tenloaisp, String hinhanhloaisp) {
        this.id = id;
        this.tenloaisp = tenloaisp;
        this.hinhanhloaisp = hinhanhloaisp;
    }

    public int getId() {
        return id;
    }

    public String getTenloaisp() {
        return tenloaisp;
    }

    public String getHinhanhloaisp() {
        return hinhanhloaisp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTenloaisp(String tenloaisp) {
        this.tenloaisp = tenloaisp;
    }

    public void setHinhanhloaisp(String hinhanhloaisp) {
        this.hinhanhloaisp = hinhanhloaisp;
    }
}
