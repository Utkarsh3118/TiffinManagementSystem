package com.USRT.tiffinmanagementsystem.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.USRT.tiffinmanagementsystem.Activity.Admin.AdminDashboard;
import com.USRT.tiffinmanagementsystem.Activity.Customer.Register;
import com.USRT.tiffinmanagementsystem.Activity.Customer.ViewAllVendors;
import com.USRT.tiffinmanagementsystem.Activity.Delivery.OwnVendor_Orders;
import com.USRT.tiffinmanagementsystem.Activity.Vendor.VendorDashboard;
import com.USRT.tiffinmanagementsystem.Model.DeliveryBoy;
import com.USRT.tiffinmanagementsystem.Model.UserModel;
import com.USRT.tiffinmanagementsystem.Model.Vendor;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    private View parent_view;

TextView sign_up;
    String usertype;
    private FirebaseAuth mAuth;
    TextInputEditText edit_username,edit_password;
    Button btn_login;
    LinearLayout lyt_signup;

    ProgressDialog mProgressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseReference databaseReference,databaseRef,dRef;
    List<Vendor> data;
    List<UserModel> data_cust;
    List<DeliveryBoy> data_boy;
    int flag = 0;
    String vendor_id,vname,v1mobile,vemail,vage,vpassword,vaddress,vphoto;
    String user_id,cust_mobile,cpassword,caddress,cemail,ccity,cust_name;
    String boy_id,boy_mobile,bpassword,baddress,bemail,bcity,boy_name,bvendor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parent_view = findViewById(android.R.id.content);
        lyt_signup= findViewById(R.id.lyt_signup);

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait..");
        edit_username=findViewById(R.id.edit_username);
        edit_password=findViewById(R.id.edit_password);
        btn_login=findViewById(R.id.btn_login);
        sign_up=findViewById(R.id.sign_up);

        data = new ArrayList<>();
        data_cust = new ArrayList<>();
        data_boy = new ArrayList<>();

        databaseReference = FirebaseDatabase. getInstance().getReference("Vendor").child("data");
        databaseRef = FirebaseDatabase.getInstance().getReference("Customer").child("user");
        dRef= FirebaseDatabase.getInstance().getReference().child("DeliveryBoy");

        Intent intent = getIntent();
        usertype = intent.getStringExtra("usertype");

        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
        String usernamelog = sharedPreferences.getString("user_name", null);
        String passwordlog = sharedPreferences.getString("password", null);
        String vusernamelog = sharedPreferences.getString("vuser_name", null);
        String cusernamelog = sharedPreferences.getString("cuser_name", null);
        String dusernamelog = sharedPreferences.getString("duser_name", null);

        if(usertype.equalsIgnoreCase("admin")) {

            // String resultlog="2";
            Log.e("login", usernamelog + "/////" + passwordlog);
            if ((!(usernamelog == null))) {

                Intent i=new Intent(Login.this, AdminDashboard.class);
                startActivity(i);
                finish();

            }


            mAuth.createUserWithEmailAndPassword("admin@gmail.com", "123456")
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //  Toast.makeText(Login.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                            if (!task.isSuccessful()) {
                                //  Toast.makeText(Login.this, "Authentication failed." + task.getException(),Toast.LENGTH_SHORT).show();
                            } else {
                                // startActivity(new Intent(Login.this, AddBus.class));
                                //  finish();
                            }
                        }
                    });
        }
        if(usertype.equalsIgnoreCase("vendor")) {
            if ((!(vusernamelog == null))) {

                Intent i = new Intent(Login.this, VendorDashboard.class);
                startActivity(i);
                finish();

            }
        }
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
                finish();
            }
        });

        if(usertype.equalsIgnoreCase("customer")) {
            lyt_signup.setVisibility(View.VISIBLE);

            if ((!(cusernamelog == null))) {

                Intent i = new Intent(Login.this, ViewAllVendors.class);
                startActivity(i);
                finish();

            }
        }
        if(usertype.equalsIgnoreCase("deliveryboy")) {
            lyt_signup.setVisibility(View.VISIBLE);

            if ((!(dusernamelog == null))) {

                Intent i = new Intent(Login.this, OwnVendor_Orders.class);
                startActivity(i);
                finish();

            }
        }



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email= edit_username.getText().toString();
                final String password= edit_password.getText().toString();

                if(usertype.equalsIgnoreCase("admin")){
                    mProgressDialog.show();
                    if (email.equals("admin@gmail.com") && password.equals("123456")) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                                            editor = sharedPreferences.edit();
                                            editor.putString("user_name", email);
                                            editor.putString("password", password);
                                            //editor.putString("user_role", "2");
                                            editor.commit();
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.e("Login : ", "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            // updateUI(user);
                                            startActivity(new Intent(Login.this, AdminDashboard.class));
                                            finish();
                                            mProgressDialog.dismiss();

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("Login :", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(Login.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            mProgressDialog.dismiss();

                                            //updateUI(null);
                                        }

                                        // ...
                                    }
                                });

                    }else{
                        Toast.makeText(Login.this, "Invalid Credentials !!",
                                Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();

                    }


                }else if(usertype.equalsIgnoreCase("vendor")){
                    mProgressDialog.show();
                    String vmobile = edit_username.getText().toString().trim();
                    String vpass = edit_password.getText().toString().trim();

                    if (vmobile.isEmpty() || vmobile.length() < 10) {
                        edit_username.setError("Enter a valid mobile");
                        edit_username.requestFocus();
                        mProgressDialog.dismiss();
                        return;
                    } else if (!vmobile.equals("")) {
                        for (int i = 0; i < data.size(); i++) {
                            if (vmobile.equals(data.get(i).getVphone()) && vpass.equals(data.get(i).getVpassword())) {
                                Log.d("da11", data.get(i).getVphone());


                                vendor_id = data.get(i).getVendor_id();
                                vname=data.get(i).getVname();
                                v1mobile=data.get(i).getVphone();
                                vemail=data.get(i).getVemail();
                                vage=data.get(i).getVage();
                                vpassword=data.get(i).getVpassword();
                                vaddress=data.get(i).getVaddress();
                                vphoto=data.get(i).getPhoto();

                                flag = 1;
                            }
                            else{
                                Toast.makeText(Login.this, "Enter Valid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Log.d("da11", String.valueOf(flag));
                        if (flag == 1) {
                            mProgressDialog.dismiss();

                            sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("vuser_name", vmobile);
                            editor.putString("vpassword", vpassword);
                            editor.putString("vendor_id", vendor_id);
                            editor.commit();
                            Intent intent = new Intent(Login.this, VendorDashboard.class);
                            intent.putExtra("mobile", v1mobile);
                            intent.putExtra("vendor_id", vendor_id);
                            intent.putExtra("vemail",vemail);
                            intent.putExtra("vaddress",vaddress);
                            intent.putExtra("vage",vage);
                            intent.putExtra("vname",vname);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Login.this, "This number is not registered", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }

                    }

                }

                else if(usertype.equalsIgnoreCase("customer")){
                    String mobile = edit_username.getText().toString().trim();
                    String pass1 = edit_password.getText().toString().trim();

                    if (mobile.isEmpty() || mobile.length() < 10) {
                        edit_username.setError("Enter a valid mobile");
                        edit_username.requestFocus();
                        return;
                    } else if (!mobile.equals("")) {
                        for (int i = 0; i < data_cust.size(); i++) {
                            if (mobile.equals(data_cust.get(i).getMobile()) && pass1.equals(data_cust.get(i).getPassword())) {
                                Log.d("da11", data_cust.get(i).getMobile());

                                user_id = data_cust.get(i).getUser_id();
                                cust_mobile=  data_cust.get(i).getMobile();
                                cpassword=  data_cust.get(i).getPassword();
                                caddress=data_cust.get(i).getAddress();
                                cemail=data_cust.get(i).getEmail();
                                ccity=data_cust.get(i).getCity();
                                cust_name=data_cust.get(i).getCust_name();
                                flag = 1;

                            }
                            else{
                                Toast.makeText(Login.this, "Enter Valid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Log.d("da11", String.valueOf(flag));
                        if (flag == 1) {

                            sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("cuser_name", cust_mobile);
                            editor.putString("cpassword", cpassword);
                            editor.putString("user_id", user_id);
                            editor.commit();

                            Intent intent = new Intent(Login.this, ViewAllVendors.class);
                            intent.putExtra("mobile", mobile);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("cust_name",cust_name);
                            intent.putExtra("address",caddress);
                            intent.putExtra("email",cemail);
                            intent.putExtra("city",ccity);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Login.this, "This number is not registered", Toast.LENGTH_SHORT).show();
                        }

                    }


                }
                else if(usertype.equalsIgnoreCase("deliveryboy")){


                    String mobile = edit_username.getText().toString().trim();
                    String pass1 = edit_password.getText().toString().trim();

                    if (mobile.isEmpty() || mobile.length() < 10) {
                        edit_username.setError("Enter a valid mobile");
                        edit_username.requestFocus();
                        return;
                    } else if (!mobile.equals("")) {
                        for (int i = 0; i < data_boy.size(); i++) {
                            if (mobile.equals(data_boy.get(i).getMobile()) && pass1.equals(data_boy.get(i).getPassword())) {
                                Log.d("da11", data_boy.get(i).getMobile());


                                boy_id = data_boy.get(i).getBoyid();
                                boy_mobile=  data_boy.get(i).getMobile();
                                bpassword=  data_boy.get(i).getPassword();
                                baddress=data_boy.get(i).getAddress();
                                bemail=data_boy.get(i).getEmail();
                                bcity=data_boy.get(i).getCity();
                                boy_name=data_boy.get(i).getName();
                                bvendor_id=data_boy.get(i).getVendor_id();
                                flag = 1;

                            }
                            else{
                                Toast.makeText(Login.this, "Enter Valid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Log.d("da11", String.valueOf(flag));
                        if (flag == 1) {

                            sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("duser_name", boy_mobile);
                            editor.putString("dpassword", bpassword);
                            editor.putString("boy_id", boy_id);
                            editor.putString("bvendor_id", bvendor_id);
                            editor.putString("boy_name", boy_name);
                            editor.commit();

                            Intent intent = new Intent(Login.this, OwnVendor_Orders.class);
                            intent.putExtra("mobile", boy_mobile);
                            intent.putExtra("boy_id", boy_id);
                            intent.putExtra("boy_name",boy_name);
                            intent.putExtra("address",baddress);
                            intent.putExtra("email",bemail);
                            intent.putExtra("city",bcity);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Login.this, "This number is not registered", Toast.LENGTH_SHORT).show();
                        }

                    }
                }


            }
        });


    }


    //When initializing your Activity, check to see if the user is currently signed in.
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Vendor model = snapshot.getValue(Vendor.class);
                        data.add(model);

                        Log.d("da", model.getVphone().toString());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel model1 = snapshot.getValue(UserModel.class);
                        data_cust.add(model1);

                        Log.e("cust da", model1.getMobile().toString());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        DeliveryBoy model2 = snapshot.getValue(DeliveryBoy.class);
                        data_boy.add(model2);

                        Log.e("Boy da", model2.getMobile().toString());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
