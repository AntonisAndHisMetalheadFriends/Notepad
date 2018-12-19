package aahmf.notepad;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.*;

public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityTestRule = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);
    private RegisterActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void RegisterUser() {
        onView(withId(R.id.etRealName)).perform(typeText( "Nikos Korompos"));
        onView(withId(R.id.etUserName)).perform(typeText("NikKoro"));
        onView(withId(R.id.etEmail)).perform(typeText("nikoskoromposnotepad@gmail.com"));
        onView(withId(R.id.etUserpass)).perform(typeText("123456"));
        onView(withId(R.id.etRetypepass)).perform(typeText("123456"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnRegister)).perform(click());


    }




    @Test
    public void checkuserexistance() {
    }

    @Test
    public void onStart() {

    }
}