package com.market.your_broker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Signup extends AppCompatActivity {
    EditText edtFullName, edtEmail, edtMobile, edtPassword;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    private String[] cameraPermissions;
    private String[] storagePermission;
    private Uri image_uri;
    ImageView photo;
    ProgressBar progressBar;
    TextView login;
    Uri pickedImgUri;
    private FirebaseAuth mAuth;
    private User user;
    private ProgressDialog progressDialog, progressDialog1;


    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressBar = findViewById(R.id.progressBar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("الرجاء الانتظار قليلًا");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setTitle("الرجاء الانتظار قليلًا");
        progressDialog1.setCanceledOnTouchOutside(false);

    }

    //back to login
    public void login() {
        Intent intent = new Intent(Signup.this, Login.class);
        startActivity(intent);
    }

    public void login2(View v) {
        Intent intent = new Intent(Signup.this, Login.class);
        startActivity(intent);
    }

    public void SignUp(View view) {
        edtFullName = findViewById(R.id.name);
        edtEmail = findViewById(R.id.email);
        edtMobile = findViewById(R.id.phone);
        edtPassword = findViewById(R.id.pass);
        login = (TextView) findViewById(R.id.login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        String em = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        String ph = edtMobile.getText().toString().trim();
        String name = edtFullName.getText().toString().trim();
        String regexPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
        int n = 0;
        if (name.equals("")) {
            edtFullName.setError("الاسم مطلوب");
            edtFullName.requestFocus();
            edtFullName.setText("");
            n++;
        } else {
            edtFullName.setBackground(this.getResources().getDrawable(R.drawable.yes_border));
        }
        if (ph.equals("")) {
            edtMobile.setError("رقم الجوال مطلوب");
            edtMobile.requestFocus();
            edtMobile.setText("");
            n++;
        } else if (ph.length() != 10) {
            edtMobile.setError("يجب ان يحتوي رقم الجوال على 10 ارقام");
            edtMobile.requestFocus();
            edtMobile.setText("");
            n++;
        } else {
            edtMobile.setBackground(this.getResources().getDrawable(R.drawable.yes_border));
        }
        if (em.equals("")) {
            edtEmail.setError("البريد الإلكتروني مطلوب");
            edtEmail.requestFocus();
            edtEmail.setText("");
            n++;
        } else if (!em.matches(regexPattern)) {
            edtEmail.setError("يُرجى إدخال بريد إلكتروني صالح");
            edtEmail.requestFocus();
            edtEmail.setText("");
            n++;
        } else {
            edtEmail.setBackground(this.getResources().getDrawable(R.drawable.yes_border));
        }
        if (pass.equals("")) {
            edtPassword.setError("كلمة المرور مطلوبة");
            edtPassword.requestFocus();
            edtPassword.setText("");
            n++;
        } else if (!pass.contains("$") && !pass.contains("*") && !pass.contains("@") && !pass.contains("#") && !pass.contains("&")) {
            edtPassword.setError("يجب أن تحتوي كلمة المرور على رموز");
            edtPassword.requestFocus();
            edtPassword.setText("");
            n++;
        } else if (pass.length() < 8) {
            edtPassword.setError("يجب أن يكون طول كلمة المرور أكثر من 7 خانات");
            edtPassword.requestFocus();
            edtPassword.setText("");
            n++;
        } else {
            edtPassword.setBackground(this.getResources().getDrawable(R.drawable.yes_border));
        }
        if (n == 0) {
            mAuth = FirebaseAuth.getInstance();
            progressBar.setVisibility(View.VISIBLE);
            mAuth.fetchSignInMethodsForEmail(em).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if (task.isSuccessful()) {
                        SignInMethodQueryResult result = task.getResult();
                        if (result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                            // User with this email already exists
                            progressBar.setVisibility(View.GONE);
                            showUserExistsDialog();
                        } else {
                            mAuth.createUserWithEmailAndPassword(em, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {


                                    progressDialog1.setMessage("يتم اضافة المستخدم");
                                    progressDialog1.show();
                                    user = new User(name, pass, em, ph );
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                            .setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                        user.sendEmailVerification();
                                                        new SweetAlertDialog(Signup.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("رائع !")
                                                                .setContentText("تم تسجيلك بنجاح , ولكن يجب عليك التحقق من بريدك الإلكتروني لتأكيد حسابك")
                                                                .setConfirmText("تم")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();
                                                                        Intent intent = new Intent(Signup.this, Login.class);
                                                                        startActivity(intent);
                                                                    }
                                                                })
                                                                .show();
                                                        progressBar.setVisibility(View.GONE);
                                                    } else {
                                                        Toast.makeText(Signup.this, "فشل التسجيل! ربما لديك حساب من قبل , حاول مرة أخرى!", Toast.LENGTH_LONG).show();
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                    }
                }
            });
        }
    }
    private void showUserExistsDialog() {
        new SweetAlertDialog(Signup.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("خطأ")
                .setContentText("المستخدم موجود بالفعل. يُرجى تسجيل الدخول")
                .setConfirmText("حسنًا")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent(Signup.this, Login.class);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private void showImagePickDialog() {
        String[] options = {"الكاميرا", "المعرض"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("التقط صورة")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            if (checkCameraPermission()) {
                                pickFromCamera();
                            } else {
                                requestCameraPermission();
                            }
                        } else {
                            if (checkStoragePermission()) {
                                pickFromGallery();
                            } else {
                                requestStoragePermission();
                            }
                        }
                    }
                })
                .show();
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
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

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

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
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "أذونات الكاميرا والتخزين مطلوبة", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "إذن التخزين مطلوب", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
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
                photo.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}