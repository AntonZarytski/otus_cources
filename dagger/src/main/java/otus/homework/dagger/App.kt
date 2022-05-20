package otus.homework.dagger

import android.app.Application

class App : Application() {

    lateinit var rootComponent: RootComponent

    override fun onCreate() {
        super.onCreate()
        rootComponent = DaggerRootComponent.builder().appModule(AppModule(applicationContext)).build()
        rootComponent.inject(application = this)
    }
}