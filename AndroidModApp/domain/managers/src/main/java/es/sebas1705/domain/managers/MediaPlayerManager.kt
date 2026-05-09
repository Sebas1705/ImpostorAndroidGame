package es.sebas1705.domain.managers

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.core.resources.Musics
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager to handle MediaPlayer operations
 *
 * @property mediaPlayer The MediaPlayer instance used to play audio
 * @property context The application context used to access resources
 *
 * @since 0.1.0
 * @author Sebas1705 05/07/2025
 */
@Singleton
class MediaPlayerManager @Inject constructor(
    private val mediaPlayer: MediaPlayer,
    @param:ApplicationContext private val context: Context
){

    fun play() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    fun pause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    fun stop() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.prepare() // Prepare the MediaPlayer for future use
        }
    }

    fun setVolume(leftVolume: Float, rightVolume: Float) {
        mediaPlayer.setVolume(leftVolume, rightVolume)
    }

    fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume, volume)
    }

    fun changeSong(music: Musics) {
        mediaPlayer.reset()
        val afd = context.resources.openRawResourceFd(music.resourceId)
        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }
}