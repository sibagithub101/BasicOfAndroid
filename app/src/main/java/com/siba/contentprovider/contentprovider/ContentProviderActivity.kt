package com.siba.contentprovider.contentprovider

import android.Manifest
import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import coil.compose.AsyncImage
import com.siba.contentprovider.ui.theme.ContentproviderTheme
import java.util.Calendar

class ContentProviderActivity : ComponentActivity() {
private val viewModel by viewModels<ImageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES),0)
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE
        )
        val milisYesterDay = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR,-10)
        }.timeInMillis

        val selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"
        val selectionArray = arrayOf(milisYesterDay.toString())
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"


        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArray,
            sortOrder)?.use { cursor->
               val idColumn= cursor.getColumnIndex(MediaStore.Images.Media._ID)
               val idName= cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
               val idTitle= cursor.getColumnIndex(MediaStore.Images.Media.TITLE)
              val images = mutableListOf<Image>()
              while (cursor.moveToNext()){
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(idName)
                val title = cursor.getString(idTitle)
                val url = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id
                )
                images.add(Image(id,name,title,url))
            }
            viewModel.updateImages(images)
        }
        setContent {
            ContentproviderTheme {
             LazyColumn(
                 modifier = Modifier.fillMaxSize()
             ){
                 items(viewModel.images){image->
                     Column(modifier = Modifier.fillMaxWidth(),
                         horizontalAlignment = Alignment.CenterHorizontally
                         ) {
                   AsyncImage(model = image.url, contentDescription = null)
                         Text(text = image.name)
                         Text(text = image.title)
                     }
                 }
             }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ContentproviderTheme {

    }
}
data class Image(
    val id:Long,
    val name:String,
    val title:String,
    val url:Uri
)