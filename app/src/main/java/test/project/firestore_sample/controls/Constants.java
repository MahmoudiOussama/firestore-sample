package test.project.firestore_sample.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.WindowManager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import test.project.firestore_sample.R;

import static android.content.Context.WINDOW_SERVICE;
import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;
import static com.bumptech.glide.load.DecodeFormat.PREFER_RGB_565;

public class Constants {

    /** New Firebase Node keys */
    public final static String TIMESTAMP              = "timestamp";
    public static final String DISCUSSION_ID          = "discussion_id";
    public static final String ORDER_ID               = "order_id";

    /** Numbers */
    public static final short MINUS_ONE                   = -1;
    public static final short ZERO                        = 0;
    public static final short ONE                         = 1;
    public static final short TWO                         = 2;
    public static final short THREE                       = 3;
    public static final short EIGHT                       = 8;
    public static final short TIME_THREE_HUNDRED_MILLIS   = 300;
    public static final short TIME_ONE_SECOND             = 1000;
    public static final short TIME_TWO_SECONDS            = 2000;
    public static final short TIME_SIX_SECONDS            = 6000;
    public static final int   TIME_ONE_MINUTE             = 60000;
    public static final short MAP_CAMERA_PADDING          = 100;
    public static final int   ONE_THOUSAND                = 1000;
    public static final int   ONE_MILLION                 = 1000000;

    /** Words */
    public static final String NEW                        = "new";
    public static final String CHECK                      = "checking";
    public static final String RECEIVED                   = "received";
    public static final String IN_PROGRESS                = "in_progress";
    public static final String CANCELED                   = "canceled";
    public static final String VALIDATED                  = "validated";
    public static final String PICKED                     = "picked";
    public static final String DELIVERED                  = "delivered";
    public static final String RETURNED                   = "returned";
    public static final String DELIVERY                   = "delivery";
    public static final String TIME_FORMAT                = "hh:mm a Z";
    public static final String DAY_FORMAT                 = "EEEE";
    public static final String EMPTY                      = "  ";
    public static final String TEL                        = "tel:";
    public static final String STATUS                     = "status";
    public static final String COORDINATES                = "coordinates";
    public static final String REGEX_PHONE_TN             = "^(?:00216|\\+216)*[0-9]{8}";
    public static final String VEHICLE                    = "vehicle";
    public static final String COUNTRY_CODE               = "+216";
    public static final String SKIP_PROTECTED_APP_CHECK   = "skipProtectedAppCheck";
    public static final String UNKNOWN                    = "Unknown";
    public static final String K                          = "K";
    public static final String M                          = "M";
    public static final String DEFAULT                    = "default";

    /** Database  */
    public static final String STORES_LOCATIONS           = "stores_locations";
    public static final String STORES_PROFILES            = "stores_profiles";
    public static final String DELIVERY_AGENTS_LOCATIONS  = "delivery_agents_locations";
    public static final String DELIVERY_AGENTS_PROFILES   = "delivery_agents_profiles";
    public static final String DELIVERY_AGENTS_PARAMS     = "delivery_agents_params";
    public static final String PHONE_CALL_COUNT           = "phone_call_count";
    public static final String DELIVERY_TIME              = "delivery_time";

    /** FireStore*/
    public static final String COLLECTION_ORDERS          = "orders";

    public static final String PREFERENCE_COUNTRY         = "COUNTRY";
    public static final String PREFERENCE_LEVEL_ONE_ZONE  = "LEVEL_ONE_ZONE";
    public static final String PREFERENCE_LEVEL_TWO_ZONE  = "LEVEL_TWO_ZONE";

    public static DisplayMetrics getScreenSize(Context context) {
        // Get Screen size as Pixels
        DisplayMetrics dm = new DisplayMetrics();
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getMetrics(dm);
            }
        } catch (Exception dmEx) {
            FirebaseCrashlytics.getInstance().recordException(dmEx);
            dm.setToDefaults();
        }

        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        TypedArray a = context.obtainStyledAttributes(new TypedValue().data,  textSizeAttr);
        int height = a.getDimensionPixelSize(0, 0);
        a.recycle();

        dm.heightPixels = dm.heightPixels - height;

        return dm;
    }

    public static int heightMenu(Context context){
        DisplayMetrics dm = getScreenSize(context);
        if(dm.heightPixels <= 800) {
            return (((dm.heightPixels)*43)/100);
        } else if(dm.heightPixels <= 1280) {
            return (((dm.heightPixels)*40)/100)-10;
        } else {
            return (((dm.heightPixels)*38)/100);
        }
    }

    public static int heightMenuOrder(Context context){
        return (getScreenSize(context).widthPixels / 4);
    }

    public static Pair<Float, Float> widthAndHeightInDp(Context context) {
        DisplayMetrics dMetrics = getScreenSize(context);
        float dpHeight = dMetrics.heightPixels;
        float dpWidth  = dMetrics.widthPixels;
        return new Pair<>(dpWidth, dpHeight);
    }

    public static DatabaseReference dbRef(String databaseUrl) {
        return FirebaseDatabase.getInstance(databaseUrl).getReference();
    }

    public enum GlideQuality {
        HIGH,
        LOW
    }

    public static RequestOptions glideRequestOptions;
    public static RequestOptions getGlideRequestOptions(GlideQuality glideQuality) {
        if (glideRequestOptions != null) {
            return glideRequestOptions;
        } else {
            glideRequestOptions = new RequestOptions()
                    .format(glideQuality == GlideQuality.HIGH ? PREFER_ARGB_8888 : PREFER_RGB_565)
                    .disallowHardwareConfig()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.DATA);
            return glideRequestOptions;
        }
    }
}
