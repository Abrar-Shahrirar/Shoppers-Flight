package com.meass.travelagenceyuser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikepenz.materialdrawer.Drawer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;

public class PlacedOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);

        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        toolbar.setTitle("Placed a Order ");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        customer_name=findViewById(R.id.customer_name);
        address_p=findViewById(R.id.address_p);
        contactp=findViewById(R.id.contactp);

        phonennu=findViewById(R.id.phonennu);
        parcelitem_p=findViewById(R.id.parcelitem_p);
        weight_p=findViewById(R.id.weight_p);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        session = new UserSession(getApplicationContext());
        getValues();
        try {
            detector =getIntent().getStringExtra("usernmae");
            weight=getIntent().getStringExtra("weight");
        }catch (Exception e) {
            detector =getIntent().getStringExtra("usernmae");
            weight=getIntent().getStringExtra("weight");
        }
        phonennu.setText(contactp.getText().toString());
        weight_p.addTextChangedListener(listet);


    }
    String weight;
    TextWatcher listet =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
check =s.toString();
if (TextUtils.isEmpty(check)) {
}
else {
    if (Double.parseDouble(check)>5) {
        Toast.makeText(PlacedOrderActivity.this, "Maximum amount for taking products from abroad for mobile , laptop and gadget is 5 and for chocklate is 5kg.", Toast.LENGTH_SHORT).show();
    }
    else {
    }
}
        }
    };
    String check;
    String lil;
    LocalDate today = LocalDate.now(ZoneId.of("Asia/Dhaka"));
    String detector;
    private UserSession session;
    private HashMap<String, String> user;
    private String name, email, photo, mobile,username;
    private Drawer result;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getValues() {
        //validating session


        try {
            //get User details if logged in
            session.isLoggedIn();
            user = session.getUserDetails();

            name = user.get(UserSession.KEY_NAME);
            email = user.get(UserSession.KEY_EMAIL);
            mobile = user.get(UserSession.KEY_MOBiLE);
            photo = user.get(UserSession.KEY_PHOTO);
            username=user.get(UserSession.Username);
            // Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();


        }catch (Exception e) {
            //get User details if logged in
            session.isLoggedIn();
            user = session.getUserDetails();

            name = user.get(UserSession.KEY_NAME);
            email = user.get(UserSession.KEY_EMAIL);
            mobile = user.get(UserSession.KEY_MOBiLE);
            photo = user.get(UserSession.KEY_PHOTO);
            username=user.get(UserSession.Username);


        }
        //Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();
    }
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText customer_name,weight_p,parcelitem_p,phonennu,contactp,address_p;
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    public void placed(View view) {

      //  customer_name,weight_p,parcelitem_p,phonennu,contactp,address_p
        phonennu.setText(contactp.getText().toString());
                if(TextUtils.isEmpty(customer_name.getText().toString())||
                        TextUtils.isEmpty(weight_p.getText().toString())||
                        TextUtils.isEmpty(parcelitem_p.getText().toString())||TextUtils.isEmpty(phonennu.getText().toString())||
                        TextUtils.isEmpty(contactp.getText().toString())||TextUtils.isEmpty(address_p.getText().toString())) {
                    Toast.makeText(this, "Enter all information", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(PlacedOrderActivity.this);
                    builder.setTitle("Confirmation")
                            .setMessage("Are you want to placed this order to suplier?")
                            .setPositiveButton("Np", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            final KProgressHUD progressDialog=  KProgressHUD.create(PlacedOrderActivity.this)
                                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                    .setLabel("Please wait....Setup Process")
                                    .setCancellable(false)
                                    .setAnimationSpeed(2)
                                    .setDimAmount(0.5f)
                                    .show();
                            firebaseFirestore.collection("All_UserID")
                                    .document(detector)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().exists()) {
                                                    String uuid=task.getResult().getString("uuid");
                                                    firebaseFirestore.collection("User2")
                                                            .document(detector)
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        if (task.getResult().exists()) {
                                                                            try {
                                                                                long ts = System.currentTimeMillis()/1000;

                                                                                String image1=task.getResult().getString("image");
                                                                                String name1=task.getResult().getString("name");
                                                                                String number1=task.getResult().getString("number");
                                                                                ///customer_name,weight_p,parcelitem_p,phonennu,contactp,address_p
                                                                                OrderDetails orderDetails = new OrderDetails(name1,
                                                                                       name,mobile,number1,""+ts,""+today, "Pending",
                                                                                        weight.toString(),address_p.getText().toString(),contactp.getText().toString(),
                                                                                        phonennu.getText().toString(),parcelitem_p.getText().toString(),weight_p.getText().toString(),
                                                                                        firebaseAuth.getCurrentUser().getEmail(),detector);

                                                                                firebaseFirestore.collection("Orderss")
                                                                                        .document(firebaseAuth.getCurrentUser().getEmail())
                                                                                        .collection("List")
                                                                                        .document(""+ts)
                                                                                        .set(orderDetails)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                            }
                                                                                        });
                                                                                firebaseFirestore.collection("Orderss")
                                                                                        .document(detector)
                                                                                        .collection("List")
                                                                                        .document(""+ts)
                                                                                        .set(orderDetails)
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    progressDialog.dismiss();
                                                                                                    Toast.makeText(PlacedOrderActivity.this, "Order is placed", Toast.LENGTH_SHORT).show();
                                                                                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                                                                                }

                                                                                            }
                                                                                        });


                                                                            }catch (Exception e) {
                                                                                progressDialog.dismiss();
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    });
                        }
                    }).create();
                    builder.show();
                }
    }
}