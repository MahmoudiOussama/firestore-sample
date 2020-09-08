package test.project.firestore_sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import test.project.firestore_sample.controls.Constants;
import test.project.firestore_sample.controls.OnSingleClickListener;
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
import com.google.gson.Gson;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static UserProfile currentUserProfile;
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
    private AppCompatButton trackOrderBtn1, trackOrderBtn2;
    private Gson gson;

    @Override
    protected void onStart() {
        super.onStart();
        int pid = android.os.Process.myPid();
        String whiteList = "logcat -P '" + pid + "'";
        try {
            Runtime.getRuntime().exec(whiteList).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculateTimeDifference();

        progressBar = findViewById(R.id.progress_bar);
        messageForTheUser = findViewById(R.id.message_for_user);
        fireStoreQueryTriggerCount = findViewById(R.id.counter);
        trackOrderBtn1 = findViewById(R.id.trackOrderBtn);
        trackOrderBtn2 = findViewById(R.id.trackOrderBtn2);
        gson = new Gson();

        trackOrderBtn1.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick() {
                FireStoreOrder fireStoreOrder = gson.fromJson(jsonOrder1, FireStoreOrder.class);

                OrderDetailsFragment fragment = OrderDetailsFragment.newInstance(fireStoreOrder.getStoreId(),
                        fireStoreOrder.getOrderId(),
                        fireStoreOrder.getStoreName(),
                        fireStoreOrder.getOrderNumber());
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });

        trackOrderBtn2.setOnClickListener(new OnSingleClickListener() {
            @Override
            protected void onSingleClick() {
                FireStoreOrder fireStoreOrder = gson.fromJson(jsonOrder2, FireStoreOrder.class);

                OrderDetailsFragment fragment = OrderDetailsFragment.newInstance(fireStoreOrder.getStoreId(),
                        fireStoreOrder.getOrderId(),
                        fireStoreOrder.getStoreName(),
                        fireStoreOrder.getOrderNumber());
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });

        mHandler = new Handler();

        /*Constants.getFireStoreInstance()
                .collection(COLLECTION_ORDERS)
                .document("arqHFzuojhY4MsqxRQcl")
                .set(gson.fromJson(jsonOrder1, FireStoreOrder.class));

        Constants.getFireStoreInstance()
                .collection(COLLECTION_ORDERS)
                .document("ZYaJKmQF9n1RknziD1tj")
                .set(gson.fromJson(jsonOrder2, FireStoreOrder.class));*/
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
            orderRealtimeListener = Constants.getFireStoreInstance()
                    .collection(Constants.COLLECTION_ORDERS)
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

    private final String jsonOrder1 = "{\n" +
            "   \"app_name\":\"MenuTium-Test\",\n" +
            "   \"app_version\":\"5.4.1\",\n" +
            "   \"comments_count\":0,\n" +
            "   \"created_at\":\"Jul 15, 2020 15:39:27\",\n" +
            "   \"currency\":\"TND\",\n" +
            "   \"delivery_agent_id\":\"ptiErsP1BZQyWcBwDIDuqx62NGv2\",\n" +
            "   \"delivery_assignment_status\":\"pending\",\n" +
            "   \"delivery_fee\":15.0,\n" +
            "   \"distance\":0.0,\n" +
            "   \"hearts_count\":0,\n" +
            "   \"in_progress_at\":\"Jul 16, 2020 13:05:33\",\n" +
            "   \"is_friends\":false,\n" +
            "   \"is_private\":false,\n" +
            "   \"is_public\":true,\n" +
            "   \"is_visible\":false,\n" +
            "   \"items\":{\n" +
            "      \"-MCHsDUdnnmkKIi_fV0n\":{\n" +
            "         \"category_id\":\"-LHD8FJXbAnbSNZQtxQQ\",\n" +
            "         \"discount\":0,\n" +
            "         \"discount_price\":0.0,\n" +
            "         \"extras\":{\n" +
            "            \"-M1-mZZnHI8AhtthTETx\":{\n" +
            "               \"is_sub_extra\":true,\n" +
            "               \"max\":1,\n" +
            "               \"name\":\"Nuggets\",\n" +
            "               \"price\":0.0,\n" +
            "               \"quantity\":1,\n" +
            "               \"row\":0\n" +
            "            },\n" +
            "            \"-M1-rBVG52eVX-_R8z02\":{\n" +
            "               \"name\":\"Cheddar\",\n" +
            "               \"price\":0.6,\n" +
            "               \"quantity\":1,\n" +
            "               \"row\":1\n" +
            "            }\n" +
            "         },\n" +
            "         \"itemPrice\":0.0,\n" +
            "         \"main_image\":{\n" +
            "            \"ref\":\"/ZKRBeLgCauXKZ15rfRcuQTY2S1k1/-LlXe_ZbTQytlKujrhLF/products/1582717663739.jpg\",\n" +
            "            \"url\":\"https://firebasestorage.googleapis.com/v0/b/urban-food-a9a70.appspot.com/o/ZKRBeLgCauXKZ15rfRcuQTY2S1k1%2F-LlXe_ZbTQytlKujrhLF%2Fproducts%2F1582717663739.jpg?alt\\u003dmedia\\u0026token\\u003d1bbfd8a0-be5a-44c2-8dd9-d5a7dacf2243\"\n" +
            "         },\n" +
            "         \"name\":\"Tacos\",\n" +
            "         \"options\":{\n" +
            "            \"-LlfWYJR_zKtO1l0DjnF\":{\n" +
            "               \"elements\":{\n" +
            "                  \"-LlfWYJQPcTUDpFLk1jr\":{\n" +
            "                     \"name\":\"S : Choisir 1 Viande\",\n" +
            "                     \"price\":5.0,\n" +
            "                     \"row\":0\n" +
            "                  }\n" +
            "               },\n" +
            "               \"name\":\"Taille\",\n" +
            "               \"row\":-1\n" +
            "            },\n" +
            "            \"-LlfX5HY1rzcQpz1p6mG\":{\n" +
            "               \"elements\":{\n" +
            "                  \"-M2XzZBtPBxdH6qePw6P\":{\n" +
            "                     \"max\":1,\n" +
            "                     \"name\":\"Sans Sauce\",\n" +
            "                     \"price\":0.0,\n" +
            "                     \"quantity\":1,\n" +
            "                     \"row\":1\n" +
            "                  }\n" +
            "               },\n" +
            "               \"name\":\"Sauces\",\n" +
            "               \"row\":3\n" +
            "            },\n" +
            "            \"-LlfWPIAjjgTiqtqEzep\":{\n" +
            "               \"elements\":{\n" +
            "                  \"-LlfWPI9_w1gH1A3HTsB\":{\n" +
            "                     \"name\":\"Tacos\",\n" +
            "                     \"price\":0.0,\n" +
            "                     \"row\":0\n" +
            "                  }\n" +
            "               },\n" +
            "               \"name\":\"Type\",\n" +
            "               \"row\":2\n" +
            "            }\n" +
            "         },\n" +
            "         \"price\":0.0,\n" +
            "         \"product_id\":\"-LlfTFlgrNBdLZRn5xgn\",\n" +
            "         \"quantity\":1,\n" +
            "         \"sub_category_id\":\"-LHD8FJWQrP3tB-tospV\"\n" +
            "      },\n" +
            "      \"-MCHuXLzkKMSSWKzBzqc\":{\n" +
            "         \"category_id\":\"-LHDT4ctLpXnjoTjx-9J\",\n" +
            "         \"discount\":0,\n" +
            "         \"discount_price\":5.0,\n" +
            "         \"itemPrice\":0.0,\n" +
            "         \"main_image\":{\n" +
            "            \"ref\":\"/0IfVAKCjMRhWuAoPsyMKwKhW8M82/-LHCvfvi5Zis0rJffGlg/products/1556554135520.jpg\",\n" +
            "            \"url\":\"https://firebasestorage.googleapis.com/v0/b/menutium-319d0.appspot.com/o/0IfVAKCjMRhWuAoPsyMKwKhW8M82%2F-LHCvfvi5Zis0rJffGlg%2Fproducts%2F1556554135520.jpg?alt\\u003dmedia\\u0026token\\u003d82b389df-d0d9-46c2-b7b5-aa880e850d43\"\n" +
            "         },\n" +
            "         \"name\":\"Boissons énergisante\",\n" +
            "         \"price\":5.0,\n" +
            "         \"product_id\":\"-LddqKjXqLC94Krderjg\",\n" +
            "         \"quantity\":1,\n" +
            "         \"sub_category_id\":\"-LHDT4cs2IeRbOHog4HJ\"\n" +
            "      },\n" +
            "      \"-MCHuWvgQzSxfjFoBwqB\":{\n" +
            "         \"category_id\":\"-LHDT4ctLpXnjoTjx-9J\",\n" +
            "         \"discount\":0,\n" +
            "         \"discount_price\":0.0,\n" +
            "         \"itemPrice\":0.0,\n" +
            "         \"main_image\":{\n" +
            "            \"ref\":\"/0IfVAKCjMRhWuAoPsyMKwKhW8M82/-LHCvfvi5Zis0rJffGlg/products/1532704431234.jpg\",\n" +
            "            \"url\":\"https://firebasestorage.googleapis.com/v0/b/menutium-319d0.appspot.com/o/0IfVAKCjMRhWuAoPsyMKwKhW8M82%2F-LHCvfvi5Zis0rJffGlg%2Fproducts%2F1532704431234.jpg?alt\\u003dmedia\\u0026token\\u003db6835d9c-92b4-4103-bd25-879cb4609fab\"\n" +
            "         },\n" +
            "         \"name\":\"CocaCola\",\n" +
            "         \"options\":{\n" +
            "            \"-LIRIRejPEjqK3Xr7U56\":{\n" +
            "               \"elements\":{\n" +
            "                  \"-LIRIRejPEjqK3Xr7U55\":{\n" +
            "                     \"name\":\"grande\",\n" +
            "                     \"price\":2.0,\n" +
            "                     \"row\":0\n" +
            "                  }\n" +
            "               },\n" +
            "               \"name\":\"taille\"\n" +
            "            }\n" +
            "         },\n" +
            "         \"price\":0.0,\n" +
            "         \"product_id\":\"-LIRI4GeA7nAnuay-WPg\",\n" +
            "         \"quantity\":1,\n" +
            "         \"sub_category_id\":\"-LHDT4cs2IeRbOHog4HJ\"\n" +
            "      }\n" +
            "   },\n" +
            "   \"itemsCount\":0,\n" +
            "   \"note\":\"8uhi9gug ubiuvu8 u8vuog\",\n" +
            "   \"order_id\":\"arqHFzuojhY4MsqxRQcl\",\n" +
            "   \"order_number\":138,\n" +
            "   \"order_type\":\"delivery\",\n" +
            "   \"paid_with_loyalty\":0.0,\n" +
            "   \"platform\":\"android\",\n" +
            "   \"processedAt\":{\n" +
            "      \"nanoseconds\":698000000,\n" +
            "      \"seconds\":1594901130\n" +
            "   },\n" +
            "   \"restaurant_photo\":\"https://firebasestorage.googleapis.com/v0/b/menutium-319d0.appspot.com/o/0IfVAKCjMRhWuAoPsyMKwKhW8M82%2F-LHCvfvi5Zis0rJffGlg%2Fprofile%2F1531995954841.jpg?alt\\u003dmedia\\u0026token\\u003d7429c724-0077-4f86-9dca-2c66b50404f8\",\n" +
            "   \"status\":\"validated\",\n" +
            "   \"store_id\":\"-LHCvfvi5Zis0rJffGlg\",\n" +
            "   \"store_name\":\"Bistro73\",\n" +
            "   \"total_discount\":2.52,\n" +
            "   \"total_price\":25.08,\n" +
            "   \"updated_at\":\"Jul 16, 2020 13:05:39\",\n" +
            "   \"user_address\":\"Aig L12، Tunisie\",\n" +
            "   \"user_coordinates\":\"35.8217595,10.5756493\",\n" +
            "   \"user_name\":\"Mo Salah\",\n" +
            "   \"user_phone\":\"+21650001002\",\n" +
            "   \"user_photo\":\"https://firebasestorage.googleapis.com/v0/b/menutium-319d0.appspot.com/o/2Bz8euUmPgMqc4Z7QZWJjLfsgV72%2Fphoto_profile?alt\\u003dmedia\\u0026token\\u003df5dcee5b-fdda-45bd-9ec3-982cfe7832bd\",\n" +
            "   \"user_uid\":\"2Bz8euUmPgMqc4Z7QZWJjLfsgV72\",\n" +
            "   \"validated_at\":\"Jul 16, 2020 13:05:39\",\n" +
            "   \"validatedBy\":{\n" +
            "      \"name\":\"Salah\",\n" +
            "      \"id\":\"-LaLYGjCxOkloG4Avhqp\"\n" +
            "   }\n" +
            "}";

    private final String jsonOrder2 = "{\n" +
            "   \"app_name\":\"MenuTium-Test\",\n" +
            "   \"app_version\":\"5.4.0\",\n" +
            "   \"canceled_at\":\"Sep 4, 2020 15:12:34\",\n" +
            "   \"comments_count\":4,\n" +
            "   \"countryID\":\"fr\",\n" +
            "   \"created_at\":\"Sep 4, 2020 12:21:55\",\n" +
            "   \"currency\":\"TND\",\n" +
            "   \"delivered_at\":\"Sep 4, 2020 15:11:30\",\n" +
            "   \"delivery_agent_id\":\"1rvlTtfJKNVPVxZ2mp4T2CcI1Fj2\",\n" +
            "   \"delivery_assignment_status\":\"accepted\",\n" +
            "   \"delivery_fee\":5.0,\n" +
            "   \"distance\":0.0,\n" +
            "   \"hearts_count\":0,\n" +
            "   \"in_progress_at\":\"Sep 4, 2020 15:06:51\",\n" +
            "   \"is_friends\":false,\n" +
            "   \"is_private\":false,\n" +
            "   \"is_public\":true,\n" +
            "   \"is_visible\":true,\n" +
            "   \"items\":{\n" +
            "      \"-MGNqMoKy8YovI11-QUK\":{\n" +
            "         \"category_id\":\"-LlbKEnwkC4KZtJyN691\",\n" +
            "         \"discount_price\":0.0,\n" +
            "         \"extras\":{\n" +
            "            \"-M1-qhcXr8tp5KpnLp9p\":{\n" +
            "               \"max\":10,\n" +
            "               \"name\":\"Chèvre\",\n" +
            "               \"price\":0.6,\n" +
            "               \"quantity\":5,\n" +
            "               \"row\":6\n" +
            "            },\n" +
            "            \"-M1-rHnm7Xzjo7rE73Pk\":{\n" +
            "               \"max\":10,\n" +
            "               \"name\":\"Vache kiri\",\n" +
            "               \"price\":0.6,\n" +
            "               \"quantity\":7,\n" +
            "               \"row\":4\n" +
            "            },\n" +
            "            \"-M1-me-3qKOqWzul67Sr\":{\n" +
            "               \"is_sub_extra\":true,\n" +
            "               \"max\":3,\n" +
            "               \"name\":\"Crispy Tenders\",\n" +
            "               \"price\":0.0,\n" +
            "               \"quantity\":1,\n" +
            "               \"row\":0\n" +
            "            },\n" +
            "            \"-M1-mKvK76SYLJBf3zh-\":{\n" +
            "               \"is_sub_extra\":true,\n" +
            "               \"max\":3,\n" +
            "               \"name\":\"Escalope de Poulet\",\n" +
            "               \"price\":0.0,\n" +
            "               \"quantity\":2,\n" +
            "               \"row\":0\n" +
            "            },\n" +
            "            \"-M1-rNaEFCyHlf9WIOaH\":{\n" +
            "               \"max\":10,\n" +
            "               \"name\":\"Raclette\",\n" +
            "               \"price\":0.6,\n" +
            "               \"quantity\":4,\n" +
            "               \"row\":5\n" +
            "            }\n" +
            "         },\n" +
            "         \"extras_title\":\"Suppléments\",\n" +
            "         \"ingredients\":{\n" +
            "            \"-MEYbO7SlOBFWUVzcvg6\":{\n" +
            "               \"description\":\"Ingredients description\",\n" +
            "               \"name\":\"Cheddar\",\n" +
            "               \"row\":0\n" +
            "            },\n" +
            "            \"-MEYbLvKvqPu3YZPPAjo\":{\n" +
            "               \"description\":\"Ingredients description\",\n" +
            "               \"name\":\"Laitus\",\n" +
            "               \"row\":0\n" +
            "            }\n" +
            "         },\n" +
            "         \"ingredients_title\":\"Ingrédients : Décocher pour enlever\",\n" +
            "         \"itemPrice\":0.0,\n" +
            "         \"main_image\":{\n" +
            "            \"ref\":\"/ZKRBeLgCauXKZ15rfRcuQTY2S1k1/-LlXe_ZbTQytlKujrhLF/products/1582717663739.jpg\",\n" +
            "            \"url\":\"https://firebasestorage.googleapis.com/v0/b/urban-food-a9a70.appspot.com/o/ZKRBeLgCauXKZ15rfRcuQTY2S1k1%2F-LlXe_ZbTQytlKujrhLF%2Fproducts%2F1582717663739.jpg?alt\\u003dmedia\\u0026token\\u003d1bbfd8a0-be5a-44c2-8dd9-d5a7dacf2243\"\n" +
            "         },\n" +
            "         \"max_extras\":30,\n" +
            "         \"name\":\"Tacos\",\n" +
            "         \"options\":{\n" +
            "            \"-LlfWYJR_zKtO1l0DjnF\":{\n" +
            "               \"elements\":{\n" +
            "                  \"-LlfWj5L6weXYZUPUoGW\":{\n" +
            "                     \"extras_title\":\"3 Viandes\",\n" +
            "                     \"max\":1,\n" +
            "                     \"max_extras\":3,\n" +
            "                     \"min_extras\":3,\n" +
            "                     \"name\":\"L : Choisir 3 Viandes\",\n" +
            "                     \"price\":9.5,\n" +
            "                     \"row\":0\n" +
            "                  }\n" +
            "               },\n" +
            "               \"name\":\"Taille\",\n" +
            "               \"row\":-1\n" +
            "            },\n" +
            "            \"-LlfX5HY1rzcQpz1p6mG\":{\n" +
            "               \"elements\":{\n" +
            "                  \"-LlfXbVB-QA-utApD3HO\":{\n" +
            "                     \"max\":3,\n" +
            "                     \"name\":\"Barbecue\",\n" +
            "                     \"price\":0.0,\n" +
            "                     \"quantity\":2,\n" +
            "                     \"row\":0\n" +
            "                  },\n" +
            "                  \"-LlfXxF-7RRTZhFwXhyX\":{\n" +
            "                     \"max\":3,\n" +
            "                     \"name\":\"Marocaine\",\n" +
            "                     \"price\":0.0,\n" +
            "                     \"quantity\":2,\n" +
            "                     \"row\":0\n" +
            "                  }\n" +
            "               },\n" +
            "               \"max_quantity\":4,\n" +
            "               \"min_quantity\":1,\n" +
            "               \"name\":\"Sauces\",\n" +
            "               \"row\":3\n" +
            "            },\n" +
            "            \"-LlfWPIAjjgTiqtqEzep\":{\n" +
            "               \"elements\":{\n" +
            "                  \"-LlfWPI9_w1gH1A3HTsB\":{\n" +
            "                     \"name\":\"Tacos\",\n" +
            "                     \"price\":0.0,\n" +
            "                     \"row\":0\n" +
            "                  }\n" +
            "               },\n" +
            "               \"name\":\"Type\",\n" +
            "               \"row\":2\n" +
            "            }\n" +
            "         },\n" +
            "         \"price\":0.0,\n" +
            "         \"product_id\":\"-LlfTFlgrNBdLZRn5xgn\",\n" +
            "         \"quantity\":2,\n" +
            "         \"sub_category_id\":\"-LlbKEnvPMIV3DnSiYlJ\"\n" +
            "      },\n" +
            "      \"-MGNqOGcRfYb-myLgc1d\":{\n" +
            "         \"category_id\":\"-LlkUx5iBSXhKdMYGxTA\",\n" +
            "         \"discount_price\":3.5,\n" +
            "         \"extras\":{\n" +
            "            \"-M2JOJaB_Gsc28GT8wEg\":{\n" +
            "               \"max\":2,\n" +
            "               \"name\":\"Harissa\",\n" +
            "               \"price\":0.0,\n" +
            "               \"quantity\":1,\n" +
            "               \"row\":2\n" +
            "            }\n" +
            "         },\n" +
            "         \"extras_title\":\"Sauces\",\n" +
            "         \"itemPrice\":0.0,\n" +
            "         \"main_image\":{\n" +
            "            \"ref\":\"/ZKRBeLgCauXKZ15rfRcuQTY2S1k1/-LlXe_ZbTQytlKujrhLF/products/1582809519364.jpg\",\n" +
            "            \"url\":\"https://firebasestorage.googleapis.com/v0/b/urban-food-a9a70.appspot.com/o/ZKRBeLgCauXKZ15rfRcuQTY2S1k1%2F-LlXe_ZbTQytlKujrhLF%2Fproducts%2F1582809519364.jpg?alt\\u003dmedia\\u0026token\\u003dabc457cb-2462-44be-886a-af83db3cb501\"\n" +
            "         },\n" +
            "         \"max_extras\":2,\n" +
            "         \"name\":\"Oignons frites\",\n" +
            "         \"options\":{\n" +
            "            \"-M15mpsXwAzsDRlLDHJI\":{\n" +
            "               \"elements\":{\n" +
            "                  \"-M15mpsXwAzsDRlLDHJG\":{\n" +
            "                     \"name\":\"Cheddar\",\n" +
            "                     \"price\":0.0,\n" +
            "                     \"row\":0\n" +
            "                  }\n" +
            "               },\n" +
            "               \"name\":\"Au choix :\",\n" +
            "               \"row\":-1\n" +
            "            }\n" +
            "         },\n" +
            "         \"price\":3.5,\n" +
            "         \"product_id\":\"-M15mV_4Ui2B90GlzsaR\",\n" +
            "         \"quantity\":1,\n" +
            "         \"sub_category_id\":\"-LlkUx5h8rXECP8L_hjQ\"\n" +
            "      },\n" +
            "      \"-MGNqOxoHFGoigUXao96\":{\n" +
            "         \"category_id\":\"-LlkUx5iBSXhKdMYGxTA\",\n" +
            "         \"discount_price\":3.5,\n" +
            "         \"extras\":{\n" +
            "            \"-M2JOmBf1oN1BmUUHeiY\":{\n" +
            "               \"name\":\"Biggy burger\",\n" +
            "               \"price\":0.0,\n" +
            "               \"quantity\":1,\n" +
            "               \"row\":0\n" +
            "            }\n" +
            "         },\n" +
            "         \"itemPrice\":0.0,\n" +
            "         \"main_image\":{\n" +
            "            \"ref\":\"/ZKRBeLgCauXKZ15rfRcuQTY2S1k1/-LlXe_ZbTQytlKujrhLF/products/1582809369789.jpg\",\n" +
            "            \"url\":\"https://firebasestorage.googleapis.com/v0/b/urban-food-a9a70.appspot.com/o/ZKRBeLgCauXKZ15rfRcuQTY2S1k1%2F-LlXe_ZbTQytlKujrhLF%2Fproducts%2F1582809369789.jpg?alt\\u003dmedia\\u0026token\\u003d705593e0-e403-4931-b040-5b2261cb96e5\"\n" +
            "         },\n" +
            "         \"name\":\"Bacon frites\",\n" +
            "         \"options\":{\n" +
            "            \"-M15lyQyHs1GujtZxMtS\":{\n" +
            "               \"elements\":{\n" +
            "                  \"-M15lyQxrRFaLAWGaa3i\":{\n" +
            "                     \"name\":\"Sauce fromagère\",\n" +
            "                     \"price\":0.0,\n" +
            "                     \"row\":0\n" +
            "                  }\n" +
            "               },\n" +
            "               \"name\":\"Au choix :\",\n" +
            "               \"row\":-1\n" +
            "            }\n" +
            "         },\n" +
            "         \"price\":3.5,\n" +
            "         \"product_id\":\"-M15lqmhF16ZOLqebPzs\",\n" +
            "         \"quantity\":1,\n" +
            "         \"sub_category_id\":\"-LlkUx5h8rXECP8L_hjQ\"\n" +
            "      }\n" +
            "   },\n" +
            "   \"itemsCount\":0,\n" +
            "   \"level_one_zone_id\":\"Île-de-France\",\n" +
            "   \"level_two_zone_id\":\"Paris\",\n" +
            "   \"order_id\":\"ZYaJKmQF9n1RknziD1tj\",\n" +
            "   \"order_number\":31,\n" +
            "   \"order_type\":\"delivery\",\n" +
            "   \"paid\":false,\n" +
            "   \"paid_with_loyalty\":0.0,\n" +
            "   \"picked_at\":\"Sep 4, 2020 15:12:46\",\n" +
            "   \"platform\":\"android\",\n" +
            "   \"processedAt\":{\n" +
            "      \"nanoseconds\":994000000,\n" +
            "      \"seconds\":1599228406\n" +
            "   },\n" +
            "   \"restaurant_photo\":\"https://firebasestorage.googleapis.com/v0/b/menutium-319d0.appspot.com/o/LHx3fTdtoaRlrAHSjznJSaPz9rP2%2F-MEYUb7iepk3pLwUyWX3%2Fprofile%2F1597250393877.jpg?alt\\u003dmedia\\u0026token\\u003dcbccd3fe-91fd-4397-ab34-79f5f241def2\",\n" +
            "   \"status\":\"picked\",\n" +
            "   \"store_id\":\"-MEYUb7iepk3pLwUyWX3\",\n" +
            "   \"store_name\":\"La Fourchette\",\n" +
            "   \"total_price\":50.2,\n" +
            "   \"updated_at\":\"Sep 7, 2020 18:54:02\",\n" +
            "   \"user_address\":\"166 Quai de Stalingrad, 92130 Issy-les-Moulineaux, France\\nIssy-les-Moulineaux\",\n" +
            "   \"user_coordinates\":\"48.8256954,2.2579879\",\n" +
            "   \"user_name\":\"Mo Salah\",\n" +
            "   \"user_phone\":\"+21650001002\",\n" +
            "   \"user_photo\":\"https://firebasestorage.googleapis.com/v0/b/menutium-319d0.appspot.com/o/2Bz8euUmPgMqc4Z7QZWJjLfsgV72%2Fphoto_profile?alt\\u003dmedia\\u0026token\\u003df5dcee5b-fdda-45bd-9ec3-982cfe7832bd\",\n" +
            "   \"user_uid\":\"2Bz8euUmPgMqc4Z7QZWJjLfsgV72\",\n" +
            "   \"validated_at\":\"Sep 4, 2020 15:12:42\",\n" +
            "   \"validatedBy\":{\n" +
            "      \"name\":\"Urban-Admin\",\n" +
            "      \"id\":\"admin\"\n" +
            "   }\n" +
            "}";
}