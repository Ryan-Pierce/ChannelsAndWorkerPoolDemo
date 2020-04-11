package me.ryanpierce.channelsandworkerpooldemo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

data class User(
    val uid: String,
    var name: String,
    var status: String,
    var photo: Photo? = null
) {

    companion object {

        val all = flowOf(
            User("1a7e1633", "Sarah Collins", "Jubilant"),
            User("62e77e54", "Emily Brawn", "Energetic"),
            User("fc9a84be", "Alex Dean", "Reflective"),
            User("41634f50", "Danielle Simmons", "Joyful"),
            User("d77d7d3c", "Rita West", "Empowered"),
            User("c1f14da7", "Nathan Richards", "Confused")
        )
         // Delayed emissions so that you can watch the
         //   stream of users cascade into the ListView.
         .onEach { delay(300L) }
    }
}