package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.PopupWindowCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import activity.MainActivity;
import course.examples.appdatphong.CalendarActivity;
import course.examples.appdatphong.DichVuActiviry;
import course.examples.appdatphong.GioHangActivity;
import course.examples.appdatphong.R;
import model.giohang;
import model.sanpham;
import ultil.server;

public class giohang_adapter extends BaseAdapter {
    Context context;
    ArrayList<model.giohang> arr_giohang;

    public giohang_adapter(Context context, ArrayList<giohang> arr_giohang) {
        this.context = context;
        this.arr_giohang = arr_giohang;
    }

    @Override
    public int getCount() {
        return arr_giohang.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_giohang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  class ViewHolder{
        public TextView textview_tenkhachsan, textview_tenloaiphong, textview_giaphong;
        public ImageView image_giohang;
        public Button btminus_dem, btnvalues_dem, btnplus_dem, btminus_phong, btnvalues_phong,
                btnplus_phong, bt_chonngay, bt_dichvu;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_giohang,null);
            viewHolder.textview_tenkhachsan = (TextView) view.findViewById(R.id.textview_tenkhachsan);
            viewHolder.textview_tenloaiphong = (TextView) view.findViewById(R.id.textview_tenloaiphong);
            viewHolder.textview_giaphong = (TextView) view.findViewById(R.id.textview_giaphong);
            viewHolder.image_giohang  = (ImageView) view.findViewById(R.id.image_giohang);
            viewHolder.btminus_dem = (Button) view.findViewById(R.id.btminus_dem);
            viewHolder.btnvalues_dem = (Button) view.findViewById(R.id.btvalues_dem);
            viewHolder.btnplus_dem = (Button) view.findViewById(R.id.btplus_dem);
            viewHolder.btminus_phong = (Button) view.findViewById(R.id.btminus_phong);
            viewHolder.btnvalues_phong = (Button) view.findViewById(R.id.btvalues_phong);
            viewHolder.btnplus_phong = (Button) view.findViewById(R.id.btplus_phong);
            viewHolder.bt_chonngay = (Button) view.findViewById(R.id.bt_chonngay);

            viewHolder.bt_chonngay.setPadding(0,0,0,0);
            viewHolder.bt_chonngay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            viewHolder.bt_dichvu = (Button) view.findViewById(R.id.bt_dichvu);
            view.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        giohang giohang = (model.giohang) getItem(position);
        viewHolder.textview_tenkhachsan.setText(giohang.getTenkhachsan());
        viewHolder.textview_tenloaiphong.setText(giohang.getTenloaiphong());
        int giagiohang = giohang.getGiaphong() / giohang.getSoluong();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.textview_giaphong.setText(decimalFormat.format(giagiohang) + " VND");
        Picasso.with(context).load(giohang.getHinhanh())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.image_giohang);
        viewHolder.btnvalues_phong.setText(giohang.getSoluong() + "");
        viewHolder.btnvalues_dem.setText(giohang.getSodem() + "");
        if (MainActivity.arr_giohang.get(position).getNgaynhanphong() != null && MainActivity.arr_giohang.get(position).getNgaytraphong() != null)
        {
            viewHolder.bt_chonngay.setPadding(15,0,0,0);
            viewHolder.bt_chonngay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_ok, 0, 0, 0);
        }

        int sophong = Integer.parseInt(viewHolder.btnvalues_phong.getText().toString());
        if (sophong >= 10)
        {
            viewHolder.btnplus_phong.setEnabled(false);
            viewHolder.btnplus_phong.setTextColor(Color.parseColor("#BBB7B7"));
            viewHolder.btnplus_phong.setBackgroundResource(R.drawable.boder3);
            viewHolder.btminus_phong.setEnabled(true);
            viewHolder.btminus_phong.setTextColor(Color.parseColor("#000000"));
            viewHolder.btminus_phong.setBackgroundResource(R.drawable.boder2);
        }
        else if (sophong <= 1)
        {
            viewHolder.btminus_phong.setEnabled(false);
            viewHolder.btminus_phong.setTextColor(Color.parseColor("#BBB7B7"));
            viewHolder.btminus_phong.setBackgroundResource(R.drawable.boder3);
        }
        else if (sophong >= 1)
        {
            viewHolder.btminus_phong.setEnabled(true);
            viewHolder.btminus_phong.setTextColor(Color.parseColor("#000000"));
            viewHolder.btminus_phong.setBackgroundResource(R.drawable.boder2);
            viewHolder.btnplus_phong.setEnabled(true);
            viewHolder.btnplus_phong.setTextColor(Color.parseColor("#000000"));
            viewHolder.btnplus_phong.setBackgroundResource(R.drawable.boder2);
        }
        viewHolder.btnplus_phong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sophongmoinhat = Integer.parseInt(viewHolder.btnvalues_phong.getText().toString()) + 1;
                int sophonghientai = MainActivity.arr_giohang.get(position).getSoluong();
                int giahientai = MainActivity.arr_giohang.get(position).getGiaphong();
                MainActivity.arr_giohang.get(position).setSoluong(sophongmoinhat);
                int giamoinhat = (giahientai * sophongmoinhat) / sophonghientai;
                MainActivity.arr_giohang.get(position).setGiaphong(giamoinhat);
                GioHangActivity.EvenUltil();
                if (sophongmoinhat > 9)
                {
                    viewHolder.btnplus_phong.setEnabled(false);
                    viewHolder.btnplus_phong.setTextColor(Color.parseColor("#BBB7B7"));
                    viewHolder.btnplus_phong.setBackgroundResource(R.drawable.boder3);
                    viewHolder.btminus_phong.setEnabled(true);
                    viewHolder.btminus_phong.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btminus_phong.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnvalues_phong.setText(String.valueOf(sophongmoinhat));
                } else
                {
                    viewHolder.btminus_phong.setEnabled(true);
                    viewHolder.btminus_phong.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btminus_phong.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnplus_phong.setEnabled(true);
                    viewHolder.btnplus_phong.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btnplus_phong.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnvalues_phong.setText(String.valueOf(sophongmoinhat));
                }
            }
        });

        viewHolder.btminus_phong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sophongmoinhat = Integer.parseInt(viewHolder.btnvalues_phong.getText().toString()) - 1;
                int sophonghientai = MainActivity.arr_giohang.get(position).getSoluong();
                int giahientai = MainActivity.arr_giohang.get(position).getGiaphong();
                MainActivity.arr_giohang.get(position).setSoluong(sophongmoinhat);
                int giamoinhat = (giahientai * sophongmoinhat) / sophonghientai;
                MainActivity.arr_giohang.get(position).setGiaphong(giamoinhat);
                GioHangActivity.EvenUltil();
                if (sophongmoinhat < 2)
                {
                    viewHolder.btminus_phong.setEnabled(false);
                    viewHolder.btminus_phong.setTextColor(Color.parseColor("#BBB7B7"));
                    viewHolder.btminus_phong.setBackgroundResource(R.drawable.boder3);
                    viewHolder.btnplus_phong.setEnabled(true);
                    viewHolder.btnplus_phong.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btnplus_phong.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnvalues_phong.setText(String.valueOf(sophongmoinhat));
                } else
                {
                    viewHolder.btminus_phong.setEnabled(true);
                    viewHolder.btminus_phong.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btminus_phong.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnplus_phong.setEnabled(true);
                    viewHolder.btnplus_phong.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btnplus_phong.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnvalues_phong.setText(String.valueOf(sophongmoinhat));
                }
            }
        });

        int sodem = Integer.parseInt(viewHolder.btnvalues_dem.getText().toString());
        if (sodem >= 10)
        {
            viewHolder.btnplus_dem.setEnabled(false);
            viewHolder.btnplus_dem.setBackgroundResource(R.drawable.boder3);
            viewHolder.btnplus_dem.setTextColor(Color.parseColor("#BBB7B7"));
            viewHolder.btminus_dem.setEnabled(true);
            viewHolder.btminus_dem.setTextColor(Color.parseColor("#000000"));
            viewHolder.btminus_dem.setBackgroundResource(R.drawable.boder2);
        }
        else if (sodem <= 1)
        {
            viewHolder.btminus_dem.setEnabled(false);
            viewHolder.btminus_dem.setTextColor(Color.parseColor("#BBB7B7"));
            viewHolder.btminus_dem.setBackgroundResource(R.drawable.boder3);
        }
        else if (sodem >= 1)
        {
            viewHolder.btminus_dem.setEnabled(true);
            viewHolder.btminus_dem.setTextColor(Color.parseColor("#000000"));
            viewHolder.btminus_dem.setBackgroundResource(R.drawable.boder2);
            viewHolder.btnplus_dem.setEnabled(true);
            viewHolder.btnplus_dem.setTextColor(Color.parseColor("#000000"));
            viewHolder.btnplus_dem.setBackgroundResource(R.drawable.boder2);
        }
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        viewHolder.btnplus_dem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sodemmoinhat = Integer.parseInt(viewHolder.btnvalues_dem.getText().toString()) + 1;
                int sodemhientai = MainActivity.arr_giohang.get(position).getSodem();
                int giahientai = MainActivity.arr_giohang.get(position).getGiaphong();
                MainActivity.arr_giohang.get(position).setSodem(sodemmoinhat);
                int giamoinhat = (giahientai * sodemmoinhat) / sodemhientai;
                MainActivity.arr_giohang.get(position).setGiaphong(giamoinhat);
                if (MainActivity.arr_giohang.get(position).getNgaynhanphong() != null && MainActivity.arr_giohang.get(position).getNgaytraphong() != null)
                {
                    Date d_tra = formatter.parse(MainActivity.arr_giohang.get(position).getNgaytraphong(),new ParsePosition(0));
                    Date d_nhan = formatter.parse(MainActivity.arr_giohang.get(position).getNgaynhanphong(),new ParsePosition(0));
                    CalendarActivity.XoaNgay(d_nhan, d_tra);
                }
                MainActivity.arr_giohang.get(position).setNgaynhanphong(null);
                MainActivity.arr_giohang.get(position).setNgaytraphong(null);
                viewHolder.bt_chonngay.setPadding(0,0,0,0);
                viewHolder.bt_chonngay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                GioHangActivity.EvenUltil();
                if (sodemmoinhat > 9)
                {
                    viewHolder.btnplus_dem.setEnabled(false);
                    viewHolder.btnplus_dem.setBackgroundResource(R.drawable.boder3);
                    viewHolder.btnplus_dem.setTextColor(Color.parseColor("#BBB7B7"));
                    viewHolder.btminus_dem.setEnabled(true);
                    viewHolder.btminus_dem.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btminus_dem.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnvalues_dem.setText(String.valueOf(sodemmoinhat));

                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    viewHolder.textview_giaphong.setText(decimalFormat.format(MainActivity.arr_giohang.get(position).getGiaphong() / MainActivity.arr_giohang.get(position).getSoluong()) + " VND");
                } else {
                    viewHolder.btminus_dem.setEnabled(true);
                    viewHolder.btminus_dem.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btminus_dem.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnplus_dem.setEnabled(true);
                    viewHolder.btnplus_dem.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btnplus_dem.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnvalues_dem.setText(String.valueOf(sodemmoinhat));
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    viewHolder.textview_giaphong.setText(decimalFormat.format(MainActivity.arr_giohang.get(position).getGiaphong() / MainActivity.arr_giohang.get(position).getSoluong()) + " VND");
                }
            }
        });

        viewHolder.btminus_dem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sodemmoinhat = Integer.parseInt(viewHolder.btnvalues_dem.getText().toString()) - 1;
                int sodemhientai = MainActivity.arr_giohang.get(position).getSodem();
                int giahientai = MainActivity.arr_giohang.get(position).getGiaphong();
                MainActivity.arr_giohang.get(position).setSodem(sodemmoinhat);
                int giamoinhat = (giahientai * sodemmoinhat) / sodemhientai;
                MainActivity.arr_giohang.get(position).setGiaphong(giamoinhat);
                if (MainActivity.arr_giohang.get(position).getNgaynhanphong() != null && MainActivity.arr_giohang.get(position).getNgaytraphong() != null)
                {
                    Date d_tra = formatter.parse(MainActivity.arr_giohang.get(position).getNgaytraphong(),new ParsePosition(0));
                    Date d_nhan = formatter.parse(MainActivity.arr_giohang.get(position).getNgaynhanphong(),new ParsePosition(0));
                    CalendarActivity.XoaNgay(d_nhan, d_tra);
                }
                MainActivity.arr_giohang.get(position).setNgaynhanphong(null);
                MainActivity.arr_giohang.get(position).setNgaytraphong(null);
                viewHolder.bt_chonngay.setPadding(0,0,0,0);
                viewHolder.bt_chonngay.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                GioHangActivity.EvenUltil();
                if (sodemmoinhat < 2)
                {
                    viewHolder.btminus_dem.setEnabled(false);
                    viewHolder.btminus_dem.setTextColor(Color.parseColor("#BBB7B7"));
                    viewHolder.btminus_dem.setBackgroundResource(R.drawable.boder3);
                    viewHolder.btnplus_dem.setEnabled(true);
                    viewHolder.btnplus_dem.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btnplus_dem.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnvalues_dem.setText(String.valueOf(sodemmoinhat));
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    viewHolder.textview_giaphong.setText(decimalFormat.format(MainActivity.arr_giohang.get(position).getGiaphong() / MainActivity.arr_giohang.get(position).getSoluong()) + " VND");
                } else {
                    viewHolder.btminus_dem.setEnabled(true);
                    viewHolder.btminus_dem.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btminus_dem.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnplus_dem.setEnabled(true);
                    viewHolder.btnplus_dem.setTextColor(Color.parseColor("#000000"));
                    viewHolder.btnplus_dem.setBackgroundResource(R.drawable.boder2);
                    viewHolder.btnvalues_dem.setText(String.valueOf(sodemmoinhat));
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    viewHolder.textview_giaphong.setText(decimalFormat.format(MainActivity.arr_giohang.get(position).getGiaphong() / MainActivity.arr_giohang.get(position).getSoluong()) + " VND");
                }
            }
        });
        viewHolder.bt_chonngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCalendarActivity(position);
            }
        });

        viewHolder.bt_dichvu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDichvuActivity(position);
            }
        });
        return view;
    }

    void startCalendarActivity(int position)
    {
        Intent intent = new Intent(context, CalendarActivity.class);
        intent.putExtra("id_donggiohang",position);
        ((Activity)context).startActivityForResult(intent, GioHangActivity.REQUEST_CODE_EXAMPLE);
    }

    void startDichvuActivity(int position)
    {
        Intent intent = new Intent(context, DichVuActiviry.class);
        intent.putExtra("id_donggiohang",position);
        ((Activity)context).startActivity(intent);
    }
}
