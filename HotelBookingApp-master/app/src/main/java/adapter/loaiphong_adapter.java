package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import activity.MainActivity;
import course.examples.appdatphong.CalendarActivity;
import course.examples.appdatphong.GioHangActivity;
import course.examples.appdatphong.R;
import model.giohang;
import model.loaiphong;
import model.sanpham;

public class loaiphong_adapter extends BaseAdapter {
    Context context;
    ArrayList<loaiphong> arr_loaiphong;
    sanpham khachsan;

    public loaiphong_adapter(Context context, ArrayList<loaiphong> arr_loaiphong, sanpham khachsan) {
        this.context = context;
        this.arr_loaiphong = arr_loaiphong;
        this.khachsan = khachsan;
    }

    @Override
    public int getCount() {
        return arr_loaiphong.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_loaiphong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public TextView tvtenphong, textview_giuong, tvgiaphong;
        public ImageView imageviewchonphong, imageview_sokhach;
        public Spinner spinnerDem, spinnerPhong;
        public Button btchonphong;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        loaiphong_adapter.ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new loaiphong_adapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_phong, null);
            viewHolder.tvtenphong = (TextView) view.findViewById(R.id.tvtenphong);
            viewHolder.textview_giuong = (TextView) view.findViewById(R.id.textview_giuong);
            viewHolder.imageviewchonphong = (ImageView) view.findViewById(R.id.imageviewchonphong);
            viewHolder.imageview_sokhach = (ImageView) view.findViewById(R.id.imageview_sokhach);
            viewHolder.tvgiaphong = (TextView) view.findViewById(R.id.tvgiaphong);
            viewHolder.spinnerDem = (Spinner) view.findViewById(R.id.spinner_sodem);
            viewHolder.spinnerPhong = (Spinner) view.findViewById(R.id.spinner_sophong);
            viewHolder.btchonphong = (Button) view.findViewById(R.id.bt_chonphong);
            view.setTag(viewHolder);
        } else {
            viewHolder = (loaiphong_adapter.ViewHolder) view.getTag();
        }
        loaiphong loaiphong = (loaiphong) getItem(position);
        viewHolder.tvtenphong.setText(loaiphong.getTenloaiphong());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvgiaphong.setText(decimalFormat.format(loaiphong.getGiaphong()) + " VND");
        viewHolder.textview_giuong.setText(loaiphong.getSogiuong());

        int sokhach = loaiphong.getSokhach();
        int width_imageview = 50;
        switch (sokhach) {
            case 1: {
                viewHolder.imageview_sokhach.setBackground(ContextCompat.getDrawable(context, R.drawable.icon1people));
                viewHolder.imageview_sokhach.getLayoutParams().width = width_imageview;
                break;
            }
            case 2: {
                viewHolder.imageview_sokhach.setBackground(ContextCompat.getDrawable(context, R.drawable.icon2people));
                viewHolder.imageview_sokhach.getLayoutParams().width = width_imageview * 2;
                break;
            }
            case 3: {
                viewHolder.imageview_sokhach.setBackground(ContextCompat.getDrawable(context, R.drawable.icon3people));
                viewHolder.imageview_sokhach.getLayoutParams().width = width_imageview * 3;
                break;
            }
            case 4: {
                viewHolder.imageview_sokhach.setBackground(ContextCompat.getDrawable(context, R.drawable.icon4people));
                viewHolder.imageview_sokhach.getLayoutParams().width = width_imageview * 4;
                break;
            }
            case 5: {
                viewHolder.imageview_sokhach.setBackground(ContextCompat.getDrawable(context, R.drawable.icon5people));
                viewHolder.imageview_sokhach.getLayoutParams().width = width_imageview * 5;
                break;
            }
        }
        Picasso.with(context).load(loaiphong.getHinhanh())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imageviewchonphong);

        int giaphong = loaiphong.getGiaphong();
        spinnerDem(viewHolder, loaiphong, giaphong);
        spinnerPhong(viewHolder, loaiphong);
        eventbutton(viewHolder, loaiphong, khachsan, giaphong);
        return view;
    }

    private void eventbutton(final ViewHolder viewHolder, final loaiphong loaiphong, final sanpham khachsan, final int giaphong) {
        viewHolder.btchonphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sodem = viewHolder.spinnerDem.getSelectedItem().toString();
                int SoDem = Integer.parseInt(sodem.replace(" đêm", ""));
                String sophong = viewHolder.spinnerPhong.getSelectedItem().toString();
                int SoPhong = Integer.parseInt(sophong.replace(" phòng", ""));

                if (MainActivity.arr_giohang.size() > 0) {
                    boolean exit = false;
                    for (int i = 0; i < MainActivity.arr_giohang.size(); i++) {
                        if (MainActivity.arr_giohang.get(i).getIdphong() == loaiphong.getIdphong()) {
                            MainActivity.arr_giohang.get(i).setSoluong(MainActivity.arr_giohang.get(i).getSoluong() + SoPhong);
                            MainActivity.arr_giohang.get(i).setSodem(MainActivity.arr_giohang.get(i).getSodem() + SoDem);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            Date d_tra = formatter.parse(MainActivity.arr_giohang.get(i).getNgaytraphong(),new ParsePosition(0));
                            Date d_nhan = formatter.parse(MainActivity.arr_giohang.get(i).getNgaynhanphong(),new ParsePosition(0));
                            CalendarActivity.XoaNgay(d_nhan, d_tra);
                            if (MainActivity.arr_giohang.get(i).getSoluong() > 10) {
                                MainActivity.arr_giohang.get(i).setSoluong(10);
                            }
                            if (MainActivity.arr_giohang.get(i).getSodem() > 10) {
                                MainActivity.arr_giohang.get(i).setSodem(10);
                            }
                            MainActivity.arr_giohang.get(i).setGiaphong(giaphong * MainActivity.arr_giohang.get(i).getSoluong() * MainActivity.arr_giohang.get(i).getSodem());
                            MainActivity.arr_giohang.get(i).setNgaynhanphong(null);
                            MainActivity.arr_giohang.get(i).setNgaytraphong(null);
                            exit = true;
                        }
                    }
                    if (exit == false) {
                        int giamoi = SoPhong * giaphong * SoDem;
                        MainActivity.arr_giohang.add(new giohang(loaiphong.getIdphong(), loaiphong.getTenloaiphong(), loaiphong.getSogiuong(),
                                loaiphong.getSokhach(), loaiphong.getHinhanh(), giamoi, loaiphong.getIdkhachsan(), khachsan.getTensanpham(), SoPhong, SoDem, null, null, ""));
                    }
                } else {
                    int giamoi = SoPhong * giaphong * SoDem;
                    MainActivity.arr_giohang.add(new giohang(loaiphong.getIdphong(), loaiphong.getTenloaiphong(), loaiphong.getSogiuong(),
                            loaiphong.getSokhach(), loaiphong.getHinhanh(), giamoi, loaiphong.getIdkhachsan(), khachsan.getTensanpham(), SoPhong, SoDem, null, null, ""));
                }
                Intent intent = new Intent(context, GioHangActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public void spinnerDem(final ViewHolder viewHolder, final loaiphong loaiphong, final int giaphong) {
        String[] sodem = new String[]{"1 đêm", "2 đêm", "3 đêm", "4 đêm", "5 đêm", "6 đêm", "7 đêm", "8 đêm", "9 đêm", "10 đêm"};
        ArrayAdapter<String> arrayAdapterDem = new ArrayAdapter<String>(context, R.layout.dong_spinnner, R.id.text, sodem);
        viewHolder.spinnerDem.setAdapter(arrayAdapterDem);
        viewHolder.spinnerDem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sodem = viewHolder.spinnerDem.getSelectedItem().toString();
                int SoDem = Integer.parseInt(sodem.replace(" đêm", ""));

                loaiphong.setGiaphong(giaphong * SoDem);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.tvgiaphong.setText(decimalFormat.format(loaiphong.getGiaphong()) + " VND");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void spinnerPhong(ViewHolder viewHolder, loaiphong loaiphong) {
        String[] sophong = new String[]{"1 phòng", "2 phòng", "3 phòng", "4 phòng", "5 phòng", "6 phòng", "7 phòng", "8 phòng", "9 phòng", "10 phòng",};
        ArrayAdapter<String> arrayAdapterPhong = new ArrayAdapter<String>(context, R.layout.dong_spinnner, R.id.text, sophong);
        viewHolder.spinnerPhong.setAdapter(arrayAdapterPhong);

    }

    public void updateArrayList(ArrayList<loaiphong> newList) {
        arr_loaiphong = new ArrayList<>();
        arr_loaiphong.addAll(newList);
        notifyDataSetChanged();
    }
}
