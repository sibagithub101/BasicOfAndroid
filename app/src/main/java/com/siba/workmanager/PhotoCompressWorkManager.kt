package com.siba.workmanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.roundToInt

class PhotoCompressWorkManager(
    private val appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO){
            val stringUrl = params.inputData.getString(KEY_CONTENT_URI)
            val compressThreshold = params.inputData.getLong(KEY_COMPRESSION_THRESHOLD,0L)
            val uri = Uri.parse(stringUrl)
            val bytes = appContext.contentResolver.openInputStream(uri)?.use {
                it.readBytes()
            } ?: return@withContext Result.failure()
            val bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
            var outputByte : ByteArray
            var quality = 100
            do{
               val outputStream = ByteArrayOutputStream()
               outputStream.use { outputStream ->
                   bitMap.compress(Bitmap.CompressFormat.JPEG,quality,outputStream)
                   outputByte = outputStream.toByteArray()
                   quality= (quality * 0.1).roundToInt()
               }
            } while (outputByte.size > compressThreshold && quality > 5)
            val file = File(appContext.cacheDir, "${params.id}.jpg")
             file.writeBytes(outputByte)

            Result.success(
                workDataOf(
                    KEY_RESULT_PATH to file.absolutePath
                )
            )
        }

    }

    companion object {
        const val KEY_CONTENT_URI = "KeyContentUri"
        const val KEY_COMPRESSION_THRESHOLD = "KeyContainThreshold"
        const val KEY_RESULT_PATH = "KeyResultPath"
    }
}