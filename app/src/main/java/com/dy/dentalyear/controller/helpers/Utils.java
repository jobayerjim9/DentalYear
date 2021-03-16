package com.dy.dentalyear.controller.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.dy.dentalyear.R;
import com.dy.dentalyear.model.constant.AppConstants;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;

public class Utils {
    public static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    public static void saveCountry(Context context, String country) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(AppConstants.COUNTRY_KEY, country).apply();
    }

    public static void subscribe(Context context, boolean purchase) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(AppConstants.SUBS_KEY, purchase).apply();
    }

    public static boolean isSubscribe(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(AppConstants.SUBS_KEY, false);
    }

    public static void setNotification(Context context, boolean isOn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(AppConstants.NOTIFICATION_KEY, isOn).apply();
    }

    public static String getCountry(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AppConstants.COUNTRY_KEY, AppConstants.USA);
    }

    public static boolean getNotificationStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(AppConstants.NOTIFICATION_KEY, false);
    }

    @BindingAdapter("strokeWidth")
    public static void setStrokeWidth(MaterialCardView cardView, boolean selected) {
        if (selected) {
            cardView.setStrokeWidth(6);
        } else {
            cardView.setStrokeWidth(0);
        }
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        if (url == null) {
            Picasso.get().load(R.drawable.ic_logo_small).into(imageView);
        } else {
            Picasso.get().load(url).placeholder(R.drawable.ic_logo_small).into(imageView);
        }

    }


}
