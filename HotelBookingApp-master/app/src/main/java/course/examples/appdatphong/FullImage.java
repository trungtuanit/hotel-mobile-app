package course.examples.appdatphong;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FullImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        ImageView full;
        full = findViewById(R.id.imageView2);
        String danhgia =  getIntent().getStringExtra("imagefull");
        //  String   hinhanhchitiet = danhgia.getHinhanhdiadiem();
        Picasso.with(getApplicationContext()).load(danhgia)
                .placeholder(R.drawable.noimage1)
                .error(R.drawable.noimage1)
                .into(full);
    }
}
