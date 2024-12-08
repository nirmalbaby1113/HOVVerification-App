package com.nirmal.baby.hovverification.features.home.presenter

import com.nirmal.baby.hovverification.features.home.interactor.HomeInteractor
import com.nirmal.baby.hovverification.features.home.view.HomeViewInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePresenter(
    private val view: HomeViewInterface,
    private val interactor: HomeInteractor
) {
    fun loadFaceData() {
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val faceData = interactor.fetchFaceData()
                CoroutineScope(Dispatchers.Main).launch {
                    view.displayFaceData(faceData)
                    view.hideLoading()
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    view.showError(e.message ?: "Error loading data")
                    view.hideLoading()
                }
            }
        }
    }
}