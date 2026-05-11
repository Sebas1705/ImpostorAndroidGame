package es.sebas1705.datastore.serializers

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import es.sebas1705.datastore.GamePreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

internal const val GAME_PREFERENCES_FILE_NAME = "game_prefs.pb"

class GameSerializer @Inject constructor() : Serializer<GamePreferences> {
    override val defaultValue: GamePreferences = GamePreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): GamePreferences =
        try {
            GamePreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw Exception("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: GamePreferences, output: OutputStream) {
        t.writeTo(output)
    }
}

