package com.meass.travelagenceyuser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar mainToolbar;
    private String current_user_id;
    private BottomNavigationView mainBottomNav;
    private DrawerLayout mainDrawer;
    private ActionBarDrawerToggle mainToggle;
    private NavigationView mainNav;

    FrameLayout frameLayout;
    private TextView drawerName,appname;
    private CircleImageView drawerImage;
    FirebaseAuth firebaseAuth;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestoreSettings settings;
    private DatabaseReference mUserRef;




    KProgressHUD kProgressHUD;
    Long tsLong = System.currentTimeMillis()/1000;
    String ts = tsLong.toString();

    private UserSession session;
    private HashMap<String, String> user;
    private String name, email, photo, mobile,username;
    private Drawer result;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    FirebaseFirestore db;

    FirebaseUser firebaseUser;
    String user1;

    IProfile profile;
    TextView nameTv,emailTv;
    CircleImageView profileImage;
    TextView coinsT1v;
    CardView dailyCheckCard,luckySpinCard,aboutCard1,aboutCard,redeemCard,referCard,taskCard;

    String sessionname,sessionmobile,sessionphoto,sessionemail,sessionusername;
    int count,count1,count2,count3;
    String package_actove;
    String daily_bonus;
    String incomeType="Daily Task";
    int main_account;
    int count12,count123;
    int main_refer;
    String main_task ;



    private TextView tvemail,tvphone;
    private HashMap<String, String> uaser;
    FloatingActionButton dialogClose;

    ; Dialog mDialog;
    @RequiresApi(api = Build.VERSION_CODES.N)
    TextView myamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("dfdfdf");
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(10.0f);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        session = new UserSession(getApplicationContext());
        myamount=findViewById(R.id.myamount);
        getValues();
        mainDrawer=findViewById(R.id.main_activity);
        mainNav = findViewById(R.id.main_nav);
        mainNav.setNavigationItemSelectedListener(this);

        mainToggle = new ActionBarDrawerToggle(this,mainDrawer,toolbar,R.string.open,R.string.close);
        mainDrawer.addDrawerListener(mainToggle);
        mainDrawer.addDrawerListener(mainToggle);
        mainToggle.setDrawerIndicatorEnabled(true);
        mainToggle.syncState();
        //////textview count
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        ////fragment show
        RequestManager glide = Glide.with(HomeActivity.this);

        blog_list = new ArrayList<>();
        blogListView = findViewById(R.id.blog_list_view);
        blogRecyclerAdapter = new BlogRecyclerAdapter(HomeActivity.this,blog_list, glide);

        blogListView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

        blogListView.setAdapter(blogRecyclerAdapter);

        //whiteNotificationBar(blogListView);




        mAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        blogListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                if (reachedBottom){
                    loadMorePosts();
                }

            }
        });

        Query firstQuery = firebaseFirestore.collection("Posts")
                .orderBy("timestamp",Query.Direction.DESCENDING).limit(4);

        firstQuery.addSnapshotListener(HomeActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }

                if (!documentSnapshots.isEmpty()){


                    if (isFirstPageFirstLoad){
                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size()-1);
                    }

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED){

                            String blogPostId = doc.getDocument().getId();

                            BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                            if (isFirstPageFirstLoad){
                                blog_list.add(blogPost);
                                blogRecyclerAdapter.notifyItemInserted(blog_list.size());
                            } else {
                                blog_list.add(0,blogPost);
                                blogRecyclerAdapter.notifyItemInserted(0);
                            }

                        }

                    }

                    isFirstPageFirstLoad = false;
                }

            }
        });

    }
    public void loadMorePosts(){

        Query nextQuery = firebaseFirestore.collection("Posts")
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(5);

        nextQuery.addSnapshotListener(HomeActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }

                if (!documentSnapshots.isEmpty()){
                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size()-1);
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED){

                            String blogPostId = doc.getDocument().getId();
                            BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                            blog_list.add(blogPost);

                            blogRecyclerAdapter.notifyItemInserted(blog_list.size());

                        }

                    }
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            super.onCreateOptionsMenu(menu);

            getMenuInflater().inflate(R.menu.home_menu,menu);

        SearchManager searchManager = (SearchManager) HomeActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(HomeActivity.this.getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                Log.i("SearchInfo",query+"");
                blogRecyclerAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                blogRecyclerAdapter.getFilter().filter(query);
                return false;
            }
        });

        return true;

    }

    public static final String TAG = HomeFragment.class.getSimpleName();

    private RecyclerView blogListView;
    private List<BlogPost> blog_list;
    private BlogRecyclerAdapter blogRecyclerAdapter;

    private FloatingActionButton openFabBtn;

    private boolean isOpen = false;




    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;
    private HomeFragment_blog homeFragment;
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
            DateFormat df = new SimpleDateFormat("h:mm:ss a");
            DateFormat df1 = new SimpleDateFormat("EEE, MMM d");
            String date = df.format(Calendar.getInstance().getTime());
            String date2 = df1.format(Calendar.getInstance().getTime());
            myamount.setText(name+"\n" +
                    ""+date2+" | "+date);

        }catch (Exception e) {
            //get User details if logged in
            session.isLoggedIn();
            user = session.getUserDetails();

            name = user.get(UserSession.KEY_NAME);
            email = user.get(UserSession.KEY_EMAIL);
            mobile = user.get(UserSession.KEY_MOBiLE);
            photo = user.get(UserSession.KEY_PHOTO);
            username=user.get(UserSession.Username);
            DateFormat df = new SimpleDateFormat("h:mm a");
            DateFormat df1 = new SimpleDateFormat("EEE, MMM d");
            String date = df.format(Calendar.getInstance().getTime());
            String date2 = df1.format(Calendar.getInstance().getTime());
            myamount.setText(name+"\n" +
                    ""+date2+" | "+date);
        }
        //Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        final FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(HomeActivity.this)
                .setBackgroundColor(R.color.white)
                .setTextTitle("Exit")
                .setCancelable(false)
                .setTextSubTitle("Are you want to exit")
                .setBody("User is not stay at app when user click exit button.")
                .setPositiveButtonText("No")
                .setPositiveColor(R.color.toolbar)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();

                    }
                })
                .setNegativeButtonText("Exit")
                .setNegativeColor(R.color.colorPrimaryDark)
                .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        finishAffinity();

                    }
                })
                .setBodyGravity(FancyAlertDialog.TextGravity.CENTER)
                .setTitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setSubtitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setCancelable(false)
                .build();
        alert.show();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        //
        if (id==R.id.orderjos) {
            startActivity(new Intent(getApplicationContext(),OrderList.class));
        }
        if (id==R.id.requested) {
            startActivity(new Intent(getApplicationContext(),RequestListActivity.class));
        }
        //
        if (id==R.id.followlist) {
            startActivity(new Intent(getApplicationContext(),FanListActivity.class));
        }
        //
        if (id==R.id.cartliost) {
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }
        if (id==R.id.mypost) {
            AlertDialog.Builder warning = new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you want to logout?")
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();



                        }
                    }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // ToDO: delete all the notes created by the Anon user


                            firebaseAuth.signOut();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();


                        }
                    });

            warning.show();
        }
        if (id==R.id.bottom_home) {
            Toasty.success(getApplicationContext(),"You are home now",Toasty.LENGTH_SHORT,true).show();

        }
        if (id==R.id.navigation_chat) {
            startActivity(new Intent(getApplicationContext(),ChatMainSection.class));
        }
        if (id==R.id.addpost) {
            firebaseFirestore.collection("DDD")
                    .document(firebaseAuth.getCurrentUser().getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    try {
                                        String name=task.getResult().getString("name");
                                        if (name.equals("a")) {
                                            String post[]={"Add Post","View Post"};
                                            AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
                                            builder.setTitle("Post Panel")
                                                    .setItems(post, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            if (which==0) {
                                                                startActivity(new Intent(getApplicationContext(),NewPostActivity.class));
                                                            }
                                                            else {
                                                                startActivity(new Intent(getApplicationContext(),My_Post.class));
                                                            }

                                                        }
                                                    }).create();
                                            builder.show();
                                        }
                                        else {
                                            Toasty.info(getApplicationContext(),"You are not traveller.",Toasty.LENGTH_SHORT,true).show();
                                            return;
                                        }
                                    }catch (Exception e) {

                                    }
                                }
                            }
                        }
                    });


        }

        return false;
    }
}