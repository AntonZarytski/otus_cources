package otus.homework.dagger

import android.content.Context
import dagger.Component
import dagger.Provides

@FragmentScope
@Component(dependencies = [ActivityComponent::class])
interface FragmentReceiverComponent {
    @get:Provides
    var context: Context

    @Component.Factory
    interface Factory {
        fun create(activityComponent: ActivityComponent): FragmentReceiverComponent
    }
}