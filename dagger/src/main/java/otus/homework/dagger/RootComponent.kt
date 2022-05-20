package otus.homework.dagger

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(modules = [AppModule::class])
interface RootComponent {

    fun inject(application: App)
}

@Module
class AppModule(val applicationContext: Context) {
    @Provides
    @Singleton
    fun providesApplication(): Context {
        return applicationContext
    }
}