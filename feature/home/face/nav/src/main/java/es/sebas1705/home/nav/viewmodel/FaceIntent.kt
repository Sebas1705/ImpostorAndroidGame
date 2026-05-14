package es.sebas1705.home.nav.viewmodel

import es.sebas1705.common.mvi.MVIBaseIntent

sealed interface FaceIntent : MVIBaseIntent {
    data object Load : FaceIntent
}

