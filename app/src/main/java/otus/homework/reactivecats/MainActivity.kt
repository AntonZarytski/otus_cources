package otus.homework.reactivecats

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private val adapter = InputAdapter()
    private val diContainer = DiContainer()

    private val catsViewModel by viewModels<InputFactViewModel> {
        CatsViewModelFactory(diContainer.localRepository(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)
        initListeners()
        catsViewModel.inputedFacts.observe(this) {
            attachInputList(it)
        }
        catsViewModel.inputError.observe(this) {
            findViewById<TextInputLayout>(R.id.new_input_layout).error = it
        }
    }

    private fun initListeners() {
        val addButton = findViewById<Button>(R.id.add_input)
        val inputEditText = findViewById<TextInputEditText>(R.id.new_input_edit)
        addButton.setOnClickListener {
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

    private fun attachInputList(list: List<Fact>) {
        adapter.submitList(list)
        findViewById<RecyclerView>(R.id.text_list).adapter = adapter
    }
}