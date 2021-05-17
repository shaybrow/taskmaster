package com.shaybrow.taskmaster1;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.AnalyticsProperties;
import com.amplifyframework.analytics.UserProfile;
import com.amplifyframework.core.Amplify;

import java.util.Date;

public class AnalyticsTracking {
    private static AnalyticsTracking analyticsTracking;

    public static AnalyticsTracking getAnalyticsTracking (){
        if (analyticsTracking == null){
            analyticsTracking = new AnalyticsTracking();
            Amplify.Analytics.registerGlobalProperties(AnalyticsProperties.builder().add("platform", "shaybrow taskmaster").build());

            if (Amplify.Auth.getCurrentUser() != null){
                String id = Amplify.Auth.getCurrentUser().getUserId();
                com.amplifyframework.analytics.UserProfile user = UserProfile.builder().email(Amplify.Auth.getCurrentUser().getUsername()).build();
                Amplify.Analytics.identifyUser(id, user);
            }
        }
        return analyticsTracking;
    }

    public void timeSpentOnPage(Date start, Date end, String activity){
        long duration = end.getTime() - start.getTime();
        int seconds = (int) (duration/1000);
        Amplify.Analytics.recordEvent(AnalyticsEvent.builder().name("timeSpentOnPage").addProperty("activity", activity).addProperty("duration on activity",seconds).build());

    }
}
