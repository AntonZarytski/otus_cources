package otus.homework.reactivecats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import otus.homework.reactivecats.local_repository.UserInput

//<UserInput, InputAdapter.FactHolder>
// UserInput = что будет item модели
// InputAdapter.FactHolder - что будет item view
class InputAdapter(var viewModel : InputFactViewModel) : ListAdapter<UserInput, InputAdapter.InputHolder>(InputDiffUtils()) {

    //метод создающий view элемента
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InputHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.input_item, parent, false)
        return InputHolder(itemView, viewModel)
    }

    //метод привязывающий item model к item itemView
    override fun onBindViewHolder(holder: InputHolder, position: Int) {
        holder.bind(getItem(position))
    }

    //Класс показывающий как связывать item view c item model(элементом списка)
    class InputHolder(itemView: View, private val viewModel : InputFactViewModel): RecyclerView.ViewHolder(itemView) {

        fun bind(item: UserInput) {
            itemView.findViewById<TextView>(R.id.inputed_text).text = item.inputValue
            itemView.setOnLongClickListener{
                //удаление на лонгклик
                viewModel.removeInput(item)
                Toast.makeText(itemView.context, "Item ${item.inputId} was deleted", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    //Класс показывающий RV как отличать items
    class InputDiffUtils: DiffUtil.ItemCallback<UserInput>() {

        override fun areItemsTheSame(oldItem: UserInput, newItem: UserInput): Boolean {
            return oldItem.inputId == newItem.inputId
        }

        override fun areContentsTheSame(oldItem: UserInput, newItem: UserInput): Boolean {
            return oldItem.inputValue == newItem.inputValue
        }

    }
}