package otus.homework.dagger

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@FragmentScope
@Component(dependencies = [ActivityComponent::class], modules = [FragmentReceiverModule::class])
interface FragmentReceiverComponent {
    var context: Context
    var vmReceiver: ViewModelReceiver

    @Component.Factory
    interface Factory {
        fun create(activityComponent: ActivityComponent): FragmentReceiverComponent
    }
}

@Module
class FragmentReceiverModule(var context: Context) {

    @Provides
    @FragmentScope
    fun provideVMReceiver(owner: ViewModelStoreOwner) :  ViewModelReceiver{
        return ViewModelProvider(owner, MyViewModelFactory(context))[ViewModelReceiver::class.java]
    }
}

@FragmentScope
@Component(dependencies = [ActivityComponent::class], modules = [FragmentProducerModule::class])
interface FragmentProducerComponent {

    var context: Context
    var vmProducer: ViewModelProducer

    @Component.Factory
    interface Factory {
        fun create(activityComponent: ActivityComponent): FragmentProducerComponent
    }
}
@Module
class FragmentProducerModule(var context: Context) {

    @Provides
    @FragmentScope
    fun provideVMReceiver(owner: ViewModelStoreOwner) :  ViewModelProducer{
        return ViewModelProvider(owner, MyViewModelFactory(context))[ViewModelProducer::class.java]
    }
}

@Scope
annotation class FragmentScope