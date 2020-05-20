package course.examples.appdatphong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.hochiminhadapter;
import model.sanpham;
import ultil.check_connection;
import ultil.server;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

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

public class HoChiMinhActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    Toolbar toolbarhcm;
    ListView listviewhcm;
    hochiminhadapter hochiminh_adapter;
    ArrayList<sanpham> arr_hcm;
    int idhcm = 0;
    int page = 1;

    View footerview;
    boolean isLoading = false;
    myHandler myHandler;
    boolean limitData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hochiminh);
        Anhxa();
        if (check_connection.haveNetworkConnection(getApplicationContext()))
        {
            GetIdloaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();
        }
        else
        {
            check_connection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối Internet");
            finish();
        }
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
        ArrayList<sanpham> newList = new ArrayList<>();
        for (sanpham item : arr_hcm){
            if (item.getTensanpham().toLowerCase().contains(userInput)){
                newList.add(item);
            }
        }
        hochiminh_adapter.updateArrayList(newList);
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

    private void LoadMoreData() {
        listviewhcm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietKhachSanActivity.class);
                intent.putExtra("thongtinsp",arr_hcm.get(position));
                startActivity(intent);
            }
        });

        listviewhcm.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstItem, int visibleItem, int totalItem) {
                if(firstItem + visibleItem  == totalItem && totalItem != 0 && isLoading == false && limitData == false) {
                    isLoading =  true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void GetData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongdan = server.duongdan_khachsan + String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int Id = 0;
                String Tenhcm = "";
                int Giahcm = 0;
                String Hinhanhhcm = "";
                String Mota = "";
                int Idsphcm = 0;
                String Diachi = "";
                if (response != null && response.length() != 2) {
                    listviewhcm.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Id = jsonObject.getInt("id");
                            Tenhcm = jsonObject.getString("tensp");
                            Giahcm = jsonObject.getInt("giasp");
                            Hinhanhhcm = jsonObject.getString("hinhanhsp");
                            Mota = jsonObject.getString("mota");
                            Idsphcm = jsonObject.getInt("idsp");
                            Diachi = jsonObject.getString("diachi");
                            arr_hcm.add(new sanpham(Id, Tenhcm, Giahcm, Hinhanhhcm, Mota, Idsphcm,Diachi));
                            hochiminh_adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    limitData = true;
                    listviewhcm.removeFooterView(footerview);
                    check_connection.ShowToast_Short(getApplicationContext(),"Đã hết dữ liệu");
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
                param.put("idsp", String.valueOf(idhcm));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarhcm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarhcm.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GetIdloaisp() {
        idhcm = getIntent().getIntExtra("idloaisp", -1);
        Log.d("giatriloaisp", idhcm + "");
    }

    private void Anhxa() {
        toolbarhcm = (Toolbar) findViewById(R.id.toolbarhochiminh);
        listviewhcm = (ListView) findViewById(R.id.listviewhochiminh);
        arr_hcm = new ArrayList<>();
        hochiminh_adapter = new hochiminhadapter(getApplicationContext(), arr_hcm);
        listviewhcm.setAdapter(hochiminh_adapter);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null);
        myHandler = new myHandler();
    }

    public class myHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    listviewhcm.addFooterView(footerview);
                    break;
                case 1:
                    page++;
                    GetData(page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class ThreadData extends Thread{
        @Override
        public void run() {
            myHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = myHandler.obtainMessage(1);
            myHandler.sendMessage(message);
            super.run();
        }
    }
}
