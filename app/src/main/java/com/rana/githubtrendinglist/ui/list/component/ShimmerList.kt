package com.rana.githubtrendinglist.ui.list.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rana.githubtrendinglist.ui.ShimmerAnimation
import com.rana.githubtrendinglist.ui.theme.GithubTendingListTheme

@Composable
fun ShimmerList() {
    ShimmerAnimation { _ ->
        ShimmerAnimation { brush ->
            LazyColumn(modifier = Modifier.testTag("shimmerList")) {
                repeat(10) {
                    item {
                        Row(
                            Modifier.padding(15.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .size(70.dp)
                                    .background(brush = brush, RoundedCornerShape(50.dp))
                            )

                            Column(Modifier.padding(horizontal = 15.dp)) {
                                Spacer(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(30.dp)
                                        .padding(vertical = 10.dp)
                                        .background(brush = brush, RoundedCornerShape(5.dp))
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(30.dp)
                                        .padding(vertical = 10.dp)
                                        .background(brush = brush, RoundedCornerShape(5.dp))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShimmerList() {
    GithubTendingListTheme {
        ShimmerList()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewDarkModeShimmerList() {
    GithubTendingListTheme {
        ShimmerList()
    }
}