package course.examples.appdatphong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ultil.check_connection;
import ultil.server;

public class DangKyActivity extends AppCompatActivity {
    EditText etTenDangNhap, etMatKhau, etKTMatKhau, etHoten, etSdt, etEmail;
    Toolbar toolbarDangki;
    Button btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        if (check_connection.haveNetworkConnection(getApplicationContext())) {
            Anhxa();
            ActionToolbar();
            EventClickButton();
        } else {
            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối Internet");
            finish();
        }
    }

    public static boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
    private void EventClickButton() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tendangnhap = etTenDangNhap.getText().toString().trim();
                final String matkhau = etMatKhau.getText().toString().trim();
                String ktmatkhau = etKTMatKhau.getText().toString().trim();
                final String hoten = etHoten.getText().toString().trim();
                final String sdt = etSdt.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();

                //Kiểm tra nhập đầy đủ thông tin
                if (tendangnhap.length() > 0 && matkhau.length() > 0 && ktmatkhau.length() > 0 &&
                        hoten.length() > 0 && sdt.length() > 0 && email.length() > 0) {

                    //Kiểm tra email hợp lệ
                    if (isEmailValid(email) == false)
                    {
                        check_connection.ShowToast_Short(getApplicationContext(), "Địa chỉ email không hợp lệ!");
                        etEmail.setText("");
                    }

                    //Kiểm tra nhập lại mật khẩu
                     else if (ktmatkhau.equals(matkhau)) {
                        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.duongdan_taikhoan,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        if (response != null) {
                                            //Duyệt dữ liệu để so sánh
                                            for (int i = 0; i < response.length(); i++) {
                                                try {
                                                    JSONObject jsonObject = response.getJSONObject(i);
                                                    String db_tendangnhap = jsonObject.getString("tendangnhap");
                                                    String db_sdt = jsonObject.getString("sodienthoai");
                                                    String db_email = jsonObject.getString("email");
                                                    if (db_tendangnhap.equals(tendangnhap)) {
                                                        check_connection.ShowToast_Short(getApplicationContext(), "Tên đăng nhập đã được sử dụng");
                                                        etTenDangNhap.setText("");
                                                        break;
                                                    }
                                                    if (db_sdt.equals(sdt)) {
                                                        check_connection.ShowToast_Short(getApplicationContext(), "Số điện thoại đã được sử dụng");
                                                        etSdt.setText("");
                                                        break;
                                                    }
                                                    if (db_email.equals(email)) {
                                                        check_connection.ShowToast_Short(getApplicationContext(), "Địa chỉ email đã được sử dụng");
                                                        etEmail.setText("");
                                                        break;
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                if(i==response.length()-1){
                                                    PostDataToRegister(tendangnhap, matkhau, hoten, sdt, email);
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
                        check_connection.ShowToast_Short(getApplicationContext(), "Nhập lại mật khẩu không chính xác!");
                        etKTMatKhau.setText("");
                    }
                } else {
                    check_connection.ShowToast_Short(getApplicationContext(), "Hãy điền đầy đủ thông tin!");
                }
            }
        });
    }

    public void PostDataToRegister(final String tendangnhap, final String matkhau, final String hoten, final String sdt, final String email) {
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdan_inserttaikhoan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mataikhoan", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("tendangnhap", tendangnhap);
                hashMap.put("matkhau", matkhau);
                hashMap.put("hoten", hoten);
                hashMap.put("sodienthoai", sdt);
                hashMap.put("email", email);
                return hashMap;
            }
        };
        requestQueue2.add(stringRequest);
        check_connection.ShowToast_Short(getApplicationContext(), "Đăng ký thành công");
        Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
        startActivity(intent);
        finish();
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarDangki);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDangki.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        etTenDangNhap = (EditText) findViewById(R.id.et_nhap_ten_dang_nhap);
        etMatKhau = (EditText) findViewById(R.id.et_nhap_mat_khau);
        etKTMatKhau = (EditText) findViewById(R.id.et_nhap_lai_mat_khau);
        etHoten = (EditText) findViewById(R.id.et_nhap_hoten);
        etSdt = (EditText) findViewById(R.id.et_nhap_sdt);
        etEmail = (EditText) findViewById(R.id.et_nhap_email);
        toolbarDangki = (Toolbar) findViewById(R.id.toolbar_dang_ky);
        btnDangKy = (Button) findViewById(R.id.btn_dang_ky);
    }
}
