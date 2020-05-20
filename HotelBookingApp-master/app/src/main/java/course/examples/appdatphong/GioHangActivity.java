package course.examples.appdatphong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

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

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import activity.MainActivity;
import adapter.giohang_adapter;
import ultil.check_connection;
import ultil.server;


public class GioHangActivity extends AppCompatActivity {

    ListView lvgiohang;
    TextView tvthongbao;
    static TextView tvtongtien;
    Button btthanhtoan, bttieptucchonkhachsan;
    Toolbar toolbargiohang;
    giohang_adapter giohang_adapter;
    public static final int REQUEST_CODE_EXAMPLE = 0x9345;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


    int db_id = 0;
    String db_hoten = "";
    String db_sdt = "";
    String db_email = "";

    public String hoten = "";
    public String sdt = "";
    public String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        Anhxa();
        GetDataTaiKhoan();
        ActionToolbar();
        CheckData();
        EvenUltil();
        CactchOnItemListView();
        EvenButton();
    }

    private void GetDataTaiKhoan() {
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(server.duongdan_taikhoan,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    db_id = jsonObject.getInt("id");
                                    db_hoten = jsonObject.getString("hoten");
                                    db_email = jsonObject.getString("email");
                                    db_sdt = jsonObject.getString("sodienthoai");
                                    if (db_id == MainActivity.idtaikhoan) {
                                        hoten = db_hoten;
                                        sdt = db_sdt;
                                        email = db_email;
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
        requestQueue2.add(jsonArrayRequest2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EXAMPLE) {
            if (resultCode == Activity.RESULT_OK) {
                giohang_adapter.notifyDataSetChanged();
            } else {

            }
        }
    }

    private void EvenButton() {
        bttieptucchonkhachsan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("idtaikhoan", MainActivity.idtaikhoan);
                startActivity(intent);
            }
        });

        btthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEnterenoughinformation = true;
                if (MainActivity.arr_giohang.size() > 0) {
                    for (int i = 0; i < MainActivity.arr_giohang.size(); i++) {
                        if (MainActivity.arr_giohang.get(i).getNgaynhanphong() == null || MainActivity.arr_giohang.get(i).getNgaytraphong() == null) {
                            check_connection.ShowToast_Short(getApplicationContext(), "Bạn chưa chọn ngày cho tất cả các phòng");
                            isEnterenoughinformation = false;
                        }
                    }
                    if (isEnterenoughinformation) {
                        View view = LayoutInflater.from(GioHangActivity.this).inflate(R.layout.custom_dialog_thanh_toan, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(GioHangActivity.this);
                        builder.setView(view);
                        builder.setCancelable(false);
                        final AlertDialog alertDialog = builder.create();

                        Button btnDongY = (Button) view.findViewById(R.id.btn_dong_y_thay_doi_thong_tin);
                        Button btnKhongDongY = (Button) view.findViewById(R.id.btn_khong_thay_doi_thong_tin);
                        Button btnTroVe = (Button) view.findViewById(R.id.btn_tro_ve);

                        btnTroVe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });

                        btnKhongDongY.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EvenBtnYesDialog();
                            }
                        });

                        btnDongY.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), ThongTinKhachHangActivity.class);
                                startActivity(intent);
                                alertDialog.cancel();
                            }
                        });

                        alertDialog.show();
                    }
                } else {
                    check_connection.ShowToast_Short(getApplicationContext(), "Giỏ hàng của bạn chưa có sản phẩm để thanh toán");
                }
            }
        });
    }

    private void EvenBtnYesDialog() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdan_donhang, new Response.Listener<String>() {
            @Override
            public void onResponse(final String madonhang) {
                Log.d("madonhang", madonhang);
                if (Integer.parseInt((madonhang)) > 0) {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest request = new StringRequest(Request.Method.POST, server.duongdan_chitietdonhang, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("1")) {
                                MainActivity.arr_giohang.clear();
                                check_connection.ShowToast_Short(getApplicationContext(), "Bạn đã đặt phòng khách sạn thành công");
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("idtaikhoan", MainActivity.idtaikhoan);
                                startActivity(intent);
                                check_connection.ShowToast_Short(getApplicationContext(), "Mời bạn tiếp tục chọn khách sạn");
                            } else {
                                check_connection.ShowToast_Short(getApplicationContext(), "Dữ liệu giỏ hàng của bạn bị lỗi");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            JSONArray jsonArray = new JSONArray();
                            for (int i = 0; i < MainActivity.arr_giohang.size(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("madonhang", madonhang);
                                    jsonObject.put("idkhachsan", MainActivity.arr_giohang.get(i).getIdkhachsan());
                                    jsonObject.put("tenkhachsan", MainActivity.arr_giohang.get(i).getTenkhachsan());
                                    jsonObject.put("maphong", MainActivity.arr_giohang.get(i).getIdphong());
                                    jsonObject.put("tenphong", MainActivity.arr_giohang.get(i).getTenloaiphong());
                                    jsonObject.put("giaphong", MainActivity.arr_giohang.get(i).getGiaphong() / MainActivity.arr_giohang.get(i).getSoluong());
                                    jsonObject.put("soluongphong", MainActivity.arr_giohang.get(i).getSoluong());
                                    jsonObject.put("sodem", MainActivity.arr_giohang.get(i).getSodem());
                                    jsonObject.put("ngaynhanphong", MainActivity.arr_giohang.get(i).getNgaynhanphong());
                                    jsonObject.put("ngaytraphong", MainActivity.arr_giohang.get(i).getNgaytraphong());
                                    jsonObject.put("dichvu", MainActivity.arr_giohang.get(i).getDichvu());
                                    jsonObject.put("trangthai", 0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                jsonArray.put(jsonObject);
                            }
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("json", jsonArray.toString());
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("tenkhachhang", hoten);
                hashMap.put("sodienthoai", sdt);
                hashMap.put("email", email);
                hashMap.put("idtaikhoan", Integer.toString(MainActivity.idtaikhoan));
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void CactchOnItemListView() {
        lvgiohang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GioHangActivity.this);
                builder.setTitle("Xác nhận xóa phòng");
                builder.setMessage("Bạn có chắc muốn xóa phòng này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (MainActivity.arr_giohang.size() <= 0) {
                            tvthongbao.setVisibility(View.VISIBLE);
                        } else {
                            Date d_tra = formatter.parse(MainActivity.arr_giohang.get(position).getNgaytraphong(),new ParsePosition(0));
                            Date d_nhan = formatter.parse(MainActivity.arr_giohang.get(position).getNgaynhanphong(),new ParsePosition(0));
                            CalendarActivity.XoaNgay(d_nhan, d_tra);
                            MainActivity.arr_giohang.remove(position);
                            giohang_adapter.notifyDataSetChanged();
                            EvenUltil();
                            if (MainActivity.arr_giohang.size() <= 0) {
                                tvthongbao.setVisibility(View.VISIBLE);
                            } else {
                                tvthongbao.setVisibility(View.INVISIBLE);
                                giohang_adapter.notifyDataSetChanged();
                                EvenUltil();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        giohang_adapter.notifyDataSetChanged();
                        EvenUltil();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public static void EvenUltil() {
        long tongtien = 0;
        for (int i = 0; i < MainActivity.arr_giohang.size(); i++) {
            tongtien += MainActivity.arr_giohang.get(i).getGiaphong();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvtongtien.setText(decimalFormat.format(tongtien) + " VND");
    }

    private void CheckData() {
        if (MainActivity.arr_giohang.size() <= 0) {
            giohang_adapter.notifyDataSetChanged();
            tvthongbao.setVisibility(View.VISIBLE);
            lvgiohang.setVisibility(View.INVISIBLE);
        } else {
            giohang_adapter.notifyDataSetChanged();
            tvthongbao.setVisibility(View.INVISIBLE);
            lvgiohang.setVisibility(View.VISIBLE);
        }
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbargiohang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbargiohang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        lvgiohang = (ListView) findViewById(R.id.listview_giohang);
        tvthongbao = (TextView) findViewById(R.id.textview_thongbao);
        tvtongtien = (TextView) findViewById(R.id.textviewtongtien);
        btthanhtoan = (Button) findViewById(R.id.button_thanhtoangiohang);
        bttieptucchonkhachsan = (Button) findViewById(R.id.button_tieptucchonkhachsan);
        toolbargiohang = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_giohang);
        giohang_adapter = new giohang_adapter(GioHangActivity.this, MainActivity.arr_giohang);
        lvgiohang.setAdapter(giohang_adapter);
    }
}
