package com.example.android.baker;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BakerInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.android.baker", appContext.getPackageName());
    }

    /**
     * Tests if MainActivity is loaded with Recipe Name information
     *
     * @throws Exception
     */
    @Test
    public void bakerRecipesListValidation() throws Exception {
        RecyclerViewActions.actionOnItem(hasDescendant(withText(containsString("Nutella Pie"))), scrollTo());
        RecyclerViewActions.actionOnItem(hasDescendant(withText(containsString("Cheesecake"))), scrollTo());
    }


    /**
     * Tests the component of all screens in Baker app
     *
     * @throws Exception
     */
    @Test
    public void navigateToDetailActivityAndRecipeStepActivity() throws Exception {
        RecyclerViewActions.actionOnItem(hasDescendant(withText(containsString("Nutella Pie"))), scrollTo());
        onView(withText("Nutella Pie")).perform(click());

        //Adding sleep statement as workaround. The right implementation is to handle these delays via idling resources
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Ingredients")).check(matches(isDisplayed()));
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
        RecyclerViewActions.actionOnItem(hasDescendant(withText(containsString("Recipe Introduction"))), scrollTo());
        onView(withText("Recipe Introduction")).perform(click());
        //Adding sleep statement as workaround. The right implementation is to handle these delays via idling resources
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("Instructions")).check(matches(isDisplayed()));
    }
}
