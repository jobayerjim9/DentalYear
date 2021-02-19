package com.dy.dentalyear.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.apis.ApiClient;
import com.dy.dentalyear.controller.apis.ApiInterface;
import com.dy.dentalyear.controller.helpers.Utils;
import com.dy.dentalyear.databinding.FragmentHomeBinding;
import com.dy.dentalyear.databinding.HomePostLayoutBinding;
import com.dy.dentalyear.model.constant.AppConstants;
import com.dy.dentalyear.model.api.PromptResponse;
import com.dy.dentalyear.ui.activity.SettingsActivity;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nl.invissvenska.modalbottomsheetdialog.Item;
import nl.invissvenska.modalbottomsheetdialog.ModalBottomSheetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dy.dentalyear.controller.helpers.Utils.getMonthForInt;

public class HomeFragment extends Fragment implements ModalBottomSheetDialog.Listener {
    FragmentHomeBinding binding;
    private ArrayList<PromptResponse> promptResponses = new ArrayList<>();
    ModalBottomSheetDialog countryPicker;
    private FragmentManager fragmentManager;
    private String selectedCountry;


    public HomeFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        String countryCheck = Utils.getCountry(requireContext());

        if (countryCheck != null && promptResponses.size() > 0) {
            selectedCountry = countryCheck;
            setupCountry(selectedCountry);
        }

    }

    private void getHomeData(Date date) {

        ImageView menuButton = binding.toolbar.findViewById(R.id.menuButton);
        PopupMenu popupMenu = new PopupMenu(requireContext(), menuButton);
        popupMenu.getMenuInflater().inflate(R.menu.home_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(requireActivity(), SettingsActivity.class));
                return true;
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();

            }
        });

        binding.setLoading(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String today = formatter.format(date);
        ApiInterface apiInterface = ApiClient.getClient(requireContext()).create(ApiInterface.class);
        Call<ArrayList<PromptResponse>> call = apiInterface.getPromptByDate("prompt_date", today);
        call.enqueue(new Callback<ArrayList<PromptResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<PromptResponse>> call, Response<ArrayList<PromptResponse>> response) {
                promptResponses.clear();
                binding.setLoading(false);
                promptResponses=response.body();
                if (promptResponses!=null && promptResponses.size()>0) {
                    if (selectedCountry != null) {
                        for (PromptResponse prompt : promptResponses) {
                            if (prompt.getAcf().getPrompt_country().toLowerCase().trim().equals(selectedCountry.toLowerCase().trim())) {
                                binding.setData(prompt);
                                break;
                            }
                        }
                    } else {
                        PromptResponse data = promptResponses.get(0);
                        binding.setData(data);
                    }

                    //  Log.d("promptResp",binding.getData().getAcf().getTodays_fun_holiday_title());
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date date = format.parse(binding.getData().getAcf().getPrompt_date());
                        String placeHolder = getMonthForInt(date.getMonth()) + " " + date.getDate() + "\n" + DateFormat.format("EEEE", date);
                        binding.promptDate.setText(placeHolder);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    setupPosts();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PromptResponse>> call, Throwable t) {
                binding.setLoading(false);
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initView() {
        selectedCountry = Utils.getCountry(requireContext());
        getHomeData(new Date());
        Calendar calendar = Calendar.getInstance();
        binding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date=new Date(year-1900,month,dayOfMonth);
                        getHomeData(date);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        countryPicker= new ModalBottomSheetDialog.Builder()
                .setHeader("Choose a country!")
                .add(R.menu.country)
                .build();
        binding.countryPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.show(getChildFragmentManager(), "countryPicker");
            }
        });
    }

    @Override
    public void onItemSelected(String tag, Item item) {
        selectedCountry = item.getTitle().toString();
        setupCountry(selectedCountry);

        if (countryPicker.isVisible()) {
            countryPicker.dismiss();
        }
    }

    private void setupCountry(String selectedCountry) {
        binding.setLoading(true);
        for (PromptResponse prompt : promptResponses) {
            if (prompt.getAcf().getPrompt_country().toLowerCase().trim().equals(selectedCountry.toLowerCase().trim())) {
                binding.setData(prompt);
                break;
            }
        }
        setupPosts();
        binding.setLoading(false);
    }

    private void setupPosts() {

        if (binding.getData().getAcf().getPrompt_country().trim().equals(AppConstants.AUSTRALIA)) {
            binding.countryPicker.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_australia));
        } else if (binding.getData().getAcf().getPrompt_country().trim().equals(AppConstants.CANADA)) {
            binding.countryPicker.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_canada));
        } else if (binding.getData().getAcf().getPrompt_country().trim().equals(AppConstants.USA)) {
            binding.countryPicker.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_united_states));
        }
        //Configured 1st Post
        HomePostLayoutBinding post1=binding.post1;
        post1.base.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.post1));
        post1.setTitle("How To \n" +
                "Celebrate?");
        post1.setOpen(false);
        post1.postText.setText(Html.fromHtml(binding.getData().getAcf().getHow_to_celebrate()));
        post1.descriptionImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.post_description_1));
        post1.dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post1.getOpen()) {
                    openPost(post1);
                }
                else {
                    closePost(post1);
                }
                post1.setOpen(!post1.getOpen());

            }
        });

        //Configured 2nd Post
        HomePostLayoutBinding post2=binding.post2;
        post2.base.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.post2));
        post2.setTitle("Daily Marketing\n" +
                "Tip");
        post2.base.setElevation((float)-1.0);
        post2.setOpen(false);
        post2.postText.setText(Html.fromHtml(binding.getData().getAcf().getDaily_marketing_tip()));
        post2.tinyImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_post_daily_marketing));
        post2.descriptionImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.daily_marketing_tip_description));
        post2.dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post2.getOpen()) {
                    openPost(post2);
                }
                else {
                    closePost(post2);
                }
                post2.setOpen(!post2.getOpen());

            }
        });


        //Configured 3rd Post
        HomePostLayoutBinding post3=binding.post3;
        post3.base.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.post3));
        post3.setTitle("Daily Post");
        post3.base.setElevation((float)-1.1);
        post3.setOpen(false);
        post3.postText.setText(Html.fromHtml(binding.getData().getAcf().getDaily_post()));
        post3.tinyImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_post_daily_post));
        post3.descriptionImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.daily_post_description));
        post3.dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post3.getOpen()) {
                    openPost(post3);
                }
                else {
                    closePost(post3);
                }
                post3.setOpen(!post3.getOpen());

            }
        });

        //Configured 4th Post
        HomePostLayoutBinding post4=binding.post4;
        post4.base.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.post4));
        post4.setTitle("How To \n" +
                "Maximize Post?");
        post4.base.setElevation((float)-1.2);
        post4.setOpen(false);
        post4.postText.setText(Html.fromHtml(binding.getData().getAcf().getHow_to_maximize_post()));
        post4.tinyImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_post_maximize_post));
        post4.descriptionImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.maximize_post_description));
        post4.dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post4.getOpen()) {
                    openPost(post4);
                }
                else {
                    closePost(post4);
                }
                post4.setOpen(!post4.getOpen());

            }
        });


        //Configured 5th Post
        HomePostLayoutBinding post5=binding.post5;
        post5.base.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.post5));
        post5.setTitle("Weekly Marketing\nExercise");
        post5.base.setElevation((float)-1.3);
        post5.setOpen(false);
        post5.postText.setText(Html.fromHtml(binding.getData().getAcf().getWeekly_marketing_exercises()));
        post5.tinyImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_post_weekly_marketing));
        post5.descriptionImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.weekly_marketing_description));
        post5.dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post5.getOpen()) {
                    openPost(post5);
                }
                else {
                    closePost(post5);
                }
                post5.setOpen(!post5.getOpen());

            }
        });

        //Configured 6th Post
        HomePostLayoutBinding post6=binding.post6;
        post6.base.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.post6));
        post6.setTitle("Marketing Trends\n" +
                "& News");
        post6.base.setElevation((float)-1.4);
        post6.setOpen(false);
        post6.postText.setText(Html.fromHtml(binding.getData().getAcf().getMarketing_trends_news_for_the_day()));
        post6.tinyImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_post_marketing_trends));
        post6.descriptionImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.marketing_trends_description));
        post6.dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post6.getOpen()) {
                    openPost(post6);
                }
                else {
                    closePost(post6);
                }
                post6.setOpen(!post6.getOpen());

            }
        });

        //Configured 7th Post
        HomePostLayoutBinding post7=binding.post7;
        post7.base.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.post7));
        post7.setTitle("This Date \n" +
                "In History");
        post7.base.setElevation((float)-1.5);
        post7.setOpen(false);
        post7.postText.setText(Html.fromHtml(binding.getData().getAcf().getThis_date_in_history()));
        post7.tinyImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_post_history));
        post7.descriptionImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.this_day_description));
        post7.dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post7.getOpen()) {
                    openPost(post7);
                }
                else {
                    closePost(post7);
                }
                post7.setOpen(!post7.getOpen());

            }
        });

        //Configured 8th Post
        HomePostLayoutBinding post8=binding.post8;
        post8.base.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.post8));
        post8.setTitle("Industry\n" +
                "Events");
        post8.base.setElevation((float)-1.6);
        post8.setOpen(false);
        post8.postText.setText(Html.fromHtml(binding.getData().getAcf().getIndustry_events()));
        post8.tinyImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_post_industry_event));
        post8.descriptionImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.industry_event_description));
        post8.dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post8.getOpen()) {
                    openPost(post8);
                }
                else {
                    closePost(post8);
                }
                post8.setOpen(!post8.getOpen());

            }
        });

        //Configured 9th Post
        HomePostLayoutBinding post9=binding.post9;
        post9.base.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),R.color.post9));
        post9.setTitle("Looking \n" +
                "Ahead");
        post9.base.setElevation((float)-1.7);
        post9.setOpen(false);
        post9.postText.setText(Html.fromHtml(binding.getData().getAcf().getLooking_ahead()));
        post9.tinyImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_post_ahead));
        post9.descriptionImage.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.looking_ahead_description));
        post9.dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post9.getOpen()) {
                    openPost(post9);
                }
                else {
                    closePost(post9);
                }
                post9.setOpen(!post9.getOpen());

            }
        });

    }
    private void openPost(HomePostLayoutBinding post1) {
        post1.dropdownButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_up_arrow));
        post1.tinyImage.animate().rotationY(post1.getRoot().getHeight()).alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                post1.tinyImage.clearAnimation();
                post1.tinyImage.setVisibility(View.GONE);
            }
        });
        post1.postText.setVisibility(View.VISIBLE);
        post1.postText.animate().alpha(1.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                post1.postText.clearAnimation();
                post1.postText.setVisibility(View.VISIBLE);
            }
        });
        post1.descriptionImage.setVisibility(View.VISIBLE);
        post1.descriptionImage.animate().rotationY(0).alpha(1.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                post1.descriptionImage.clearAnimation();
                post1.descriptionImage.setVisibility(View.VISIBLE);
            }
        });
    }
    private void closePost(HomePostLayoutBinding post1) {
        post1.dropdownButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_down_arrow));
        post1.tinyImage.setVisibility(View.VISIBLE);
        post1.tinyImage.animate().rotationY(0).alpha(1.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                post1.tinyImage.clearAnimation();
                post1.tinyImage.setVisibility(View.VISIBLE);
            }
        });
        post1.postText.animate().alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                post1.postText.clearAnimation();
                post1.postText.setVisibility(View.GONE);
            }
        });
        post1.descriptionImage.animate().rotationY(post1.getRoot().getHeight()).alpha(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                post1.descriptionImage.clearAnimation();
                post1.descriptionImage.setVisibility(View.GONE);
            }
        });

    }




}