package aahmf.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.DisableOnAndroidDebug;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class NewNoteActivityTest {



    @Rule
    public ActivityTestRule<NewNoteActivity> mActivityTestRule = new ActivityTestRule<NewNoteActivity>(NewNoteActivity.class);
    private NewNoteActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        Intents.init();
    }

    @Test
    public void createAndDeleteNote(){
        onView(withId(R.id.etWriteNote)).perform(typeText("sample_text"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.etKeywords)).perform(typeText("sample_keywords"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save)).perform(click());
        onView(withId(R.integer.title_id)).inRoot(isDialog()).check(matches(isDisplayed())).perform(typeText("notefortest"));
        Espresso.closeSoftKeyboard();
        String noteTitle = getText(withId(R.integer.title_id));
        Looper.prepare();
        mActivity.WriteXml(noteTitle);
        //onView(withText("Save")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        intended(hasComponent(MainMenuActivity.class.getName()));

        File testnote = new File("/data/user/0/aahmf.notepad/files","notefortest");
        boolean check;
        if (testnote.exists()){
            check = true;
        }
        else{
            check = false;
        }

        assertTrue(check);
        testnote.delete();

        if (testnote.exists()){
            check = true;
        }
        else{
            check = false;
        }
        assertFalse(check);

    }

    @Test
    public void directoryChooserTest(){
        onView(withId(R.id.etWriteNote)).perform(typeText("Sample_text"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnExport)).perform(click());
        onView(withText("Choose directory")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        Espresso.pressBack();
        Espresso.pressBack();
        onView(withText("OK")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        String expected = "/storage/emulated/0/Android/data";
        assertEquals(mActivity.exportPath,expected);

    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(EditText.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                EditText tv = (EditText)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }



}

