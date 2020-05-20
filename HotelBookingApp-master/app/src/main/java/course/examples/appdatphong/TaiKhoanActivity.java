package course.examples.appdatphong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ultil.check_connection;
import ultil.server;

public class TaiKhoanActivity extends AppCompatActivity {
    Toolbar toolbarTK;
    TextView tvTenDangNhap;
    TextView tvMatKhau;
    static TextView tvHoTen;
    static TextView tvSdt;
    public static TextView tvEmail;
    Button btnDangXuat, btnThayDoi, btnLichsu;

    static int id = 0;
    int db_id = 0;
    String db_tendangnhap = "";
    String db_matkhau = "";
    String db_hoten = "";
    String db_email = "";
    String db_sdt = "";

    String hotenmoi = "";
    String sdtmoi = "";
    String emailmoi = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);

        if (check_connection.haveNetworkConnection(getApplicationContext())) {
            getIdtaikhoan();
            Anhxa();
            eventButton();
            ActionToolbar();
            getData();
        } else {
            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối Internet");
            finish();
        }
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.duongdan_taikhoan,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    db_id = jsonObject.getInt("id");
                                    db_tendangnhap = jsonObject.getString("tendangnhap");
                                    db_matkhau = jsonObject.getString("matkhau");
                                    db_hoten = jsonObject.getString("hoten");
                                    db_email = jsonObject.getString("email");
                                    db_sdt = jsonObject.getString("sodienthoai");

                                    if (db_id == id) {
                                        tvTenDangNhap.setText(db_tendangnhap);
                                        tvMatKhau.setText(db_matkhau);
                                        tvHoTen.setText(db_hoten);
                                        tvEmail.setText(db_email);
                                        tvSdt.setText(db_sdt);
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        check_connection.ShowToast_Short(getApplicationContext(), error.toString());
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarTK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTK.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Đăng xuất
    private void eventButton() {
        btnLichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaiKhoanActivity.this, LichSuDatPhongActivity.class);
                intent.putExtra("idtaikhoan", id);
                startActivity(intent);
            }
        });

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaiKhoanActivity.this, DangNhapActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnThayDoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(TaiKhoanActivity.this);
                final View view = li.inflate(R.layout.custom_dialog_kt_matkhau_cu, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(TaiKhoanActivity.this);
                builder.setView(view);
                builder.setCancelable(false);
                final AlertDialog alertDialog = builder.create();

                final EditText etMatKhauCu = (EditText) view.findViewById(R.id.et_mat_khau_cu);
                Button btnDoiMatKhau = (Button) view.findViewById(R.id.btn_doi_mat_khau);
                Button btnDoiThongTin = (Button) view.findViewById(R.id.btn_doi_thong_tin_tk);
                Button btnThoat = (Button) view.findViewById(R.id.btn_thoat);

                //Nhấn nút Đổi mật khẩu chuyển sang dialog tạo mật khẩu mới
                btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etMatKhauCu.getText().toString().trim().equals(db_matkhau)) {
                            View view1 = LayoutInflater.from(TaiKhoanActivity.this).inflate(R.layout.custom_dialog_tao_matkhau_moi, null);
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(TaiKhoanActivity.this);
                            builder1.setView(view1);
                            builder1.setCancelable(false);
                            final AlertDialog alertDialog1 = builder1.create();

                            final EditText etNhapMatKhauMoi = (EditText) view1.findViewById(R.id.et_nhap_mat_khau_moi);
                            final EditText etKTMatKhauMoi = (EditText) view1.findViewById(R.id.et_kt_mat_khau_moi);
                            Button btnXacNhanDoiMatKhau = (Button) view1.findViewById(R.id.btn_xac_nhan_doi_matkhau);
                            Button btnThoatDoiMatKhau = (Button) view1.findViewById(R.id.btn_thoat_doi_matkhau);

                            btnXacNhanDoiMatKhau.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (etNhapMatKhauMoi.getText().toString().length() > 0 && etKTMatKhauMoi.getText().toString().length() > 0) {
                                        if (etKTMatKhauMoi.getText().toString().trim().equals(etNhapMatKhauMoi.getText().toString().trim())) {
                                            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdan_updatematkhau,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {

                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                        }
                                                    }) {
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("id", String.valueOf(id));
                                                    hashMap.put("matkhau", etNhapMatKhauMoi.getText().toString().trim());
                                                    return hashMap;
                                                }
                                            };
                                            requestQueue.add(stringRequest);
                                            check_connection.ShowToast_Short(getApplicationContext(), "Thay đổi mật khẩu thành công");
                                            alertDialog1.cancel();
                                            alertDialog.cancel();
                                            getData();   //cập nhật lại thông tin sau khi đổi mật khẩu
                                        } else {
                                            check_connection.ShowToast_Short(getApplicationContext(), "Nhập lại mật khẩu mới không chính xác!");
                                        }
                                    } else {
                                        check_connection.ShowToast_Short(getApplicationContext(), "Hãy nhập đầy đủ thông tin!");
                                    }
                                }
                            });
                            btnThoatDoiMatKhau.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog1.cancel();
                                    alertDialog.cancel();
                                }
                            });

                            alertDialog1.show();
                        } else {
                            check_connection.ShowToast_Short(getApplicationContext(), "Sai mật khẩu!");
                        }
                    }
                });

                //Nhấn nút đổi thông tin tài khoản chuyển sang dialog đổi thông tin
                btnDoiThongTin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etMatKhauCu.getText().toString().trim().equals(db_matkhau)) {
                            View view2 = LayoutInflater.from(TaiKhoanActivity.this).inflate(R.layout.custom_dialog_doi_thong_tin, null);
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(TaiKhoanActivity.this);
                            builder2.setView(view2);
                            builder2.setCancelable(false);
                            final AlertDialog alertDialog2 = builder2.create();

                            final EditText etHoTenMoi = (EditText) view2.findViewById(R.id.et_hoten_moi);
                            final EditText etSdtMoi = (EditText) view2.findViewById(R.id.et_sdt_moi);
                            final EditText etEmailMoi = (EditText) view2.findViewById(R.id.et_email_moi);
                            final Button btnXacNhanDoiThongTin = (Button) view2.findViewById(R.id.btn_xac_nhan_doi_thong_tin);
                            Button btnThoatDoiThongTin = (Button) view2.findViewById(R.id.btn_thoat_doi_thong_tin);

                            btnXacNhanDoiThongTin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (etHoTenMoi.getText().toString().trim().equals(db_hoten)) {
                                        check_connection.ShowToast_Short(getApplicationContext(), "Bạn đang dùng họ tên này");
                                        etHoTenMoi.setText("");
                                    }
                                    else if (etSdtMoi.getText().toString().trim().equals(db_sdt)) {
                                        check_connection.ShowToast_Short(getApplicationContext(), "Bạn đang dùng số điện thoại này");
                                        etSdtMoi.setText("");
                                    }
                                    else if (etEmailMoi.getText().toString().trim().equals(db_email)) {
                                        check_connection.ShowToast_Short(getApplicationContext(), "Bạn đang dùng địa chỉ email này");
                                        etEmailMoi.setText("");
                                    }

                                    //Kiểm tra email hợp lệ
                                    else if (etEmailMoi.getText().toString().length() > 0 && DangKyActivity.isEmailValid(etEmailMoi.getText().toString().trim()) == false)
                                    {
                                        check_connection.ShowToast_Short(getApplicationContext(), "Địa chỉ email không hợp lệ!");
                                        etEmailMoi.setText("");
                                    }
                                    else if (etHoTenMoi.getText().toString().length() > 0 || etSdtMoi.getText().toString().length() > 0 || etEmailMoi.getText().toString().length() > 0) {
                                        hotenmoi = etHoTenMoi.getText().toString().trim();

                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.duongdan_taikhoan,
                                                new Response.Listener<JSONArray>() {
                                                    @Override
                                                    public void onResponse(JSONArray response) {
                                                        if (response != null) {
                                                            String dbSdt = "";
                                                            String dbEmail = "";
                                                            for (int i = 0; i < response.length(); i++) {
                                                                try {
                                                                    JSONObject jsonObject = response.getJSONObject(i);
                                                                    dbSdt = jsonObject.getString("sodienthoai");
                                                                    dbEmail = jsonObject.getString("email");
                                                                    if (etSdtMoi.getText().toString().trim().equals(dbSdt)) {
                                                                        check_connection.ShowToast_Short(getApplicationContext(), "Số điện thoại này đã được sử dụng!");
                                                                        etSdtMoi.setText("");
                                                                        break;
                                                                    } else if (etEmailMoi.getText().toString().trim().equals(dbEmail)) {
                                                                        check_connection.ShowToast_Short(getApplicationContext(), "Địa chỉ email này đã được sử dụng!");
                                                                        etEmailMoi.setText("");
                                                                        break;
                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                if (i == response.length() - 1) {
                                                                    sdtmoi = etSdtMoi.getText().toString().trim();
                                                                    emailmoi = etEmailMoi.getText().toString().trim();
                                                                    final RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                                                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, server.duongdan_updatethongtin,
                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {

                                                                                }
                                                                            },
                                                                            new Response.ErrorListener() {
                                                                                @Override
                                                                                public void onErrorResponse(VolleyError error) {

                                                                                }
                                                                            }) {
                                                                        @Override
                                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                                            HashMap<String, String> hashMap = new HashMap<>();
                                                                            hashMap.put("id", String.valueOf(id));
                                                                            hashMap.put("hoten", hotenmoi);
                                                                            hashMap.put("sodienthoai", sdtmoi);
                                                                            hashMap.put("email", emailmoi);
                                                                            return hashMap;
                                                                        }
                                                                    };
                                                                    requestQueue1.add(stringRequest1);
                                                                    check_connection.ShowToast_Short(getApplicationContext(), "Thay đổi thông tin thành công");
                                                                    getData();
                                                                    alertDialog2.cancel();
                                                                    alertDialog.cancel();
                                                                }
                                                            }
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                    }
                                                });
                                        requestQueue.add(jsonArrayRequest);

                                    } else {
                                        check_connection.ShowToast_Short(getApplicationContext(), "Hãy nhập ít nhất  một thông tin mới!");
                                    }
                                }
                            });
                            btnThoatDoiThongTin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog2.cancel();
                                    alertDialog.cancel();
                                }
                            });

                            alertDialog2.show();
                        } else {
                            check_connection.ShowToast_Short(getApplicationContext(), "Sai mật khẩu!");
                        }
                    }
                });

                //thoát dialog
                btnThoat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                alertDialog.show();
            }
        });
    }

    private void Anhxa() {
        toolbarTK = (Toolbar) findViewById(R.id.toolbar_taikhoan);
        tvTenDangNhap = (TextView) findViewById(R.id.tv_ten_dang_nhap);
        tvMatKhau = (TextView) findViewById(R.id.tv_mat_khau);
        tvHoTen = (TextView) findViewById(R.id.tv_hoten);
        tvSdt = (TextView) findViewById(R.id.tv_sdt);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvEmail.setMovementMethod(new ScrollingMovementMethod());
        btnDangXuat = (Button) findViewById(R.id.btn_dang_xuat);
        btnThayDoi = (Button) findViewById(R.id.btn_thay_doi);
        btnLichsu= (Button) findViewById(R.id.btn_history);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_onlycart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menucart:
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getIdtaikhoan() {
        id = getIntent().getIntExtra("idtaikhoan", -1);
    }

}
