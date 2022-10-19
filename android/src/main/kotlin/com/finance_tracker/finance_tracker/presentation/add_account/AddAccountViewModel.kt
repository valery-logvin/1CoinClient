package com.finance_tracker.finance_tracker.presentation.add_account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finance_tracker.finance_tracker.R
import com.finance_tracker.finance_tracker.core.common.EventFlow
import com.finance_tracker.finance_tracker.core.common.toHexString
import com.finance_tracker.finance_tracker.data.repositories.AccountsRepository
import com.finance_tracker.finance_tracker.domain.models.Account
import com.finance_tracker.finance_tracker.domain.models.AccountColorData
import com.finance_tracker.finance_tracker.domain.models.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

private val mockAccountTypes = listOf(
    "Debit card",
    "Credit card",
    "Cash"
) // TODO: Удалить моковые данные

private val mockAmountCurrencies = listOf(
    Currency(name = "USD", sign = "\$"),
    Currency(name = "EURO", sign = "€"),
    Currency(name = "GBR", sign = "£")
) // TODO: Удалить моковые данные

@KoinViewModel
class AddAccountViewModel(
    private val accountsRepository: AccountsRepository
): ViewModel() {

    private val _amountCurrencies = MutableStateFlow(mockAmountCurrencies)
    val amountCurrencies = _amountCurrencies.asStateFlow()

    private val _types = MutableStateFlow(mockAccountTypes)
    val types = _types.asStateFlow()

    private val _colors = MutableStateFlow(emptyList<AccountColorData>())
    val colors = _colors.asStateFlow()

    private val _selectedColor = MutableStateFlow<AccountColorData?>(null)
    val selectedColor = _selectedColor.asStateFlow()

    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType = _selectedType.asStateFlow()

    private val _selectedCurrency = MutableStateFlow(amountCurrencies.value.first())
    val selectedCurrency = _selectedCurrency.asStateFlow()

    private val _enteredAccountName = MutableStateFlow("")
    val enteredAccountName = _enteredAccountName.asStateFlow()

    private val _enteredAmount = MutableStateFlow("")
    val enteredAmount = _enteredAmount.asStateFlow()

    val events = EventFlow<AddAccountEvent?>()

    val isAddButtonEnabled = combine(enteredAccountName, selectedType, selectedColor, enteredAmount) {
            accountName, selectedType, selectedColor, enteredAmount ->
        accountName.isNotBlank() && selectedType != null &&
                selectedColor != null && enteredAmount.isNotBlank()
    }.stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = false)

    init {
        loadAccountColors()
    }

    private fun loadAccountColors() {
        viewModelScope.launch {
            _colors.value = accountsRepository.getAllAccountColors()
        }
    }

    fun onAccountNameChange(accountName: String) {
        _enteredAccountName.value = accountName
    }

    fun onAccountTypeSelect(accountType: String) {
        _selectedType.value = accountType
    }

    fun onCurrencySelect(currency: Currency) {
        _selectedCurrency.value = currency
    }

    fun onAccountColorSelect(accountColor: AccountColorData) {
        _selectedColor.value = accountColor
    }

    fun onAmountChange(amount: String) {
        _enteredAmount.value = amount
    }

    fun onAddAccountClick() {
        viewModelScope.launch {
            val accountName = enteredAccountName.value.takeIf { it.isNotBlank() } ?: run {
                events.emit(AddAccountEvent.ShowToast(
                    text = R.string.new_account_error_enter_account_name
                ))
                return@launch
            }
            val selectedColor = selectedColor.value?.color ?: run {
                events.emit(AddAccountEvent.ShowToast(
                    text = R.string.new_account_error_select_account_color
                ))
                return@launch
            }
            val balance = enteredAmount.value.toDoubleOrNull() ?: run {
                events.emit(AddAccountEvent.ShowToast(
                    text = R.string.new_account_error_enter_account_balance
                ))
                return@launch
            }
            val type = selectedType.value ?: run {
                events.emit(AddAccountEvent.ShowToast(
                    text = R.string.new_account_error_select_account_type
                ))
            }
            accountsRepository.insertAccount(
                accountName = accountName,
                balance = balance,
                colorHex = selectedColor.toHexString(),
                type = type as Account.Type
            )
            events.emit(AddAccountEvent.Close)
        }
    }
}