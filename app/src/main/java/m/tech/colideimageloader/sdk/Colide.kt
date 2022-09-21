package m.tech.colideimageloader.sdk

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import m.tech.colideimageloader.sdk.request.ColideRequest

object Colide {

    fun with(owner: LifecycleOwner): ColideRequest {
       return ColideRequest.create(owner)
    }

}