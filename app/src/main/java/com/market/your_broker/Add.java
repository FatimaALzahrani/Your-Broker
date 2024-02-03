package com.market.your_broker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Add extends AppCompatActivity {
    private EditText Q, D, price, num;
    private Button add_announcements;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    private String[] cameraPermissions;
    private String[] storagePermission;
    private Uri image_uri;
    private ProgressDialog progressDialog, progressDialog1;
    private String k, username, email, img, Qustion, Desc;
    int i = 0;
    int added = 0;

    private DatabaseReference databaseReference;
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Q = findViewById(R.id.Name);
        D = findViewById(R.id.Description);
        price = findViewById(R.id.price);
        num = findViewById(R.id.num);
        photo = findViewById(R.id.imageview);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickDialog();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        String userID = user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    username = userProfile.username;
                    email = userProfile.email;
                    img = userProfile.image;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("الرجاء الانتظار قليلًا");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setTitle("الرجاء الانتظار قليلًا");
        progressDialog1.setCanceledOnTouchOutside(false);
    }
    public void submit(View view) {
        Qustion = Q.getText().toString().trim();
        Desc = D.getText().toString().trim();
        if (TextUtils.isEmpty(Qustion)) {
            Q.setError("اسم المنتج مطلوب");
            Toast.makeText(getApplicationContext(), "يجب تحديد المنتج!", Toast.LENGTH_SHORT).show();
        } else if (image_uri == null) {
            Toast.makeText(getApplicationContext(), "الصورة مطلوبه", Toast.LENGTH_SHORT).show();
        } else {
            // Upload the image to Firebase Storage and then add the product to the database
            uploadImageToFirebase(image_uri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        progressDialog.setMessage("يتم الآن رفع الصورة 📸");
        progressDialog.show();

        String k = FirebaseDatabase.getInstance().getReference("ProductAdmin").push().getKey();
        String filePathAndName = "image/" + k;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    progressDialog.dismiss();

                    // Get the download URL of the uploaded image
                    storageReference.getDownloadUrl().addOnSuccessListener(downloadImageUri -> {
                        // Add the product to the database with the image URL
                        addProductToDatabase(k, downloadImageUri.toString());
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "فشل في الحصول على رابط الصورة", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "فشل في رفع الصورة", Toast.LENGTH_SHORT).show();
                });
    }
    private void addProductToDatabase(String productId, String imageUrl) {
        progressDialog.setMessage("يتم الآن إضافة المنتج 📨");
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        TimeZone saudiTimeZone = TimeZone.getTimeZone("Asia/Riyadh");
        calendar.setTimeZone(saudiTimeZone);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String d = "" + day + "/" + month + "/" + year + " " + hour + ":" + minute;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProductAdmin");
        community community = new community();
        community.setUserImage(img);
        community.setDate(d);
        community.setUserName(username);
        community.setEmail(email);
        community.setQustions(Qustion);
        community.setDescription(Desc);
        community.setImage(imageUrl);
        community.setId(productId);
        community.setNum(num.getText().toString());
        int pr = Integer.parseInt(price.getText().toString());
        int added;
        if (pr <= 500) {
            added = 50;
        } else if (pr <= 1000) {
            added = 100;
        } else if (pr <= 5000) {
            added = 200;
        } else {
            added = 300;
        }
        community.setPrice(String.valueOf(pr + added));
        community.setOrprice(String.valueOf(pr));

        // Add the image URL to the "imageurl" key
        community.setImage(imageUrl);

        databaseReference.child(productId).setValue(community)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "تمت إضافة المنتج بنجاح ✅", Toast.LENGTH_SHORT).show();
                        new SweetAlertDialog(Add.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("رائع !")
                                .setContentText("قيمة المُنتج مع رِبح وسيـطك " + (pr + added) + "\n حيث قيمة خدمة وسيـطك = " + added)
                                .setConfirmText("تم")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        finish();
                                    }
                                })
                                .show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "حدث خطأ أثناء إضافة المنتج ❌", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addPost() {
        progressDialog.setMessage("يتم الآن إضافة منتجك 📨");
        progressDialog.show();
        k = FirebaseDatabase.getInstance().getReference("ProductAdmin").push().getKey();
        Uri image2 = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();
        Calendar calendar = Calendar.getInstance();
        TimeZone saudiTimeZone = TimeZone.getTimeZone("Asia/Riyadh");
        calendar.setTimeZone(saudiTimeZone);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String d = "" + day + "/" + month + "/" + year + " " + hour + ":" + minute;

        String filePathAndName = "image/" + k;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog1.setMessage("يتم اضافة المنتج");
                        progressDialog1.show();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadImageUri) {
                                if (downloadImageUri != null) {
                                    community community = new community();
                                    if (img != null) {
                                        community.setUserImage(img);
                                    }
                                    community.setDate(d);
                                    community.setUserName(username);
                                    community.setEmail(email);
                                    community.setQustions(Qustion);
                                    community.setDescription(Desc);
                                    community.setImage(downloadImageUri.toString());
                                    community.setId(k);
                                    community.setNum(num.getText().toString());
                                    int pr = Integer.parseInt(price.getText().toString());
                                    if (pr <= 500) {
                                        added = 50;
                                    } else if (pr <= 1000) {
                                        added = 100;
                                    } else if (pr <= 5000) {
                                        added = 200;
                                    } else {
                                        added = 300;
                                    }
                                    community.setPrice(String.valueOf(pr + added));
                                    databaseReference = FirebaseDatabase.getInstance().getReference("ProductAdmin");
                                    databaseReference.child(k).setValue(community, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if (error != null) {
                                                Toast.makeText(getApplicationContext(), "حدث خطأ اثناء اضافة المنتج ❌", Toast.LENGTH_SHORT).show();
                                                progressDialog1.dismiss();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "تمت اضافة منتجك بنجاح ✅", Toast.LENGTH_SHORT).show();
                                                progressDialog1.dismiss();
                                                progressDialog1.setMessage("قيمة المُنتج مع رِبح وسيـطك " + (pr + added) + "\n حيث قيمة خدمة وسيـطك = " + added);
                                                progressDialog1.show();
                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
    }

    private void showImagePickDialog() {
        pickFromCamera();
    }


    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

//    private boolean checkStoragePermission() {
//        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestStoragePermission() {
//        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
//    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission is granted, proceed with the desired action
                    // For example, open the camera or perform the intended operation.
                    pickFromCamera();
                } else {
                    // Camera permission is not granted, inform the user
                    Toast.makeText(this, "أذونة الكاميرا مطلوبة", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            // Remove the case for STORAGE_REQUEST_CODE
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                photo.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // No additional logic needed here
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
