package jp.ikanoshiokara.ringtone_sample_in_collect

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ikanoshiokara.ringtone_sample_in_collect.ui.theme.RingtonesampleincollectTheme

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
    SimpleRingtoneScreen(modifier)
}

@Composable
fun SimpleRingtoneScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Button(
                    onClick = {
                        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                        val ringtone = RingtoneManager.getRingtone(context, uri)
                        ringtone.play()
                    }
                ) {
                    Text(text = "音を鳴らす")
                }
            }
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

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            val position = cursor.position
                            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                            Text(text = "$position: $title")

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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RingtonesampleincollectTheme {
        App()
    }
}