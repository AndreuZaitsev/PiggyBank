package com.example.piggybank.screen

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.piggybank.R
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object DailyStatScreen : KScreen<DailyStatScreen>() {

    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val rvExpenses = KRecyclerView(
        builder = {withId(R.id.rv_expenses)},
        itemTypeBuilder = {itemType(::ItemExpensesScreen)}
    )

    class ItemExpensesScreen(matcher: Matcher<View>) : KRecyclerItem<ItemExpensesScreen>(matcher) {

        val tvExpensesName = KTextView { withId(R.id.tv_expense_name) }
        val tvExpensesValue = KTextView { withId(R.id.tv_expense_value) }
        val tvData = KTextView{withId(R.id.tv_date_expense)}
    }
}