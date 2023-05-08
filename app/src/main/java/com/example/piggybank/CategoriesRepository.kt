package com.example.piggybank

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesRepository(
    private val categoryDao: CategoryDao
) {

    private val initialCategories = listOf(
        Category("car", R.drawable.ic_car, 1),
        Category("pet", R.drawable.ic_pet, 2),
        Category("alcohol", R.drawable.ic_alcohole, 3),
        Category("clothes", R.drawable.ic_clothes, 4),
        Category("food", R.drawable.ic_food, 5)
    )

    init {
        prepopulate()
    }

    private fun prepopulate() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            categoryDao.insertOrIgnore(*initialCategories.toTypedArray())
        }
    }

   suspend fun getCategories(): List<Category>{
       return withContext(Dispatchers.IO) {
           categoryDao.getAll()
       }
    }

    suspend fun addCategory(){
        withContext(Dispatchers.IO){
            categoryDao.insertAll(Category("cooking", R.drawable.ic_cooking))
        }
    }

}