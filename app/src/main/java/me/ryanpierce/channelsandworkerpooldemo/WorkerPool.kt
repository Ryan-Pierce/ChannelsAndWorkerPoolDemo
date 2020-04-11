package me.ryanpierce.channelsandworkerpooldemo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class WorkerPool<T, R>(
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    concurrency: Int = 4,
    transform: suspend (T) -> R
) {

    val sendChannel = Channel<T>()
    val receiveChannel = Channel<R>()

    init {
        repeat(concurrency) {
            coroutineScope.launch {
                sendChannel.consumeEach {
                    receiveChannel.send(transform(it))
                }
            }
        }
    }

    fun cancel() = coroutineScope.cancel()
}