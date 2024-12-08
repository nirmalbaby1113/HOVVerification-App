package com.nirmal.baby.hovverification.features.hovVerification.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.nirmal.baby.hovverification.features.home.view.HomeActivity
import com.nirmal.baby.hovverification.features.hovVerification.view.HOVActivity

class HOVRouter(private val context: Context) {
    fun navigateToHome() {
        context.startActivity(Intent(context, HomeActivity::class.java))
    }
}