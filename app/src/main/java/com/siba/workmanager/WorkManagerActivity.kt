package com.siba.workmanager

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.AsyncImage
import com.siba.workmanager.ui.theme.ContentproviderTheme

class WorkManagerActivity : ComponentActivity() {
    private lateinit var workManager: WorkManager
    private val viewModel by viewModels<PhotoViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(applicationContext)
        setContent {
            ContentproviderTheme {
                val workerResult = viewModel.workId?.let { id ->
                    workManager.getWorkInfoByIdLiveData(id).observeAsState().value
                }
                LaunchedEffect(key1 = workerResult?.outputData) {
                    if (workerResult?.outputData != null) {
                        val filePath = workerResult.outputData.getString(PhotoCompressWorkManager.KEY_RESULT_PATH)
                        filePath?.let {
                            val bitMap = BitmapFactory.decodeFile(it)
                            viewModel.updateCompressBitMap(bitMap)
                        }
                    }
                }
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    viewModel.unCompressUri?.let {
                        Text(text = "Uncompressed Photo:")
                        AsyncImage(model = it, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    viewModel.compressBitMap?.let {
                        Text(text = "Compressed Photo:")
                        AsyncImage(model = it, contentDescription = null)
                    }

                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM)
        } ?: return

        viewModel.updateUncompressUri(uri)

        val request = OneTimeWorkRequestBuilder<PhotoCompressWorkManager>()
            .setInputData(
                workDataOf(
                    PhotoCompressWorkManager.KEY_CONTENT_URI to uri.toString(),
                    PhotoCompressWorkManager.KEY_COMPRESSION_THRESHOLD to 1024 * 20L
                )
            )
            .build()
        viewModel.updateWorkId(request.id)
        workManager.enqueue(request)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ContentproviderTheme {

    }
}