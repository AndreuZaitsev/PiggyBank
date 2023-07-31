package com.example.piggybank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.piggybank.adapters.EditIncomeAdapter.EditIncomeItem
import com.example.piggybank.application.DataBaseHolder
import com.example.piggybank.dao.IncomeEntity
import com.example.piggybank.repository.IncomeRepository
import com.example.piggybank.uistates.EditIncomeUiState
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditIncomeViewModel : ViewModel() {

    private val _incomeState = MutableStateFlow(EditIncomeUiState())
    val incomeState: StateFlow<EditIncomeUiState> = _incomeState.asStateFlow()

    private val _navigateToExpensesStatFragmentEvent = MutableSharedFlow<Unit>()
    val navigateToExpensesStatFragmentEvent: SharedFlow<Unit> = _navigateToExpensesStatFragmentEvent.asSharedFlow()

    private val repository = IncomeRepository(DataBaseHolder.dataBase.incomeDao())

    private var deletedIncome: EditIncomeItem? = null

    fun showIncomes() {
        viewModelScope.launch {
            _incomeState.update { currentState ->
                currentState.copy(
                    incomes = loadIncomes()
                )
            }
        }
    }

    fun onDelete(id: Int) {
        viewModelScope.launch {
            deletedIncome = incomeState.value.incomes.find { it.id == id }
            repository.deleteIncome(id)
            showIncomes()
        }
    }

    fun onUndo() {
        viewModelScope.launch {
            deletedIncome?.let {
                repository.saveIncomeValue(it.toEntity()) // map IncomeEntity
                showIncomes()
                deletedIncome = null
            }
        }
    }

    fun onStatisticsClicked(){
        viewModelScope.launch {
            _navigateToExpensesStatFragmentEvent.emit(Unit)
        }
    }

    private suspend fun loadIncomes(): List<EditIncomeItem> = repository.getIncomes()
        .map { it.toItem() }

    private fun EditIncomeItem.toEntity(): IncomeEntity {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val timeMillis = dateFormat.parse(this.date)?.time ?: 0L
        return IncomeEntity(timeMillis, this.incomeValue, this.id, this.name)
    }

    private fun IncomeEntity.toItem(): EditIncomeItem {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val uiDate = dateFormat.format(this.timeInMillis)
        return EditIncomeItem(this.id, this.name, uiDate, this.incomeValue)
    }
}
