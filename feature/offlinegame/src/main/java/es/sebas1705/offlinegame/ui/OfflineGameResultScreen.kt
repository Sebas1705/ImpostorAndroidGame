@file:Suppress("LongMethod")

package es.sebas1705.offlinegame.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.sebas1705.core.resources.R
import es.sebas1705.offlinegame.models.OfflineGameResult
import es.sebas1705.offlinegame.models.OfflineWinner
import es.sebas1705.offlinegame.models.screenLogI
import es.sebas1705.offlinegame.ui.components.EmptyResultCard
import es.sebas1705.offlinegame.ui.components.ResultCloseButton
import es.sebas1705.offlinegame.ui.components.ResultDetailsCard
import es.sebas1705.offlinegame.ui.components.ResultVotesRow
import es.sebas1705.offlinegame.ui.components.ResultWinnerCard

@Composable
internal fun OfflineGameResultScreen(
    result: OfflineGameResult?,
    showImpostorsInResult: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val resultImpostors = stringResource(R.string.core_resources_result_impostors)
    val resultImpostorsHidden = stringResource(R.string.core_resources_result_impostors_hidden_by_mode)

    LaunchedEffect(result) {
        screenLogI(
            if (result == null) "result phase reached with null result"
            else "result phase winner=${result.winner} word='${result.word}' " +
                    "correctVotes=${result.correctVotes} incorrectVotes=${result.incorrectVotes}"
        )
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (result == null) {
            item(contentType = "contentType1") {
                EmptyResultCard(onBack = onBack)
            }
            return@LazyColumn
        }

        item(contentType = "contentType2") {
            val winnerTitle = when (result.winner) {
                OfflineWinner.Civilians -> stringResource(R.string.core_resources_result_civilians_win)
                OfflineWinner.Impostors -> stringResource(R.string.core_resources_result_impostors_win)
                OfflineWinner.Tie -> stringResource(R.string.core_resources_result_tie)
            }
            ResultWinnerCard(
                winnerTitle = winnerTitle,
                reason = result.reason
            )
        }

        item(contentType = "contentType3") {
            val impostorsText = remember(showImpostorsInResult, result) {
                if (showImpostorsInResult)
                    String.format(
                        resultImpostors,
                        result.impostorNames.joinToString()
                    )
                else resultImpostorsHidden
            }
            ResultDetailsCard(
                word = result.word,
                impostorsText = impostorsText
            )
        }

        item(contentType = "contentType4") {
            ResultVotesRow(
                correctVotes = result.correctVotes,
                incorrectVotes = result.incorrectVotes
            )
        }

        item(contentType = "contentType5") {
            Spacer(modifier = Modifier.height(4.dp))
        }

        item(contentType = "contentType6") {
            ResultCloseButton(onBack = onBack)
        }
    }
}
