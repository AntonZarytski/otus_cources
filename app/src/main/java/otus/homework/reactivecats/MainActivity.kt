package otus.homework.reactivecats

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import otus.homework.reactivecats.local_repository.UserInput

class MainActivity : AppCompatActivity() {

    private val catsViewModel by viewModels<InputFactViewModel> {
        CatsViewModelFactory(diContainer.localRepository(applicationContext))
    }
    private val adapter by lazy {InputAdapter(catsViewModel)}
    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)
        initListeners()
        //view обновляется из-за изменений в LiveData тут
        catsViewModel.inputedFacts.observe(this) {
            attachInputList(it)
        }
        //подписка на вывод ошибок
        catsViewModel.inputError.observe(this) {
            findViewById<TextInputLayout>(R.id.new_input_layout).error = it
        }
    }

    private fun initListeners() {
        val addButton = findViewById<Button>(R.id.add_input)
        val inputEditText = findViewById<TextInputEditText>(R.id.new_input_edit)
        addButton.setOnClickListener {
            //добавление инпута в базу
            catsViewModel.addNewInput(inputEditText.text.toString())
        }
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                findViewById<TextInputLayout>(R.id.new_input_layout).error = ""
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun attachInputList(list: List<UserInput>) {
        adapter.submitList(list)
        val recycler: RecyclerView = findViewById(R.id.text_list)
        if(recycler.adapter == null || recycler.adapter?.itemCount == 0) {
            recycler.adapter = adapter
        }
        adapter.notifyDataSetChanged()
    }
}