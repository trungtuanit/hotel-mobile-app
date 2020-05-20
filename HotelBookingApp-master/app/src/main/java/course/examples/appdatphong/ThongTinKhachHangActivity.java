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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import activity.MainActivity;
import ultil.check_connection;
import ultil.server;

public class ThongTinKhachHangActivity extends AppCompatActivity {

    Toolbar toolbarthongtinkhachhang;
    EditText edttenkhachhang, edtemail, edtsdt;
    Button btnxacnhan, btntrove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_khach_hang);
        Anhxa();
        ActionToolbar();
        btntrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (check_connection.haveNetworkConnection(getApplicationContext()))
        {
            EventButton();
        } else
        {
            check_connection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối Internet");
        }
    }

    private void EventButton() {
        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ten = edttenkhachhang.getText().toString().trim();
                final String sdt = edtsdt.getText().toString().trim();
                final String email = edtemail.getText().toString().trim();
                //Kiểm tra email hợp lệ
                if (email.length() > 0 && DangKyActivity.isEmailValid(email) == false)
                {
                    check_connection.ShowToast_Short(getApplicationContext(), "Địa chỉ email không hợp lệ!");
                    edtemail.setText("");
                }
                else if (ten.length() > 0 && sdt.length() > 0 && email.length() > 0)
                {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdan_donhang, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String madonhang) {
                            Log.d("madonhang",madonhang);
                            if (Integer.parseInt((madonhang)) > 0)
                            {
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                StringRequest request = new StringRequest(Request.Method.POST, server.duongdan_chitietdonhang, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("1"))
                                        {
                                            MainActivity.arr_giohang.clear();
                                            check_connection.ShowToast_Short(getApplicationContext(),"Bạn đã đặt phòng khách sạn thành công");
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            intent.putExtra("idtaikhoan", MainActivity.idtaikhoan);
                                            startActivity(intent);
                                            check_connection.ShowToast_Short(getApplicationContext(),"Mời bạn tiếp tục chọn khách sạn");
                                        } else
                                        {
                                            check_connection.ShowToast_Short(getApplicationContext(),"Dữ liệu giỏ hàng của bạn bị lỗi");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                })
                                {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        JSONArray jsonArray = new JSONArray();
                                        for (int i = 0; i < MainActivity.arr_giohang.size(); i++)
                                        {
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("madonhang",madonhang);
                                                jsonObject.put("idkhachsan",MainActivity.arr_giohang.get(i).getIdkhachsan());
                                                jsonObject.put("tenkhachsan",MainActivity.arr_giohang.get(i).getTenkhachsan());
                                                jsonObject.put("maphong",MainActivity.arr_giohang.get(i).getIdphong());
                                                jsonObject.put("tenphong",MainActivity.arr_giohang.get(i).getTenloaiphong());
                                                jsonObject.put("giaphong",MainActivity.arr_giohang.get(i).getGiaphong() / MainActivity.arr_giohang.get(i).getSoluong());
                                                jsonObject.put("soluongphong",MainActivity.arr_giohang.get(i).getSoluong());
                                                jsonObject.put("sodem",MainActivity.arr_giohang.get(i).getSodem());
                                                jsonObject.put("ngaynhanphong",MainActivity.arr_giohang.get(i).getNgaynhanphong());
                                                jsonObject.put("ngaytraphong",MainActivity.arr_giohang.get(i).getNgaytraphong());
                                                jsonObject.put("dichvu", MainActivity.arr_giohang.get(i).getDichvu());
                                                jsonObject.put("trangthai", "0");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(jsonObject);
                                        }
                                        HashMap<String, String> hashMap = new HashMap<String, String>();
                                        hashMap.put("json",jsonArray.toString());
                                        return hashMap;
                                    }
                                };
                                queue.add(request);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> hashMap = new HashMap<String,String>();
                            hashMap.put("tenkhachhang",ten);
                            hashMap.put("sodienthoai",sdt);
                            hashMap.put("email",email);
                            hashMap.put("idtaikhoan", Integer.toString(MainActivity.idtaikhoan));
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                } else
                {
                    check_connection.ShowToast_Short(getApplicationContext(),"Hãy kiểm tra lại dữ liệu");
                }
            }
        });
    }

    private void Anhxa() {
        toolbarthongtinkhachhang = (Toolbar) findViewById(R.id.toolbardthongtinkhachhang);
        edttenkhachhang = (EditText) findViewById(R.id.edittext_tenkhachhang);
        edtemail = (EditText) findViewById(R.id.edittext_emailkhachhang);
        edtsdt = (EditText) findViewById(R.id.edittext_sdtkhachhang);
        btnxacnhan = (Button) findViewById(R.id.btxacnhan);
        btntrove = (Button) findViewById(R.id.bttrove);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarthongtinkhachhang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarthongtinkhachhang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
