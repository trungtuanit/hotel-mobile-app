package course.examples.appdatphong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.lichsugiaodich_adapter;
import model.chitietdatphong;
import model.sanpham;
import ultil.check_connection;
import ultil.server;

public class LichSuDatPhongActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    Toolbar tbLichSu;
    ListView lvLichSu;
    lichsugiaodich_adapter lichsugiaodich_adapter;
    ArrayList<chitietdatphong> arr_chitietdatphong;
    int idtaikhoan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_dat_phong);
        if (check_connection.haveNetworkConnection(getApplicationContext())) {
            Anhxa();
            GetIdTaikhoan();
            ActionToolbar();
            GetData();
            LoadMoreData();
        } else {
            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối Internet");
            finish();
        }
    }

    private void LoadMoreData() {
        lvLichSu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(arr_chitietdatphong.get(position).getTrangthai() == 0){
                    Intent intent = new Intent(getApplicationContext(), ChiTietLichSuActivity.class);
                    intent.putExtra("idchitiet", arr_chitietdatphong.get(position).getId());
                    intent.putExtra("hinhanhphong", arr_chitietdatphong.get(position).getHinhanh());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), ChiTietDaHuyActivity.class);
                    intent.putExtra("idchitiet", arr_chitietdatphong.get(position).getId());
                    intent.putExtra("hinhanhphong", arr_chitietdatphong.get(position).getHinhanh());
                    startActivity(intent);
                }
            }
        });
    }

    private void GetIdTaikhoan() {
        idtaikhoan = getIntent().getIntExtra("idtaikhoan", -1);
        Log.d("idtaikhoan: ", idtaikhoan + "");
    }

    private void GetData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdan_lichsudatphong, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    int id = 0;
                    int madonhang = 0;
                    int idkhachsan = 0;
                    String tenkhachsan = "";
                    int maphong = 0;
                    String tenphong = "";
                    int giaphong = 0;
                    int soluongphong = 0;
                    int sodem = 0;
                    String ngaynhanphong = "";
                    String ngaytraphong = "";
                    String dichvu = "";
                    int trangthai = 0;
                    String hinhanh = "";
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            madonhang = jsonObject.getInt("madonhang");
                            idkhachsan = jsonObject.getInt("idkhachsan");
                            tenkhachsan = jsonObject.getString("tenkhachsan");
                            maphong = jsonObject.getInt("maphong");
                            tenphong = jsonObject.getString("tenphong");
                            giaphong = jsonObject.getInt("giaphong");
                            soluongphong = jsonObject.getInt("soluongphong");
                            sodem = jsonObject.getInt("sodem");
                            ngaynhanphong = jsonObject.getString("ngaynhanphong");
                            ngaytraphong = jsonObject.getString("ngaytraphong");
                            dichvu = jsonObject.getString("dichvu");
                            trangthai = jsonObject.getInt("trangthai");
                            hinhanh = jsonObject.getString("hinhanh");
                            arr_chitietdatphong.add(new chitietdatphong(id, madonhang, idkhachsan, tenkhachsan, maphong, tenphong, giaphong, soluongphong, sodem, ngaynhanphong, ngaytraphong, dichvu, trangthai, hinhanh));
                            lichsugiaodich_adapter.notifyDataSetChanged();
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
                param.put("id_user", String.valueOf(idtaikhoan));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(tbLichSu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbLichSu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        tbLichSu = (Toolbar) findViewById(R.id.toolbar_lichsu);
        lvLichSu = (ListView) findViewById(R.id.listviewlichsu);
        arr_chitietdatphong = new ArrayList<>();
        lichsugiaodich_adapter = new lichsugiaodich_adapter(getApplicationContext(), arr_chitietdatphong);
        lvLichSu.setAdapter(lichsugiaodich_adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<chitietdatphong> newList = new ArrayList<>();
        for (chitietdatphong item : arr_chitietdatphong) {
            if (item.getTenkhachsan().toLowerCase().contains(userInput)) {
                newList.add(item);
            }
        }
        lichsugiaodich_adapter.updateArrayList(newList);
        return true;
    }
}
