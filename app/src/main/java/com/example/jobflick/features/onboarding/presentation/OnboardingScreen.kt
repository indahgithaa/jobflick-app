package com.example.jobflick.features.onboarding.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobflick.R
import com.example.jobflick.core.ui.components.JFPrimaryButton
import com.example.jobflick.core.ui.theme.Jost
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.isUnspecified
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.jobflick.core.ui.theme.BluePrimary
import com.example.jobflick.core.ui.theme.OrangePrimary

private data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val desc: String,
    val buttonText: String,
    val buttonColor: Color
)

@OptIn(ExperimentalFoundationApi::class)
@Preview(
    showBackground = true,
)

@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen(onFinish = {})
}

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.onboarding_blue,
            title = "Temukan pekerjaan ideal\nhanya dengan swipe",
            desc = "Cari lowongan kerja dengan cara yang seru dan cepat. Cocokkan keahlian dan minatmu lewat fitur swipe yang praktis.",
            buttonText = "Lanjut",
            buttonColor = BluePrimary
        ),
        OnboardingPage(
            imageRes = R.drawable.onboarding_orange,
            title = "Rekrut kandidat terbaik\ndalam hitungan detik",
            desc = "Temukan talenta terbaik untuk perusahaanmu secara cepat, mudah, dan efisien tanpa proses yang merepotkan.",
            buttonText = "Mulai Sekarang",
            buttonColor = OrangePrimary
        )
    )

    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val painter = painterResource(id = pages[page].imageRes)
            val ratio = if (painter.intrinsicSize.isUnspecified) 16f / 9f
            else (painter.intrinsicSize.width / painter.intrinsicSize.height)

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio),
                contentScale = ContentScale.FillWidth
            )
        }

        Spacer(Modifier.height(90.dp))

        Text(
            text = pages[pagerState.currentPage].title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = Jost,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                lineHeight = 35.sp,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 45.dp)
        )

        Spacer(Modifier.height(30.dp))

        Text(
            text = pages[pagerState.currentPage].desc,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = Jost,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 45.dp)
        )

        Spacer(Modifier.height(90.dp))

        JFPrimaryButton(
            text = pages[pagerState.currentPage].buttonText,
            backgroundColor = pages[pagerState.currentPage].buttonColor,
            onClick = {
                if (pagerState.currentPage < pages.lastIndex) {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                } else onFinish()
            }
        )

        Spacer(Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(pages.size) { i ->
                val selected = i == pagerState.currentPage
                Box(
                    Modifier
                        .padding(horizontal = 4.dp)
                        .size(if (selected) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (selected)
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                            else
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f)
                        )
                )
            }
        }
    }
}
