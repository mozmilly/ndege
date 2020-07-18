package com.example.ndege.units;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ndege.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class ClientDetailsActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    EditText clientPhone, clientName, margin, clientSize;
    Button next;

    private static final String TAG = ClientDetailsActivity.class.getSimpleName();
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private Uri uri;
    File file = null;

    ImageView imageView;
    Button upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        imageView = findViewById(R.id.client_image);

        clientName = findViewById(R.id.client_name);
        clientPhone = findViewById(R.id.client_phone);
        margin = findViewById(R.id.my_margin);
        next = findViewById(R.id.next_btn);
        upload = findViewById(R.id.upload_image);
        clientSize = findViewById(R.id.client_size);

        upload.setOnClickListener(v ->{
            AlertDialog alertDialog1 = new AlertDialog.Builder(ClientDetailsActivity.this).create();
            alertDialog1.setMessage("Select whether to choose file or take a photo.");
            alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "Take Photo...",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startCamera();
                        }
                    });
            alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "Choose photo from gallery...",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                    if (ContextCompat.checkSelfPermission(ClientDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(ClientDetailsActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_GALLERY_CODE);
                                        Toast.makeText(ClientDetailsActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(pickPhoto , REQUEST_GALLERY_CODE);//one can be replaced with any action code
                                    }

                                } else {
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto , REQUEST_GALLERY_CODE);//one can be replaced with any action code
                                }
                            } else {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , REQUEST_GALLERY_CODE);//one can be replaced with any action code

                            }
                        }
                    });
            alertDialog1.show();
            alertDialog1.setCanceledOnTouchOutside(false);
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (margin.getText().toString().isEmpty()){
                    Toast.makeText(ClientDetailsActivity.this, "You must fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ClientDetailsActivity.this, CheckoutActivity.class);
                    intent.putExtra("client_name", clientName.getText().toString());
                    intent.putExtra("client_phone", clientPhone.getText().toString());
                    intent.putExtra("margin", margin.getText().toString());
                    intent.putExtra("client_size", clientSize.getText().toString());
                    intent.putExtra("file", file);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, ClientDetailsActivity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            uri = data.getData();
            Uri selectedImage = uri;
            imageView.setImageURI(selectedImage);
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, ClientDetailsActivity.this);
                file = new File(filePath);
                Log.d(TAG, "Filename " + file.getName());
                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else if (requestCode==1&&resultCode==RESULT_OK){
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uri = getImageUri(getApplicationContext(), imageBitmap);
            Uri selectedImage = uri;
            imageView.setImageURI(selectedImage);
//            Toast.makeText(this, String.valueOf(uri), Toast.LENGTH_SHORT).show();
            if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, ClientDetailsActivity.this);
                file = new File(filePath);
                Log.d(TAG, "Filename " + file.getName());
                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }

        }
        else {
            Toast.makeText(this, "Done else", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(uri != null){
            String filePath = getRealPathFromURIPath(uri, ClientDetailsActivity.this);
            file = new File(filePath);
            Uri selectedImage = uri;
            imageView.setImageURI(selectedImage);

        } else {
            Toast.makeText(this, "uri is null", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    private void startCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }

    }
}
