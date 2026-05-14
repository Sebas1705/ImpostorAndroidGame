package es.sebas1705.domain.managers

import android.content.Context
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.core.resources.Sounds
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager to handle sound effects using SoundPool.
 *
 * @property soundPool [SoundPool]: The SoundPool instance to manage sound effects.
 * @property context [Context]: The application context to access resources.
 *
 * @since 0.1.0
 * @author Sebas1705 03/07/2025
 */
@Singleton
class SoundPoolManager @Inject constructor(
    private val soundPool: SoundPool,
    @param:ApplicationContext private val context: Context
) {

    fun play(sound: Sounds, volume: Float = 1f) {
        soundPool.play(
            sound.resourceId,
            volume,
            volume,
            1,
            0,
            1f
        )
    }

    fun release() {
        soundPool.release()
    }
}