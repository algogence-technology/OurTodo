package com.algogence.ourtodo

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.algogence.ourtodo.database.AppDatabase
import com.algogence.ourtodo.database.MyTasks
import com.algogence.ourtodo.mypackage.Task
import com.algogence.ourtodo.ui.theme.OurTodoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {
    private val pending = mutableStateOf(true)
    private val context: Context by lazy{ this }
    private val sharedPreferences: MySharedPreferences by lazy{ MySharedPreferences(context) }
    private val iid = mutableStateOf(0)
    private val store = mutableStateListOf<Task>()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OurTodoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White//MaterialTheme.colorScheme.background
                ) {
                    Column(){
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = "splash") {
                            composable("splash") { SplashPage(navController) }
                            composable("intro") { IntroPage(navController) }
                            composable("home") { HomePage(navController) }
                            composable("add") { AddTask(navController) }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SplashPage(navController: NavHostController) {
        //val context = LocalContext.current
        LaunchedEffect(key1 = Unit){
            delay(1700)
            val introDone = sharedPreferences.getBoolean("introDone",false)
            if(introDone){
                navController.navigate("home")
            }
            else{
                navController.navigate("intro")
            }
        }
        Box(
            contentAlignment = Alignment.Center
        ){
            Loader()
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun IntroPage(navController: NavHostController) {
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
            var introSeen by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(key1 = state.currentPage){
                if(state.currentPage==2){
                    introSeen = true
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            val context = LocalContext.current
            Button(
                onClick = {
                    navController.navigate("home")
                    saveIntroDone(context)
                }
            ) {
                Text(if(introSeen) "Get Started" else "Skip")
            }
        }

    }

    private fun saveIntroDone(context: Context) {
        /*val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean("introDone", true)
            apply()
        }*/
        MySharedPreferences(context).addBoolean("introDone", true)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddTask(navController: NavHostController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = ""
                    )
                }
                Text("New Task")
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                var title by remember {
                    mutableStateOf("")
                }
                var description by remember {
                    mutableStateOf("")
                }
                val canProceed by remember {
                    derivedStateOf {
                        title.isNotEmpty() && description.isNotEmpty()
                    }
                }
                Text(
                    text = "New Task",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff246BFE)
                )

                OutlinedTextField(
                    modifier = Modifier
                        .width(320.dp)
                        .padding(top = 20.dp)
                        .border(BorderStroke(1.dp, SolidColor(Color.Black))),
                    colors = TextFieldDefaults
                        .textFieldColors(
                            containerColor = Color.White,
                            textColor = Color.Gray,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Bold
                    ),
                    placeholder = {
                        Text("Title")
                    }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .width(320.dp)
                        .height(200.dp)
                        .padding(top = 20.dp)
                        .border(BorderStroke(1.dp, SolidColor(Color.Black))),
                    colors = TextFieldDefaults
                        .textFieldColors(
                            containerColor = Color.White,
                            textColor = Color.Gray,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Bold
                    ),
                    placeholder = {
                        Text("Description")
                    }
                )
                val scope = rememberCoroutineScope()
                var mDate by remember { mutableStateOf("") }
                val mContext = LocalContext.current
                Row(
                    modifier = Modifier
                        .width(320.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = {
                            val mYear: Int
                            val mMonth: Int
                            val mDay: Int
                            val mCalendar = Calendar.getInstance()
                            val parts = mDate.split("/")//02/07/2023
                            if(parts.size==3){
                                mDay = parts[0].toInt()
                                mMonth = parts[1].toInt()-1
                                mYear = parts[2].toInt()
                            }
                            else{
                                mYear = mCalendar.get(Calendar.YEAR)
                                mMonth = mCalendar.get(Calendar.MONTH)
                                mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
                            }

                            mCalendar.time = Date()
                            val mDatePickerDialog = DatePickerDialog(
                                mContext,
                                { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                                    mDate = "$mDayOfMonth/${mMonth+1}/$mYear"
                                }, mYear, mMonth, mDay
                            )

                            mDatePickerDialog.show()
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = "")
                    }
                    Text(mDate)
                }
                Button(
                    enabled = canProceed,
                    modifier = Modifier
                        .padding(horizontal = 36.dp)
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(top = 20.dp),
                    onClick = {
                        //store.add(Task(++iid.value, title, description))
                        scope.launch(Dispatchers.IO) {
                            saveTaskToDatabase(Task(++iid.value, title, description,mDate))
                        }

                        navController.popBackStack()
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF457EF5)
                    )
                ) {
                    Text(
                        text = "Add",
                        fontWeight = FontWeight.Bold,
                    )

                }


            }
        }

    }

    private fun saveTaskToDatabase(task: Task) {
        MyTasks(applicationContext).insert(task)
    }

    private fun getTasksFromDatabase(): List<Task> {
        return if(pending.value){
            MyTasks(applicationContext).getAll()
        } else{
            MyTasks(applicationContext).getAllDone()
        }
    }

    @Composable
    fun HomePage(navController: NavHostController) {
        LaunchedEffect(key1 = Unit){
            withContext(Dispatchers.IO){
                refreshTasks()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ){
                if(store.isEmpty()){
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Column() {
                            Text(
                                "No tasks yet",
                                color = Color.Gray
                            )
                            TextButton(
                                onClick = {
                                    navController.navigate("add")
                                }
                            ) {
                                Text("Add one")
                            }
                        }

                    }

                }
                else{
                    LazyColumn(
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ){
                        items(store){
                            TaskUI(it)
                        }
                    }
                }


            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                val scope = rememberCoroutineScope()
                Button(
                    onClick = {
                        pending.value = true
                        scope.launch(Dispatchers.IO) {
                            refreshTasks()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(pending.value) Color.Blue else Color.Gray,
                        contentColor = if(pending.value) Color.White else Color.Blue
                    )
                ) {
                    Text("Pending")
                }
                FloatingActionButton(
                    onClick = {
                        navController.navigate("add")
                    },
                    shape = CircleShape,
                    containerColor = Color(0xff00C14D),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                Button(
                    onClick = {
                        pending.value = false
                        scope.launch(Dispatchers.IO) {
                            refreshTasks()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(pending.value) Color.Gray else Color.Blue,
                        contentColor = if(pending.value) Color.Blue else Color.White
                    )
                ) {
                    Text("Done")
                }
            }
        }
    }

    private fun refreshTasks() {
        store.clear()
        store.addAll(getTasksFromDatabase())
    }

    @Composable
    private fun TaskUI(task: Task) {
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .shadow(
                    2.dp,
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(24.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    task.title.capitalize(),
                    fontSize = 24.sp,
                    color = Color(0xff00C14D),
                    fontWeight = FontWeight.Bold
                )

                Checkbox(
                    checked = task.done,
                    onCheckedChange = {
                        scope.launch(Dispatchers.IO) {
                            MyTasks(applicationContext).update(task.apply { done = !done })
                            refreshTasks()
                        }
                    }
                )
            }
            Text(
                task.description,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                IconButton(
                    onClick = {
                        val id = task.id
                        val item = store.find {
                            it.id == id
                        }
                        //store.remove(item)
                        scope.launch(Dispatchers.IO) {
                            deleteTask(item)
                        }
                    },
                    modifier = Modifier
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = ""
                    )
                }
                Text(
                    task.date,
                    fontSize = 12.sp,
                    color = Color.Blue
                )
            }

        }
    }

    private fun deleteTask(item: Task?) {
        if(item==null){
            return
        }
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "taskDatabase"
        ).build()

        val taskDao = db.taskDao()
        val dbTask = com.algogence.ourtodo.database.Task(
            item.id,
            item.title,
            item.description,
            "02-07-2023",
            false
        )
        taskDao.delete(dbTask)
        refreshTasks()
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

// navigation, back
// data class
// list, mutable state list
// find, remove
// floating action button
// derived state of
// button enable/disable

// check box padding, icon button ripple effect, capitalize alternative

// room
// kapt

// sharedPreference

// coroutine



// shadow
// checkbox
// theme