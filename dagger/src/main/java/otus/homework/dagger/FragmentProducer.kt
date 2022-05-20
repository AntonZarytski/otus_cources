package otus.homework.dagger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class FragmentProducer : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val producerComnponent = DaggerFragmentProducerComponent.factory().create()
        view.findViewById<Button>(R.id.button).setOnClickListener {
            //отправить результат через livedata в другой фрагмент
        }
    }
}