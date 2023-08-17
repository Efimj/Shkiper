package com.jobik.shkiper.helpers

import android.content.Context
import android.content.Intent
import com.jobik.shkiper.R

class IntentHelper {
    fun sendMailIntent(context: Context, mailList: List<String>, header: String, text: String = "") {
        // on below line we are creating
        // an intent to send an email
        val intent = Intent(Intent.ACTION_SEND)

        // on below line we are passing email address,
        // email subject and email body
        val emailAddress = arrayOf("efim1tv@gmail.com")
        intent.putExtra(Intent.EXTRA_EMAIL, mailList.toTypedArray())
        intent.putExtra(Intent.EXTRA_SUBJECT, header)
        intent.putExtra(Intent.EXTRA_TEXT, text)

        // on below line we are
        // setting type of intent
        intent.setType("message/rfc822")

        // on the below line we are starting our activity to open email application.
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.ChooseEmailClient)))
    }
}