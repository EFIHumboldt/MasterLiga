import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.R

class CustomArrayAdapter(
    context: Context,
    resource: Int,
    private val selectedTournament: Torneo?,
    private val fechasDeportivas: List<FechaDeportiva>,
    private val customFontPath: String? = null,
     private val styleResId: Int? = null
) : ArrayAdapter<FechaDeportiva>(context, resource, fechasDeportivas) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent, true)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent, false)
    }

    private fun createCustomView(position: Int, convertView: View?, parent: ViewGroup, isItemSelected: Boolean): View {
        val view: View = convertView ?: inflater.inflate(R.layout.custom_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        val fechaDeportiva = getItem(position)

        styleResId?.let { textView.setTextAppearance(context, it) }


        if (fechaDeportiva != null) {
            val rangoFechas : String
            if(fechaDeportiva.fechaInicio != null || fechaDeportiva.fechaFin != null)
            {
                 rangoFechas = "Fecha ${fechaDeportiva.valor} (${fechaDeportiva.fechaInicio} / ${fechaDeportiva.fechaFin})"
            }
            else
            {
                rangoFechas = "Fecha ${fechaDeportiva.valor}"
            }
            textView.text = rangoFechas
        } else {
            textView.text = ""
        }


        customFontPath?.let {
            val customTypeface = ResourcesCompat.getFont(context, R.font.montserrat_regular)
            textView.typeface = customTypeface
        }

        // Aplica color diferente al elemento seleccionado
        if (isItemSelected) {
            textView.setTextColor(context.resources.getColor(R.color.white))
        } else {
            textView.setTextColor(context.resources.getColor(R.color.spinnerItem))
        }

        // Ajusta el tamaño del texto
        textView.textSize = 18f

        // Centra el texto
        textView.gravity = Gravity.CENTER

        return view
    }
    fun getSelectedPosition(): Int {
        for ((index, fechaDeportiva) in fechasDeportivas.withIndex()) {
            if (fechaDeportiva.mostrar) {
                return index
            }
        }
        return -1
    }
}
