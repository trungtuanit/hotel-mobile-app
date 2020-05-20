package course.examples.appdatphong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

import adapter.loaiphong_adapter;
import model.loaiphong;
import model.sanpham;
import ultil.check_connection;
import ultil.server;

public class PhongActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    Toolbar toolbarphong;
    ListView listViewphong;
    TextView tvtenkhachsan;
    loaiphong_adapter loaiphong_adapter;
    ArrayList<loaiphong> arr_loaiphong;
    int idkhachsan = 0;
    sanpham khachsan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong);
        GetIdloaisp();
        Anhxa();
        if (check_connection.haveNetworkConnection(getApplicationContext()))
        {
            ActionToolbar();
            GetData();
        }
        else
        {
            check_connection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối Internet");
            finish();
        }
    }

    private void GetIdloaisp() {
        khachsan = (sanpham) getIntent().getSerializableExtra("khachsan");
        idkhachsan = khachsan.getId();
    }

    private void GetData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = server.duongdan_loaiphong;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int Id = 0;
                String Tenloaiphong = "";
                String Sogiuong = "";
                int Sokhach = 0;
                String Hinhanh = "";
                int Giaphong = 0;
                int Idkhachsan = 0;
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Id = jsonObject.getInt("id");
                            Tenloaiphong = jsonObject.getString("tenloaiphong");
                            Sogiuong = jsonObject.getString("sogiuong");
                            Sokhach = jsonObject.getInt("sokhach");
                            Hinhanh = jsonObject.getString("hinhanh");
                            Giaphong = jsonObject.getInt("giaphong");
                            Idkhachsan = jsonObject.getInt("idkhachsan");

                            arr_loaiphong.add(new loaiphong(Id, Tenloaiphong, Sogiuong, Sokhach, Hinhanh, Giaphong, Idkhachsan, 0));
                            loaiphong_adapter.notifyDataSetChanged();
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
                param.put("idkhachsan", String.valueOf(idkhachsan));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarphong);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarphong.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cart, menu);
        MenuItem searchItem = menu.findItem(R.id.menusearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<loaiphong> newList = new ArrayList<>();
        for (loaiphong item : arr_loaiphong){
            if (item.getTenloaiphong().toLowerCase().contains(userInput)){
                newList.add(item);
            }
        }
        loaiphong_adapter.updateArrayList(newList);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menucart:
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void Anhxa() {
        toolbarphong = (Toolbar) findViewById(R.id.toolbarchonphong);
        listViewphong = (ListView) findViewById(R.id.listviewchonphong);
        arr_loaiphong = new ArrayList<>();
        loaiphong_adapter = new loaiphong_adapter(getApplicationContext(), arr_loaiphong, khachsan);
        listViewphong.setAdapter(loaiphong_adapter);
        tvtenkhachsan = (TextView) findViewById(R.id.tvtenkhachsan);
        tvtenkhachsan.setText(khachsan.getTensanpham());
    }
}
