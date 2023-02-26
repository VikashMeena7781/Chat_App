package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat_app.Model.User_Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {
    EditText username,email,password;
    Button register;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=findViewById(R.id.user_name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users_Data");
                            String id = user.getUid();

                            User_Info user_info = new User_Info();
                            user_info.setUsername(username.getText().toString());
                            user_info.setId(id);
                            user_info.setStatus("offline");
                            user_info.setSearch(username.getText().toString().toLowerCase());
                            user_info.setImageUrl("default");
                            databaseReference.child(id).setValue(user_info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Register_Activity.this, "Registration Success..", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                                            startActivity(intent);
                                            finish();

                                        }else{
                                            Toast.makeText(Register_Activity.this, "Registration Failed..", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                        }else{
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                email.setError("Your email is invalid or already in use. Kindly re-enter.");
                                email.requestFocus();
                            }catch (FirebaseAuthUserCollisionException e){
                                email.setError("User is already registered with this email. Use another email");
                                email.requestFocus();
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }
        });


    }
}