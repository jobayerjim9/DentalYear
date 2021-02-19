package com.dy.dentalyear.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.helpers.Utils;
import com.dy.dentalyear.databinding.ActivitySettingsBinding;
import com.dy.dentalyear.model.constant.AppConstants;

import nl.invissvenska.modalbottomsheetdialog.Item;
import nl.invissvenska.modalbottomsheetdialog.ModalBottomSheetDialog;

public class SettingsActivity extends AppCompatActivity implements ModalBottomSheetDialog.Listener {
    ActivitySettingsBinding binding;
    ModalBottomSheetDialog countryPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initView();
    }

    private void initView() {
        String country = Utils.getCountry(this);
        binding.notificationSwitch.setChecked(Utils.getNotificationStatus(this));
        if (country != null) {
            binding.countryText.setText(country);
        }
        countryPicker = new ModalBottomSheetDialog.Builder()
                .setHeader("Choose a country!")
                .add(R.menu.country)
                .build();
        binding.pickCountry.setOnClickListener(v -> countryPicker.show(getSupportFragmentManager(), "countryPicker"));
        binding.back.setOnClickListener(v -> finish());
        binding.notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Utils.setNotification(SettingsActivity.this, isChecked);
                Toast.makeText(SettingsActivity.this, "Notification Settings Updated!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.shareItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This Dental Year App Enjoy!\n" + AppConstants.PLAY_STORE_URL);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
        binding.feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.PLAY_STORE_URL));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    public void onItemSelected(String tag, Item item) {
        Utils.saveCountry(SettingsActivity.this, item.getTitle().toString().trim());
        if (countryPicker.isVisible()) {
            countryPicker.dismiss();
        }
        binding.countryText.setText(item.getTitle().toString().trim());
        Toast.makeText(this, "Country Settings Saved", Toast.LENGTH_SHORT).show();
    }
}