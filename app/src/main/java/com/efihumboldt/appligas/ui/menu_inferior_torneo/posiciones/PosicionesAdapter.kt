package com.efihumboldt.appligas.ui.menu_inferior_torneo.posiciones

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.EquipoSimple
import com.efihumboldt.appligas.entidades.PosicionesPorZona
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.EquipoSimpleDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.EquipoSimpleDAO
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.services.EquipoSimpleService
import com.efihumboldt.appligas.ui.activities.DetalleEquipo.DetalleEquipoActivity
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PosicionesAdapter(
    private val listaPosicionesPorZona: List<PosicionesPorZona>?,
    private val bd: String?,
    private val teamID: Int?,
    private val context: Context,
    private val recyclerViewPosiciones : RecyclerView,
    private val bannerList : List<Banner>

) : RecyclerView.Adapter<PosicionesAdapter.PosicionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosicionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_posiciones_por_zona, parent, false)
        return PosicionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PosicionViewHolder, position: Int) {

        val zona = listaPosicionesPorZona?.get(position)?.zona
        val posiciones = listaPosicionesPorZona?.get(position)?.posiciones

        holder.textViewZona.text = zona

        val posicionesAdapter = PosicionesPorZonaAdapter(posiciones, bd, teamID, holder.itemView.context, recyclerViewPosiciones) { posicion ->

            val selectedTournament = SharedDataHolder.selectedTournament

            val apiEquipoService: ApiService = ApiServiceImpl(context, bd)
            val equipoDAO: EquipoSimpleDAO = EquipoSimpleDAOImpl(apiEquipoService.equipoSimpleApiService, bd,selectedTournament!!.divisionID, context)
            val equipoSimpleService = EquipoSimpleService(equipoDAO)

            CoroutineScope(Dispatchers.Main).launch {

                try {
                    val equipoSeleccionado : List<EquipoSimple> = equipoSimpleService.getTeamByID(posicion.equipo)
                    equipoSeleccionado.let{ SharedDataHolder.selectedTeam = (it[0])}

                    val intent = Intent(holder.itemView.context, DetalleEquipoActivity::class.java)
                    context.startActivity(intent)

                } catch (e : Exception)
                {
                    Log.e("error", e.toString())
                }


            }
        }
        if (bannerList.isNotEmpty() && bannerList.size-1 >= position)
        {
            Glide.with(holder.banner.context).load("${bd}/${bannerList[position].link}").into(holder.banner)

        }
        holder.recyclerViewPosicionesPorZona.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.recyclerViewPosicionesPorZona.adapter = posicionesAdapter
    }

    override fun getItemCount(): Int {
        return listaPosicionesPorZona?.size ?:0
    }

    inner class PosicionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textViewZona: TextView = view.findViewById(R.id.textViewZona)
        val recyclerViewPosicionesPorZona: RecyclerView = view.findViewById(R.id.recyclerViewPosicionesPorZona)
        val banner : ImageView = view.findViewById(R.id.banner1)

    }
}