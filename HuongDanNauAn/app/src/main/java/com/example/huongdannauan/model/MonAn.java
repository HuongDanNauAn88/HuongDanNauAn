package com.example.huongdannauan.model;

public class MonAn {
    int id;
    String tenMonAn, tenDayDu, hinhAnh;
    int diemSucKhoe, thoiGianChuanBi, thoiGianNau, soNguoiAnMotPhan;
    String moTa, huongDan, nguonUrl;

    public MonAn() {
    }

    public MonAn(int id, String tenMonAn, String hinhAnh) {
        this.id = id;
        this.tenMonAn = tenMonAn;
        this.hinhAnh = hinhAnh;
    }

    public MonAn(int id, String tenMonAn, String tenDayDu, String hinhAnh, int diemSucKhoe, int thoiGianChuanBi, int thoiGianNau, int soNguoiAnMotPhan, String moTa, String huongDan, String nguonUrl) {
        this.id = id;
        this.tenMonAn = tenMonAn;
        this.tenDayDu = tenDayDu;
        this.hinhAnh = hinhAnh;
        this.diemSucKhoe = diemSucKhoe;
        this.thoiGianChuanBi = thoiGianChuanBi;
        this.thoiGianNau = thoiGianNau;
        this.soNguoiAnMotPhan = soNguoiAnMotPhan;
        this.moTa = moTa;
        this.huongDan = huongDan;
        this.nguonUrl = nguonUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public String getTenDayDu() {
        return tenDayDu;
    }

    public void setTenDayDu(String tenDayDu) {
        this.tenDayDu = tenDayDu;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getDiemSucKhoe() {
        return diemSucKhoe;
    }

    public void setDiemSucKhoe(int diemSucKhoe) {
        this.diemSucKhoe = diemSucKhoe;
    }

    public int getThoiGianChuanBi() {
        return thoiGianChuanBi;
    }

    public void setThoiGianChuanBi(int thoiGianChuanBi) {
        this.thoiGianChuanBi = thoiGianChuanBi;
    }

    public int getThoiGianNau() {
        return thoiGianNau;
    }

    public void setThoiGianNau(int thoiGianNau) {
        this.thoiGianNau = thoiGianNau;
    }

    public int getSoNguoiAnMotPhan() {
        return soNguoiAnMotPhan;
    }

    public void setSoNguoiAnMotPhan(int soNguoiAnMotPhan) {
        this.soNguoiAnMotPhan = soNguoiAnMotPhan;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHuongDan() {
        return huongDan;
    }

    public void setHuongDan(String huongDan) {
        this.huongDan = huongDan;
    }

    public String getNguonUrl() {
        return nguonUrl;
    }

    public void setNguonUrl(String nguonUrl) {
        this.nguonUrl = nguonUrl;
    }
}
