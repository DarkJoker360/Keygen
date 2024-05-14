package com.esposito.keygen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esposito.keygen.ui.theme.KeygenTheme

class MainActivity : ComponentActivity() {
    private var length: Int = 4
    private var ctx = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeygenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Header(getString(R.string.app_name))
                        Body()
                    }
                }
            }
        }
    }

    @Composable
    private fun Header(text: String) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Text(
                text,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1
            )
        }
    }

    @Composable
    private fun Body() {
        var shouldAddNumbers by remember { mutableStateOf(false) }
        var shouldAddSpecialChars by remember { mutableStateOf(false) }

        val btnGenerate = remember {
            mutableStateOf(false)
        }

        val passwd = remember {
            mutableStateOf("")
        }

        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(300.dp)
                    .padding(15.dp)
                    ,
                shape = RoundedCornerShape(corner = CornerSize(15.dp))
            ) {
                Slider()
                Divider(Modifier.fillMaxWidth(), color = Color.Transparent)

                /* START OF OPTIONS */
                Row (modifier = Modifier.fillMaxWidth()) {
                    Text(getString(R.string.add_numbers),
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    var checked by remember { mutableStateOf(false) }
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            shouldAddNumbers = it
                        }
                    )
                }
                Row (modifier = Modifier.fillMaxWidth()) {
                    Text(getString(R.string.add_chars),
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Left,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    var checked by remember { mutableStateOf(false) }
                    Switch(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            shouldAddSpecialChars = it
                        }
                    )
                }
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Button(
                        onClick = {
                            btnGenerate.value = true
                            passwd.value =  Keygen.generate(length, shouldAddNumbers, shouldAddSpecialChars)
                        }
                    ) {
                        Text(getString(R.string.action_generate),
                            style = MaterialTheme.typography.labelMedium)
                    }
                }
                /* END OF OPTIONS */
            }
        }

        if (btnGenerate.value) {
            Surface(
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    Header(getString(R.string.output))
                    Card(
                        modifier = Modifier
                            .width(500.dp)
                            .size(100.dp)
                            .padding(15.dp),
                        shape = RoundedCornerShape(corner = CornerSize(20.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                passwd.value,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxSize(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)) {
                Button(
                    onClick = {
                        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("label", passwd.value)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(ctx, getString(R.string.action_copied), Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(getString(R.string.action_copy),
                        style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }

    @Composable
    private fun Slider() {
        var lengthState by remember { mutableFloatStateOf(4f) }
        Column (modifier = Modifier.fillMaxWidth()) {
            Text(getString(R.string.length) + lengthState.toInt().toString(),
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.headlineSmall
            )
            Slider(value = lengthState,
                modifier = Modifier.padding(8.dp),
                steps = 60,
                valueRange = 4f..64f,
                onValueChange = {
                    lengthState = it
                    length = lengthState.toInt()
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun HeaderPreview() {
        Header("Keygen")
    }

    @Preview(showBackground = true)
    @Composable
    fun BodyPreview() {
        Body()
    }

    @Preview(showBackground = true)
    @Composable
    fun SliderPreview() {
        Slider()
    }
}

