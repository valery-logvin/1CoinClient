package com.finance_tracker.finance_tracker.features.plans.setup.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.finance_tracker.finance_tracker.MR
import com.finance_tracker.finance_tracker.core.common.clicks.scaleClickAnimation
import com.finance_tracker.finance_tracker.core.common.`if`
import com.finance_tracker.finance_tracker.core.common.rememberAsyncImagePainter
import com.finance_tracker.finance_tracker.core.theme.CoinTheme
import com.finance_tracker.finance_tracker.core.theme.staticTextSize
import com.finance_tracker.finance_tracker.domain.models.Account
import com.finance_tracker.finance_tracker.domain.models.Category
import com.finance_tracker.finance_tracker.features.plans.setup.SetupPlanStep
import dev.icerock.moko.resources.compose.stringResource
import ru.alexgladkov.odyssey.compose.helpers.noRippleClickable

data class StepsSetupPlanBarData(
    val steps: List<SetupPlanStep>,
    val currentStep: SetupPlanStep? = SetupPlanStep.Category,
    val primaryAccountData: Account? = null,
    val secondaryAccountData: Account? = null,
    val categoryData: Category? = null,
)

@Composable
internal fun StepsSetupPlanBar(
    data: StepsSetupPlanBarData,
    modifier: Modifier = Modifier,
    onStepSelect: (SetupPlanStep) -> Unit = {}
) {
    val steps by remember(data.steps) {
        derivedStateOf {
            data.steps.filter { it != SetupPlanStep.PrimaryAmount }
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(CoinTheme.color.background)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        steps.forEachIndexed { index, enterTransactionStep ->
            when (enterTransactionStep) {
                SetupPlanStep.Category -> {
                    CategoryStage(
                        data = data,
                        onStageSelect = onStepSelect
                    )
                }
                SetupPlanStep.PrimaryAmount -> {
                }
            }

            if (index < steps.lastIndex) {
                NextIcon()
            }
        }
    }
}

@Composable
internal fun RowScope.CategoryStage(
    data: StepsSetupPlanBarData,
    modifier: Modifier = Modifier,
    onStageSelect: (SetupPlanStep) -> Unit = {}
) {
    StageText(
        modifier = modifier,
        currentStep = SetupPlanStep.Category,
        data = data.categoryData,
        selectedStep = data.currentStep,
        onStepSelect = onStageSelect,
        dataContent = { CategoryRow(category = it) }
    )
}

@Composable
internal fun CategoryRow(
    category: Category,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = rememberAsyncImagePainter(category.icon),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = category.name,
            style = CoinTheme.typography.subtitle1.staticTextSize(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
internal fun NextIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier
            .padding(horizontal = 2.dp),
        painter = rememberAsyncImagePainter(MR.files.ic_arrow_next_small),
        contentDescription = null,
        tint = CoinTheme.color.content
    )
}

@Composable
private fun <T: Any> RowScope.StageText(
    currentStep: SetupPlanStep,
    data: T?,
    selectedStep: SetupPlanStep?,
    onStepSelect: (SetupPlanStep) -> Unit,
    modifier: Modifier = Modifier,
    dataContent: @Composable (data: T) -> Unit = {}
) {
    val isActiveStage = currentStep == selectedStep
    Box(
        modifier = modifier
            .scaleClickAnimation(enabled = !isActiveStage)
            .`if`(!isActiveStage) {
                noRippleClickable { onStepSelect.invoke(currentStep) }
            }
            .weight(1f)
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .border(
                width = 1.dp,
                color = if (isActiveStage) {
                    CoinTheme.color.primary
                } else {
                    CoinTheme.color.dividers
                },
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(
                start = 12.dp,
                end = 12.dp
            )
            .height(36.dp),
        contentAlignment = Alignment.Center
    ) {
        if (data == null) {
            Text(
                text = currentStep.textId?.let { stringResource(it) }.orEmpty(),
                style = CoinTheme.typography.subtitle1,
                textAlign = TextAlign.Center,
                color = if (isActiveStage) {
                    LocalContentColor.current
                } else {
                    LocalContentColor.current.copy(alpha = 0.3f)
                }
            )
        } else {
            dataContent.invoke(data)
        }
    }
}