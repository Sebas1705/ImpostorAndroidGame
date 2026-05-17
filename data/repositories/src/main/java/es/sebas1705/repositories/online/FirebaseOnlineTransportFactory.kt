package es.sebas1705.repositories.online

import es.sebas1705.firestore.lobby.GameRoomFirestoreDataSource
import es.sebas1705.models.OnlinePlayer
import es.sebas1705.realtime.game.RtdbGameDataSource
import javax.inject.Inject

class FirebaseOnlineTransportFactory @Inject constructor(
    private val lobbySource: GameRoomFirestoreDataSource,
    private val rtdbSource: RtdbGameDataSource,
) {
    fun create(localPlayer: OnlinePlayer): FirebaseOnlineTransport =
        FirebaseOnlineTransport(
            localPlayer = localPlayer,
            lobbySource = lobbySource,
            rtdbSource = rtdbSource,
        )
}
