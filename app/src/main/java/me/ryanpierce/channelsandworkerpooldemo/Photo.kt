package me.ryanpierce.channelsandworkerpooldemo

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * @property uid the uid of the user in the Photo.
 */
data class Photo(var uid: String, var image: Drawable?)

// This demo project uses drawables from the resource folder to create Photos.
suspend fun String.getPhoto(context: Context): Photo = withContext(Dispatchers.IO) {
    suspendCancellableCoroutine<Photo> { cancellableContinuation ->
        val drawable = with(context) {
            val resourceId = resources.getIdentifier(
                "image_${this@getPhoto}",
                "drawable",
                packageName
            )
            ContextCompat.getDrawable(this, resourceId)
        }
        val photo = Photo(this@getPhoto, drawable)
        cancellableContinuation.resume(photo)
    }
}