package test.project.firestore_sample;

import androidx.appcompat.app.AppCompatActivity;
import test.project.firestore_sample.models.FireStoreOrder;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class MainActivity extends AppCompatActivity {

    private final String COLLECTION_ORDERS = "orders";
    private final String DOCUMENT_ORDER_ID = "test_order";
    private TextView fireStoreQueryTriggerCount;
    private ListenerRegistration orderRealtimeListener;
    private FireStoreOrder mOrder;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fireStoreQueryTriggerCount = findViewById(R.id.counter);
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
}