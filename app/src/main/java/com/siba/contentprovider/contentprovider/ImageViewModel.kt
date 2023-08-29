package com.siba.contentprovider.contentprovider

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ImageViewModel:ViewModel() {
    var images by mutableStateOf(emptyList<Image>())
        private set
    fun updateImages(image:List<Image>){
        this.images = image
    }

}