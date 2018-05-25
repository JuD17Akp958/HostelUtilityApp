package com.medeveloper.ayaz.hostelutility;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medeveloper.ayaz.hostelutility.officials.OfficialsHome;
import com.medeveloper.ayaz.hostelutility.student.Home;
import com.medeveloper.ayaz.hostelutility.student.StudentForm;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginAcitivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText Email,Password;
    Button Login;
    TextView Register;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitivity);
        ActionBar bar=getSupportActionBar();
        bar.hide();
        pDialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loging in").setContentText("Please wait while we log you in");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final RadioGroup loginAs=findViewById(R.id.signin_radio);


        mAuth= FirebaseAuth.getInstance();


        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(this,Home.class));
            finish();
        }


        Email=findViewById(R.id.email_login);
        Password=findViewById(R.id.password_login);
        Login=findViewById(R.id.login_button);
        Register=findViewById(R.id.login_signup);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();
                String email=Email.getText().toString();
                String pass=Password.getText().toString();
                int id=loginAs.getCheckedRadioButtonId();
                //tempLogIn(id);

                if (isOkay(email,pass))
                   authenticate(email,pass,id);//*/
                else pDialog.dismiss();

            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAcitivity.this,StudentForm.class));
                finish();
            }
        });





    }

    private void tempLogIn(int id) {
        if(id == R.id.student_radio)
        {
            startActivity(new Intent(this,Home.class));

        }
        else if(id==R.id.teacher_radio)
        {
            startActivity(new Intent(this,OfficialsHome.class));
        }
    }

    private boolean isOkay(String email, String pass) {


        Email.setError(null);
        Password.setError(null);
        boolean isOkay=true;
        if(email.equals(""))
        {
            Email.setError("Required Field");
            Email.requestFocus();
            isOkay=false;
            pDialog.dismiss();
        }
        else if(email.length()<6||!email.contains("@")||!email.contains(".com"))
        {
            pDialog.dismiss();
            Email.setError("Invalid Email");
            Email.requestFocus();
            isOkay=false;

        }
        else if(pass.equals(""))
        {
            pDialog.dismiss();
            Password.setError("Required Field");
            Password.requestFocus();
            isOkay=false;
        }
        else if(pass.length()<6)
        {
            Password.setError("Password length must be at least 6 digit");
            Password.requestFocus();


            isOkay=false;
        }
        else if(pass.equals("123456"))
        {/*
            Password.setError("Password must be complicated");
            Password.requestFocus();
            isOkay=false;
            */
        }

        return isOkay;
    }


    private void authenticate(final String email, final String pass, int id) {


        if(id==R.id.student_radio)
        {
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {

                        pDialog.dismiss();
                        startActivity(new Intent(LoginAcitivity.this,Home.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),""+task.getException(),Toast.LENGTH_SHORT).show();

                        pDialog.dismiss();
                        final SweetAlertDialog sDialog=new SweetAlertDialog(LoginAcitivity.this,SweetAlertDialog.ERROR_TYPE);
                        sDialog.setCancelable(false);
                        sDialog.setTitleText("Can't log you in").setContentText("Email: "+email+"\nPass: "+pass+"\nError: "+task.getException()).
                                setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sDialog.dismiss();
                                        // finish();
                                    }
                                });
                        sDialog.show();
                    }
                }
            });
        }
        else if(id==R.id.teacher_radio)
        {

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        pDialog.dismiss();
                        startActivity(new Intent(LoginAcitivity.this,OfficialsHome.class));
                        finish();
                    }
                    else
                        Toast.makeText(getApplicationContext(),""+task.getException(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(id==R.id.guest_radio)
        {
            new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE).
                    setTitleText("In progress").
                    setContentText("Guest login is in development process").show();
           //mAuth.signInAnonymously();
        }




    }

}
