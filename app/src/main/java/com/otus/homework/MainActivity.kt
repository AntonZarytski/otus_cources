package com.otus.homework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private var catsPresenter: CatsPresenter? = null

    private var catsViewModel: CatsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        //TODO Uncomment to use presentor mode
//        catsPresenter = CatsPresenter(diContainer.service)
        if (catsPresenter != null) {
            view.presenter = catsPresenter
            catsPresenter?.attachView(view)
            catsPresenter?.onInitComplete()
        }

        catsViewModel = ViewModelFactory(diContainer.service).create(CatsViewModel::class.java)
        if (catsViewModel != null) {
            view.viewModel = catsViewModel
            catsViewModel?.onInitComplete()
            catsViewModel?.imageVm?.observe(this) { image ->
                view.setImage(image.url)
            }
            catsViewModel?.factVm?.observe(this) { fact ->
                view.populate(fact)
            }
            catsViewModel?.errorVm?.observe(this) { message ->
                view.connectionError(message)
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter?.detachView()
        }
        super.onStop()
    }
}