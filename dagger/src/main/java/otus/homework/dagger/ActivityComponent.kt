package otus.homework.dagger

import android.content.Context
import dagger.Component
import dagger.Provides

@ActivityScope
@Component(dependencies = [RootComponent::class])
interface ActivityComponent {
    @get:Provides
    var context: Context

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(rootComponent: RootComponent): ActivityComponent
    }
}


