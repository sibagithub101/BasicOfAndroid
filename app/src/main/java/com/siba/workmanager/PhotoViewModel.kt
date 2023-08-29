package com.siba.workmanager

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.UUID

class PhotoViewModel : ViewModel() {
    var unCompressUri: Uri? by mutableStateOf(null)
        private set
    var compressBitMap: Bitmap? by mutableStateOf(null)
        private set
    var workId: UUID? by mutableStateOf(null)
        private set

    fun updateUncompressUri(uri:Uri?){
        unCompressUri = uri
    }

    fun updateCompressBitMap(bmp:Bitmap?){
        compressBitMap = bmp
    }
    fun updateWorkId(uuid:UUID){
        workId = uuid
    }



}