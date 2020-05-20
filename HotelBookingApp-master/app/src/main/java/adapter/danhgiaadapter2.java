package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import course.examples.appdatphong.FullImage;
import course.examples.appdatphong.R;
import model.danhgia;

public class danhgiaadapter2 extends BaseAdapter {
    Context context;
    ArrayList<danhgia> arrayListdanhgia;

    // man hinh, de cho no biet no dang o man hinh nao



    public danhgiaadapter2(Context context, ArrayList<danhgia> arrayListdanhgia) {
        this.context = context;
        this.arrayListdanhgia = arrayListdanhgia;
    }
    @Override
    public int getCount() {
        return arrayListdanhgia.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListdanhgia.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder{
        TextView txtusername, txtheader, txtdetail,txtmark;
        ImageView imageView;


    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dongdanhgia, null);
            viewHolder.imageView = view.findViewById(R.id.imageviewdanhgia);
            viewHolder.txtusername = view.findViewById(R.id.tenuser);
            viewHolder.txtheader = view.findViewById(R.id.headeruser);
            viewHolder.txtdetail = view.findViewById(R.id.detailuser);
            viewHolder.txtmark = view.findViewById(R.id.markuser);

            //gan id vao viewholder
            view.setTag(viewHolder);

        } else {
            // khi run lan thu â co gt cac view nay roi thi chi can gan lai thoi
            viewHolder = (ViewHolder) view.getTag();

        }
        //lay du lieu ra tu mang
        final danhgia danhgia = (model.danhgia) getItem(i);
        viewHolder.txtusername.setText(danhgia.getUsername());
        viewHolder.txtheader.setText(danhgia.getHeader());
        viewHolder.txtdetail.setText(danhgia.getDetail());
        viewHolder.txtmark.setText(danhgia.getMark() + "");
        Picasso.with(context).load(danhgia.getHinhanhdiadiem())
                .placeholder(R.drawable.noimage1)
                .error(R.drawable.noimage1)
                .into(viewHolder.imageView);
        //    viewHolder.txtmark.setText(danhgia.getMark());
        //   viewHolder.txtmark.setText(danhgia.getMark());\
        //them cho nay
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullImage.class);
                intent.putExtra("imagefull",danhgia.getHinhanhdiadiem());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //  CheckConnection.showToast_Short(context,arraykhachsan.get(getAdapterPosition()).getTenkhachsan());
                context.startActivity(intent);
            }
        });


        return view;
    }
}


