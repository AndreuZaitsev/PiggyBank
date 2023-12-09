package com.example.piggybank.matchers

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class BackgroundMatcher(
    private val targetContext: Context,
    @DrawableRes private val resId: Int?,
) : TypeSafeMatcher<View>() {

    override fun matchesSafely(target: View): Boolean {
        val drawable: Drawable? = target.background
        val resources: Resources = target.context.resources
        val expectedDrawable: Drawable? = resId?.let { resources.getDrawable(resId, targetContext.theme) }

        return expectedDrawable?.constantState == drawable?.constantState
    }

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: $resId")
        resId?.let {
            targetContext.resources.getResourceEntryName(it)?.let { description.appendText("[$it]") }
        }
    }
}