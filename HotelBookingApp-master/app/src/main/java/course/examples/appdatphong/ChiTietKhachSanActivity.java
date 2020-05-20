package course.examples.appdatphong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import model.CustomScrollView;
import model.sanpham;
import ultil.check_connection;

public class ChiTietKhachSanActivity extends AppCompatActivity implements OnMapReadyCallback {
    //public class ChiTietKhachSanActivity extends AppCompatActivity {
    Toolbar toolbarchitiet;
    ImageView imgchitiet;
    TextView txtten, txtmota;
    Button buttonchonphong;
    TextView txtdiachikhachsan, txtdanhgia, txtmotaks;
    TextView txttoolbar;

    RelativeLayout diachilayout, relativelayout_main, ggmap;

    int id = 0;
    String tenchitiet = "";
    String motachitiet = "";
    String hinhanhchitiet = "";
    int idsanpham = 0;
    String diachi = "";

    boolean isClickAdd = false;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_sp);
        Anhxa();
        ActionToolbar();
        GetInformation();
        EventButton();
        EventAddress();
    }

    private void EventAddress() {
        diachilayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickAdd) {
                    ggmap.setVisibility(View.GONE);
                    isClickAdd = false;
                    return;
                }
                if (!isClickAdd) {
                    ggmap.setVisibility(View.VISIBLE);
                    isClickAdd = true;
                    return;
                }
            }
        });
    }

    private void EventButton() {
        buttonchonphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanpham khachsan = new sanpham(id, tenchitiet, 0, hinhanhchitiet, motachitiet, idsanpham, diachi);
                Intent intent = new Intent(getApplicationContext(), PhongActivity.class);
                intent.putExtra("khachsan", khachsan);
                startActivity(intent);
            }
        });
        txtmotaks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtmota.getVisibility() == View.GONE) {

                    txtmota.setVisibility(View.VISIBLE);

                } else {

                    txtmota.setVisibility(View.GONE);
                }
            }
        });
        txtdanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DanhGia.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });


    }

    private void GetInformation() {
        sanpham sanpham = (sanpham) getIntent().getSerializableExtra("thongtinsp");
        id = sanpham.getId();
        tenchitiet = sanpham.getTensanpham();
        motachitiet = sanpham.getMota();
        hinhanhchitiet = sanpham.getHinhanhsanpham();
        idsanpham = sanpham.getIdsanpham();
        diachi = sanpham.getDiachi();

        txtten.setText(tenchitiet);
        txtmota.setText(motachitiet);
        txtmota.setVisibility(View.GONE);
        txtdiachikhachsan.setText(diachi);
        txttoolbar.setText(tenchitiet);

        Picasso.with(getApplicationContext()).load(hinhanhchitiet)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(imgchitiet);
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarchitiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarchitiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        toolbarchitiet = (Toolbar) findViewById(R.id.toolbar_chitietkhachsan);
        imgchitiet = (ImageView) findViewById(R.id.imageview_chitietsp);
        txtten = (TextView) findViewById(R.id.textview_tenchitietsp);
        txtmota = (TextView) findViewById(R.id.textview_motachitietsp);
        buttonchonphong = (Button) findViewById(R.id.buttonchonphong);
        txtdiachikhachsan = (TextView) findViewById(R.id.textview_diachikhachsan);
        txttoolbar = (TextView) findViewById(R.id.textview_toolbar);
        txtdanhgia = findViewById(R.id.danhgia);
        txtmotaks = findViewById(R.id.motakhachsan);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapkhachsan);
        mapFragment.getMapAsync(this);
        ggmap = (RelativeLayout) findViewById(R.id.ggmap);
        diachilayout = (RelativeLayout) findViewById(R.id.diachilayout);
        relativelayout_main = (RelativeLayout) findViewById(R.id.relativelayout_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_onlycart, menu);
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

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            //   May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }
        return p1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the username_icon grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng location_hotel = getLocationFromAddress(getApplicationContext(), txtten.getText().toString());
        mMap.addMarker(new MarkerOptions().position(location_hotel).title(txtten.getText().toString()).snippet(txtdiachikhachsan.getText().toString()).icon(BitmapDescriptorFactory.defaultMarker()));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location_hotel).zoom(18).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
