package com.example.piggybank.screen

import android.view.View
import com.example.piggybank.R
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object MainScreen : KScreen<MainScreen>() {

    override val layoutId: Int? = null
    override val viewClass: Class<*>? = null

    val numbers = KTextView { withId(R.id.tv_numbers) }
    val num1 = KTextView{withId(R.id.tv_1)}
    val num5 = KTextView{ withId(R.id.tv_5) }
    val numPlus = KTextView{ withId(R.id.tv_plus) }
    val ivEnter = KImageView{withId(R.id.iv_enter)}
    val tvBalance = KTextView{withId(R.id.tv_balance)}
    val tvStatistic = KTextView{withId(R.id.tv_statistic)}
    val listCategories = KRecyclerView (
        builder = { withId(R.id.list_categories) },
        itemTypeBuilder = { itemType(::CategoryItemScreen) }
    )

    class CategoryItemScreen(matcher: Matcher<View>) : KRecyclerItem<CategoryItemScreen>(matcher) {

        val tvName = KTextView(matcher) { withId(R.id.tv_name) }
        val ivIcon = KImageView(matcher) { withId(R.id.iv_icon) }
    }
}