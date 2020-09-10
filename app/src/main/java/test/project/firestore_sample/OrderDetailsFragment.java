package test.project.firestore_sample;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Callable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import test.project.firestore_sample.adapters.CartAdapter;
import test.project.firestore_sample.controls.Constants;
import test.project.firestore_sample.controls.OnSingleClickListener;
import test.project.firestore_sample.controls.Utils;
import test.project.firestore_sample.models.Coordinate;
import test.project.firestore_sample.models.CustomMapView;
import test.project.firestore_sample.models.DeliveryAgent;
import test.project.firestore_sample.models.FireStoreOrder;
import test.project.firestore_sample.models.GeoFireObject;
import test.project.firestore_sample.models.OrderItem;
import test.project.firestore_sample.models.WrapContentLinearLayoutManager;

/* Created by ioBirdOussama on 28/01/2018. */

public class OrderDetailsFragment extends Fragment
        implements OnMapReadyCallback{

    private static final String KEY_STORE_ID     = "storeId";
    private static final String KEY_ORDER_ID     = "orderId";
    private static final String KEY_STORE_NAME   = "store name";
    private static final String KEY_ORDER_NUMBER = "order_number";

    private View mView;
    private String storeId, orderId, storeName;
    private ValueEventListener courierPositionListener;
    private ProgressBar progressBar;
    private ScrollView trackingLayout;
    private boolean isStatusRefreshedOnce, firstTimeOrderListener;
    private FireStoreOrder mOrder;
    private CustomMapView mMapView;
    private GoogleMap googleMap;
    private Coordinate clientPosition;
    private Bundle savedState;
    private Marker clientMarker, storeMarker, agentMarker;
    private TextView buttonContactSupport, buttonCancelOrder, buttonTrackOrder;
    private int orderNumber, deliveryTime = 90;
    private ListenerRegistration orderRealtimeListener;
    private int count = 0;

    public static OrderDetailsFragment newInstance(String storeId, String orderId, String storeName, int orderNumber) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_STORE_ID, storeId);
        args.putString(KEY_ORDER_ID, orderId);
        args.putString(KEY_STORE_NAME, storeName);
        args.putInt(KEY_ORDER_NUMBER, orderNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.order_details_layout, container, false);

        if (getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
            if (getActivity().getIntent().getExtras().containsKey(Constants.ORDER_ID)) {
                getActivity().getIntent().removeExtra(Constants.ORDER_ID);
            }
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        savedState = savedInstanceState;
        buttonCancelOrder = view.findViewById(R.id.button_cancel_order);
        buttonContactSupport = view.findViewById(R.id.button_support);
        buttonTrackOrder = view.findViewById(R.id.button_track_order);

        if (getArguments() != null && !getArguments().isEmpty()) {
            storeId = (storeId == null) ? getArguments().getString(KEY_STORE_ID) : storeId;
            orderId = (orderId == null) ? getArguments().getString(KEY_ORDER_ID) : orderId;
            storeName = (storeName == null) ? getArguments().getString(KEY_STORE_NAME) : storeName;
            orderNumber = getArguments().getInt(KEY_ORDER_NUMBER);
        }

        if (getActivity() != null && getActivity() instanceof MainActivity) {
            getActivity().setTitle(getString(R.string.order_number, orderNumber));
            if (progressBar == null) {
                progressBar = ((MainActivity) getActivity()).progressBar;
            }
            progressBar.setVisibility(View.VISIBLE);
        }

        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrder != null && (!mOrder.getStatus().equals(Constants.RECEIVED))) {
                    if (getActivity() != null) {
                        ((MainActivity) getActivity()).displayMessage(R.string.cannot_cancel_order, android.R.color.holo_red_light, Constants.TIME_SIX_SECONDS);
                    }
                } else {
                    Utils.createAlertDialog(getActivity(), getString(R.string.your_order_will_be_canceled), getString(R.string.i_am_sure), getString(R.string.no_thanks), R.layout.warning_dialog_layout, new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            Constants.getFireStoreInstance().runTransaction(new com.google.firebase.firestore.Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull com.google.firebase.firestore.Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentReference ref = Constants.getFireStoreInstance().collection(Constants.COLLECTION_ORDERS).document(orderId);
                                    DocumentSnapshot snapshot = transaction.get(ref);
                                    FireStoreOrder order = snapshot.toObject(FireStoreOrder.class);
                                    if (order != null && order.getStatus() != null && (order.getStatus().equals(Constants.RECEIVED))) {
                                        transaction.update(ref, Constants.STATUS, Constants.CANCELED);
                                    }
                                    return null;
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isCanceled()) {
                                        ((MainActivity) getActivity()).displayMessage(R.string.cannot_cancel_order, android.R.color.holo_red_light, Constants.TIME_SIX_SECONDS);
                                    }
                                }
                            });
                            return null;
                        }
                    }, new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            return null;
                        }
                    });
                }
            }
        });

        buttonContactSupport.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick() {
                //buttonContactSupport.setEnabled(false);
                //getOrCreateDiscussion();
            }
        });

        buttonTrackOrder.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick() {
                try {
                    if (trackingLayout.getVisibility() == View.VISIBLE) {
                        trackingLayout.setVisibility(View.GONE);
                        mView.findViewById(R.id.products_layout).setVisibility(View.VISIBLE);
                        buttonTrackOrder.setCompoundDrawablesWithIntrinsicBounds(Constants.ZERO, R.drawable.track_order, Constants.ZERO, Constants.ZERO);
                        buttonTrackOrder.setText(getString(R.string.track_order));
                    } else {
                        trackingLayout.setVisibility(View.VISIBLE);
                        mView.findViewById(R.id.products_layout).setVisibility(View.GONE);
                        buttonTrackOrder.setCompoundDrawablesWithIntrinsicBounds(Constants.ZERO, R.drawable.order_details, Constants.ZERO, Constants.ZERO);
                        buttonTrackOrder.setText(getString(R.string.order_details));
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(storeId) && !TextUtils.isEmpty(orderId)) {
            getOrderFromDatabase();
        } else {
            progressBar.setVisibility(View.GONE);
        }
        if (mMapView != null) {
            try {
                mMapView.onResume();
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }
    }

    @Override
    public void onPause() {
        try {
            FirebaseDatabase.getInstance().getReference()
                    .child(Constants.DELIVERY_AGENTS_LOCATIONS)
                    .child(mOrder.getDeliveryAgentId())
                    .child(Constants.COORDINATES)
                    .removeEventListener(courierPositionListener);
            courierPositionListener = null;
        } catch (Exception ignored) {}

        if (mMapView != null) {
            try {
                mMapView.onPause();
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }

        try {
            ((MainActivity) Objects.requireNonNull(getActivity())).hideMessage();
        } catch (Exception ignored) {}

        try {
            orderRealtimeListener.remove();
        } catch (Exception ignored) {}
        orderRealtimeListener = null;
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            try {
                mMapView.onLowMemory();
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void onDestroyView() {
        try {
            FirebaseDatabase.getInstance().getReference()
                    .child(Constants.DELIVERY_AGENTS_LOCATIONS)
                    .child(mOrder.getDeliveryAgentId())
                    .child(Constants.COORDINATES)
                    .removeEventListener(courierPositionListener);
            courierPositionListener = null;
        } catch (Exception ignored) {}

        if (mMapView != null) {
            try { googleMap.clear(); } catch (Exception ignored) {}
            try { mMapView.onDestroy(); } catch (Exception ignored) {}
        }

        try {
            ((MainActivity) Objects.requireNonNull(getActivity())).hideMessage();
        } catch (Exception ignored) {}

        try {
            orderRealtimeListener.remove();
        } catch (Exception ignored) {}
        orderRealtimeListener = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (googleMap != null) {
            try { googleMap.clear(); } catch (Exception ignored) {}
        }
        if (mMapView != null) {
            try { mMapView.onDestroy(); } catch (Exception ignored) {}
        }
        try {
            ((MainActivity) Objects.requireNonNull(getActivity())).hideMessage();
        } catch (Exception ignored) {}

        try {
            orderRealtimeListener.remove();
        } catch (Exception ignored) {}
        orderRealtimeListener = null;
        super.onDestroy();
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
            count = 0;
            firstTimeOrderListener = true;
            orderRealtimeListener = Constants.getFireStoreInstance()
                    .collection(Constants.COLLECTION_ORDERS)
                    .document(orderId)
                    .addSnapshotListener(MetadataChanges.EXCLUDE, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                            @Nullable FirebaseFirestoreException e) {
                            try {
                                if (progressBar.getVisibility() == View.VISIBLE) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            } catch (Exception ex) {}

                            if (e != null) {
                                Log.w("testing", "Listen failed.", e);
                                return;
                            }
                            count++;
                            Log.d("testing", "Firestore onEvent triggered : "+count);
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                String previousStatus = (mOrder != null && mOrder.getStatus() != null) ? mOrder.getStatus() : null;
                                mOrder = documentSnapshot.toObject(FireStoreOrder.class);
                                if (mOrder != null && mOrder.getStatus() != null) {
                                    try {
                                        if (firstTimeOrderListener) {
                                            if (!TextUtils.isEmpty(mOrder.getOrderType())) {
                                                createTrackingView(mOrder);
                                            }
                                            //Calculate item price for each product
                                            for (OrderItem orderItem : mOrder.extractMealsList()) {
                                                orderItem.calculateItemPrice();
                                            }
                                            //Initialize listView instance.
                                            RecyclerView recyclerView = mView.findViewById(R.id.products_recyclerView);
                                            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                                    LinearLayoutManager.VERTICAL);
                                            recyclerView.addItemDecoration(dividerItemDecoration);
                                            //Create adapter for displaying products as a list.
                                            CartAdapter adapter = new CartAdapter(getContext(), mOrder.extractMealsList(), Constants.CHECK);
                                            //assign adapter to previously created recyclerView.
                                            recyclerView.setAdapter(adapter);

                                            displayOrderPriceDetails(mOrder);
                                        }

                                        if (firstTimeOrderListener || !mOrder.getStatus().equals(previousStatus)) {
                                            //String newStatus = dataSnapshot.getValue().toString();
                                            switch (mOrder.getStatus()) {
                                                case Constants.RECEIVED: {
                                                    setCancelAndTimerVisibility(true, false);
                                                    if (isStatusRefreshedOnce) {
                                                        updateStatusTextView(R.id.state_1, R.string.order_accepted, R.drawable.state_off);
                                                        updateStatusTextView(R.id.state_2, R.string.order_preparing, R.drawable.state_off);
                                                        updateStatusTextView(R.id.state_3, R.string.order_prepared, R.drawable.state_off);
                                                        updateStatusView(R.id.order_delivery_text, R.string.order_on_the_way, R.id.order_delivery_img, R.drawable.state_off);
                                                        updateStatusView(R.id.order_delivered_text, R.string.order_arrived, R.id.order_delivered_img, R.drawable.state_off);
                                                        //hide map view
                                                        if (mView.findViewById(R.id.delivery_map_layout).getVisibility() == View.VISIBLE) {
                                                            mView.findViewById(R.id.delivery_map_layout).setVisibility(View.GONE);
                                                        }
                                                    }
                                                    break;
                                                }
                                                case Constants.CANCELED: {
                                                    if (mOrder != null && mOrder.getCanceledAt() == null) {
                                                        mOrder.setCanceledAt(new Date(System.currentTimeMillis() - MainActivity.timeDifferenceFromServer));
                                                    }
                                                    setCancelAndTimerVisibility(false, false);
                                                    updateStatusTextView(R.id.state_1, R.string.order_rejected, R.drawable.state_cancel);
                                                    updateStatusTextView(R.id.state_2, R.string.order_preparing, R.drawable.state_off);
                                                    updateStatusTextView(R.id.state_3, R.string.order_prepared, R.drawable.state_off);
                                                    updateStatusView(R.id.order_delivery_text, R.string.order_on_the_way, R.id.order_delivery_img, R.drawable.state_off);
                                                    updateStatusView(R.id.order_delivered_text, R.string.order_arrived, R.id.order_delivered_img, R.drawable.state_off);
                                                    //hide map view
                                                    if (mView.findViewById(R.id.delivery_map_layout).getVisibility() == View.VISIBLE) {
                                                        mView.findViewById(R.id.delivery_map_layout).setVisibility(View.GONE);
                                                    }
                                                    break;
                                                }
                                                case Constants.IN_PROGRESS: {
                                                    if (mOrder != null && mOrder.getInProgressAt() == null) {
                                                        mOrder.setInProgressAt(new Date(System.currentTimeMillis() - MainActivity.timeDifferenceFromServer));
                                                    }
                                                    setCancelAndTimerVisibility(false, true);
                                                    updateStatusTextView(R.id.state_1, R.string.order_accepted, R.drawable.state_ok);
                                                    updateStatusTextView(R.id.state_2, R.string.order_preparing, R.drawable.state_pending);
                                                    updateStatusTextView(R.id.state_3, R.string.order_prepared, R.drawable.state_off);
                                                    updateStatusView(R.id.order_delivery_text, R.string.order_on_the_way, R.id.order_delivery_img, R.drawable.state_off);
                                                    updateStatusView(R.id.order_delivered_text, R.string.order_arrived, R.id.order_delivered_img, R.drawable.state_off);
                                                    //hide map view
                                                    if (mView.findViewById(R.id.delivery_map_layout).getVisibility() == View.VISIBLE) {
                                                        mView.findViewById(R.id.delivery_map_layout).setVisibility(View.GONE);
                                                    }
                                                    break;
                                                }
                                                case Constants.VALIDATED: {
                                                    if (mOrder != null && mOrder.getValidatedAt() == null) {
                                                        mOrder.setValidatedAt(new Date(System.currentTimeMillis() - MainActivity.timeDifferenceFromServer));
                                                    }
                                                    setCancelAndTimerVisibility(false, true);
                                                    updateStatusTextView(R.id.state_1, R.string.order_accepted, R.drawable.state_ok);
                                                    updateStatusTextView(R.id.state_2, R.string.order_preparing, R.drawable.state_ok);
                                                    updateStatusTextView(R.id.state_3, R.string.order_prepared, mOrder.getOrderType().equals(Constants.DELIVERY) ? R.drawable.state_pending : R.drawable.state_ok);
                                                    updateStatusView(R.id.order_delivery_text, R.string.order_on_the_way, R.id.order_delivery_img, R.drawable.state_off);
                                                    updateStatusView(R.id.order_delivered_text, R.string.order_arrived, R.id.order_delivered_img, R.drawable.state_off);
                                                    //hide map view
                                                    if (mView.findViewById(R.id.delivery_map_layout).getVisibility() == View.VISIBLE) {
                                                        mView.findViewById(R.id.delivery_map_layout).setVisibility(View.GONE);
                                                    }
                                                    break;
                                                }
                                                case Constants.PICKED: {
                                                    if (mOrder != null && mOrder.getPickedAt() == null) {
                                                        mOrder.setPickedAt(new Date(System.currentTimeMillis() - MainActivity.timeDifferenceFromServer));
                                                    }
                                                    setCancelAndTimerVisibility(false, false);

                                                    initializeMapView(savedState);

                                                    //show map view
                                                    mView.findViewById(R.id.delivery_map_layout).setVisibility(View.VISIBLE);

                                                    updateStatusTextView(R.id.state_1, R.string.order_accepted, R.drawable.state_ok);
                                                    updateStatusTextView(R.id.state_2, R.string.order_preparing, R.drawable.state_ok);
                                                    updateStatusTextView(R.id.state_3, R.string.order_prepared, R.drawable.state_ok);
                                                    updateStatusView(R.id.order_delivery_text, R.string.order_on_the_way, R.id.order_delivery_img, R.drawable.state_pending);
                                                    updateStatusView(R.id.order_delivered_text, R.string.order_arrived, R.id.order_delivered_img, R.drawable.state_off);

                                                    //get the mini map into the screen
                                                    trackingLayout.post(() -> trackingLayout.smoothScrollTo(Constants.ZERO, mView.findViewById(R.id.state_4).getBottom()));

                                                    //restart listeners if fragment resumed
                                                    if (courierPositionListener != null && !TextUtils.isEmpty(mOrder.getDeliveryAgentId())) {
                                                        listenToCourierPositionUpdates();
                                                    }
                                                    break;
                                                }
                                                case Constants.DELIVERED: {
                                                    if (mOrder != null && mOrder.getDeliveredAt() == null) {
                                                        mOrder.setDeliveredAt(new Date(System.currentTimeMillis() - MainActivity.timeDifferenceFromServer));
                                                    }
                                                    setCancelAndTimerVisibility(false, false);
                                                    updateStatusTextView(R.id.state_1, R.string.order_accepted, R.drawable.state_ok);
                                                    updateStatusTextView(R.id.state_2, R.string.order_preparing, R.drawable.state_ok);
                                                    updateStatusTextView(R.id.state_3, R.string.order_prepared, R.drawable.state_ok);
                                                    updateStatusView(R.id.order_delivery_text, R.string.order_on_the_way, R.id.order_delivery_img, R.drawable.state_ok);
                                                    updateStatusView(R.id.order_delivered_text, R.string.order_arrived, R.id.order_delivered_img, R.drawable.state_ok);

                                                    //hide map view
                                                    if (mView.findViewById(R.id.delivery_map_layout).getVisibility() == View.VISIBLE) {
                                                        mView.findViewById(R.id.delivery_map_layout).setVisibility(View.GONE);
                                                    }
                                                    //check if the user already submitted a rating for this order
                                                    //checkAlreadyRatedOrder();
                                                    break;
                                                }
                                                case Constants.RETURNED: {
                                                    if (mOrder != null && mOrder.getReturnedAt() == null) {
                                                        mOrder.setReturnedAt(new Date(System.currentTimeMillis() - MainActivity.timeDifferenceFromServer));
                                                    }
                                                    setCancelAndTimerVisibility(false, false);
                                                    updateStatusTextView(R.id.state_1, R.string.order_accepted, R.drawable.state_ok);
                                                    updateStatusTextView(R.id.state_2, R.string.order_preparing, R.drawable.state_ok);
                                                    updateStatusTextView(R.id.state_3, R.string.order_prepared, R.drawable.state_ok);
                                                    updateStatusView(R.id.order_delivery_text, R.string.order_on_the_way, R.id.order_delivery_img, R.drawable.state_ok);
                                                    updateStatusView(R.id.order_delivered_text, R.string.order_returned, R.id.order_delivered_img, R.drawable.state_cancel);

                                                    //hide map view
                                                    if (mView.findViewById(R.id.delivery_map_layout).getVisibility() == View.VISIBLE) {
                                                        mView.findViewById(R.id.delivery_map_layout).setVisibility(View.GONE);
                                                    }
                                                    //check if the user already submitted a rating for this order
                                                    //checkAlreadyRatedOrder();
                                                    break;
                                                }
                                            }
                                        }
                                        /*if (System.currentTimeMillis() - MainActivity.timeDifferenceFromServer - (long)mOrder.getCreatedAt() > Constants.TIME_ONE_DAY) {
                                            buttonContactSupport.setVisibility(View.GONE);
                                        } else {
                                            buttonContactSupport.setVisibility(View.VISIBLE);
                                        }*/
                                    } catch (Exception ex) {
                                        Log.w("testing", "Firestore onEvent crashed : ", ex);
                                        ex.printStackTrace();
                                        FirebaseCrashlytics.getInstance().recordException(ex);
                                    }
                                }
                            }
                            firstTimeOrderListener = false;
                        }
                    });
        } catch (Exception exx) {
            Log.w("testing", "Firestore query crashed : ", exx);
            FirebaseCrashlytics.getInstance().recordException(exx);
        }
    }

    private void createTrackingView(FireStoreOrder order) {
        //Initialize tracking layout
        trackingLayout = mView.findViewById(R.id.scrollview_layout);

        getStoreDeliveryTime();

        //if this order is a "delivery" type order
        if (!order.getOrderType().equals(Constants.DELIVERY)) {
            mView.findViewById(R.id.vertical_line).setVisibility(View.GONE);
            mView.findViewById(R.id.state_4).setVisibility(View.GONE);
            mView.findViewById(R.id.state_5).setVisibility(View.GONE);
        }
    }

    private void getStoreDeliveryTime() {
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.STORES_PROFILES)
                .child(storeId)
                .child(Constants.DELIVERY_TIME)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.getValue() != null) {
                                deliveryTime = dataSnapshot.getValue(Integer.class);
                            }
                        } catch (Exception e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setCancelAndTimerVisibility(boolean isCancelButtonVisible, boolean isCountdownVisible) {
        //TODO : Remove the line below, if we want to re-enable the timer
        isCountdownVisible = false;
        try {
            if (buttonCancelOrder != null) {
                buttonCancelOrder.setCompoundDrawablesWithIntrinsicBounds(Constants.ZERO, isCancelButtonVisible ? R.drawable.cancel_order : R.drawable.cancel_order_gray, Constants.ZERO, Constants.ZERO);
                buttonCancelOrder.setTextColor(getResources().getColor(isCancelButtonVisible ? R.color.white : R.color.gray));
                buttonCancelOrder.setEnabled(isCancelButtonVisible);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void updateStatusTextView(int statusViewId, int messageValueId, int iconDrawableId) {
        TextView statusTextView = mView.findViewById(statusViewId);

        //update status message
        statusTextView.setText(messageValueId);
        //change status icon
        statusTextView.setCompoundDrawablesWithIntrinsicBounds(iconDrawableId, Constants.ZERO, Constants.ZERO, Constants.ZERO);
        //change text color
        if (getContext() != null) {
            if (iconDrawableId == R.drawable.state_off) {
                statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
            } else {
                statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
        }
        if (!isStatusRefreshedOnce)
            isStatusRefreshedOnce = true;
    }

    private void updateStatusView(int messageViewId, int messageValueId, int iconViewId, int iconDrawableId) {
        TextView  statusTextView = mView.findViewById(messageViewId);
        ImageView statusIconView = mView.findViewById(iconViewId);

        //change status message
        statusTextView.setText(messageValueId);
        //change status icon and text color
        if (getContext() != null) {
            statusIconView.setImageDrawable(ContextCompat.getDrawable(getContext(), iconDrawableId));
            if (iconDrawableId == R.drawable.state_off) {
                statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
            } else {
                statusTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
        }
        if (!isStatusRefreshedOnce)
            isStatusRefreshedOnce = true;
    }

    private void initializeMapView(Bundle savedInstanceState) {
        if (mMapView == null) {
            mMapView = mView.findViewById(R.id.tracking_mapView);
            mMapView.setViewParent(trackingLayout);

            //create googlemap
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);

            //edit the MapView height
            try {
                ViewGroup.LayoutParams params = mMapView.getLayoutParams();
                params.height = (int) (Constants.widthAndHeightInDp(getContext()).first * 0.7f);
                mMapView.setLayoutParams(params);
            } catch (Exception ex) {
                FirebaseCrashlytics.getInstance().recordException(ex);
            }
        } else if (trackingLayout.getVisibility() == View.VISIBLE) {
            //display the map view and the agent profile view
            mView.findViewById(R.id.delivery_map_layout).setVisibility(View.VISIBLE);

            //extract client position from order object
            String[] coordinates = mOrder.getUserCoordinates().split(",");
            clientPosition = new Coordinate(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
            //Display the restaurant and the client inside the google Map
            createClientAndRestaurantMarkers();

            if (!TextUtils.isEmpty(mOrder.getDeliveryAgentId())) {
                listenToCourierPositionUpdates();

                getCourierProfile();

                getDrivenVehicle();
            }
            createCameraBoundsAndMoveCamera();
        }
    }

    @Override
    public void onMapReady(GoogleMap Map) {
        googleMap = Map;
        mMapView.onResume();
        setUpGoogleMap();

        //display the map view and the agent profile view
        mView.findViewById(R.id.delivery_map_layout).setVisibility(View.VISIBLE);

        //extract client position from order object
        String[] coordinates = mOrder.getUserCoordinates().split(",");
        clientPosition = new Coordinate(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
        //Display the restaurant and the client inside the google Map
        createClientAndRestaurantMarkers();

        if (!TextUtils.isEmpty(mOrder.getDeliveryAgentId())) {
            listenToCourierPositionUpdates();

            getCourierProfile();

            getDrivenVehicle();
        }
    }

    //Setting up mapview object
    private void setUpGoogleMap() {
        if (getContext() != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            /* This means Location permission is granted */

                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        }
    }

    //Getting restaurant location from database
    //Then create and display both the client and the store as markers.
    private void createClientAndRestaurantMarkers() {
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.STORES_LOCATIONS)
                .child(storeId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            GeoFireObject geoFireObject = dataSnapshot.getValue(GeoFireObject.class);
                            LatLng restaurantPosition = null;

                            if (geoFireObject != null) {
                                restaurantPosition = new LatLng(geoFireObject.getLatitude(), geoFireObject.getLongitude());
                                //Display the restaurant position as a marker on the map
                                if (storeName != null) {
                                    storeMarker = googleMap.addMarker(new MarkerOptions().position(restaurantPosition)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_active))
                                            .title(storeName));
                                } else {
                                    storeMarker = googleMap.addMarker(new MarkerOptions().position(restaurantPosition)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_active)));
                                }
                            }
                            //Display the client position as a marker on the map
                            try {
                                clientMarker = googleMap.addMarker(new MarkerOptions()
                                         .position(clientPosition.toLatLng())
                                         .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_client))
                                         .title(getString(R.string.you_are_here)));
                            } catch (Exception e) {
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }

                            if (trackingLayout.getVisibility() == View.VISIBLE) {
                                createCameraBoundsAndMoveCamera();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
        }       );
    }

    private void createCameraBoundsAndMoveCamera() {
        try {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

            //include the client position to the googlemap camera bounds
            if (clientMarker != null && clientMarker.getPosition() != null)
                boundsBuilder.include(clientMarker.getPosition());

            //include the restaurant position to the camera bounds
            if (storeMarker != null && storeMarker.getPosition() != null)
                boundsBuilder.include(storeMarker.getPosition());

            //include the agent position to the googlemap camera bounds
            if (agentMarker != null && agentMarker.getPosition() != null) {
                boundsBuilder.include(agentMarker.getPosition());
            }

            final LatLngBounds bounds = boundsBuilder.build();
            //Try to move camera using created bounds
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                try {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, Constants.MAP_CAMERA_PADDING));
                } catch (Exception e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            }, Constants.TIME_THREE_HUNDRED_MILLIS);

        } catch (Exception ignored) {}
    }

    private void listenToCourierPositionUpdates() {
        if (courierPositionListener == null) {
            courierPositionListener = FirebaseDatabase.getInstance().getReference()
                    .child(Constants.DELIVERY_AGENTS_LOCATIONS)
                    .child(mOrder.getDeliveryAgentId())
                    .child(Constants.COORDINATES)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                String coordinate = dataSnapshot.getValue().toString();
                                if (!TextUtils.isEmpty(coordinate)) {
                                    //now extract latitude and longitude values
                                    //used to create a LatLng object
                                    String[] coordinates = coordinate.split(",");
                                    LatLng agentPosition = new LatLng(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));

                                    //use the LatLng (position) object to update the agent position on the map
                                    if (agentMarker == null) {
                                        agentMarker = googleMap.addMarker(new MarkerOptions()
                                                .position(agentPosition)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_driver))
                                                .title(""));
                                    } else {
                                        agentMarker.setPosition(agentPosition);
                                    }
                                    if (trackingLayout.getVisibility() == View.VISIBLE) {
                                        createCameraBoundsAndMoveCamera();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {
            FirebaseDatabase.getInstance().getReference()
                    .child(Constants.DELIVERY_AGENTS_LOCATIONS)
                    .child(mOrder.getDeliveryAgentId())
                    .child(Constants.COORDINATES)
                    .addValueEventListener(courierPositionListener);
        }
    }

    private void getCourierProfile() {
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.DELIVERY_AGENTS_PROFILES)
                .child(mOrder.getDeliveryAgentId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            final DeliveryAgent agentProfile = dataSnapshot.getValue(DeliveryAgent.class);
                            if (agentProfile != null) {
                                //Show agent photo if existent
                                if (agentProfile.getImage() != null && !TextUtils.isEmpty(agentProfile.getImage().getUrl())) {
                                    Glide.with(getContext())
                                            .load(agentProfile.getImage().getUrl())
                                            .apply(Constants.getGlideRequestOptions(Constants.GlideQuality.LOW)
                                                    .centerCrop()
                                                    .placeholder(R.drawable.profile_photo_placeholder)
                                                    .fallback(R.drawable.profile_photo_placeholder))
                                            .transition(DrawableTransitionOptions.withCrossFade(Constants.TIME_ONE_SECOND))
                                            .into((ImageView) mView.findViewById(R.id.driver_photo));
                                }

                                //display agent name
                                if (!TextUtils.isEmpty(agentProfile.getName())) {
                                    ((TextView) mView.findViewById(R.id.driver_name)).setText(agentProfile.getName());
                                    if (agentMarker != null)
                                        agentMarker.setTitle(agentProfile.getName());
                                }

                                //implement phone call click listener
                                mView.findViewById(R.id.driver_call).setOnClickListener(new OnSingleClickListener() {
                                    @Override
                                    protected void onSingleClick() {
                                        //Popup for the user before proceeding to phone call
                                        Utils.createPhoneCallDialog(getActivity(), agentProfile.getPhone(), null);

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getDrivenVehicle() {
        FirebaseDatabase.getInstance().getReference()
                .child(Constants.DELIVERY_AGENTS_PARAMS)
                .child(mOrder.getDeliveryAgentId())
                .child(Constants.VEHICLE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            try {
                                @SuppressWarnings("ConstantConditions") final int vehicleId = dataSnapshot.getValue(Integer.class);
                                String[] vehiclesArray = getResources().getStringArray(R.array.vehicles);

                                ((TextView) mView.findViewById(R.id.driver_vehicle)).setText(vehiclesArray[vehicleId]);
                            } catch (Exception exception) {
                                FirebaseCrashlytics.getInstance().recordException(exception);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void displayOrderPriceDetails(FireStoreOrder order) {
        try {
            if (!order.getOrderType().equals(Constants.DELIVERY) && order.getPaidWithLoyalty() == Constants.ZERO && order.getTotalDiscount() == null) {
                //display the final order total price
                ((TextView) mView.findViewById(R.id.order_final_price))
                        .setText(Utils.displayPrice(order.getTotalPrice(), true, order.getCountryID()));
            } else {
                double initialOrderPrice = order.getTotalPrice();

                //display discount value if exist
                if (order.getTotalDiscount() != null && order.getTotalDiscount() > Constants.ZERO) {
                    //make discount layout visible
                    mView.findViewById(R.id.discount_layout).setVisibility(View.VISIBLE);
                    //display the discount value inside of the appropriate view
                    ((TextView) mView.findViewById(R.id.discount_value_view)).setText(Utils.displayPrice((order.getTotalDiscount() * Constants.MINUS_ONE), true, order.getCountryID()));

                    initialOrderPrice += order.getTotalDiscount();
                    //display the initial price layout
                    mView.findViewById(R.id.products_price_layout).setVisibility(View.VISIBLE);
                }

                //display paid with loyalty
                if (order.getPaidWithLoyalty() > Constants.ZERO) {
                    mView.findViewById(R.id.paid_with_loyalty_layout).setVisibility(View.VISIBLE);
                    ((TextView) mView.findViewById(R.id.paid_with_loyalty_view)).setText(Utils.displayPrice((order.getPaidWithLoyalty() * -1), true, order.getCountryID()));

                    initialOrderPrice += order.getPaidWithLoyalty();
                    //display the initial price layout
                    mView.findViewById(R.id.products_price_layout).setVisibility(View.VISIBLE);
                }

                if (order.getDeliveryFee() != null && order.getDeliveryFee() > Constants.ZERO) {
                    mView.findViewById(R.id.delivery_fee_layout).setVisibility(View.VISIBLE);
                    ((TextView) mView.findViewById(R.id.delivery_fee_view)).setText(Utils.displayPrice(order.getDeliveryFee(), true, order.getCountryID()));

                    initialOrderPrice -= order.getDeliveryFee();
                    //display the initial price layout
                    mView.findViewById(R.id.products_price_layout).setVisibility(View.VISIBLE);
                }

                //display the products price value
                ((TextView) mView.findViewById(R.id.products_price_view)).setText(Utils.displayPrice(initialOrderPrice, true, order.getCountryID()));

                //display the final order total price
                ((TextView) mView.findViewById(R.id.order_final_price))
                        .setText(Utils.displayPrice(order.getTotalPrice(), true, order.getCountryID()));
            }
        } catch (Exception exception) {
            FirebaseCrashlytics.getInstance().recordException(exception);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.statuses_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() != null) {
            updateOrderStatusInFireStore(item.getTitle().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateOrderStatusInFireStore(String newStatus) {
        if (!TextUtils.isEmpty(orderId)) {
            FirebaseFirestore.getInstance()
                    .collection(Constants.COLLECTION_ORDERS)
                    .document(orderId)
                    .update("status", newStatus)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ((MainActivity)getActivity()).displayMessage(R.string.status_successfully_sent, android.R.color.holo_green_dark, Constants.TIME_ONE_SECOND);
                            } else {
                                ((MainActivity)getActivity()).displayMessage(R.string.status_failed_tobe_sent, android.R.color.holo_red_light, Constants.TIME_ONE_SECOND);
                            }
                        }
                    });
        } else {
            ((MainActivity)getActivity()).displayMessage(R.string.status_failed_tobe_sent, android.R.color.holo_red_light, Constants.TIME_ONE_SECOND);
        }
    }
}
