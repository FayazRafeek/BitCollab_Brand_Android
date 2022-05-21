package com.fyzanz.bitcollab.Model.Utils;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Campaign;
import com.fyzanz.bitcollab.Model.Data.CollabRequest;
import com.fyzanz.bitcollab.Model.Data.Influencer;

import java.util.ArrayList;

public class AppSingleton {

    public static AppSingleton INSTANCE = null;
    public static AppSingleton getInstance(){
        if(INSTANCE == null) INSTANCE = new AppSingleton();
        return INSTANCE;
    }


    //USER TYPE BRAND/INFLUENCER
    String USER_TYPE = "";
    public String getUSER_TYPE() {
        return USER_TYPE;
    }
    public void setUSER_TYPE(String USER_TYPE) {
        this.USER_TYPE = USER_TYPE;
    }


    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ConstraintLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density) * 2);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density) * 2);
        v.startAnimation(a);
    }

//

    //Selected Influencer in screen
    Influencer selectedInfluencer;
    public Influencer getSelectedInfluencer() {
        return selectedInfluencer;
    }
    public void setSelectedInfluencer(Influencer selectedInfluencer) {
        this.selectedInfluencer = selectedInfluencer;
    }
    //
    //Selected brand in screen
    Brand selectedBrand;
    public Brand getSelectedBrand() {
        return selectedBrand;
    }
    public void setSelectedBrand(Brand selectedBrand) {
        this.selectedBrand = selectedBrand;
    }
    //
    //Selected Campaign
    Campaign selectedCampaign;

    public Campaign getSelectedCampaign() {
        return selectedCampaign;
    }

    public void setSelectedCampaign(Campaign selectedCampaign) {
        this.selectedCampaign = selectedCampaign;
    }

    //Selelcted request
    CollabRequest selectedReq;

    public CollabRequest getSelectedReq() {
        return selectedReq;
    }

    public void setSelectedReq(CollabRequest selectedReq) {
        this.selectedReq = selectedReq;
    }
}
