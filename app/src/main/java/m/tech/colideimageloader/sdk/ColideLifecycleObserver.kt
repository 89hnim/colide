package m.tech.colideimageloader.sdk

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal abstract class ColideLifecycleObserver : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {}

    override fun onCreate(owner: LifecycleOwner) {}

    override fun onResume(owner: LifecycleOwner) {}

    override fun onPause(owner: LifecycleOwner) {}

    override fun onStop(owner: LifecycleOwner) {}

    override fun onDestroy(owner: LifecycleOwner) {}
}
