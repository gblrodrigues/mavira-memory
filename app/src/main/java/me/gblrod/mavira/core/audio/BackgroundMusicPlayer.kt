package me.gblrod.mavira.core.audio

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import me.gblrod.mavira.R

class BackgroundMusicPlayer(context: Context) {
    private val player: ExoPlayer = ExoPlayer.Builder(
        context.applicationContext
    ).build().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(C.USAGE_GAME)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build(),
            true
        )

        setHandleAudioBecomingNoisy(true)

        repeatMode = Player.REPEAT_MODE_ONE
        volume = DEFAULT_VOLUME

        val musicUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.packageName)
            .appendPath(R.raw.mavira_background_music.toString())
            .build()

        setMediaItem(MediaItem.fromUri(musicUri))
        prepare()
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun release() {
        player.release()
    }

    private companion object {
        const val DEFAULT_VOLUME = 0.25f
    }
}