package com.rana.githubtrendinglist.ui.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rana.githubtendinglist.R
import com.rana.githubtrendinglist.ui.theme.GithubTendingListTheme

@Composable
fun RetryScreen(onRetryClick: () -> Unit) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.retry_anim))

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("errorAnimation"),
            iterations = LottieConstants.IterateForever
        )
        Text(
            modifier = Modifier.padding(top = 30.dp),
            text = stringResource(id = R.string.error_title),
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(id = R.string.error_subtitle),
        )
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            onClick = onRetryClick,
            border = BorderStroke(2.dp, Color.Green),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("Retry button")
        ) {
            Text(
                text = stringResource(id = R.string.retry),
                color = Color.Green,
            )
        }
    }
}

@Preview
@Composable
fun PreviewRetryScreen() {
    GithubTendingListTheme { RetryScreen {} }
}