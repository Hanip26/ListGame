package com.example.listgame.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listgame.model.api.ApiPackage
import com.example.listgame.network.PackageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PackageViewModel : ViewModel() {

    private val repository = PackageRepository()

    private val _packages =
        MutableStateFlow<List<ApiPackage>>(emptyList())

    val packages: StateFlow<List<ApiPackage>>
        get() = _packages

    fun loadPackages(gameId: Int) {

        viewModelScope.launch {

            val response =
                repository.getPackages(gameId)

            if (response.isSuccessful) {

                _packages.value =
                    response.body()?.packages
                        ?: emptyList()
            }
        }
    }
}