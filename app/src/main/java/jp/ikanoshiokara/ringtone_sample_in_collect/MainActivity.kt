package jp.ikanoshiokara.ringtone_sample_in_collect

import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jp.ikanoshiokara.ringtone_sample_in_collect.ui.theme.RingtonesampleincollectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val LocalNavController =
    staticCompositionLocalOf<NavController> {
        error("No NavGraph provided")
    }


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RingtonesampleincollectTheme {
                App()
            }
        }
    }
}

@Composable
fun App(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalNavController provides navController,
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = {
                val items = listOf(
                    Destination.Simple,
                    Destination.List,
                    Destination.MediaPlayer
                )
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Destination.Simple.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Destination.Simple.route) {
                    SimpleRingtoneScreen()
                }
                composable(Destination.List.route) {
                    RingtoneListScreen()
                }
                composable(Destination.MediaPlayer.route) {
                    RingtoneMediaPlayerScreen()
                }
            }
        }
    }
}

sealed class Destination(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    data object Simple: Destination(
        "Simple",
        "simple",
        Icons.Default.Notifications
    )

    data object List: Destination(
        "List",
        "list",
        Icons.AutoMirrored.Filled.List
    )

    data object MediaPlayer: Destination(
        "MediaPlayer",
        "media",
        Icons.Default.PlayArrow
    )
}

@Composable
fun SimpleRingtoneScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            8.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        Button(
            onClick = {
                scope.launch {
                    val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val ringtone = RingtoneManager.getRingtone(context, uri)
                    ringtone.play()
                    delay(3000)
                    ringtone.stop()
                }
            }
        ) {
            Text(text = "TYPE_NOTIFICATION")
        }
        Button(
            onClick = {
                scope.launch {
                    val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    val ringtone = RingtoneManager.getRingtone(context, uri)
                    ringtone.play()
                    delay(3000)
                    ringtone.stop()
                }
            }
        ) {
            Text(text = "TYPE_ALARM")
        }
        Button(
            onClick = {
                scope.launch {
                    val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                    val ringtone = RingtoneManager.getRingtone(context, uri)
                    ringtone.play()
                    delay(3000)
                    ringtone.stop()
                }
            }
        ) {
            Text(text = "TYPE_RINGTONE")
        }
    }
}

@Composable
fun RingtoneListScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val manager = RingtoneManager(context)
    manager.setType(RingtoneManager.TYPE_ALL)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val cursor = manager.cursor
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                HorizontalDivider()
            }
            item {
                while (cursor.moveToNext()) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        val position = cursor.position
                        val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                        val id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX)
                        val uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)
                        
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "$position: $title")
                            Text(
                                text = "$uri/$id",
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }

                        IconButton(
                            onClick = {
                                val ringtone = manager.getRingtone(position)
                                ringtone.play()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun RingtoneMediaPlayerScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val player = MediaPlayer()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            8.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        Button(
            onClick = {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                player.setDataSource(context, uri)
                player.isLooping = false
                player.prepare()

                player.start()
            }
        ) {
            Text(text = "Media Player")
        }
    }
}
