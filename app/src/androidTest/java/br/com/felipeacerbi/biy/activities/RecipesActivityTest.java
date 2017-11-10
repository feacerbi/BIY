package br.com.felipeacerbi.biy.activities;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.felipeacerbi.biy.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    private CountingIdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void recipeMobileStepsTest() {

        onView(withId(R.id.recipes_list_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.recipes_list_fragment)).perform(RecyclerViewActions.scrollToPosition(0));

        onView(withId(R.id.recipes_list_fragment)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.btn_start_recipe)));

        for(int i = 0; i < 10; i++) {
            onView(withId(R.id.fab_next)).check(matches(isDisplayed()));
            onView(withId(R.id.fab_next)).perform(click());
        }

        onView(allOf(withId(android.R.id.button1), withText("Yes"))).check(matches(isDisplayed()));
        onView(allOf(withId(android.R.id.button1), withText("Yes"))).perform(click());

        onView(withId(R.id.recipes_list_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void recipeShowIngredientsTest() {
        onView(withId(R.id.recipes_list_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.recipes_list_fragment)).perform(RecyclerViewActions.scrollToPosition(0));

        onView(withId(R.id.recipes_list_fragment)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.btn_show_ingredients)));

        onView(withId(R.id.rv_ingredients_list)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}
