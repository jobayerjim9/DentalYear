package com.dy.dentalyear.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.helpers.Utils;
import com.dy.dentalyear.databinding.ActivityPurchaseBinding;

import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.CarouselOnScrollListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    ActivityPurchaseBinding binding;
    int selectedPlan;
    private BillingClient billingClient;
    List<SkuDetails> skuDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase);
        ArrayList<CarouselItem> carouselItems = new ArrayList<>();
        carouselItems.add(new CarouselItem(R.drawable.monthly_plan, ""));
        carouselItems.add(new CarouselItem(R.drawable.annual_plan, ""));
        binding.carousel.addData(carouselItems);
        binding.carousel.setOnScrollListener(new CarouselOnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int i, int i1, @Nullable CarouselItem carouselItem) {
                Log.d("onScrolled", i1 + "");
                selectedPlan = i1;
            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int i, int i1) {

            }
        });
        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                    List<String> skuList = new ArrayList<>();
                    skuList.add("com.dentalyear.monthly30free");
                    skuList.add("com.dyear.dentalyear.yr365");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,
                                                                 List<SkuDetails> skuDetailsList) {
                                    // Process the result.
                                    PurchaseActivity.this.skuDetailsList = skuDetailsList;
                                }
                            });
                    billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, new PurchaseHistoryResponseListener() {
                        @Override
                        public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @androidx.annotation.Nullable List<PurchaseHistoryRecord> list) {

                        }
                    });

                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

        binding.subsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(selectedPlan))
                        .build();
                int responseCode = billingClient.launchBillingFlow(PurchaseActivity.this, billingFlowParams).getResponseCode();

            }
        });
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @androidx.annotation.Nullable List<Purchase> list) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && list != null) {
            for (Purchase purchase : list) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Toast.makeText(this, "Purchase Cancelled", Toast.LENGTH_SHORT).show();
        } else {
            // Handle any other error codes.
        }
    }

    void handlePurchase(Purchase purchase) {
        // Purchase retrieved from BillingClient#queryPurchases or your PurchasesUpdatedListener.

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                    Utils.subscribe(PurchaseActivity.this, true);
                    startActivity(new Intent(PurchaseActivity.this, MainActivity.class));
                    finish();
                }
            }
        };

        billingClient.consumeAsync(consumeParams, listener);
    }

}