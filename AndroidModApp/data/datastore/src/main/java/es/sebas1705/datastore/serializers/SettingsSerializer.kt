package es.sebas1705.datastore.serializers

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import es.sebas1705.datastore.SettingsPreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

internal const val SETTINGS_PREFERENCES_FILE_NAME = "settings_prefs.pb"

class SettingsSerializer @Inject constructor() : Serializer<SettingsPreferences> {
    override val defaultValue: SettingsPreferences = SettingsPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingsPreferences =
        try {
            // readFrom is already called on the data store background thread
            SettingsPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw Exception("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: SettingsPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}