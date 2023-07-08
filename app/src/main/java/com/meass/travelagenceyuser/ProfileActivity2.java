package com.meass.travelagenceyuser;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikepenz.materialdrawer.Drawer;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity2 extends AppCompatActivity {

    private RecyclerView profileBlogListView;
    private List<BlogPost> blog_list;
    private ProfileBlogRecyclerAdapter profileBlogRecyclerAdapter;


    private Toolbar mToolbar;
    private TextView mUsername, mAbout, mChange;
    private TextView mFollowers, mFollowing, postCount;
    private CircleImageView mProfileImage;
    private ProgressBar progressBar;
    private Button followBtn;
    private final static String TAG = "PROFILE_ACTIVITY";
    private LinearLayout mFollowerLayout, mFollowingLayout;

    private RelativeLayout mRequestLayout;
    private TextView mRequestName;
    private Button mRequestAccept, mRequestCancel;
    private ProgressBar mAcceptProgress, mCancelProgress;
    private int mCurrentState;
    private String mCurrentUserId;

    private CollectionReference mRequestReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String mName;
    private String mCurrentName;
     String user_id;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        mToolbar = findViewById(R.id.profile_toolbar);
        mToolbar.setTitle("Profile");
        mToolbar.setElevation(10.0f);
        mToolbar.setTitleTextAppearance(ProfileActivity2.this, R.style.TitleBarTextAppearance);
        mToolbar.setTitleTextColor(ContextCompat.getColor(ProfileActivity2.this, R.color.black));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        session = new UserSession(getApplicationContext());
        getValues();
        mCurrentUserId = mAuth.getCurrentUser().getEmail();
        firebaseFirestore = FirebaseFirestore.getInstance();

         user_id = getIntent().getStringExtra("user_id");
       ///Toast.makeText(this, ""+user_id, Toast.LENGTH_SHORT).show();
        mRequestReference = firebaseFirestore.collection("Requests");
        //Toast.makeText(this, "" + mCurrentUserId, Toast.LENGTH_SHORT).show();
        mRequestLayout = findViewById(R.id.request_layout);
        mRequestName = findViewById(R.id.request_name);
        mRequestAccept = findViewById(R.id.accept_request_btn);
        mRequestCancel = findViewById(R.id.cancel_request_btn);
        mAcceptProgress = findViewById(R.id.acceptProgress);
        mCancelProgress = findViewById(R.id.cancelProgress);
        //mCurrentState = NOT_FOLLOW;


        mUsername = findViewById(R.id.profile_name);
        mAbout = findViewById(R.id.profile_desc);
        mChange = findViewById(R.id.profile_change);
        mProfileImage = findViewById(R.id.profile_image);

        postCount = findViewById(R.id.postCount);
        firebaseFirestore.collection("User2")
                .document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                try {
                                    String immm="https://firebasestorage.googleapis.com/v0/b/cash-money-express-ltd.appspot.com/o/profile_images%2Fo8Dnqf5LFodKSwocGQ4nKB7ZEkW2.jpg?alt=media&token=c22700e2-67ca-4497-8bf1-204ac83b6749";

                                    String image=task.getResult().getString("image");
                                    if (image.equals(immm)) {
                                        Picasso.get().load(R.drawable.app_logo).into(mProfileImage);
                                        mUsername.setText(task.getResult().getString("name"));
                                    }
                                    else {
                                        Picasso.get().load(task.getResult().getString("image")).into(mProfileImage);
                                        mUsername.setText(task.getResult().getString("name"));
                                    }

                                }catch (Exception e) {
                                   e.printStackTrace();
                                }

                            }
                        }
                    }
                });
        follow_btn=findViewById(R.id.follow_btn);
        follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("FirendsList")
                        .document("MyList")
                        .collection(mAuth.getCurrentUser().getEmail())
                        .document("List")
                        .collection("List")
                        .document(user_id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        Toast.makeText(ProfileActivity2.this, "Already on friends list", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        final KProgressHUD progressDialog=  KProgressHUD.create(ProfileActivity2.this)
                                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                .setLabel("Please wait")
                                                .setCancellable(false)
                                                .setAnimationSpeed(2)
                                                .setDimAmount(0.5f)
                                                .show();
                                        firebaseFirestore.collection("All_UserID")
                                                .document(user_id)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if (task.getResult().exists()) {
                                                                String uuid=task.getResult().getString("uuid");
                                                                firebaseFirestore.collection("User2")
                                                                        .document(user_id)
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    if (task.getResult().exists()) {
                                                                                        try {

                                                                                            String image1=task.getResult().getString("image");
                                                                                            String name1=task.getResult().getString("name");
                                                                                            String email1=task.getResult().getString("number")+"@gmail.com";
                                                                                            String mobile1=task.getResult().getString("number");
                                                                                            String username1=task.getResult().getString("username");
                                                                                            String userid1=mAuth.getCurrentUser().getEmail();
                                                                                            long fg1 = System.currentTimeMillis()/1000;
                                                                                            FollerModelAndList follerModelAndList =new FollerModelAndList(name1, email1,
                                                                                                    image1, mobile1,username1,userid1,""+fg1);
                                                                                            firebaseFirestore.collection("FirendsList")
                                                                                                    .document("MyList")
                                                                                                    .collection(mAuth.getCurrentUser().getEmail())
                                                                                                    .document("List")
                                                                                                    .collection("List")
                                                                                                    .document(user_id)
                                                                                                    .set(follerModelAndList)
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                                        }
                                                                                                    });

                                                                                            FollerModelAndList follerModelAndList1 =new FollerModelAndList(name, email, photo, mobile,username,mAuth.getCurrentUser().getEmail()
                                                                                                    ,""+fg1);

                                                                                            firebaseFirestore.collection("FirendsList")
                                                                                                    .document("Requesteds")
                                                                                                    .collection(user_id)
                                                                                                    .document("List")
                                                                                                    .collection("List")
                                                                                                    .document(mAuth.getCurrentUser().getEmail())
                                                                                                    .set(follerModelAndList1)
                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                progressDialog.dismiss();
                                                                                                                Toast.makeText(ProfileActivity2.this, "Request gone to user wait for his/her response", Toast.LENGTH_SHORT).show();
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

                                }
                            }
                        });
            }
        });
        postCount=findViewById(R.id.postCount);
        followers_count=findViewById(R.id.followers_count);
        firebaseFirestore.collection("My_Post_Indi")
                .document(user_id)
                .collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int ncount1 = 0;

                        for (DocumentSnapshot document : task.getResult()) {
                            ncount1++;
                        }
                        postCount.setText(""+ncount1);

                    }
                });

        firebaseFirestore.collection("FirendsList")
                .document("MyList")
                .collection(user_id)
                .document("List")
                .collection("List")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int ncount11 = 0;

                        for (DocumentSnapshot document : task.getResult()) {
                            ncount11++;
                        }
                        followers_count.setText(""+ncount11);

                    }
                });
    }
TextView followers_count;

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
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    public void sendmessage(View view) {
        final KProgressHUD progressDialog=  KProgressHUD.create(ProfileActivity2.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        firebaseFirestore.collection("All_UserID")
                .document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if (task.isSuccessful()) {
                           if (task.getResult().exists()) {
                               String uuid=task.getResult().getString("uuid");
                               firebaseFirestore.collection("User2")
                                       .document(user_id)
                                       .get()
                                       .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   if (task.getResult().exists()) {
                                                      try {
                                                          progressDialog.dismiss();
                                                          String image=task.getResult().getString("image");
                                                          String name=task.getResult().getString("name");
                                                          Intent intent=new Intent(getApplicationContext(),Main_Chat.class);
                                                          intent.putExtra("visit_user_id",uuid);
                                                          intent.putExtra("visit_user_name",name);
                                                          intent.putExtra("visit_image",image);
                                                          startActivity(intent);
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
    Button follow_btn;
}