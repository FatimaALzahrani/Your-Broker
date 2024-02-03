package com.market.your_broker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Home extends Fragment {
    RecyclerView recview;
    ProductAdapter adapter;
    private SearchView searchView;
    MeowBottomNavigation bottomNavigation;
    DatabaseReference ref;
    List<Product> productList = new ArrayList<>();
    List<Detail> productListD = new ArrayList<>();
    private DatabaseReference reference;
    private String userID;
    DatabaseReference databaseReference;
    static int PERMISSION_CODE= 100;
    private FirebaseDatabase firebaseDatabase;
    private String table;

    public Home() {
        // Required empty public constructor
    }
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        searchView=(SearchView) view.findViewById(R.id.searchview);
        searchView.clearFocus();
        String em= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        if (Objects.equals(em, "12fatimah.15@gmail.com")) {
        table="ProductAdmin";
        }else{
            table="Product";
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref.child(table).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    Detail product2 = snapshot.getValue(Detail.class);
                    product.setPrice(product.getPrice()+" ريال ");
                    productList.add(product);
                    productListD.add(product2);
                    adapter.notifyDataSetChanged();
                }
                Collections.reverse(productList);
                Collections.reverse(productListD);
            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.productlist);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter = new ProductAdapter(getContext(), productList, new ProductAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Product p){
                Intent intent=new Intent(getActivity(),DetailAnnoucement.class);
                Bundle bundle = new Bundle();
                intent.putExtra("name",p.name);
                intent.putExtra("price",p.price);
                intent.putExtra("Orprice",p.Orprice);
                intent.putExtra("description",p.description);
                intent.putExtra("num",p.num);
                intent.putExtra("image",p.image);
                intent.putExtra("username",p.userName);
                intent.putExtra("email",p.email);
                intent.putExtra("date",p.date);
                intent.putExtra("id",p.id);
                startActivity(intent);
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Cart");
        userID=user.getUid();
        ref = database.getReference();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClicked(new ProductAdapter.OnItemClickListener() {
                                     @Override
                                     public void onItemClicked(int position) {
                                         Detail product = new Detail();
                                         Map<Detail, Integer> FMA_Map2 = new HashMap<Detail, Integer>();
                                         ;
                                         Query query = reference
                                                 .child("Cart")
                                                 .orderByChild("name_of_announcement")
                                                 .equalTo(productList.get(position).name);
                                         query.addListenerForSingleValueEvent(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                 if (FMA_Map2.containsKey(product)) {
                                                     for (Map.Entry s : FMA_Map2.entrySet()) {
                                                         if (s.getKey() == product) {
                                                             Query queryRef = ref.child("Cart").orderByChild("name_of_announcement").equalTo(product.name);
                                                             queryRef.addChildEventListener(new ChildEventListener() {
                                                                 @Override
                                                                 public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                                                     snapshot.getRef().setValue(null);
                                                                 }

                                                                 @Override
                                                                 public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                                 }

                                                                 @Override
                                                                 public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                                                 }

                                                                 @Override
                                                                 public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                                                 }

                                                                 @Override
                                                                 public void onCancelled(@NonNull DatabaseError error) {

                                                                 }
                                                             });
                                                             Integer count2 = ((Number) s.getValue()).intValue() + 1;
                                                             product.setDate(productList.get(position).date);
                                                             product.setUserName(productList.get(position).userName);
                                                             product.setDescription(productList.get(position).description);
                                                             product.setId(productList.get(position).id);
                                                             product.setNum(productList.get(position).num);
                                                             product.setPrice(productList.get(position).price);
                                                             product.setImage(productList.get(position).image);
                                                             product.setQustions(productList.get(position).name);
                                                             product.setEmail(user.getEmail());
                                                             FMA_Map2.put((Detail) (s.getKey()), count2);
                                                         }
                                                     }
                                                 } else {
                                                     product.setDate(productList.get(position).date);
                                                     product.setUserName(productList.get(position).userName);
                                                     product.setDescription(productList.get(position).description);
                                                     product.setId(productList.get(position).id);
                                                     product.setNum(productList.get(position).num);
                                                     product.setPrice(productList.get(position).price);
                                                     product.setImage(productList.get(position).image);
                                                     product.setQustions(productList.get(position).name);
                                                     product.setEmail(user.getEmail());
                                                     firebaseDatabase = FirebaseDatabase.getInstance();
                                                     String id = firebaseDatabase.getReference("Cart").push().getKey();
                                                     firebaseDatabase.getReference("Cart").child(id).setValue(product);
                                                 }
                                                 Toast.makeText(getContext(), "Announcements Added to Cart", Toast.LENGTH_SHORT).show();
                                             }


                                             @Override
                                             public void onCancelled(DatabaseError databaseError) {

                                             }
                                         });
//                productList.remove(position);
//                adapter.notifyItemRemoved(position);
//                recyclerView.setAdapter(adapter);
                                     }
                                 });
        SearchView searchView = (SearchView) view.findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Query fireQuery = ref.child(table);
                fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        } else {
                            List<Product> searchList = new ArrayList<Product>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Product product = snapshot.getValue(Product.class);
                                if(product.name.contains(query.toLowerCase()) || product.price.toLowerCase().contains(query.toLowerCase())
                                        ||product.description.toLowerCase().contains(query.toLowerCase()))
                                    searchList.add(product);
                                adapter = new ProductAdapter(getContext(), searchList, new ProductAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(Product p) {
                                        Intent intent=new Intent(getActivity(),DetailAnnoucement.class);
                                        intent.putExtra("name",p.name);
                                        intent.putExtra("price",p.price);
                                        intent.putExtra("Orprice",p.Orprice);
                                        intent.putExtra("description",p.description);
                                        intent.putExtra("num",p.num);
                                        intent.putExtra("image",p.image);
                                        intent.putExtra("username",p.userName);
                                        intent.putExtra("email",p.email);
                                        intent.putExtra("date",p.date);
                                        intent.putExtra("id",p.id);
                                        startActivity(intent);
                                    }
                                });
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter = new ProductAdapter(getContext(), productList,new ProductAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(Product p) {
                        Intent intent=new Intent(getActivity(),DetailAnnoucement.class);
                        Bundle bundle = new Bundle();
                        intent.putExtra("name",p.name);
                        intent.putExtra("price",p.price);
                        intent.putExtra("Orprice",p.Orprice);
                        intent.putExtra("description",p.description);
                        intent.putExtra("num",p.num);
                        intent.putExtra("image",p.image);
                        intent.putExtra("username",p.userName);
                        intent.putExtra("email",p.email);
                        intent.putExtra("date",p.date);
                        intent.putExtra("id",p.id);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
                return false;
            }
        });
//        bottomNavigation=view.findViewById(R.id.navi);
//        bottomNavigation.setCount(4,Cart22.count);
        return view;
    }
}