package com.jonasdevrient.citypinboard

import android.content.Context
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.jonasdevrient.citypinboard.authentication.LoginFragment
import com.jonasdevrient.citypinboard.authentication.RegistreerFragment
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
/**
 * This MainActivityTest tests the LoginFragment, RegistreerFragment and GeenInternetFragment
 * This is because all these above fragments are used in the MainActivityT
 */
class MainActivityTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    private lateinit var activity: MainActivity
    private lateinit var loginFragment: LoginFragment
    private lateinit var registreerFragment: RegistreerFragment
    private lateinit var context: Context

    @Before
    fun init() {
        context = getInstrumentation().targetContext

        activity = activityRule.activity
        loginFragment = activity.loginFragment
        registreerFragment = RegistreerFragment()
    }

    @Test
    fun clickRegisterButton_HidesLoginFragment() {
        Espresso.onView(ViewMatchers.withId(R.id.registreer_button)).perform(ViewActions.click())

        Assert.assertFalse(loginFragment.isResumed)
    }

    @Test
    fun clickBackButtonOnRegisterFragment_HidesRegisterFragment() {
        Espresso.onView(ViewMatchers.withId(R.id.registreer_button)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.back_button)).perform(ViewActions.click())

        Assert.assertFalse(registreerFragment.isResumed)
    }

}



