package es.sebas1705.offlinegame.models

import es.sebas1705.common.utlis.extensions.types.logD
import es.sebas1705.common.utlis.extensions.types.logI

private const val OFFLINE_GAME_LOG_TAG = "OfflineGameFlow"

internal fun screenLogD(message: String) = OFFLINE_GAME_LOG_TAG.logD(message)
internal fun screenLogI(message: String) = OFFLINE_GAME_LOG_TAG.logI(message)

