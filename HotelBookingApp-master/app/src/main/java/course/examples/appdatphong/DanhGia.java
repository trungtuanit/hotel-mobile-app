package course.examples.appdatphong;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import activity.MainActivity;
import adapter.danhgiaadapter2;
import model.danhgia;
import ultil.check_connection;

import static ultil.server.urlGetData1;

public class DanhGia extends AppCompatActivity {
    Toolbar toolbardanhgia;
    int id = 0;
    ListView lvdg;
    Button btnthemdanhgia;
    TextView txttongdiem;
    adapter.danhgiaadapter2 danhgiaadapter2;
    ArrayList<danhgia> mangdg;
    int page = 1;
    View footerview;
    int s = 0;
    ProgressBar pbXuLy;
    public static final String UPLOAD_URL =   "http://datphongkhachsanonline.000webhostapp.com/server/getdanhgiaks.php";
    //  String urlGetData1 = "http://192.168.85.1/androidwebservice/getdanhgia.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_gia);
        Anhxa();
        if (check_connection.haveNetworkConnection(getApplicationContext()))
        {
            ActionToolbar();
            GetIDdiem();

            GetData(page,UPLOAD_URL);
            EventButton();

        }
        else
        {
            check_connection.ShowToast_Short(getApplicationContext(),"Bạn hãy kiểm tra lại kết nối Internet");
            finish();
        }
    }
    private void GetData(int Page,String url) {
        // dua request len server
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //     String duongdan = url+String.valueOf(Page);
        //duongdan bang url
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                String username = "";
                String header ="";

                String detail = "";
                int mark = 0;
                int iddd= 0;
                int s = 0;
                int iddddd = 0;
                int current = pbXuLy.getProgress();
                String hinhanhks = "";
                if(response != null && response.length() != 2){
                    lvdg.removeFooterView(footerview);


                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        //di vao va doc het tat ca du lieu cho json array
                        for(int i= 0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            hinhanhks = jsonObject.getString("image_path");
                            username  =jsonObject.getString("image_name");

                            iddd = jsonObject.getInt("idkhachsan");
                            header = jsonObject.getString("title");
                            detail =jsonObject.getString("detail");
                            mark = jsonObject.getInt("mark");
                            s = s + jsonObject.getInt("mark");
                            Log.d("mak1",s+"");
                            iddddd = iddddd +1;
                            //them cho nay
                            //   s = s + mark;
                            //  iddd = jsonObject.getInt("idkhachsan")
                            mangdg.add(new danhgia(iddd,username,header,detail,mark,hinhanhks));
                            danhgiaadapter2.notifyDataSetChanged();

                        }
                        float h = s/iddddd;
                        txttongdiem.setText(h + "");
                        pbXuLy.setProgress(current + (int)(h));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    // limitdata = true;
                    lvdg.removeFooterView(footerview);

                    check_connection.ShowToast_Short(getApplicationContext(),"Đã hết dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            //day len cho server dang hashmap
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<String, String>();
                param.put("iddanhgia",String.valueOf(id));
                return param;
            }
        };
        requestQueue.add(stringRequest);


    }
    private void EventButton() {
        btnthemdanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExist = false;
                for (int i = 0; i < MainActivity.arr_khachsandadat.size(); i++)
                {
                    if (MainActivity.arr_khachsandadat.get(i).getId() == id)
                    {
                        isExist = true;
                        Intent intent = new Intent(getApplicationContext(), Nhapchitietdanhgia.class);
                        intent.putExtra("id1", id);
                        startActivity(intent);
                        break;
                    }
                }
                if (isExist == false) Toast.makeText(getApplicationContext(), "Bạn chưa thể đánh giá vì chưa đặt khách sạn này", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetIDdiem() {
        //-1 de bik bao loi o dau
        id = getIntent().getIntExtra("id",-1);
        Log.d("id",id+"");
    }
    private void ActionToolbar() {
        setSupportActionBar(toolbardanhgia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbardanhgia.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        toolbardanhgia = (Toolbar) findViewById(R.id.toolbar_danhgia);
        btnthemdanhgia = findViewById(R.id.buttonnhapdanhgia);
        lvdg = findViewById(R.id.lvdg);
        txttongdiem = findViewById(R.id.tongmark);
        pbXuLy = (ProgressBar) findViewById(R.id.progressbar2);
        mangdg = new ArrayList<>();

        danhgiaadapter2 = new danhgiaadapter2(getApplicationContext(),mangdg);

        lvdg.setAdapter(danhgiaadapter2);

    }
}
