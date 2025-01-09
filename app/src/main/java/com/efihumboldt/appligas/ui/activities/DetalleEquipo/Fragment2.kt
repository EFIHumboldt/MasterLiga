package com.efihumboldt.appligas.ui.activities.DetalleEquipo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.databinding.Fragment1Binding
import com.efihumboldt.appligas.databinding.Fragment2Binding
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.BannerDAOImpl
import com.efihumboldt.appligas.implementaciones.PartidoDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import com.efihumboldt.appligas.interfaces.PartidoDAO
import com.efihumboldt.appligas.services.BannerService
import com.efihumboldt.appligas.services.PartidoService
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class Fragment2 : Fragment() {

    private lateinit var viewModel: DetalleEquipoViewModel
    private lateinit var viewModelTorneo: DetalleTorneoViewModel
    private lateinit var binding: Fragment2Binding
    private lateinit var bannerList: List<Banner>

    private lateinit var recyclerViewPartidosEquipo: RecyclerView
    private lateinit var swipeRefreshLayoutActivityEquipo: SwipeRefreshLayout
    private var cargaDatosJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento
        binding = Fragment2Binding.inflate(inflater, container, false) // Cambiar a tu clase de binding correspondiente
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DetalleEquipoViewModel::class.java)
        viewModelTorneo = ViewModelProvider(this).get(DetalleTorneoViewModel::class.java)

        recyclerViewPartidosEquipo = binding.recyclerViewPartidosEquipo

        // Inicializar vistas

        swipeRefreshLayoutActivityEquipo = binding.swipeRefreshLayoutActivityEquipo2

        val bd = viewModel.bd
        val torneoSeleccionado = viewModel.selectedTournament
        val selectedTeam = viewModel.selectedTeam

        val apiService: ApiService = ApiServiceImpl(requireContext(), bd)

        val partidoDAO: PartidoDAO = PartidoDAOImpl(apiService.partidoApiService, bd, requireContext())
        val partidoService = PartidoService(partidoDAO)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (!isDetached) {
                    cargarBanner(bd)
                }
            }
            catch (_ : CancellationException)
            {

            }
        }

        CoroutineScope(Dispatchers.Main).launch {

            try {

                if (!isDetached)
                {
                    val matchList = partidoService.getAllMatchsByTeamID(selectedTeam!!.id)
                    val partidoAdapter = PartidosEquipoAdapter(matchList, SharedDataHolder.bd)
                    recyclerViewPartidosEquipo.layoutManager = LinearLayoutManager(requireContext())
                    recyclerViewPartidosEquipo.adapter = partidoAdapter
                    val banner : ImageView = binding.banner8
                    if (bannerList.isNotEmpty())
                    {
                        Glide.with(banner.context).load("${bd}/${bannerList[0].link}").into(banner)
                    }
                }

            } catch (_ : CancellationException)
            {

            }
        }



        swipeRefreshLayoutActivityEquipo.setOnRefreshListener {

            cargaDatosJob?.cancel()


            cargaDatosJob = CoroutineScope(Dispatchers.Main).launch {
                try {

                    if (!isDetached)
                    {
                        val matchList = partidoService.getAllMatchsByTeamID(selectedTeam!!.id)
                        val partidoAdapter = PartidosEquipoAdapter(matchList, SharedDataHolder.bd)
                        recyclerViewPartidosEquipo.layoutManager = LinearLayoutManager(requireContext())
                        recyclerViewPartidosEquipo.adapter = partidoAdapter
                        swipeRefreshLayoutActivityEquipo.isRefreshing = false

                    }

                } catch (e : CancellationException)
                {

                }

            }
        }
    }

    private suspend fun cargarBanner(bd : String?){

        val apiService: ApiService = ApiServiceImpl(requireContext(), bd)
        val bannerDAO: BannerDAO = BannerDAOImpl(apiService.bannerApiService, bd, SharedDataHolder.selectedTournament!!.divisionID, requireContext())
        val bannerService = BannerService(bannerDAO)
        bannerList = bannerService.getBanners8ByDivisionID(SharedDataHolder.selectedTournament!!.divisionID, viewModel.selectedTeam!!.id)


    }

}