package com.nafanya.mp3world.presentation.entrypoint

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nafanya.mp3world.R
import com.nafanya.mp3world.core.state_machines.State
import com.nafanya.mp3world.core.state_machines.StateModel

@Composable
fun MainMenuOptionWidget(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes iconRes: Int,
    @StringRes textRes: Int,
    model: StateModel<Int>
) {
    Row(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .height(40.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(iconRes),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(textRes),
            fontSize = 18.sp,
            color = Color.White
        )

        val state by model.currentState.collectAsState()
        when (state) {
            State.Loading -> MainMenuOptionLoading()
            is State.Success -> MainMenuOptionData((state as State.Success<Int>).data)
            is State.Updated -> MainMenuOptionData((state as State.Updated<Int>).data)
            else -> {}
        }

        Image(
            modifier = Modifier.size(25.dp),
            painter = painterResource(R.drawable.icv_arrow_forward),
            contentDescription = null
        )
    }
}

@Composable
private fun MainMenuOptionLoading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(25.dp),
        strokeWidth = 2.dp,
        color = Color(3, 219, 197)
    )
}

@Composable
private fun MainMenuOptionData(
    count: Int,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = count.toString(),
        fontSize = 18.sp,
        color = Color.White
    )
}

private class MainMenuOptionWidgetPreviewParameterProvider : PreviewParameterProvider<StateModel<Int>> {
    override val values: Sequence<StateModel<Int>>
        get() = sequenceOf(
            StateModel<Int>().apply { this.load() },
            StateModel<Int>().apply { load(); success(10) }
        )

}

@Preview
@Composable
private fun MainMenuOptionWidgetPreview(
    @PreviewParameter(MainMenuOptionWidgetPreviewParameterProvider::class) stateModel: StateModel<Int>
) {
    MainMenuOptionWidget(
        onClick = {},
        iconRes = R.drawable.icv_album,
        textRes = R.string.albums,
        model = stateModel
    )
}
