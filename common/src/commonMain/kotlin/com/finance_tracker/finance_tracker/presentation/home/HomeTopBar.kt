package com.finance_tracker.finance_tracker.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.finance_tracker.finance_tracker.core.common.DialogConfigurations
import com.finance_tracker.finance_tracker.core.common.stringResource
import com.finance_tracker.finance_tracker.core.theme.CoinTheme
import com.finance_tracker.finance_tracker.core.ui.CoinTopAppBar
import com.finance_tracker.finance_tracker.core.ui.rememberVectorPainter
import com.finance_tracker.finance_tracker.domain.models.Amount
import com.finance_tracker.finance_tracker.presentation.common.formatters.AmountFormatMode
import com.finance_tracker.finance_tracker.presentation.common.formatters.format
import com.finance_tracker.finance_tracker.presentation.settings_sheet.SettingsSheet
import ru.alexgladkov.odyssey.compose.extensions.present
import ru.alexgladkov.odyssey.compose.local.LocalRootController

@Composable
fun HomeTopBar(
    totalBalance: Amount,
    modifier: Modifier = Modifier
) {
    val rootController = LocalRootController.current
    val modalController = rootController.findModalController()

    CoinTopAppBar(
        modifier = modifier,
        appBarHeight = 64.dp,
        title = {
            Column {
                Text(
                    text = totalBalance.format(mode = AmountFormatMode.NegativeSign),
                    style = CoinTheme.typography.h4
                )
                Text(
                    text = stringResource("home_topbar_text"),
                    style = CoinTheme.typography.subtitle2,
                    color = CoinTheme.color.secondary
                )
            }
        },
        actions = {
            Icon(
                painter = rememberVectorPainter("ic_settings"),
                contentDescription = null,
                Modifier
                    .padding(end = 2.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable {
                        modalController.present(DialogConfigurations.bottomSheet) { key ->
                            SettingsSheet(
                                onCloseClick = { modalController.popBackStack(key) }
                            )
                        }
                    }
                    .padding(6.dp),
                tint = CoinTheme.color.content
            )
        }
    )
}