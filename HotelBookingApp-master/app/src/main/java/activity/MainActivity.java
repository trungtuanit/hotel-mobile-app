package activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import adapter.khachsandadat_adapter;
import adapter.loaispadapter;
import adapter.sanpham_adapter;
import course.examples.appdatphong.DaNangActivity;
import course.examples.appdatphong.DangNhapActivity;
import course.examples.appdatphong.HoChiMinhActivity;
import course.examples.appdatphong.GioHangActivity;
import course.examples.appdatphong.HaNoiActivity;
import course.examples.appdatphong.R;
import course.examples.appdatphong.TaiKhoanActivity;
import course.examples.appdatphong.ThongTinActivity;
import model.giohang;
import model.loaisp;
import model.sanpham;
import model.user_info;
import ultil.check_connection;
import ultil.server;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewmanhinhchinnh, recyclerViewkhachsandadat;
    NavigationView navigationView;
    ListView listViewmanhinhchinh;
    DrawerLayout drawerLayout;
    ArrayList<loaisp> arr_loaisp;
    loaispadapter loaispadapter;
    int id = 0;
    String tenloaisp = "";
    String hinhanhloaisp = "";
    ArrayList<sanpham> arr_sanpham;
    sanpham_adapter sanpham_adapter;
    public static ArrayList<sanpham> arr_khachsandadat;
    khachsandadat_adapter khachsandadat_adapter;

    loaispadapter diadiemadapter;
    ArrayList<loaisp> arr_diadiem;
    TextView textView_diadiem;
    public static long[] date;

    public static ArrayList<giohang> arr_giohang;
    int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

    public static int idtaikhoan = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        Permission();
        if (check_connection.haveNetworkConnection(getApplicationContext())) {
            getIdTaiKhoan();
            ActionBar();
            ActionViewFlipper();
            GetDuLieuDiaDiem();
            GetDuLieuSanPhamMoiNhat();
            GetDuLieuKhachSanDaDat();
            CatchOnItemListView();
        } else {
            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối Internet");
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    public void Permission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the username_icon *asynchronously* -- don't block
                // this thread waiting for the username_icon's response! After the username_icon
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
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
        for (sanpham item : arr_sanpham) {
            if (item.getTensanpham().toLowerCase().contains(userInput)) {
                newList.add(item);
            }
        }
        sanpham_adapter.updateArrayList(newList);
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

    private void CatchOnItemListView() {
        listViewmanhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                switch (i) {
                    case 0:
                        if (check_connection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            intent.putExtra("idtaikhoan", idtaikhoan);
                            startActivity(intent);
                        } else {
                            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if (check_connection.haveNetworkConnection(getApplicationContext())) {
                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                            builderSingle.setIcon(R.drawable.icon);
                            builderSingle.setTitle("Chọn địa điểm của khách sạn");

                            builderSingle.setAdapter(diadiemadapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which)
                                    {
                                        case 0:
                                            if (check_connection.haveNetworkConnection(getApplicationContext())) {
                                                Intent intent = new Intent(MainActivity.this, HoChiMinhActivity.class);
                                                intent.putExtra("idloaisp", 1);
                                                startActivity(intent);
                                            } else {
                                                check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                                            }
                                            drawerLayout.closeDrawer(GravityCompat.START);
                                            break;
                                        case 1:
                                            if (check_connection.haveNetworkConnection(getApplicationContext())) {
                                                Intent intent = new Intent(MainActivity.this, HaNoiActivity.class);
                                                intent.putExtra("idloaisp", 2);
                                                startActivity(intent);
                                            } else {
                                                check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                                            }
                                            drawerLayout.closeDrawer(GravityCompat.START);
                                            break;
                                        case 2:
                                            if (check_connection.haveNetworkConnection(getApplicationContext())) {
                                                Intent intent = new Intent(MainActivity.this, DaNangActivity.class);
                                                intent.putExtra("idloaisp", 3);
                                                startActivity(intent);
                                            } else {
                                                check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                                            }
                                            drawerLayout.closeDrawer(GravityCompat.START);
                                            break;
                                    }
                                }

                            });
                            builderSingle.show();

                        } else {
                            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                        }
                        break;
                    case 2:
                        if (check_connection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, TaiKhoanActivity.class);
                            intent.putExtra("idtaikhoan", idtaikhoan);
                            startActivity(intent);
                        } else {
                            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        if (check_connection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, ThongTinActivity.class);
                            startActivity(intent);
                        } else {
                            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });
        textView_diadiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_connection.haveNetworkConnection(getApplicationContext())) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                    builderSingle.setIcon(R.drawable.icon);
                    builderSingle.setTitle("Chọn địa điểm của khách sạn");

                    builderSingle.setAdapter(diadiemadapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which)
                            {
                                case 0:
                                    if (check_connection.haveNetworkConnection(getApplicationContext())) {
                                        Intent intent = new Intent(MainActivity.this, HoChiMinhActivity.class);
                                        intent.putExtra("idloaisp", 1);
                                        startActivity(intent);
                                    } else {
                                        check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                                    }
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    break;
                                case 1:
                                    if (check_connection.haveNetworkConnection(getApplicationContext())) {
                                        Intent intent = new Intent(MainActivity.this, HaNoiActivity.class);
                                        intent.putExtra("idloaisp", 2);
                                        startActivity(intent);
                                    } else {
                                        check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                                    }
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    break;
                                case 2:
                                    if (check_connection.haveNetworkConnection(getApplicationContext())) {
                                        Intent intent = new Intent(MainActivity.this, DaNangActivity.class);
                                        intent.putExtra("idloaisp", 3);
                                        startActivity(intent);
                                    } else {
                                        check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                                    }
                                    drawerLayout.closeDrawer(GravityCompat.START);
                                    break;
                            }
                        }

                    });
                    builderSingle.show();

                } else {
                    check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối");
                }
            }
        });
    }

    private void GetDuLieuSanPhamMoiNhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.duongdan_spmoinhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    int ID = 0;
                    String Tensp = "";
                    Integer Gia = 0;
                    String Hinhanhsp = "";
                    String Mota = "";
                    int IDsp = 0;
                    String Diachi = "";
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            Tensp = jsonObject.getString("tensp");
                            Gia = jsonObject.getInt("giasp");
                            Hinhanhsp = jsonObject.getString("hinhanhsp");
                            Mota = jsonObject.getString("mota");
                            IDsp = jsonObject.getInt("idsp");
                            Diachi = jsonObject.getString("diachi");
                            arr_sanpham.add(new sanpham(ID, Tensp, Gia, Hinhanhsp, Mota, IDsp, Diachi));
                            sanpham_adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void GetDuLieuKhachSanDaDat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server.duongdan_khachsandadat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    int ID = 0;
                    String Tensp = "";
                    Integer Gia = 0;
                    String Hinhanhsp = "";
                    String Mota = "";
                    int IDsp = 0;
                    String Diachi = "";
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            Tensp = jsonObject.getString("tensp");
                            Gia = jsonObject.getInt("giasp");
                            Hinhanhsp = jsonObject.getString("hinhanhsp");
                            Mota = jsonObject.getString("mota");
                            IDsp = jsonObject.getInt("idsp");
                            Diachi = jsonObject.getString("diachi");
                            arr_khachsandadat.add(new sanpham(ID, Tensp, Gia, Hinhanhsp, Mota, IDsp, Diachi));
                            khachsandadat_adapter.notifyDataSetChanged();
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
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id_user", String.valueOf(idtaikhoan));
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetDuLieuDiaDiem() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(server.duongdan_loaisp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenloaisp = jsonObject.getString("tenloaisp");
                            hinhanhloaisp = jsonObject.getString("hinhanhloaisp");
                            arr_diadiem.add(new loaisp(id, tenloaisp, hinhanhloaisp));
                            diadiemadapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                check_connection.ShowToast_Short(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionViewFlipper() {
        ArrayList<String> arr_quangcao = new ArrayList<>();
        arr_quangcao.add("https://staticproxy.mytourcdn.com/0x0/resources/pictures/banners/advertising1571041429.jpg");
        arr_quangcao.add("https://cdn1.ivivu.com/iVivu/2019/10/09/14/d-the-anam-1140x250.png");
        arr_quangcao.add("https://cdn1.ivivu.com/iVivu/2019/08/02/14/d-intercon-phu-quoc-1140x250.png");
        arr_quangcao.add("https://cdn1.ivivu.com/iVivu/2019/09/19/11/d-de-la-sapa-1140x250.png");
        for (int i = 0; i < arr_quangcao.size(); i++) {
            ImageView imageview = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(arr_quangcao.get(i)).into(imageview);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageview);
        }
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbarManhinhchinh);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        recyclerViewmanhinhchinnh = (RecyclerView) findViewById(R.id.recyclerview_khachsanmoinhat);
        recyclerViewkhachsandadat = (RecyclerView) findViewById(R.id.recyclerview_khachsandadat);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        listViewmanhinhchinh = (ListView) findViewById(R.id.listview_manhinhchinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        arr_loaisp = new ArrayList<>();
        arr_loaisp.add(0, new loaisp(0, "Trang chủ", "https://www.pngkit.com/png/full/28-287135_home-getting-started-icon.png"));
        arr_loaisp.add(1, new loaisp(0, "Địa điểm", "https://ya-webdesign.com/transparent250_/svg-marker-custom-map-1.png"));
        arr_loaisp.add(2, new loaisp(0, "Tài khoản", "https://pngimage.net/wp-content/uploads/2018/05/account-png-2.png"));
        arr_loaisp.add(3, new loaisp(0, "Thông tin", "https://img.icons8.com/bubbles/500/000000/info.png"));
        loaispadapter = new loaispadapter(arr_loaisp, getApplicationContext());
        listViewmanhinhchinh.setAdapter(loaispadapter);

        arr_sanpham = new ArrayList<>();
        sanpham_adapter = new sanpham_adapter(getApplicationContext(), arr_sanpham);
        arr_khachsandadat = new ArrayList<>();
        khachsandadat_adapter = new khachsandadat_adapter(getApplicationContext(), arr_khachsandadat);

        LinearLayoutManager layoutManager1
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewmanhinhchinnh.setLayoutManager(layoutManager1);
        recyclerViewmanhinhchinnh.setHasFixedSize(true);
        recyclerViewmanhinhchinnh.setAdapter(sanpham_adapter);
        recyclerViewkhachsandadat.setLayoutManager(layoutManager2);
        recyclerViewkhachsandadat.setHasFixedSize(true);
        recyclerViewkhachsandadat.setAdapter(khachsandadat_adapter);

        arr_diadiem = new ArrayList<>();
        diadiemadapter = new loaispadapter(arr_diadiem,getApplicationContext());
        textView_diadiem = (TextView) findViewById(R.id.textview_diadiem);
        date = new long[60];
        Arrays.fill(date, 0);

        if (arr_giohang != null) {

        } else {
            arr_giohang = new ArrayList<>();
        }
    }

    private void getIdTaiKhoan() {
        idtaikhoan = getIntent().getIntExtra("idtaikhoan", -1);
    }
}
