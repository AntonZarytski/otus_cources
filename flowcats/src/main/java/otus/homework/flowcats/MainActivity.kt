package otus.homework.flowcats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        lifecycleScope.launchWhenStarted {
            catsViewModel.factsFLow.collect{
                when(it) {
                    is Result.Success<*> -> view.populate(it.value as Fact)
                    is Result.Error -> view.printMessage(it.throwable.message)
                }
            }
        }
    }
}