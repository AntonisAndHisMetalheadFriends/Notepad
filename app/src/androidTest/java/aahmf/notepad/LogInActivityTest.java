package aahmf.notepad;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
///import android.support.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

public class LogInActivityTest {

    @Rule
    public ActivityTestRule<LogInActivity> mActivityTestRule = new ActivityTestRule<LogInActivity>(LogInActivity.class);
    private LogInActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }



    @Test
    public void logInWithCorrectUser() {
        onView(withId(R.id.etName)).perform(typeText("nickspo93@gmail.com"));
        onView(withId(R.id.etPsswrd)).perform(typeText("123456"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnLogIn)).perform(click());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assertNotNull(user);
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void logInWithWrong() {
        onView(withId(R.id.etName)).perform(typeText("nickspo93@false.com"));
        onView(withId(R.id.etPsswrd)).perform(typeText("132456"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnLogIn)).perform(click());
    }


    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}