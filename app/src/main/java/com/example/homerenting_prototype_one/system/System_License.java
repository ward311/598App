package com.example.homerenting_prototype_one.system;

import static com.example.homerenting_prototype_one.show.global_function.getCompany_id;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.homerenting_prototype_one.BuildConfig;
import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.calendar.Calendar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class System_License extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 111;
    Button takePic, uploadPic;
    ImageButton back;
    Context context = this;
    String TAG = "System_License";
    ImageView picView;
    String base64Image;
    Bundle bundle;
    String new_plateNum;
    Bitmap selectedImage;
    private String uploadServerUri = "http://140.117.71.91/598_new/app/uploadImage.php";
    private int serverResponseCode = 0;
    String img_src;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_license);
        linking();
        if (shouldAskPermissions()) {
            askPermissions();
        }
        back.setOnClickListener(v -> finish());

        uploadPic.setOnClickListener(v -> uploadImage());

        takePic.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        });
        bundle = getIntent().getExtras();
        new_plateNum = bundle.getString("new_plateNum");
        Log.d(TAG, "getVehiclePlate: "+ new_plateNum);
    }

    private void linking(){
        takePic = findViewById(R.id.picBtn);
        uploadPic = findViewById(R.id.uploadBtn);
        back = findViewById(R.id.imageBackButton);
        picView = findViewById(R.id.imageView_photo);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                Uri imageUri = data.getData();
                img_src = getPath(imageUri);
                Log.d(TAG, "src: "+img_src);
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                picView.setImageBitmap(selectedImage);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private String convertImage(Bitmap pic){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap newResizedImg = pic.copy(Bitmap.Config.ARGB_8888, true);
        newResizedImg.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return base64Image;
    }

    private void uploadImage(){
        if(picView.getDrawable() != null){
            Log.d(TAG, img_src+" uploadImage");
            new AsyncRetrieve().execute();
        }else{
            Toast.makeText(context, "尚未選擇照片", Toast.LENGTH_LONG).show();
        }
    }

    public int uploadFile(String sourceFileUri) {
        Log.d(TAG, sourceFileUri);

        String fileName = sourceFileUri;
        HttpURLConnection conn ;
        DataOutputStream dos ;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            Log.e("uploadFile", "Source File not exist :"+img_src);

            return 0;
        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(uploadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("company_id", getCompany_id(context));
                conn.setRequestProperty("plate_num", new_plateNum);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);
                conn.setRequestProperty("company_id", "1");
                Log.d(TAG, "FileName" + fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"company_id\"" + lineEnd);

                dos.writeBytes(lineEnd);


                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necessary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(() -> {
                        String msg = "File Upload Completed.\n\n See uploaded file your server. \n\n";
                        Log.d(TAG, msg);
                        Toast.makeText(context, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                    });
                    InputStream inputStream = conn.getInputStream();
                    //輸入串流的代表物件InputStream//對取得的資料進行讀取
                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(inputStream));
                    //宣告一個型態為BufferedReader的物件變數
                    //new BufferedReader表示以BufferedReader類別建構一個物件
                    // new InputStreamReader(inputStream)
                    //表示接受一個inputStream物件來建構一個InputStreamReader物件
                    String line = "";
                    String data = "";
                    while (line != null) { //  line不等於空值的時候
                        line = bufferedReader.readLine();
                        //readLine讀取一行文本。
                        data = data + line;
                    }
                    Log.d(TAG, "d : "+data);
                    String [] new_data = data.split("null");
                    String path = new_data [0];
                    Log.d(TAG, "path: "+path);
                    updateLicense(path);
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                runOnUiThread(() -> Toast.makeText(context, "MalformedURLException", Toast.LENGTH_SHORT).show());

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show());
                Log.d(TAG, "Exception : "  + e.getMessage(), e);
            }
            return serverResponseCode;

        } // End else block
    }
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    private void updateLicense(String filePath){
       String function_name = "update_vehicleLicense";
        RequestBody body = new FormBody.Builder()
                .add("function_name", function_name)
                .add("company_id", getCompany_id(context))
                .add("plate_num", new_plateNum)
                .add("license", filePath)
                .build();
        Log.d(TAG, "company_id: "+getCompany_id(context));
        Log.d(TAG, "plate_num: "+new_plateNum);
        Log.d(TAG, "license : " + filePath);

        Request request = new Request.Builder()
                .url(BuildConfig.SERVER_URL+"/functional.php")
                .post(body)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed: " + e.getMessage()); //顯示錯誤訊息
                //在app畫面上呈現錯誤訊息
                runOnUiThread(() -> Toast.makeText(context, "Toast onFailure.", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d(TAG,"responseData of license_update: "+responseData); //顯示資料

                try {
                    JSONObject result = new JSONObject(responseData);
                    //取得資料
                    if(result.getString("status").equals("failed")){
                        runOnUiThread(() -> Toast.makeText(context, "資料上傳失敗", Toast.LENGTH_LONG).show());
                    }else{
                        Log.d(TAG, ""+result.getString("status")+" "+result.getString("message"));

                        runOnUiThread(() -> {
                            Toast.makeText(context, "資料已上傳，請等待審核完畢", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, System_Data.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });

                    }
                    //顯示資料
                } catch (JSONException e) {
                    e.printStackTrace();
//
                }
            }
        });
    }

    public class AsyncRetrieve extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... Void) {
            uploadFile(img_src);
            return null;
        }
    }

}