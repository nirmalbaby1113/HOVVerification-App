package com.nirmal.baby.hovverification.features.hovVerification.presenter

import android.util.Log
import com.nirmal.baby.hovverification.features.hovVerification.interactor.HOVInteractor
import com.nirmal.baby.hovverification.features.hovVerification.router.HOVRouter
import com.nirmal.baby.hovverification.features.hovVerification.view.HOVViewInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class HOVPresenter(
    private val view: HOVViewInterface,
    private val interactor: HOVInteractor,
    private val router: HOVRouter
) {
    fun onImageCaptured(imageFile: File) {
        Log.d("HOVPresenter", "Inside OnImageCaptured")
        view.showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = interactor.fetchFaceData(imageFile)
                withContext(Dispatchers.Main) {
                    view.showFaceCount(response.face_num)
                    //view.hideLoading()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val detailedError = e.message ?: "An unknown error occurred"
                    Log.e("HOVPresenter", "Error: $detailedError")
                    view.showError(detailedError) // Show detailed error
                    //view.hideLoading()
                }
            }
        }
    }
}
