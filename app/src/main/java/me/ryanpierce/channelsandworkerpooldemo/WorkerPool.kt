package me.ryanpierce.channelsandworkerpooldemo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class WorkerPool<T, R>(
    val scope: CoroutineScope,
    concurrency: Int = 4,
    transform: suspend (T) -> R
) {

    val sendChannel = Channel<T>()
    val receiveChannel = Channel<R>()

    init {
        repeat(concurrency) {
            scope.launch(Dispatchers.Default) {
                for (element in sendChannel) {
                    receiveChannel.send(transform(element))
                }
            }
        }
    }
}