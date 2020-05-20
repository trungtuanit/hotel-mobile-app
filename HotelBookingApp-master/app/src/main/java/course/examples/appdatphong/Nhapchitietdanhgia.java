package course.examples.appdatphong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.provider.OpenableColumns;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import activity.MainActivity;
import ultil.check_connection;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import static ultil.server.urlGetData;

public class Nhapchitietdanhgia extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbarnhapdanhgia;
    private int PICK_IMAGE_REQUEST = 1;
    private Button buttonUpload;
    private Button buttonChoose;
    public static final String UPLOAD_KEY = "image_path";
    EditText edttenkhdg, edtheaderdg,edtdetaildg,edtmark;
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;
    Button btnxacnhan ;
    private String tendangnhap;
    private String header;
    private String detail;
    private String mark;
    //private int mark = 0;
    public static final String UPLOAD_URL = "http://datphongkhachsanonline.000webhostapp.com/server/upload5.php";
    String urlGetData = "http://datphongkhachsanonline.000webhostapp.com/server/getdanhgiamoilam1.php";
    int id1= 0;
    String s;

    // String urlGetData = "http://192.168.85.1/androidwebservice/getdanhgiamoilam.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhapchitietdanhgia);

        if (check_connection.haveNetworkConnection(getApplicationContext())) {
            Anhxa();
            GetIDdiem();
            ActionToolbar();
            //  EventClickButton();
        } else {
            check_connection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối Internet");
            finish();
        }
    }
    private void ActionToolbar() {
        setSupportActionBar(toolbarnhapdanhgia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarnhapdanhgia.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void GetIDdiem() {
        //-1 de bik bao loi o dau
        id1 = getIntent().getIntExtra("id1",-1);
        s=String.valueOf(id1);
        Log.d("id1",id1+"");
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            //  tendangnhap = edttenkhdg.getText().toString().trim();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    private void EventClickButton() {
        //   btnxacnhan.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //   public void onClick(View view) {
        //  final String tendangnhap = edttenkhdg.getText().toString().trim();
        //final String header = edtheaderdg.getText().toString().trim();
        //final String detail = edtdetaildg.getText().toString().trim();
        //  final String mark = edtmark.getText().toString().trim();
        //final int mark = Integer.parseInt(edtmark.getText().toString());
        final String tendangnhap = edttenkhdg.getText().toString().trim();
        final String header = edtheaderdg.getText().toString().trim();
        final String detail = edtdetaildg.getText().toString().trim();
        final String mark = edtmark.getText().toString().trim();
        int mark_int = Integer.valueOf(mark);

        //final String email = etEmail.getText().toString().trim();
        //   if (tendangnhap.length() > 0 && header.length() > 0 && detail.length() > 0 &&
        //         mark.length()>0) {
        //Kiểm tra nhập đầy đủ thông tin
        if (tendangnhap.length() > 0 && header.length() > 0 && detail.length() > 0 &&
                mark.length()>0 && mark_int >= 1 && mark_int <= 10) {
// mark>0 && mark<11
            RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetData,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("mataikhoan", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    JSONArray jsonArray = new JSONArray();
                    //  for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        //  jsonObject.put("tenuser", tendangnhap);
                        //jsonObject.put("title", header);
                        //jsonObject.put("detail", detail);
                        //jsonObject.put("mark", mark);
                        //jsonObject.put("idkhachsan", id1);
                        jsonObject.put(UPLOAD_KEY, get1());
                        jsonObject.put("image_name",get1());
                        // jsonObject.put("idkhachsan",get4());
                        jsonObject.put("idkhachsan",id1);
                        //  data.put("tenuser",get1());
                        jsonObject.put("title",get2());
                        jsonObject.put("detail",get3());

                        // data.put("mark",get4());

                        jsonObject.put("mark",get4());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);

                    //     }


                    HashMap<String, String> hashMap = new HashMap<String, String>();


                    hashMap.put("json", jsonArray.toString());
                    return hashMap;
                }
            };
            requestQueue2.add(stringRequest);
            check_connection.ShowToast_Short(getApplicationContext(), "Thêm Đánh Giá thành công");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("idtaikhoan", MainActivity.idtaikhoan);
            startActivity(intent);
            finish();
        } else {

            check_connection.ShowToast_Short(getApplicationContext(), "Hãy kiểm tra lại thông tin!");
        }
        //   }
        // });
    }

    private void uploadImage(){
        // tra ve string
        class UploadImage extends AsyncTask<Bitmap,Void,String>{
            ProgressDialog loading;
            //  RequestHandler rh = new RequestHandler();
            RequestHandler rh = new RequestHandler();
            //    hàm này được dùng để hiển thị thanh progressbar thông báo cho người dùng biết tác vụ bắt đầu thực hiện

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Nhapchitietdanhgia.this, "Uploading Image", "Please wait...",true,true);
            }
            //Hàm này được gọi khi doInBackground hàm thành công việc. Kết quả của doInBackground() sẽ được trả cho hàm này để hiển thị lên giao diện người dùng.
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }


            //     Tất cả code mà cần thời gian thực hiện sẽ được đặt trong hàm này.  Vì hàm này được thực hiện ở một thread hoàn toàn riêng biệt với UI thread nên bạn không được phép cập nhật giao diện ở đây.

            //   Để có thể cập nhập giao diện khi tác vụ đang thực hiện. Ví dụ như cập nhập trạng thái % file đã download được, chúng ta sẽ phải sử dụng đến hàm bên dưới onProgressUpdate()

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
                //   String ten = get(t);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                //  data.put("image_name",getFileName(filePath));
                data.put("image_name",get1());
                //  data.put("tenuser",get1());
                data.put("title",get2());
                data.put("detail",get3());

                // data.put("mark",get4());
                data.put("idkhachsan",s);
                // data.put("idkhachsan",id1);
                data.put("mark",get4());
                String result = rh.postRequest(UPLOAD_URL,data);
                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    //  @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        //     if(v == buttonUpload){
        //       String ten = get1();
        //     if(filePath !=null && ten.length() >0) {
        //       uploadImage();
        // } else {
        //   Toast.makeText(MainActivity.this,"Setlect Image",Toast.LENGTH_LONG).show();
        //}
        //
        // }
        if(v == buttonUpload){
            String ten = get1();
            String header = get2();
            String detail = get3();
            String mark = get4();
            int mark_int = Integer.valueOf(mark);
            //&& mark1>0 && mark1<11
            //   int mark1 = Integer.parseInt(edtmark.getText().toString());
            // String detail1 = get5();
            if(filePath !=null && ten.length() >0 && header.length()>0 && detail.length()>0
                    && mark.length()>0  && mark_int >= 1 && mark_int <= 10) {

                uploadImage();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("idtaikhoan", MainActivity.idtaikhoan);
                startActivity(intent);
            }
            else if(filePath ==null && ten.length() >0 &&header.length()>0 && detail.length()>0
                    && mark.length()>0  && mark_int >= 1 && mark_int <= 10) {
                EventClickButton();
            }
            // else if(ten.length() <0 || header.length()<0 || detail.length()<0 ||  mark.length()>0) {
            //   Toast.makeText(Nhapchitietdanhgia.this,"Hãy điền đầy đủ thông tin cần thiết!",Toast.LENGTH_LONG).show();
            //}
            //  if (tendangnhap.length() > 0 && header.length() > 0 && detail.length() > 0 &&
            //        mark.length()>0) {

            else {
                Toast.makeText(Nhapchitietdanhgia.this,"Hãy kiểm tra lại các thông tin",Toast.LENGTH_LONG).show();
            }
        }
        //   if(v == buttonintent){
        //     Intent intent = new Intent(MainActivity.this,getdanhgia.class);
        //   // id cuua loai san pham
        //   / //i la index ma cac ban muon lay
        //vi i = 1 trung vs id cho nen get(i)

        //intent.putExtra("idloai", mangdiadiem.get(i).getId());
        //startActivity(intent);
        //  }


    }
    String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    String get1(){
        tendangnhap = edttenkhdg.getText().toString().trim();
        return tendangnhap;
    }
    String get2(){
        header = edtheaderdg.getText().toString().trim();
        return header;
    }
    String get3(){
        detail = edtdetaildg.getText().toString().trim();
        return detail;
    }
    String get4(){
        // int get4(){
        // int mark = Integer.parseInt(edtmark.getText().toString());
        mark = edtmark.getText().toString().trim();
        return mark;
    }
    String get5(){
        int mark = Integer.parseInt(edtmark.getText().toString());
        s=String.valueOf(mark);
        return s;
    }
    private void Anhxa() {
        edttenkhdg = (EditText) findViewById(R.id.namedg);
        edtheaderdg = (EditText) findViewById(R.id.headerdg);
        edtdetaildg = (EditText) findViewById(R.id.detaildg);
        imageView  = (ImageView) findViewById(R.id.imageView);
        edtmark = (EditText) findViewById(R.id.markdg);
        buttonChoose = findViewById(R.id.btnChoose);
        buttonUpload = findViewById(R.id.btnUpload);
        btnxacnhan = (Button) findViewById(R.id.btn_xac_nhan_doi_thong_tin);
        toolbarnhapdanhgia = (Toolbar) findViewById(R.id.toolbarnhapdanhgia);
        //  buttonChoose.setOnClickListener((View.OnClickListener) this);
        // buttonintent.setOnClickListener(this);
        // buttonUpload.setOnClickListener((View.OnClickListener) this);
        buttonChoose.setOnClickListener(this);
        //  buttonintent.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);



    }
}
