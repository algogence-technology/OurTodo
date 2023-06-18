package com.algogence.ourtodo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.algogence.ourtodo.ui.theme.OurTodoTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OurTodoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IntroPage()
                }
            }
        }
    }

    @Composable
    private fun SplashPage() {
        Box(
            contentAlignment = Alignment.Center
        ){
            //AnimatedLogo()
            Loader()
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun IntroPage(){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            var currentPage by remember {
                mutableStateOf(0)
            }
            val state = rememberPagerState()
            val cs = rememberCoroutineScope()
            HorizontalPager(
                state = state,
                pageCount = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),

            ) { page ->
                Box(
                    modifier = Modifier
                        .padding(top = 200.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f),
                    contentAlignment = Alignment.Center
                ){
                    when(page){
                        0->Intro1()
                        1->Intro2()
                        2->Intro3()
                    }
                }

            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Dot(state.currentPage==0){
                    cs.launch {
                        state.animateScrollToPage(0)
                    }
                }
                Dot(state.currentPage==1){
                    cs.launch {
                        state.animateScrollToPage(1)
                    }
                }
                Dot(state.currentPage==2){
                    cs.launch {
                        state.animateScrollToPage(2)
                    }
                }
            }
        }

    }

    @Composable
    private fun Dot(
        selected: Boolean,
        onClick: ()->Unit
    ) {
        val color by animateColorAsState(
            targetValue = if (selected) Color(0xff008cff) else Color.LightGray,
            tween(300)
        )
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
                .clickable(onClick = onClick)
        )
    }

    @Composable
    private fun Intro1() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.intro_feature1),
                contentDescription = "",
                modifier = Modifier
                    .size(300.dp)
            )
            Text(
                "Organize your tasks",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xff008cff)
            )
        }
    }

    @Composable
    private fun Intro3() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.intro_feature3),
                contentDescription = "",
                modifier = Modifier
                    .size(300.dp)
            )
            Text(
                "Share tasks",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xff008cff)
            )
        }
    }

    @Composable
    private fun Intro2() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Box(
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.intro_feature2),
                    contentDescription = "",
                    modifier = Modifier
                        .size(300.dp)
                )
                Text(
                    "Universe is preparing something for you",
                    fontSize = 20.sp,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .width(200.dp),
                    textAlign = TextAlign.Center
                )
            }

            Text(
                "Set Reminder",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xff008cff),
                modifier = Modifier
                    .width(280.dp),
                lineHeight = 42.sp,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun AnimatedLogo() {
        var t by remember {
            mutableStateOf(0f)
        }
        var s by remember {
            mutableStateOf(0f)
        }
        LaunchedEffect(key1 = Unit){
            t = 1f
            s = 1f
        }
        val transparency by animateFloatAsState(
            targetValue = t,
            tween(2000)
        )
        val scale by animateFloatAsState(
            targetValue = t,
            tween(2000)
        )
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier = Modifier
                .alpha(transparency)
                .scale(scale)
        )
    }
}

@Composable
fun Loader() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_logo))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}