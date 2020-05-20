package course.examples.appdatphong;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import activity.MainActivity;
import ultil.check_connection;
import ultil.server;

public class DangNhapActivity extends AppCompatActivity {
    EditText etTenDangNhap, etMatKhau;
    Button btnDangNhap, btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        if (check_connection.haveNetworkConnection(getApplicationContext())) {
            Anhxa();
            EventClickButton();
        } else {
            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối Internet");
            finish();
        }
    }

    private void EventClickButton() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
                startActivity(intent);
                etTenDangNhap.setText("");
                etMatKhau.setText("");
                etTenDangNhap.requestFocus();
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tendangnhap = etTenDangNhap.getText().toString().trim();
                final String matkhau = etMatKhau.getText().toString().trim();
                if (tendangnhap.length() > 0 && matkhau.length() > 0) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.duongdan_taikhoan,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    if (response != null) {
                                        int db_id = 0;
                                        String db_tendangnhap = "";
                                        String db_matkhau = "";
                                        for (int i = 0; i < response.length(); i++) {
                                            try {
                                                JSONObject jsonObject = response.getJSONObject(i);
                                                db_id = jsonObject.getInt("id");
                                                db_tendangnhap = jsonObject.getString("tendangnhap");
                                                db_matkhau = jsonObject.getString("matkhau");
                                                if (db_tendangnhap.equals(tendangnhap) && db_matkhau.equals(matkhau)) {
                                                    Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                                                    intent.putExtra("idtaikhoan", db_id);
                                                    startActivity(intent);
                                                    finish();
                                                    break;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if (i == response.length() - 1 ) {
                                                check_connection.ShowToast_Short(getApplicationContext(), "Tên đăng nhập hoặc mật khẩu sai!");
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
                } else {
                    check_connection.ShowToast_Short(getApplicationContext(), "Hãy nhập đầy đủ thông tin!");
                }
            }
        });
    }

    private void Anhxa() {
        etTenDangNhap = (EditText) findViewById(R.id.et_ten_dang_nhap);
        etMatKhau = (EditText) findViewById(R.id.et_mat_khau);
        btnDangNhap = (Button) findViewById(R.id.btn_dang_nhap);
        btnDangKy = (Button) findViewById(R.id.btn_chuyen_dang_ky);
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
