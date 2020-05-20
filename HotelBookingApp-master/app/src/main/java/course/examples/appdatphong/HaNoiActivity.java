package course.examples.appdatphong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

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

import adapter.hanoiadapter;
import model.sanpham;
import ultil.check_connection;
import ultil.server;

public class HaNoiActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    Toolbar toolbarhanoi;
    ListView listviewhanoi;
    hanoiadapter hanoi_adapter;
    ArrayList<sanpham> arr_hanoi;
    int idhanoi = 0;
    int page = 1;

    View footerview;
    boolean isLoading = false;
    myHandler myHandler;
    boolean limitData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hanoi);
        if (check_connection.haveNetworkConnection(getApplicationContext())){
            Anhxa();
            GetIdloaisp();
            ActionToolbar();
            GetData(page);
            LoadMoreData();
        } else {
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
        for (sanpham item : arr_hanoi){
            if (item.getTensanpham().toLowerCase().contains(userInput)){
                newList.add(item);
            }
        }
        hanoi_adapter.updateArrayList(newList);
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
        toolbarhanoi = (Toolbar) findViewById(R.id.toolbardhanoi);
        listviewhanoi = (ListView) findViewById(R.id.listviewhanoi);
        arr_hanoi = new ArrayList<>();
        hanoi_adapter = new hanoiadapter(getApplicationContext(), arr_hanoi);
        listviewhanoi.setAdapter(hanoi_adapter);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.progressbar, null);
        myHandler = new myHandler();
    }

    private void GetIdloaisp() {
        idhanoi = getIntent().getIntExtra("idloaisp", -1);
        Log.d("giatriloaisp", idhanoi + "");
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarhanoi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarhanoi.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                String Tenhanoi = "";
                int Giahanoi = 0;
                String Hinhanhhanoi = "";
                String Mota = "";
                int Idsphanoi = 0;
                String Diachi = "";
                if (response != null && response.length() != 2) {
                    listviewhanoi.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Id = jsonObject.getInt("id");
                            Tenhanoi = jsonObject.getString("tensp");
                            Giahanoi = jsonObject.getInt("giasp");
                            Hinhanhhanoi = jsonObject.getString("hinhanhsp");
                            Mota = jsonObject.getString("mota");
                            Idsphanoi = jsonObject.getInt("idsp");
                            Diachi = jsonObject.getString("diachi");
                            arr_hanoi.add(new sanpham(Id, Tenhanoi, Giahanoi, Hinhanhhanoi, Mota, Idsphanoi,Diachi));
                            hanoi_adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    limitData = true;
                    listviewhanoi.removeFooterView(footerview);
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
                param.put("idsp", String.valueOf(idhanoi));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void LoadMoreData() {
        listviewhanoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ChiTietKhachSanActivity.class);
                intent.putExtra("thongtinsp",arr_hanoi.get(position));
                startActivity(intent);
            }
        });

        listviewhanoi.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    public class myHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    listviewhanoi.addFooterView(footerview);
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
