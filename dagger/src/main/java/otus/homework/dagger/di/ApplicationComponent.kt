package otus.homework.dagger

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    var context: Context

    fun injectInto(activity: MainActivity)
}

@Module
internal class AppModule(var app: App) {
    @Singleton
    @Provides
    fun provideContext(): Context {
        return app.applicationContext
    }
}