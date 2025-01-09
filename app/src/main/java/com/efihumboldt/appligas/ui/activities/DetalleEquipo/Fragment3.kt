package com.efihumboldt.appligas.ui.activities.DetalleEquipo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.databinding.Fragment3Binding
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.JugadorDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.JugadorDAO
import com.efihumboldt.appligas.services.JugadorService
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class Fragment3 : Fragment() {

    private lateinit var viewModel : DetalleEquipoViewModel
    private lateinit var viewModelTorneo : DetalleTorneoViewModel
    private lateinit var binding : Fragment3Binding

    private lateinit var recyclerViewPlantel : RecyclerView
    private lateinit var playersAdaper : PlantelAdapter
    private lateinit var swipeRefreshLayoutActivityEquipos : SwipeRefreshLayout
    private var cargarDatosJob : Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DetalleEquipoViewModel::class.java)
        viewModelTorneo = ViewModelProvider(this).get(DetalleTorneoViewModel::class.java)
        recyclerViewPlantel = binding.recyclerViewPlantel
        swipeRefreshLayoutActivityEquipos = binding.swipeRefreshLayoutActivityEquipo3

        val bd = viewModel.bd
        val selectedTournament = viewModel.selectedTournament
        val selectedTeam = viewModel.selectedTeam

        val apiService : ApiService = ApiServiceImpl(requireContext(), bd)

        val jugadorDAO : JugadorDAO = JugadorDAOImpl(apiService.jugadorApiService, bd, requireContext())
        val jugadorService = JugadorService(jugadorDAO)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (!isDetached) {
                    val playersList = jugadorService.getJugadoresByTeamID(selectedTeam!!.id)
                    playersAdaper = PlantelAdapter(playersList, SharedDataHolder.bd, selectedTeam.id, requireContext())
                    recyclerViewPlantel.layoutManager = LinearLayoutManager(requireContext())
                    recyclerViewPlantel.adapter = playersAdaper
                }
            } catch (_ : CancellationException) {

            }
        }

        swipeRefreshLayoutActivityEquipos.setOnRefreshListener {
            cargarDatosJob?.cancel()
            cargarDatosJob = CoroutineScope(Dispatchers.Main).launch {
                try {
                    if (!isDetached) {
                        val playersList = jugadorService.getJugadoresByTeamID(selectedTeam!!.id)
                        playersAdaper = PlantelAdapter(playersList, SharedDataHolder.bd, selectedTeam.id, requireContext())
                        recyclerViewPlantel.layoutManager = LinearLayoutManager(requireContext())
                        recyclerViewPlantel.adapter = playersAdaper
                        swipeRefreshLayoutActivityEquipos.isRefreshing = false
                    }
                } catch (_ : CancellationException) {
                    }
            }
        }
    }
}