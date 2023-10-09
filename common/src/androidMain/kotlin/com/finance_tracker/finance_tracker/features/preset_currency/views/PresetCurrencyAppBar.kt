package com.finance_tracker.finance_tracker.features.preset_currency.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.finance_tracker.finance_tracker.MR
import com.finance_tracker.finance_tracker.core.theme.CoinTheme
import com.finance_tracker.finance_tracker.core.ui.CoinTopAppBar
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun PresetCurrencyAppBar(
    modifier: Modifier = Modifier
) {
    CoinTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(MR.strings.preset_currency_title),
                style = CoinTheme.typography.h4,
                color = CoinTheme.color.content,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}