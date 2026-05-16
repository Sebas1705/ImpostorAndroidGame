package es.sebas1705.domain.managers

import android.content.Context
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import es.sebas1705.core.resources.Sounds
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundPoolManager @Inject constructor(
    private val soundPool: SoundPool,
    @param:ApplicationContext private val context: Context
) {
    private val soundIds: Map<Sounds, Int>

    init {
        soundIds = Sounds.entries.associateWith { sound ->
            soundPool.load(context, sound.resourceId, 1)
        }
    }

    fun play(sound: Sounds, volume: Float = 1f) {
        val id = soundIds[sound] ?: return
        soundPool.play(id, volume, volume, 1, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }
}
