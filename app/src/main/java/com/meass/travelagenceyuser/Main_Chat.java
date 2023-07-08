package com.meass.travelagenceyuser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_Chat extends AppCompatActivity {
    private String messageRecieverId,getMessageRecievername,messagereceiverimage,messageSenderId,chatstatus;
    private TextView username,userlastseen;
    private CircleImageView userprofile;
    private Toolbar chattoolbar;
    private ImageButton sendMessageButton,sendFileButton;
    private EditText messagesentinput;
    private FirebaseAuth mauth;
    private DatabaseReference RootRef;
    private final List<Messages> messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView usermessagerecyclerview;


    private String savecurrentTime,savecurrentDate;
    private String checker="",myUrl="";
    private StorageTask uploadTask;
    private Uri fileuri;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__chat);
        firebaseFirestore=FirebaseFirestore.getInstance();

        loadingBar=new ProgressDialog(this);
        mauth=FirebaseAuth.getInstance();
        messageSenderId=mauth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();

        try {
            messageRecieverId=getIntent().getExtras().get("visit_user_id").toString();
            getMessageRecievername=getIntent().getExtras().get("visit_user_name").toString();
            messagereceiverimage=getIntent().getExtras().get("visit_image").toString();
            chatstatus="12: 36 PM";
        }catch (Exception e) {
            messageRecieverId=getIntent().getExtras().get("visit_user_id").toString();
            getMessageRecievername=getIntent().getExtras().get("visit_user_name").toString();
            messagereceiverimage=getIntent().getExtras().get("visit_image").toString();
            chatstatus=getIntent().getExtras().get("chatstatus").toString();
        }
       Toast.makeText(this, ""+messageSenderId+""+getMessageRecievername, Toast.LENGTH_SHORT).show();

        chattoolbar=findViewById(R.id.chat_toolbar);

        setSupportActionBar(chattoolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionbarview= layoutInflater.inflate(R.layout.custom_chat_bar,null);
        actionBar.setCustomView(actionbarview);
        firebaseFirestore= FirebaseFirestore.getInstance();

        username=findViewById(R.id.custom_profile_name);
        userlastseen=findViewById(R.id.custom_user_last_seen);
        userprofile=findViewById(R.id.custom_profile_image);
        sendMessageButton=findViewById(R.id.send_message_btn);
        sendFileButton=findViewById(R.id.send_files_btn);

        messagesentinput=findViewById(R.id.input_messages);

        messageAdapter=new MessageAdapter(messagesList);
        usermessagerecyclerview=findViewById(R.id.private_message_list_of_users);
        linearLayoutManager=new LinearLayoutManager(this);
        usermessagerecyclerview.setLayoutManager(linearLayoutManager);
        usermessagerecyclerview.setAdapter(messageAdapter);

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/yyyy");
        savecurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        savecurrentTime=currentTime.format(calendar.getTime());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());


        username.setText(getMessageRecievername);
        Picasso.get().load(messagereceiverimage).placeholder(R.drawable.profile_image).into(userprofile);

        userlastseen.setText("Online");
        //Displaylastseen();
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Users")
                        .document(messageRecieverId)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        try {
                                            Intent intent =new Intent(getApplicationContext(),ProfileActivity2.class);
                                            intent.putExtra("user_id",""+task.getResult().getString("number")+"@gmail.com");
                                            startActivity(intent);

                                        }catch (Exception e) {

                                        }
                                    }
                                }
                            }
                        });

            }
        });
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();
            }
        });

        sendFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[]=new CharSequence[]{
                        "Images","PDF Files","Ms Word Files"
                };

                AlertDialog.Builder builder=new AlertDialog.Builder(Main_Chat.this);
                builder.setTitle("Select File");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                            checker="image";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent,"Select Image"),555);

                        }else if(which==1)
                        {
                            checker="pdf";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent.createChooser(intent,"Select PDF File"),555);


                        }else if(which==2)
                        {
                            checker="docx";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
                            startActivityForResult(intent.createChooser(intent,"Select Ms Word File"),555);
                        }
                    }
                });

                builder.show();
            }
        });

        RootRef.child("Messages").child(messageSenderId).child(messageRecieverId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages=dataSnapshot.getValue(Messages.class);
                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();
                usermessagerecyclerview.smoothScrollToPosition(usermessagerecyclerview.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ///gift

    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    @Override
    public boolean onNavigateUp() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        return super.onNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==555 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            loadingBar.setTitle("Sending File");
            loadingBar.setMessage("please wait, we are sending that file...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            fileuri=data.getData();
            if(!checker.equals("image"))
            {
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Document Files");

                final String messageSenderRef="Messages/"+messageSenderId+"/"+messageRecieverId;
                final String messageReceiverRef="Messages/"+messageRecieverId+"/"+messageSenderId;

                DatabaseReference Usermessagekeyref=RootRef.child("Messages").child(messageSenderId).child(messageRecieverId).push();
                final String messagePushID=Usermessagekeyref.getKey();

                final StorageReference filepath=storageReference.child(messagePushID+"."+checker);

                filepath.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

                                Map messageDocsBody = new HashMap();
                                messageDocsBody.put("message",downloadUrl);
                                messageDocsBody.put("name",fileuri.getLastPathSegment());
                                messageDocsBody.put("type",checker);
                                messageDocsBody.put("from",messageSenderId);
                                messageDocsBody.put("to", messageRecieverId);
                                messageDocsBody.put("messageID", messagePushID);
                                messageDocsBody.put("time", savecurrentTime);
                                messageDocsBody.put("date", savecurrentDate);


                                Map messageBodyDDetail = new HashMap();
                                messageBodyDDetail.put(messageSenderRef + "/" + messagePushID, messageDocsBody);
                                messageBodyDDetail.put(messageReceiverRef + "/" + messagePushID, messageDocsBody);

                                RootRef.updateChildren(messageBodyDDetail);
                                loadingBar.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loadingBar.dismiss();
                                Toast.makeText(Main_Chat.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double p=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        loadingBar.setMessage((int) p+" % Uploading...");
                    }
                });
            }
            else if(checker.equals("image"))
            {
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image Files");

                final String messageSenderRef="Messages/"+messageSenderId+"/"+messageRecieverId;
                final String messageReceiverRef="Messages/"+messageRecieverId+"/"+messageSenderId;

                DatabaseReference Usermessagekeyref=RootRef.child("Messages").child(messageSenderId).child(messageRecieverId).push();
                final String messagePushID=Usermessagekeyref.getKey();

                final StorageReference filepath=storageReference.child(messagePushID+"."+"jpg");
                uploadTask =filepath.putFile(fileuri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Uri downloadUrl=task.getResult();
                            myUrl=downloadUrl.toString();

                            Map messageTextBody=new HashMap();
                            messageTextBody.put("message",myUrl);
                            messageTextBody.put("name",fileuri.getLastPathSegment());
                            messageTextBody.put("type",checker);
                            messageTextBody.put("from",messageSenderId);
                            messageTextBody.put("to",messageRecieverId);
                            messageTextBody.put("messageID",messagePushID);
                            messageTextBody.put("time",savecurrentTime);
                            messageTextBody.put("date",savecurrentDate);

                            Map messageBodyDetails =new HashMap();
                            messageBodyDetails.put(messageSenderRef+"/"+messagePushID,messageTextBody);
                            messageBodyDetails.put(messageReceiverRef+"/"+messagePushID,messageTextBody);

                            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful())
                                    {
                                        loadingBar.dismiss();
                                        //Toast.makeText(ChatActivity.this,"Message sent Successfully...",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(Main_Chat.this,"Error:",Toast.LENGTH_SHORT).show();
                                    }
                                    messagesentinput.setText("");
                                }
                            });
                        }
                    }
                });

            }
            else
            {
                loadingBar.dismiss();
                Toast.makeText(this,"please select file",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Displaylastseen()
    {
        RootRef.child("Users").child(messageRecieverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("userState").hasChild("state"))
                {
                    String state=dataSnapshot.child("userState").child("state").getValue().toString();
                    String date=dataSnapshot.child("userState").child("date").getValue().toString();
                    String time=dataSnapshot.child("userState").child("time").getValue().toString();

                    if(state.equals("online"))
                    {
                        userlastseen.setText("online");
                    }
                    else if(state.equals("offline"))
                    {
                        userlastseen.setText("Last seen: "+"\n"+date+" "+time);
                    }
                }
                else
                {
                    userlastseen.setText("offline");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    DatabaseReference databaseReference2;
    private void SendMessage() {
        firebaseDatabase2=FirebaseDatabase.getInstance();
        databaseReference2=firebaseDatabase2.getReference().child("user").child(mauth.getCurrentUser().getUid());

        String messagetext=messagesentinput.getText().toString();
        if(TextUtils.isEmpty(messagetext))
        {
            Toast.makeText(this,"Please enter message first..",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String messageSenderRef="Messages/"+messageSenderId+"/"+messageRecieverId;
            String messageReceiverRef="Messages/"+messageRecieverId+"/"+messageSenderId;


            DatabaseReference Usermessagekeyref=RootRef.child("Messages").child(messageSenderId).child(messageRecieverId).push();
            String messagePushID=Usermessagekeyref.getKey();
            Map messageTextBody=new HashMap();
            messageTextBody.put("message",messagetext);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderId);
            messageTextBody.put("to",messageRecieverId);
            messageTextBody.put("messageID",messagePushID);
            messageTextBody.put("time",savecurrentTime);
            messageTextBody.put("date",savecurrentDate);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(c.getTime());


            Map status=new HashMap();
            status.put("message",messagetext);
            status.put("name",""+getMessageRecievername);
            status.put("image",messagereceiverimage);
            status.put("status","Online");
            status.put("name",getMessageRecievername);
            status.put("image",messagereceiverimage);
            status.put("time",""+strDate);
            status.put("uuid",messageRecieverId);
            long lo=System.currentTimeMillis()/1000;
            LastMessageDate lastMessageDate=new LastMessageDate(messageRecieverId,messagetext,""+lo);

            FirebaseDatabase.getInstance().getReference().child("LastMessage").child(messageRecieverId)
                    .setValue(lastMessageDate)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
            firebaseFirestore.collection("LastMessage").document(messageRecieverId)
                    .set(lastMessageDate)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

            LastMessageDate lastMessageDatde=new LastMessageDate(mauth.getCurrentUser().getUid(),messagetext,""+lo);

            FirebaseDatabase.getInstance().getReference().child("LastMessage").child(mauth.getCurrentUser().getUid())
                    .setValue(lastMessageDatde)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
            firebaseFirestore.collection("LastMessage").document(mauth.getCurrentUser().getUid())
                    .set(lastMessageDatde)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
            ////










            FirebaseDatabase.getInstance().getReference().child("Contacts").child(mauth.getCurrentUser().getUid()).child("List")
                    .child(messageRecieverId)
                    .setValue(status)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
            firebaseFirestore.collection("Contacts").document(mauth.getCurrentUser().getUid())
                    .collection("List")
                    .document(messageRecieverId)
                    .set(status)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
            /////
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=database.getReference();
            databaseReference.child("user").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot = task.getResult();
                        if(snapshot.exists()){
                            String picture= String.valueOf(snapshot.child("picture").getValue());
                            String photomain = String.valueOf(snapshot.child("picturemain").getValue());
                            String name1 = String.valueOf(snapshot.child("name").getValue());
                            String age = String.valueOf(snapshot.child("age").getValue());
                            long ts=System.currentTimeMillis()/1000;
                            String timm=""+ts;
                            Map status1=new HashMap();
                            status1.put("message",messagetext);
                            status1.put("name",""+name1);
                            status1.put("image",picture);
                            status1.put("status","Online");
                            status1.put("name",name1);
                            status1.put("image",picture);
                            status1.put("time",""+timm);
                            status1.put("uuid",mauth.getCurrentUser().getUid());
                            Map status2=new HashMap();
                            status2.put("picture",picture);
                            status2.put("name",name1);
                            status2.put("photomain",photomain);
                            status2.put("age",age);

                            databaseReference2.setValue(status2)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                            FirebaseDatabase.getInstance().getReference().child("Contacts").child(messageRecieverId).child("List")
                                    .child(auth.getCurrentUser().getUid())
                                    .setValue(status1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                            firebaseFirestore.collection("Contacts")
                                    .document(messageRecieverId)
                                    .collection("List")
                                    .document(auth.getCurrentUser().getUid())
                                    .set(status1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });

                        }else {

                            firebaseFirestore.collection("User2")
                                    .document(mauth.getCurrentUser().getEmail())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult().exists()) {
                                                    String picture= task.getResult().getString("image");
                                                    String photomain =  task.getResult().getString("image");
                                                    String name1 =  task.getResult().getString("name");
                                                    String age =  task.getResult().getString("name");
                                                    Map status2=new HashMap();
                                                    status2.put("picture",picture);
                                                    status2.put("name",name1);
                                                    status2.put("photomain",photomain);
                                                    status2.put("age",age);
                                                    databaseReference2.setValue(status2)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                }
                                                            });
                                                    long ts=System.currentTimeMillis()/1000;
                                                    String timm=""+ts;


                                                    Map status1=new HashMap();
                                                    status1.put("message",messagetext);
                                                    status1.put("name",""+name1);
                                                    status1.put("image",picture);
                                                    status1.put("status","Online");
                                                    status1.put("name",name1);
                                                    status1.put("image",picture);
                                                    status1.put("time",""+ts);
                                                    status1.put("uuid",mauth.getCurrentUser().getUid());
                                                    FirebaseDatabase.getInstance().getReference().child("Contacts").child(messageRecieverId).child("List")
                                                            .child(auth.getCurrentUser().getUid())
                                                            .setValue(status1)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                }
                                                            });
                                                    firebaseFirestore.collection("Contacts")
                                                            .document(messageRecieverId)
                                                            .collection("List")
                                                            .document(auth.getCurrentUser().getUid())
                                                            .set(status1)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                    else {
                        firebaseFirestore.collection("User2")
                                .document(mauth.getCurrentUser().getEmail())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                                String picture= task.getResult().getString("image");
                                                String photomain =  task.getResult().getString("image");
                                                String name1 =  task.getResult().getString("name");
                                                String age =  task.getResult().getString("name");
                                                Map status2=new HashMap();
                                                status2.put("picture",picture);
                                                status2.put("name",name1);
                                                status2.put("photomain",photomain);
                                                status2.put("age",age);
                                                databaseReference2.setValue(status2)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });
                                                long ts=System.currentTimeMillis()/1000;
                                                String timm=""+ts;
                                                Map status1=new HashMap();
                                                status1.put("message",messagetext);
                                                status1.put("name",""+name1);
                                                status1.put("image",picture);
                                                status1.put("status","Online");
                                                status1.put("name",name1);
                                                status1.put("image",picture);
                                                status1.put("time",""+ts);
                                                status1.put("uuid",mauth.getCurrentUser().getUid());
                                                FirebaseDatabase.getInstance().getReference().child("Contacts").child(messageRecieverId).child("List")
                                                        .child(auth.getCurrentUser().getUid())
                                                        .setValue(status1)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });
                                                firebaseFirestore.collection("Contacts")
                                                        .document(messageRecieverId)
                                                        .collection("List")
                                                        .document(auth.getCurrentUser().getUid())
                                                        .set(status1)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                    }
                }
            });



            Map messageBodyDetails =new HashMap();
            messageBodyDetails.put(messageSenderRef+"/"+messagePushID,messageTextBody);
            messageBodyDetails.put(messageReceiverRef+"/"+messagePushID,messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        // Toast.makeText(ChatActivity.this,"Message sent Successfully...",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Main_Chat.this,"Error:",Toast.LENGTH_SHORT).show();
                    }
                    messagesentinput.setText("");
                }
            });
        }
    }
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase2;


}
