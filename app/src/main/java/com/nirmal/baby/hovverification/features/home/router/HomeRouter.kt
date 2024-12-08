package com.nirmal.baby.hovverification.features.home.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.nirmal.baby.hovverification.features.hovVerification.view.HOVActivity

class HomeRouter(private val activity: Activity) {
    fun navigateToHOVVerification() {
        activity.startActivity(Intent(activity, HOVActivity::class.java))
        activity.finish()
    }
}