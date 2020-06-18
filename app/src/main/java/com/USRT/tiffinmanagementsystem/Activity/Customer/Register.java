package com.USRT.tiffinmanagementsystem.Activity.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.USRT.tiffinmanagementsystem.Model.UserModel;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    private Button btnRegister;
    private EditText editName,editContact,editTextpassword,editEmail,editAddress,editcity;
    private ProgressDialog progressDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    //firebase auth object
    DatabaseReference databaseReference;
    String user_id ="";
    String token;
    List<UserModel> data;
    int flag = 0;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait..");

        btnRegister=(Button) findViewById(R.id.buttonSignup);
        editName=(EditText)findViewById(R.id.editTextName);
        editContact=(EditText)findViewById(R.id.editTextmobile);
        editTextpassword=(EditText)findViewById(R.id.editTextpassword);
        editAddress=(EditText)findViewById(R.id.editTextAddress);
        editcity=(EditText)findViewById(R.id.editTextcity);
        editEmail=(EditText)findViewById(R.id.editTextEmail);
        data = new ArrayList<>();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("notification", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast

                        Log.d("notification11", token);
                        // Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child("user");

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
            }
        });


    }


    private void registerUser(){

        //getting email and password from edit texts
        String name = editName.getText().toString().trim();
        final String contact  = editContact.getText().toString().trim();
        String password = editTextpassword.getText().toString();
        String address  = editAddress.getText().toString().trim();
        String city  = editcity.getText().toString().trim();
        String email  = editEmail.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(name)){
            editName.setError("Please enter name");
            return;
        }else if(TextUtils.isEmpty(contact)){
            editContact.setError("Please enter contact");
            return;
        }
        else if(TextUtils.isEmpty(password) || password.length()<4){
            editContact.setError("Please enter password greater than 4 digits");
            return;
        }else if(TextUtils.isEmpty(address)){
            editAddress.setError("Please enter address");

            return;
        }else if(TextUtils.isEmpty(city)){
            editcity.setError("Please enter city");

            return;
        }else if(TextUtils.isEmpty(email)){
            editEmail.setError("Please enter email");

            return;
        }else {
            mProgressDialog.show();
            user_id = databaseReference.push().getKey();

            for (int i = 0; i < data.size(); i++) {
                if (contact.equals(data.get(i).getMobile())) {
                    Log.d("da11", data.get(i).getMobile());
                    user_id = data.get(i).getUser_id();
                    flag = 1;
                }
            }
            Log.d("da11", String.valueOf(flag));
            if (flag == 1) {
                flag = 0;
                Toast.makeText(Register.this, "This number already registered", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();

            } else {
                databaseReference.child(user_id).setValue(new UserModel(user_id,name,contact,password,email,address,city,token))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //  Toast.makeText(RegisterActivity.this, "User Added Successful", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setMessage("Register Successfully")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                mProgressDialog.dismiss();

                                                Intent i = new Intent(Register.this, OTPVerification.class);
                                                i.putExtra("mobile", contact);
                                                i.putExtra("user_id", user_id);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Toast.makeText(RegisterActivity.this, "User Not Added Successful", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                        builder.setMessage("Something Went Wrong !!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                        mProgressDialog.dismiss();

                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }
        }

        //if the email and password are not empty
        //displaying a progress dialog

        /*progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();*/





    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        data.add(model);

                        Log.d("da", model.getMobile().toString());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
