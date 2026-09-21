package me.gblrod.mavira.core.manager

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND

class ShareManager(
    private val context: Context
) {
    fun shareText(text: String) {
        val sendIntent: Intent = Intent().apply {
            action = ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
}