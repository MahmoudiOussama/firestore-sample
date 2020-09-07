package test.project.firestore_sample.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import test.project.firestore_sample.MainActivity;
import test.project.firestore_sample.OrderDetailsFragment;
import test.project.firestore_sample.R;
import test.project.firestore_sample.controls.Constants;
import test.project.firestore_sample.controls.OnSingleClickListener;
import test.project.firestore_sample.controls.Utils;
import test.project.firestore_sample.models.Cart;
import test.project.firestore_sample.models.CartProductItem;
import test.project.firestore_sample.models.Element;
import test.project.firestore_sample.models.Extra;
import test.project.firestore_sample.models.Ingredient;
import test.project.firestore_sample.models.Option;
import test.project.firestore_sample.models.OrderItem;

// Created by ioBirdOussama on 13/03/2017.

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final Context context;
    private List<OrderItem> orderMealsList;
    private Cart cart;
    private final int calculatedHeight;
    //private int defaultWidth;
    private final String cartMode;
    private final LayoutInflater inflater;
    private int latestClickedProductItem;

    public CartAdapter(Context context, List<OrderItem> orderMealsList, String cartMode) {
        this.context = context;
        this.orderMealsList = orderMealsList;
        this.cartMode = cartMode;
        calculatedHeight = Constants.heightMenuOrder(context);
        inflater = LayoutInflater.from(context);
    }

    private Object getItem(int position) {
        /*Object object;
        if (cartMode.equals(Constants.NEW)) {
            object = cart.getItemsList().get(position);
        } else {
            object = orderMealsList.get(position);
        }*/
        return orderMealsList.get(position);
    }

    //method which returns a view containing the option/extra Title and the chosen elements
    private View createChoiceView(String optionName, String elementName) {
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams") View cartOptionItem = inflater.inflate(R.layout.cart_option_item, null);

        ((TextView)cartOptionItem.findViewById(R.id.option_name)).setText(optionName);
        ((TextView) cartOptionItem.findViewById(R.id.element_name)).setText(elementName);

        return cartOptionItem;
    }

    //method which returns a view containing the option/extra Title and the chosen elements
    private View createChoiceView(String optionName, SpannableStringBuilder elementName) {
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams") View cartOptionItem = inflater.inflate(R.layout.cart_option_item, null);

        ((TextView)cartOptionItem.findViewById(R.id.option_name)).setText(optionName);
        ((TextView) cartOptionItem.findViewById(R.id.element_name)).setText(elementName);
        return cartOptionItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ViewGroup.LayoutParams lp = holder.orderPicture.getLayoutParams();
        lp.height = calculatedHeight;
        //defaultWidth = lp.width;
        holder.orderPicture.setLayoutParams(lp);

        String productImageURL = null;
        Map<String, Option> productOptions = new HashMap<>();
        Map<String, Extra> productExtras = new HashMap<>();
        Map<String, Ingredient> productIngredients = new HashMap<>();
        String extrasTitle = null, subExtrasTitle = null, ingredientsTitle = null;

        //means we are inside order history
        OrderItem orderItem = orderMealsList.get(holder.getAdapterPosition());
        if (orderItem != null && orderItem.getMainImage() != null) {
            productImageURL = orderItem.getMainImage().getUrl();
        }
        holder.mealName.setText(orderItem.getName());
        holder.mealPrice.setText(Utils.displayPrice(orderItem.getItemPrice(), true, "FR"));
        holder.mealQuantity.setText(context.getString(R.string.Integer, orderItem.getQuantity()));

        holder.itemUp.setVisibility(View.INVISIBLE);
        holder.itemDown.setVisibility(View.INVISIBLE);

        //Clear options and extras layout if it contains views
        if (holder.optionAndExtrasContainer.getChildCount() > 0) {
            holder.optionAndExtrasContainer.removeAllViews();
        }

        //display Options if exist
        if (orderItem.getOptions() != null) {
            productOptions = orderItem.getOptions();
        }

        //display Extras if exist
        if (orderItem.getExtras() != null) {
            productExtras = orderItem.getExtras();
            extrasTitle = orderItem.getExtrasTitle();
        }

        //check Ingredients exist
        if (orderItem.getIngredients() != null) {
            productIngredients = orderItem.getIngredients();
            ingredientsTitle = orderItem.getIngredientsTitle();
        }

        //display product image
        try {
            Glide.with(context)
                    .load(productImageURL)
                    .apply(Constants.getGlideRequestOptions(Constants.GlideQuality.LOW)
                            .placeholder(R.drawable.loading_img)
                            .fallback(R.drawable.loading_img))
                    .transition(DrawableTransitionOptions.withCrossFade(Constants.TIME_ONE_SECOND))
                    .into(holder.orderPicture);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            Glide.with(context)
                    .load(R.drawable.loading_img)
                    .apply(Constants.getGlideRequestOptions(Constants.GlideQuality.LOW)
                            .placeholder(R.drawable.loading_img)
                            .fallback(R.drawable.loading_img))
                    .into(holder.orderPicture);
        }

        //display Options if exist
        if (productOptions.size() > Constants.ZERO) {
            for (Option option : productOptions.values()) {
                if (option.getElements() != null && option.getElements().size() > Constants.ZERO) {
                    if (option.getElements().size() == Constants.ONE) {
                        Element element = option.getElements().entrySet().iterator().next().getValue();
                        if (element.getExtrasTitle() != null) {
                            subExtrasTitle = element.getExtrasTitle();
                        }
                        if (element.getQuantity() != null && element.getQuantity() >= Constants.ONE) {
                            holder.optionAndExtrasContainer.addView(createChoiceView(option.getName() + " :", context.getString(R.string.extra_quantity, element.getQuantity(), element.getName())));
                        } else {
                            holder.optionAndExtrasContainer.addView(createChoiceView(option.getName() + " :", element.getName()));
                        }
                    } else {
                        StringBuilder elementsNamesList = new StringBuilder();

                        Iterator<Map.Entry<String, Element>> it = option.getElements().entrySet().iterator();
                        while (it.hasNext()) {
                            Element element = it.next().getValue();
                            if (element.getExtrasTitle() != null) {
                                subExtrasTitle = element.getExtrasTitle();
                            }
                            if (element.getQuantity() != null) {
                                elementsNamesList.append(context.getString(R.string.extra_quantity, element.getQuantity(), element.getName()));
                            } else {
                                elementsNamesList.append(element.getName());
                            }
                            if (it.hasNext()) {
                                elementsNamesList.append(", ");
                            }
                        }

                        holder.optionAndExtrasContainer.addView(createChoiceView(option.getName() + " :", elementsNamesList.toString()));
                    }
                } else if (option.getElement() != null) {
                    try {
                        holder.optionAndExtrasContainer.addView(createChoiceView(option.getName() + " :", option.getElement().getName()));
                    } catch (Exception ignored) {
                        holder.optionAndExtrasContainer.addView(createChoiceView(option.getName() + " :", Constants.UNKNOWN));
                    }
                }
            }
        }

        //display Extras if exist
        if (productExtras.size() > Constants.ZERO) {
            StringBuilder extrasList = new StringBuilder(), subExtrasList = new StringBuilder();

            for (Map.Entry<String, Extra> extraEntry : productExtras.entrySet()) {
                Extra extra = extraEntry.getValue();
                if (extra.isSubExtra() != null && extra.isSubExtra()) {
                    if (subExtrasList.length() > Constants.ZERO) {
                        subExtrasList.append(", ");
                    }
                    subExtrasList.append(context.getString(R.string.extra_quantity, extra.getQuantity(), extra.getName()));
                } else {
                    if (extrasList.length() > Constants.ZERO) {
                        extrasList.append(", ");
                    }
                    extrasList.append(context.getString(R.string.extra_quantity, extra.getQuantity(), extra.getName()));
                }
            }

            if (extrasTitle != null || subExtrasTitle != null) {
                if (subExtrasList.length() > Constants.ZERO) {
                    holder.optionAndExtrasContainer.addView(createChoiceView(subExtrasTitle != null ? subExtrasTitle + ": " : context.getString(R.string.supplements) + ": ", subExtrasList.toString()));
                }
                if (extrasList.length() > Constants.ZERO) {
                    holder.optionAndExtrasContainer.addView(createChoiceView(extrasTitle != null ? extrasTitle + ": " : context.getString(R.string.with_extras), extrasList.toString()));
                }
            } else {
                String extrasToDisplay = subExtrasList.length() > Constants.ZERO
                        ? (extrasList.length() > Constants.ZERO ? (subExtrasList.toString() + ", " + extrasList.toString()) : subExtrasList.toString())
                        : (extrasList.length() > Constants.ZERO ? extrasList.toString() : Constants.EMPTY);
                holder.optionAndExtrasContainer.addView(createChoiceView(context.getString(R.string.with_extras), extrasToDisplay));
            }
        }

        if (productIngredients.size() > Constants.ZERO) {
            //StringBuilder ingredientsList = new StringBuilder();
            // Initialize a new SpannableStringBuilder instance
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
            for (Map.Entry<String, Ingredient> ingredientEntry : productIngredients.entrySet()) {
                Ingredient tempIngredient = ingredientEntry.getValue();
                if (ssBuilder.length() > Constants.ZERO) {
                    //ingredientsList.append(", ");
                    ssBuilder.append(", ");
                }
                int start = ssBuilder.length();
                //ingredientsList.append(tempIngredient.getName());
                ssBuilder.append(tempIngredient.getName());
                ssBuilder.setSpan(
                        new StrikethroughSpan(), // Span to add
                        start, // Start of the span (inclusive)
                        ssBuilder.length(), // End of the span (exclusive)
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
                );
                ssBuilder.setSpan(
                        new ForegroundColorSpan(context.getResources().getColor(android.R.color.holo_red_light)), // Span to add
                        start, // Start of the span (inclusive)
                        ssBuilder.length(), // End of the span (exclusive)
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
                );
            }

            if (ssBuilder.length() > Constants.ZERO) {
                if (ingredientsTitle == null) {
                    //ingredientsTitle = context.getString(R.string.concat_two_strings, ingredientsTitle, context.getString(R.string.without_ingredients));
                    ingredientsTitle = context.getString(R.string.without_ingredients);
                }
                holder.optionAndExtrasContainer.addView(createChoiceView(ingredientsTitle + ": ", ssBuilder));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (cartMode.equals(Constants.NEW)) {
            return cart.getItemsList().size();
        } else {
            return orderMealsList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mealName, mealPrice, mealQuantity;
        final LinearLayout itemUp, itemDown, optionAndExtrasContainer;
        final ImageView orderPicture;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mealName = view.findViewById(R.id.order_item_name);
            mealPrice = view.findViewById(R.id.order_item_price);
            mealQuantity = view.findViewById(R.id.order_item_count);
            itemUp = view.findViewById(R.id.order_item_up);
            itemDown = view.findViewById(R.id.order_item_down);
            orderPicture = view.findViewById(R.id.order_item_Pic);
            optionAndExtrasContainer = view.findViewById(R.id.options_extras_container);
        }
    }
}