package com.rana.githubtrendinglist.list.component

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.ImageRequest.Listener
import com.rana.domain.entity.RepositoryItemEntity
import com.rana.githubtendinglist.R
import com.rana.githubtrendinglist.ui.theme.GithubTendingListTheme

@Composable
fun TrendingList(
    modifier: Modifier = Modifier,
    trendingRepos: List<RepositoryItemEntity>
) {
    var selectedIndex by remember { mutableStateOf(0) }

    LazyColumn(Modifier.testTag("TrendingList")) {
        items(trendingRepos.size) { index ->
            TrendingListItem(
                trendingRepos[index],
                selectedIndex == index
            ) { selectedIndex = index }

            Divider(Modifier.padding(vertical = 10.dp))
        }
    }
}

@Composable
fun TrendingListItem(
    trendingRepo: RepositoryItemEntity,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .testTag("TrendingListItem")
            .clickable(onClick = onSelect),
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(trendingRepo.avatar)
                .crossfade(true)
                .listener(object : Listener{
                    override fun onError(request: ImageRequest, result: ErrorResult) {
                        super.onError(request, result)

                        Log.e("Item", "url = " + trendingRepo.avatar )
                    }
                })
                .build(),
            contentDescription = stringResource(R.string.description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clip(CircleShape)
                .size(80.dp)
        )

        Column(
            Modifier.padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = trendingRepo.name)
            Text(text = trendingRepo.description)
            AnimatedVisibility(visible = isSelected) {
                Column {
                    Text(
                        text = trendingRepo.url,
                        Modifier.clickable { uriHandler.openUri(trendingRepo.url) })
                    Row(
                        Modifier
                            .width(200.dp)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.Green)
                        )
                        Text(text = trendingRepo.language)
                        Image(
                            painter = painterResource(id = R.drawable.star_rate),
                            contentDescription = "Score"
                        )
                        Text(text = trendingRepo.score.toString())
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTrendingListItem() {
    GithubTendingListTheme {
        TrendingListItem(
            RepositoryItemEntity(
                "Kotlin",
                avatar = "https://avatars.githubusercontent.com/u/4314092?v=4",
                description = "kotlin repo"
            ),
            false
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectedTrendingListItem() {
    GithubTendingListTheme {
        TrendingListItem(
            RepositoryItemEntity(
                "Kotlin",
                avatar = "https://avatars.githubusercontent.com/u/4314092?v=4",
                url = "https://avatars.githubusercontent.com/u/4314092?v=4",
                description = "kotlin repo",
                language = "Python",
                score = "5"
            ),
            true
        ) {}
    }
}