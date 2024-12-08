package com.nirmal.baby.hovverification.features.home.view

import com.nirmal.baby.hovverification.features.home.entity.HomeEntity

interface HomeViewInterface {
    fun displayFaceData(data: List<HomeEntity>)
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
}