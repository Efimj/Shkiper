package com.jobik.shkiper.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.jobik.shkiper.R

class IntentHelper {
    fun sendMailIntent(context: Context, mailList: List<String>, header: String, text: String = "") {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, mailList.toTypedArray())
        intent.putExtra(Intent.EXTRA_SUBJECT, header)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.setType("message/rfc822")
        intent.setPackage("com.google.android.gm")
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            intent.setPackage(null)
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.ChooseEmailClient)))
        }
    }

    fun openBrowserIntent(context: Context, link:String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        context.startActivity(intent)
    }
}