package com.meass.travelagenceyuser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class My_Post extends AppCompatActivity {
    LottieAnimationView empty_cart;
    DocumentReference documentReference;
    RecyclerView recyclerView;
    BlogRecyclerAdapter__11 getDataAdapter1;
    List<BlogPost> getList;
    String url;

    FirebaseUser firebaseUser;
    KProgressHUD progressHUD;
    String cus_name;
    SearchView name;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__post);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        toolbar.setTitle("All Products");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        ////
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


      /*
        getList = new ArrayList<>();
        RequestManager glide = Glide.with(My_Post.this);
        getDataAdapter1 = new BlogRecyclerAdapter__11(My_Post.this,getList,glide);
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference =   firebaseFirestore.collection("My_Post_Indi")
                .document(firebaseAuth.getCurrentUser().getEmail())
        .collection("posts").document();
        recyclerView = findViewById(R.id.rreeeed);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(My_Post.this));
        recyclerView.setAdapter(getDataAdapter1);
        reciveData();
       */



        //////textview count
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        ////fragment show
        RequestManager glide = Glide.with(My_Post.this);

        blog_list = new ArrayList<>();
        blogListView = findViewById(R.id.rreeeed);
        blogRecyclerAdapter = new BlogRecyclerAdapter__11(My_Post.this,blog_list, glide);

        blogListView.setLayoutManager(new LinearLayoutManager(My_Post.this));

        blogListView.setAdapter(blogRecyclerAdapter);

        //whiteNotificationBar(blogListView);




        firebaseAuth = FirebaseAuth.getInstance();

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

        Query firstQuery = firebaseFirestore.collection("My_Post_Indi")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("posts").limit(4);

        firstQuery.addSnapshotListener(My_Post.this,new EventListener<QuerySnapshot>() {
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
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }
    public void loadMorePosts(){

        Query nextQuery = firebaseFirestore.collection("My_Post_Indi")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("posts")
                .startAfter(lastVisible)
                .limit(5);

        nextQuery.addSnapshotListener(My_Post.this,new EventListener<QuerySnapshot>() {
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

    public static final String TAG = HomeFragment.class.getSimpleName();

    private RecyclerView blogListView;
    private List<BlogPost> blog_list;
    private BlogRecyclerAdapter__11 blogRecyclerAdapter;

    private FloatingActionButton openFabBtn;

    private boolean isOpen = false;




    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;
    private HomeFragment_blog homeFragment;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void reciveData() {

        firebaseFirestore.collection("My_Post_Indi")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange ds : queryDocumentSnapshots.getDocumentChanges()) {
                    if (ds.getType() == DocumentChange.Type.ADDED) {

                 /*String first;
                 first = ds.getDocument().getString("name");
                 Toast.makeText(MainActivity2.this, "" + first, Toast.LENGTH_SHORT).show();*/
                        BlogPost get = ds.getDocument().toObject(BlogPost.class);
                        getList.add(get);
                        getDataAdapter1.notifyDataSetChanged();
                    }

                }
            }
        });

    }
}