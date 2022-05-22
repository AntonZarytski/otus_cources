package otus.homework.dagger

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    var context: Context

    fun injectInto(producer: FragmentProducer)
    fun injectInto(receiver: FragmentReceiver)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): ActivityComponent
    }
}

@Module
class ActivityModule {
    @get:Provides
    @ActivityScope
    var observer = ColorFlow()
}

@Scope
annotation class ActivityScope