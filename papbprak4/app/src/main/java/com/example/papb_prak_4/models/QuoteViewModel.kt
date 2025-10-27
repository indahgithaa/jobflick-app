package com.example.papb_prak_4.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.papb_prak_4.repositories.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {
    private val repository = QuoteRepository()

    private val _quote = MutableStateFlow("Tap the button to get a quote")
    val quote: StateFlow<String> get() = _quote

    fun fetchQuote() {
        viewModelScope.launch {
            try {
                _quote.value = "Loading..."

                // Simulasi error buatan (misalnya error koneksi atau null)
                val result = repository.getRandomQuote()
                if (result.isEmpty()) {
                    throw Exception("Data kosong dari server!") // contoh error manual
                }

                // Contoh error lain: pembagian dengan nol
                val x = 10 / 0

                _quote.value = result
            } catch (e: Exception) {
                _quote.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }




}
