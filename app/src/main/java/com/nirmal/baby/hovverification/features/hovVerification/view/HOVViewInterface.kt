package com.nirmal.baby.hovverification.features.hovVerification.view

interface HOVViewInterface {
    fun showFaceCount(count: Int)
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
}