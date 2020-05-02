package com.example.shrrmoviecat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Bitmap newProfilePic;
    String imageuri;
    public static final int PICK_IMAGE = 1;

    @BindView(R.id.name_profile)
    EditText name;

    @BindView(R.id.email_profile)
    EditText email;

    @BindView(R.id.imageView_profile)
    CircleImageView imageView;

    @BindView(R.id.save_profile)
    Button save;

    @BindView(R.id.edit)
    TextView edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
setTitle("Profile Settings");
        sp=getSharedPreferences("profile",MODE_PRIVATE);
editor=sp.edit();
        name.setText(sp.getString("name","Shrrayan Goel"));
        email.setText(sp.getString("email","goelshrrayan123@gmail.com"));
        imageView.setImageURI(Uri.parse(sp.getString("imageurl","goelshrrayan123@gmail.com")));
imageuri=sp.getString("imageurl","goelshrrayan123@gmail.com");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("name",name.getText().toString());
                editor.putString("email",email.getText().toString());
                editor.putString("imageurl",  imageuri);
                editor.apply();

                startActivity(new Intent(ProfileActivity.this,MainActivity.class));


            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

if(isStoragePermissionGranted()) {
    Intent intent = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
    intent.setType("image/*");
    intent.putExtra("crop", "true");
    intent.putExtra("scale", true);
    intent.putExtra("outputX", 256);
    intent.putExtra("outputY", 256);
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    intent.putExtra("return-data", true);
    startActivityForResult(intent, 1);
}
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {Log.i("Inonac","te");
        if (resultCode != RESULT_OK) {
            Log.i("Inonac","t02e");
            return;
        }
        if (requestCode == 1) {
            Log.i("Inonac","te3");



            if (data != null) {
                //Get image
                Uri _uri = data.getData();
                String filePath = null;
                if (_uri != null && "content".equals(_uri.getScheme())) {
                    Cursor cursor = this.getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
                    cursor.moveToFirst();
                    filePath = cursor.getString(0);
                    cursor.close();
                } else {
                    filePath = _uri.getPath();
                }

                Log.i("Inonac","te4");
                imageuri=filePath+"";
                imageView.setImageURI(Uri.parse(filePath));
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();




            }
        }



        }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
}

