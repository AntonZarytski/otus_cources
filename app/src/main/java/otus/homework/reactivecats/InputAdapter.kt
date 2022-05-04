package otus.homework.reactivecats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

//<Fact, InputAdapter.FactHolder>
// Fact = что будет item модели
// InputAdapter.FactHolder - что будет item view
class InputAdapter() : ListAdapter<Fact, InputAdapter.FactHolder>(FactDiffUtils()) {

    //метод создающий view элемента
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.input_item, parent, false)
        return FactHolder(itemView)
    }

    //метод привязывающий item model к item itemView
    override fun onBindViewHolder(holder: FactHolder, position: Int) {
        holder.bind(getItem(position))
    }

    //Класс показывающий как связывать item view c item model(элементом списка)
    class FactHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: Fact) {
            itemView.findViewById<TextView>(R.id.inputed_text).text = item.text
        }

    }

    //Класс показывающий RV как отличать items
    class FactDiffUtils: DiffUtil.ItemCallback<Fact>() {

        override fun areItemsTheSame(oldItem: Fact, newItem: Fact): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: Fact, newItem: Fact): Boolean {
            return oldItem.text == newItem.text
        }

    }
}