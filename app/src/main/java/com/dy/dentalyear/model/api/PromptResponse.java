package com.dy.dentalyear.model.api;

import com.google.gson.annotations.SerializedName;

public class PromptResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("date")
    private String date;
    @SerializedName("date_gmt")
    private String date_gmt;
    @SerializedName("status")
    private String status;
    @SerializedName("type")
    private String type;
    @SerializedName("guid")
    private Rendered guid;
    @SerializedName("title")
    private Rendered title;
    @SerializedName("prompt_country")
    private String prompt_country;
    @SerializedName("prompt_date")
    private String prompt_date;
    @SerializedName("todays_fun_holiday_title")
    private String todays_fun_holiday_title;
    @SerializedName("daily_marketing_tip")
    private String daily_marketing_tip;
    @SerializedName("daily_post")
    private String daily_post;
    @SerializedName("how_to_maximize_post")
    private String how_to_maximize_post;
    @SerializedName("weekly_marketing_exercises")
    private String weekly_marketing_exercises;
    @SerializedName("marketing_trends_&_news_for_the_day")
    private String marketing_trends_news_for_the_day;
    @SerializedName("this_date_in_history")
    private String this_date_in_history;
    @SerializedName("industry_events")
    private String industry_events;
    @SerializedName("looking_ahead")
    private String looking_ahead;
    @SerializedName("acf")
    private ACF acf;

    public ACF getAcf() {
        return acf;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDate_gmt() {
        return date_gmt;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public Rendered getGuid() {
        return guid;
    }

    public Rendered getTitle() {
        return title;
    }

    public String getPrompt_country() {
        return prompt_country;
    }

    public String getPrompt_date() {
        return prompt_date;
    }

    public String getTodays_fun_holiday_title() {
        return todays_fun_holiday_title;
    }

    public String getDaily_marketing_tip() {
        return daily_marketing_tip;
    }

    public String getDaily_post() {
        return daily_post;
    }

    public String getHow_to_maximize_post() {
        return how_to_maximize_post;
    }

    public String getWeekly_marketing_exercises() {
        return weekly_marketing_exercises;
    }

    public String getMarketing_trends_news_for_the_day() {
        return marketing_trends_news_for_the_day;
    }

    public String getThis_date_in_history() {
        return this_date_in_history;
    }

    public String getIndustry_events() {
        return industry_events;
    }

    public String getLooking_ahead() {
        return looking_ahead;
    }

    public class Rendered {
        @SerializedName("rendered")
        private String rendered;

        public String getRendered() {
            return rendered;
        }

    }
    public class ACF {
        @SerializedName("prompt_country")
        private String prompt_country;
        @SerializedName("prompt_date")
        private String prompt_date;
        @SerializedName("todays_fun_holiday_title")
        private String todays_fun_holiday_title;
        @SerializedName("how_to_celebrate")
        private String how_to_celebrate;
        @SerializedName("daily_marketing_tip")
        private String daily_marketing_tip;
        @SerializedName("daily_post")
        private String daily_post;
        @SerializedName("how_to_maximize_post")
        private String how_to_maximize_post;
        @SerializedName("weekly_marketing_exercises")
        private String weekly_marketing_exercises;
        @SerializedName("marketing_trends_&_news_for_the_day")
        private String marketing_trends_news_for_the_day;
        @SerializedName("this_date_in_history")
        private String this_date_in_history;
        @SerializedName("industry_events")
        private String industry_events;
        @SerializedName("looking_ahead")
        private String looking_ahead;
        @SerializedName("share_with_team_member")
        private String share_with_team_member;

        public String getPrompt_country() {
            return prompt_country;
        }

        public String getPrompt_date() {
            return prompt_date;
        }

        public String getTodays_fun_holiday_title() {
            return todays_fun_holiday_title;
        }

        public String getHow_to_celebrate() {
            return how_to_celebrate;
        }

        public String getDaily_marketing_tip() {
            return daily_marketing_tip;
        }

        public String getDaily_post() {
            return daily_post;
        }

        public String getHow_to_maximize_post() {
            return how_to_maximize_post;
        }

        public String getWeekly_marketing_exercises() {
            return weekly_marketing_exercises;
        }

        public String getMarketing_trends_news_for_the_day() {
            return marketing_trends_news_for_the_day;
        }

        public String getThis_date_in_history() {
            return this_date_in_history;
        }

        public String getIndustry_events() {
            return industry_events;
        }

        public String getLooking_ahead() {
            return looking_ahead;
        }

        public String getShare_with_team_member() {
            return share_with_team_member;
        }
    }
}
