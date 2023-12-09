package com.example.piggybank.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.piggybank.matchers.BackgroundMatcher
import io.github.kakaocup.kakao.common.assertions.BaseAssertions
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

val KTextView.text: String?
    get() {
        var stringHolder: String? = null
        view.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isAssignableFrom(TextView::class.java)

            override fun getDescription(): String = "getting text from a TextView"

            override fun perform(uiController: UiController?, view: View) {
                stringHolder = (view as TextView).text.toString()
            }
        })
        return stringHolder
    }

fun BaseAssertions.hasBackground(@DrawableRes resId: Int, context: Context) {
    view.check(ViewAssertions.matches(BackgroundMatcher(context, resId)))
}

fun BaseAssertions.hasNoBackground(context: Context) {
    view.check(ViewAssertions.matches(BackgroundMatcher(context, resId = null)))
}