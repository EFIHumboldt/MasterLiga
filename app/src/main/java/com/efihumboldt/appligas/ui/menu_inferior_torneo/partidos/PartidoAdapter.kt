package com.efihumboldt.appligas.ui.menu_inferior_torneo.partidos

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.R
import java.util.Calendar

class PartidoAdapter(private val listaPartido: List<Partido>, private val bd: String?, private val context: Context,
                     private val recyclerViewFechas : RecyclerView,
                     private val onItemClick: (Partido) -> Unit) : RecyclerView.Adapter<PartidoAdapter.PartidoViewHolder>() {


    val horaActual = Calendar.getInstance().time


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_partido, parent, false)
        return PartidoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PartidoViewHolder, position: Int) {
        val posicion = listaPartido[position]

        Glide.with(holder.imgLocal.context).load(R.drawable.escudo_default).into(holder.imgLocal)
        Glide.with(holder.imgVisita.context).load(R.drawable.escudo_default).into(holder.imgVisita)

        //Cambiar despues
        //Glide.with(holder.imgLocal.context).load("${bd}/${posicion.imgLocal}").into(holder.imgLocal)
        //Glide.with(holder.imgVisita.context).load("${bd}/${posicion.imgVisita}").into(holder.imgVisita)

        holder.nombreLocal.text = posicion.nombreLocal
        holder.nombreVisita.text = posicion.nombreVisita
        holder.resultadoLocal.text = posicion.resultadoLocal
        holder.resultadoVisita.text = posicion.resultadoVisita
        holder.estadoPartido.text = posicion.infoPartido

        if (posicion.finSegundoTiempo == null && posicion.inicioPrimerTiempo != null)
        {
            holder.estadoPartido.apply {
                typeface = ResourcesCompat.getFont(context, R.font.montserrat_bold)
                setTextColor(ContextCompat.getColor(context, R.color.verdeMatch))
            }
        }
    }

    override fun getItemCount(): Int {
        return listaPartido.size
    }

    inner class PartidoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var imgLocal: ImageView = view.findViewById(R.id.imgLocal)
        var imgVisita: ImageView = view.findViewById(R.id.imgVisita)
        var nombreLocal: TextView = view.findViewById(R.id.tvNombreLocal)
        var nombreVisita: TextView = view.findViewById(R.id.tvNombreVisita)
        var resultadoLocal: TextView = view.findViewById(R.id.tvResultadoLocal)
        var resultadoVisita: TextView = view.findViewById(R.id.tvResultadoVisita)
        var estadoPartido: TextView = view.findViewById(R.id.tvEstadoPartido)
        var link : ImageView = view.findViewById(R.id.button_youtube)

        init {
            // Listener de eventos táctiles para bloquear el desplazamiento vertical
            itemView.setOnTouchListener { _, event ->
                // Detectar eventos de desplazamiento vertical
                when (event.actionMasked) {
                    MotionEvent.ACTION_MOVE -> {
                        // Bloquear el desplazamiento vertical si el RecyclerView no puede desplazarse hacia arriba
                        if (!itemView.canScrollVertically(-1)) {
                            recyclerViewFechas.requestFocus()
                            return@setOnTouchListener true
                        }
                    }
                }
                false
            }

            // Listener de clics para manejar los clics en los elementos del RecyclerView secundario
            link.setOnClickListener {
                Toast.makeText(context, "REDIRIGIR A YOUTUBE", Toast.LENGTH_SHORT).show()
                /* CAMBIAR
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(listaPartido[position])
                }

                 */
            }
        }

    }
}