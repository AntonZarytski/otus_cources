package otus.homework.dagger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import javax.inject.Inject
import javax.inject.Scope

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var observer: Observer

    lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

interface Observer {
    fun send(message: String)
}

class ImplObserver @Inject constructor() : Observer {

    override fun send(message: String) {
        TODO("Not yet implemented")
    }

}

@Scope
annotation class ActivityScope

@Scope
annotation class FragmentScope