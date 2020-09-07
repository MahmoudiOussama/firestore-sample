package test.project.firestore_sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import test.project.firestore_sample.controls.Constants;
import test.project.firestore_sample.models.FireStoreOrder;
import test.project.firestore_sample.models.UserProfile;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class MainActivity extends AppCompatActivity {

    public static UserProfile currentUserProfile;
    private final String COLLECTION_ORDERS = "orders";
    private final String DOCUMENT_ORDER_ID = "test_order";
    private TextView fireStoreQueryTriggerCount;
    private ListenerRegistration orderRealtimeListener;
    private FireStoreOrder mOrder;
    private int count = 0;
    public ProgressBar progressBar;
    private TextView messageForTheUser;
    private Runnable mRunnable;
    private Handler mHandler;
    public static long timeDifferenceFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        messageForTheUser = findViewById(R.id.message_for_user);
        fireStoreQueryTriggerCount = findViewById(R.id.counter);

        mHandler = new Handler();
    }

    @Override
    public void onResume() {
        super.onResume();
        count = 0;
        fireStoreQueryTriggerCount.setText(String.valueOf(count));
        getOrderFromDatabase();
    }

    private void getOrderFromDatabase() {
        try {
            if (orderRealtimeListener != null) {
                try {
                    orderRealtimeListener.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            orderRealtimeListener = FirebaseFirestore.getInstance().collection(COLLECTION_ORDERS)
                    .document(DOCUMENT_ORDER_ID)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException exception) {
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                count++;
                                fireStoreQueryTriggerCount.setText(String.valueOf(count));
                                mOrder = documentSnapshot.toObject(FireStoreOrder.class);
                                //i will do nothing with this order, just extracted data.
                            }
                        }
                    });
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    public void onPause() {
        try {
            if (orderRealtimeListener != null) {
                try {
                    orderRealtimeListener.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ignored) {}
        super.onPause();
    }

    //method used to display message to the user
    public void displayMessage(int stringId, int backgroundColorId, int displayTime) {
        //try to cancel the previous triggered handler
        try {
            mHandler.removeCallbacks(mRunnable);
        } catch (Exception ignored) {}

        //set text tu display
        messageForTheUser.setText(getString(stringId));
        //set the background color
        messageForTheUser.setBackgroundResource(backgroundColorId);
        //display the message
        messageForTheUser.setVisibility(View.VISIBLE);

        if (displayTime != 0) {
            //hide the message after wanted time (in milliseconds)
            mRunnable = () -> messageForTheUser.setVisibility(View.GONE);
            mHandler.postDelayed(mRunnable, displayTime);
        }
        //else, the message is meant to stick there until manually hidden
    }

    //method used to display message to the user
    public void displayMessage(String message, int backgroundColorId, int displayTime) {
        //try to cancel the previous triggered handler
        try {
            mHandler.removeCallbacks(mRunnable);
        } catch (Exception ignored) {}
        //set text tu display
        messageForTheUser.setText(message);
        //set the background color
        messageForTheUser.setBackgroundResource(backgroundColorId);
        //display the message
        messageForTheUser.setVisibility(View.VISIBLE);

        if (displayTime != 0) {
            //hide the message after wanted time (in milliseconds)
            mRunnable = () -> messageForTheUser.setVisibility(View.GONE);
            mHandler.postDelayed(mRunnable, displayTime);
        }
        //else, the message is meant to stick there until manually hided
    }

    public void hideMessage() {
        messageForTheUser.setVisibility(View.GONE);
    }

    //method used to get current time from Firebase.
    //calculate and save the difference of time between Firebase(GMT) and current user device.
    protected void calculateTimeDifference() {
        //we need to get current time from database
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.TIMESTAMP)
                .setValue(ServerValue.TIMESTAMP)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Constants.dbRef()
                                .child(Constants.TIMESTAMP)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            Long serverTime = dataSnapshot.getValue(Long.class);
                                            if (serverTime != null) {
                                                timeDifferenceFromServer = System.currentTimeMillis() - serverTime;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                });
    }
}