package com.example.roomres;


import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class FrontPageActivityTest {

    @Rule
    public ActivityTestRule<FrontPageActivity> mActivityTestRule = new ActivityTestRule<>(FrontPageActivity.class);

    @Test
    public void frontPageActivityTest() throws InterruptedException{
        onView(withId(R.id.swipingMotion)).perform(swipeLeft());
    }
}
