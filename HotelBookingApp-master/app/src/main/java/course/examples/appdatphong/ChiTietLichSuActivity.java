package course.examples.appdatphong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import activity.MainActivity;
import model.chitietdatphong;
import ultil.check_connection;
import ultil.server;

public class ChiTietLichSuActivity extends AppCompatActivity {
    Toolbar tbChitietLichsu;
    ImageView imgPhong;
    TextView tvTenKS, tvTenPhong, tvGia, tvSLPhong, tvSoDem, tvNgayNhanPhong, tvNgayTraPhong, tvDichVu;
    Button btnHuyDatPhong;

    int idchitiet = 0;
    String hinhanh = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_lich_su);
        if (check_connection.haveNetworkConnection(getApplicationContext())) {
            Anhxa();
            GetIdChitietdonhang();
            GetHinhanhphong();
            ActionToolbar();
            GetData();
            EventButton();
        } else {
            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối Internet");
            finish();
        }
    }

    private void EventButton() {
        btnHuyDatPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdan_huydonhang, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", String.valueOf(idchitiet));
                        return hashMap;
                    }
                };
                requestQueue.add(stringRequest);
                check_connection.ShowToast_Short(getApplicationContext(), "Bạn đã hủy đặt phòng thành công!");
                Intent intent = new Intent(ChiTietLichSuActivity.this, LichSuDatPhongActivity.class);
                intent.putExtra("idtaikhoan", MainActivity.idtaikhoan);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void GetData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdan_chitietlichsu, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    int id = 0;
                    String tenkhachsan = "";
                    String tenphong = "";
                    int giaphong = 0;
                    int soluongphong = 0;
                    int sodem = 0;
                    String ngaynhanphong = "";
                    String ngaytraphong = "";
                    String dichvu = "";
                    int trangthai = 0;
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenkhachsan = jsonObject.getString("tenkhachsan");
                            tenphong = jsonObject.getString("tenphong");
                            giaphong = jsonObject.getInt("giaphong");
                            soluongphong = jsonObject.getInt("soluongphong");
                            sodem = jsonObject.getInt("sodem");
                            ngaynhanphong = jsonObject.getString("ngaynhanphong");
                            ngaytraphong = jsonObject.getString("ngaytraphong");
                            dichvu = jsonObject.getString("dichvu");
                            trangthai = jsonObject.getInt("trangthai");
                            if (id == idchitiet) {
                                LoadData(tenkhachsan, tenphong, giaphong, soluongphong, sodem, ngaynhanphong, ngaytraphong, dichvu, trangthai);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("id", String.valueOf(idchitiet));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void LoadData(String tenkhachsan, String tenphong, int giaphong, int soluongphong, int sodem, String ngaynhanphong, String ngaytraphong, String dichvu, int trangthai) {
        tvTenKS.setText(tenkhachsan);
        tvTenPhong.setText(tenphong);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvGia.setText("Giá: " + decimalFormat.format(giaphong) + " VND");
        tvSLPhong.setText("Số lượng phòng: " + soluongphong);
        tvSoDem.setText("Số đêm: " + sodem);
        tvNgayNhanPhong.setText("Ngày nhận phòng: " + ngaynhanphong);
        tvNgayTraPhong.setText("Ngày trả phòng: " + ngaytraphong);
        if (dichvu.equals("")) {
            tvDichVu.setText("Dịch vụ: Không có");
        } else {
            tvDichVu.setText("Dịch vụ: " + dichvu);
        }
        Picasso.with(this).load(hinhanh)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(imgPhong);
    }

    private void ActionToolbar() {
        setSupportActionBar(tbChitietLichsu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbChitietLichsu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GetHinhanhphong() {
        hinhanh = getIntent().getStringExtra("hinhanhphong");
    }

    private void GetIdChitietdonhang() {
        idchitiet = getIntent().getIntExtra("idchitiet", -1);
    }

    private void Anhxa() {
        tbChitietLichsu = (Toolbar) findViewById(R.id.toolbar_chitietlichsu);
        imgPhong = (ImageView) findViewById(R.id.img_chitiethinhanh_lichsu);
        tvTenKS = (TextView) findViewById(R.id.tv_tenchitietkhachsan_lichsu);
        tvTenPhong = (TextView) findViewById(R.id.tv_tenchitietphong_lichsu);
        tvGia = (TextView) findViewById(R.id.tv_giachitiet_lichsu);
        tvSLPhong = (TextView) findViewById(R.id.tv_soluongphong_lichsu);
        tvSoDem = (TextView) findViewById(R.id.tv_sodem_lichsu);
        tvNgayNhanPhong = (TextView) findViewById(R.id.tv_ngaynhanphong_lichsu);
        tvNgayTraPhong = (TextView) findViewById(R.id.tv_ngaytraphong_lichsu);
        tvDichVu = (TextView) findViewById(R.id.tv_dichvu_lichsu);
        btnHuyDatPhong = (Button) findViewById(R.id.btn_huydatphong);
    }
}
