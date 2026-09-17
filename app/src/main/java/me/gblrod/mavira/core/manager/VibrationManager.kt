package me.gblrod.mavira.core.manager

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class VibrationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val vibrator: Vibrator? = context.getSystemService(Vibrator::class.java)

    fun pairMatched() {
        vibrator?.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(
                    0L,
                    70L,
                    50L,
                    70L
                ),
                -1
            )
        )
    }

    fun gameCompleted() {
        vibrator?.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(
                    0L,
                    50L,
                    40L,
                    100L,
                    40L,
                    150L,
                    50L,
                    200L
                ),
                -1
            )
        )
    }
}