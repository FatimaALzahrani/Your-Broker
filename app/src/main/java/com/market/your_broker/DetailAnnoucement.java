package com.market.your_broker;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DetailAnnoucement extends AppCompatActivity {
    String name,price,description,num,image,user,id,email,date;
    ImageView imageview;
    TextView name2,price2,description2,num2;
    private Button buy;
    private DatabaseReference ref;
    private String Orprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_annoucement);
        Intent intent=getIntent();
        name= intent.getStringExtra("name");
        price= Objects.requireNonNull(intent.getStringExtra("price")).replace(" ريال", "");
        Orprice= intent.getStringExtra("Orprice");
        description=intent.getStringExtra("description");
        num=intent.getStringExtra("num");
        image=intent.getStringExtra("image");
        user=intent.getStringExtra("username");
        id=intent.getStringExtra("id");
        email=intent.getStringExtra("email");
        date=intent.getStringExtra("date");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        name2= findViewById(R.id.name);
        price2=findViewById(R.id.price);
        description2=findViewById(R.id.Dis);
        imageview=findViewById(R.id.image);
        buy=findViewById(R.id.buy);

        String em= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        if (Objects.equals(em, "12fatimah.15@gmail.com")) {
            buy.setText("تأكيد أو حذف");
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailAnnoucement.this);
                    builder.setTitle("تأكيد");
                    builder.setMessage("هل ترغب في تأكيد هذا العنصر؟");
                    builder.setPositiveButton("تأكيد", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ref.child("ProductAdmin").child(id).removeValue();
                            community community=new community();
                            community.setImage(image);
                            community.setDate(date);
                            community.setUserName(user);
                            community.setEmail(email);
                            community.setQustions(name);
                            community.setDescription(description);
                            community.setId(id);
                            community.setNum(num);
                            community.setOrprice(Orprice);
                            community.setPrice(price);
                            ref.child("Product").child(id).setValue(community);
                        }
                    });
                    builder.setNegativeButton("حذف", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ref.child("ProductAdmin").child(id).removeValue();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }else{
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        name2.setText(name);
        price2.setText(price);
        description2.setText(description);
        Glide.get(imageview.getContext()).clearMemory();
        Glide.with(imageview.getContext())
                .load(image)
                .override(imageview.getWidth(), 240)// Set the desired image size
                .centerCrop()
                .into(imageview);

    }

    public void back(View view) {
        Intent intent=new Intent(this,Home.class);
        startActivity(intent);
    }

    public void contact(View view) {
    }

}