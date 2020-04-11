package me.ryanpierce.channelsandworkerpooldemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var listView: RecyclerView
    private lateinit var workerPool: WorkerPool<String, Photo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.userListView)
        val adapter = UserListAdapter()
        listView.adapter = adapter

        // Start a worker pool that retrieves photos of users
        //   based on their uid. By default, 4 coroutines
        //   are launched into the pool.
        workerPool = WorkerPool { uid ->
            uid.getPhoto(this)
        }

        // Receive photos from the worker pool and add
        //   them to the users in the listView.
        launch {
            workerPool.receiveChannel.consumeEach { photo ->
                adapter.updateUserPhoto(photo)
            }
        }

        // Stream a flow of users into the listView and
        //   send a request to the worker pool to retrieve
        //   a photo of each user.
        User.all.onEach { user ->
            adapter.addUser(user)
            workerPool.sendChannel.send(user.uid)
        }.launchIn(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        workerPool.cancel() // Clean up our worker pool
        cancel()
    }
}
