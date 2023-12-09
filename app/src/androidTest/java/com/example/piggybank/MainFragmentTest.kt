package com.example.piggybank

import androidx.test.ext.junit.rules.activityScenarioRule
import com.example.piggybank.activity.MainActivity
import com.example.piggybank.screen.MainScreen
import com.example.piggybank.utils.hasBackground
import com.example.piggybank.utils.hasNoBackground
import com.example.piggybank.utils.text
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class MainFragmentTest : TestCase() {

    @get: Rule
    val activityRule = activityScenarioRule<MainActivity>()

    @Test
    fun addingExpensiveSuccess() = run {
        step("Open target screen") {
            MainScreen {
                tvBalance.hasAnyText()

                numbers {
                    isVisible()
                    hasEmptyText()
                }
                ivEnter {
                    isVisible()
                    isClickable()
                }
                flakySafely {
                    Assert.assertEquals(6, listCategories.getSize())
                }
            }
        }
        step("Check category items") {
            MainScreen {
                listCategories {
                    children<MainScreen.CategoryItemScreen> {
                        isVisible()
                        isClickable()
                    }
                }
            }
        }
        step("Check first category") {
            MainScreen {
                listCategories {
                    childAt<MainScreen.CategoryItemScreen>(0) {
                        tvName.hasText("car")
                        ivIcon.hasDrawable(R.drawable.ic_car)
                        ivIcon.hasNoBackground(device.targetContext)
                    }
                }
            }
        }
        step("Select first category") {
            MainScreen {
                listCategories {
                    firstChild<MainScreen.CategoryItemScreen> {
                        click()
                        ivIcon.hasBackground(R.drawable.bg_checked, device.targetContext)
                    }
                }
            }
        }
        step("Enter expense value") {
            MainScreen {
                num1.click()
                numPlus.click()
                num5.click()
            }
        }

        step("Expenses of the first category added successfully") {
            MainScreen {
                numbers.hasText("1+5")
                val previousBalance = tvBalance.text.orEmpty().toDouble()
                ivEnter.click()
                numbers.hasEmptyText()
                listCategories {
                    firstChild<MainScreen.CategoryItemScreen> {
                        ivIcon.hasNoBackground(device.targetContext)
                    }
                }
                val newBalance = previousBalance - 6.0
                tvBalance.hasText(newBalance.toString())
            }
        }
    }
}
