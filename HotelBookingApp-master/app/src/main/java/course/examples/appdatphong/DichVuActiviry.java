package course.examples.appdatphong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;

import activity.MainActivity;
import ultil.check_connection;

public class DichVuActiviry extends AppCompatActivity {
    Toolbar toolbarDichvu;
    Button button_xacnhan;
    int position = 0;
    CheckBox checkbox_buasang, checkbox_spa, checkbox_phonghop, checkbox_giatui,
            checkbox_xe, checkbox_dichvuphong, checkbox_doingoaite;

    String dichvu = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dich_vu_activiry);
        Anhxa();
        ActionToolbar();
        GetIdloaisp();
        LoadActivity();
        EventButton();
    }

    private void LoadActivity() {
        if (MainActivity.arr_giohang.get(position).getDichvu() != "")
        {
            dichvu = MainActivity.arr_giohang.get(position).getDichvu();
            checkbox_buasang.setChecked(dichvu.contains("Bữa sáng Buffet"));
            checkbox_spa.setChecked(dichvu.contains("Dịch vụ Spa"));
            checkbox_phonghop.setChecked(dichvu.contains("Dịch vụ phòng họp"));
            checkbox_giatui.setChecked(dichvu.contains("Dịch vụ giặt ủi quần áo"));
            checkbox_xe.setChecked(dichvu.contains("Dịch vụ xe đưa đón"));
            checkbox_dichvuphong.setChecked(dichvu.contains("Dịch vụ phòng 24/24"));
            checkbox_doingoaite.setChecked(dichvu.contains("Thu đổi ngoại tệ"));
            dichvu = "";
        }
    }

    private void EventButton() {
        button_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_buasang.isChecked())
                {
                    dichvu += "Bữa sáng Buffet; ";
                }
                if (checkbox_spa.isChecked())
                {
                    dichvu += "Dịch vụ Spa; ";
                }
                if (checkbox_phonghop.isChecked())
                {
                    dichvu += "Dịch vụ phòng họp; ";
                }
                if (checkbox_giatui.isChecked())
                {
                    dichvu += "Dịch vụ giặt ủi quần áo; ";
                }
                if (checkbox_xe.isChecked())
                {
                    dichvu += "Dịch vụ xe đưa đón; ";
                }
                if (checkbox_dichvuphong.isChecked())
                {
                    dichvu += "Dịch vụ phòng 24/24; ";
                }
                if (checkbox_doingoaite.isChecked())
                {
                    dichvu += "Thu đổi ngoại tệ";
                }

                boolean end_of_dichvu = dichvu.endsWith("; ");
                if (end_of_dichvu)
                {
                    dichvu = dichvu.substring(0,dichvu.length()-2); //Xóa chuỗi "; " ở cuối
                }

                if (dichvu != "")
                {
                    MainActivity.arr_giohang.get(position).setDichvu(dichvu);
                    dichvu = "";
                    finish();
                }
                else
                {
                    check_connection.ShowToast_Short(getApplicationContext(),"Bạn chưa chọn dịch vụ nào");
                    MainActivity.arr_giohang.get(position).setDichvu(dichvu);
                    finish();
                }

            }
        });
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbarDichvu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDichvu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.arr_giohang.get(position).getDichvu() == "")
                {
                    check_connection.ShowToast_Short(getApplicationContext(), "Chưa dịch vụ nào được xác nhận");
                }
                finish();
            }
        });
    }

    private void Anhxa() {
        button_xacnhan = (Button) findViewById(R.id.bt_xacnhandichvu);
        toolbarDichvu = (Toolbar) findViewById(R.id.toolbar_dichvu);
        checkbox_buasang = (CheckBox) findViewById(R.id.checkbox_buasang);
        checkbox_spa = (CheckBox) findViewById(R.id.checkbox_spa);
        checkbox_phonghop = (CheckBox) findViewById(R.id.checkbox_phonghop);
        checkbox_giatui = (CheckBox) findViewById(R.id.checkbox_giatui);
        checkbox_xe = (CheckBox) findViewById(R.id.checkbox_xe);
        checkbox_dichvuphong = (CheckBox) findViewById(R.id.checkbox_dichvuphong);
        checkbox_doingoaite = (CheckBox) findViewById(R.id.checkbox_doingoaite);
    }

    void GetIdloaisp() {
        position = getIntent().getIntExtra("id_donggiohang", -1);
        Log.d("id_donggiohang", position + "");
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        if (MainActivity.arr_giohang.get(position).getDichvu() == "")
        {
            check_connection.ShowToast_Short(getApplicationContext(), "Chưa dịch vụ nào được xác nhận");
        }
        finish();
    }
}
