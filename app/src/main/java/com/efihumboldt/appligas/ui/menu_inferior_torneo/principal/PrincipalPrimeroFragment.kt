package com.efihumboldt.appligas.ui.menu_inferior_torneo.principal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.databinding.FragmentPrincipalBinding
import com.efihumboldt.appligas.databinding.FragmentPrincipalPrimeroBinding
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.BannerDAOImpl
import com.efihumboldt.appligas.implementaciones.PosicionDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import com.efihumboldt.appligas.interfaces.PosicionDAO
import com.efihumboldt.appligas.services.BannerService
import com.efihumboldt.appligas.services.PosicionService
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException


class PrincipalPrimeroFragment : Fragment() {

    private lateinit var viewModel: DetalleTorneoViewModel
    private lateinit var tablaPosiciones: List<Posicion>
    private lateinit var bd: String
    private lateinit var swipeRefreshLayoutFragmentPosiciones: SwipeRefreshLayout
    private lateinit var recyclerViewPosiciones: RecyclerView
    private lateinit var recyclerViewDescripcionColores: RecyclerView
    private var cargaDatosJob: Job? = null
    private var cargaDatosJob2: Job? = null
    private lateinit var bannerList: List<Banner>

    private var _binding: FragmentPrincipalPrimeroBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrincipalPrimeroBinding.inflate(inflater, container, false)
        binding.progressBar.visibility = View.VISIBLE

        binding.progressBar.visibility = View.VISIBLE

        recyclerViewPosiciones = binding.recyclerViewTablaPosiciones
        recyclerViewPosiciones.layoutManager = LinearLayoutManager(activity)

        recyclerViewDescripcionColores = binding.recyclerViewDescripcionColores
        recyclerViewDescripcionColores.layoutManager = LinearLayoutManager(activity)

        swipeRefreshLayoutFragmentPosiciones = binding.swipeRefreshLayoutFragmentPosiciones


        viewModel = ViewModelProvider(requireActivity()).get(DetalleTorneoViewModel::class.java)

        val torneoSeleccionado = viewModel.torneoSeleccionado
        bd = viewModel.bd!!


        val apiService: ApiService = ApiServiceImpl(requireContext(), bd)
        val posicionDAO: PosicionDAO = PosicionDAOImpl(
            apiService.posicionApiService,
            bd,
            torneoSeleccionado!!.divisionID,
            requireContext()
        )
        val posicionService = PosicionService(posicionDAO)





        cargaDatosJob2 = viewLifecycleOwner.lifecycleScope.launch {
            coroutineScope {
                val bannerDAO: BannerDAO = BannerDAOImpl(
                    apiService.bannerApiService,
                    bd,
                    torneoSeleccionado.divisionID,
                    requireContext()
                )
                val bannerService = BannerService(bannerDAO)
                bannerList =
                    bannerService.getBanners1ByDivisionID(viewModel.torneoSeleccionado!!.divisionID)
                    cargarDatos(posicionService, 0, torneoSeleccionado!!.divisionID)
            }
        }

        swipeRefreshLayoutFragmentPosiciones.setOnRefreshListener {
            cargaDatosJob?.cancel()
            cargaDatosJob2?.cancel()
            //VER ESTO

            cargaDatosJob = viewLifecycleOwner.lifecycleScope.launch {

                try {
                    coroutineScope {
                        binding.progressBar.visibility = View.VISIBLE
                        val bannerDAO: BannerDAO = BannerDAOImpl(
                            apiService.bannerApiService,
                            bd,
                            torneoSeleccionado.divisionID,
                            requireContext()
                        )
                        val bannerService = BannerService(bannerDAO)
                        bannerList =
                            bannerService.getBanners1ByDivisionID(viewModel.torneoSeleccionado!!.divisionID)
                        cargarDatos(posicionService, 2000, torneoSeleccionado.divisionID)
                        binding.progressBar.visibility = View.GONE
                    }

                } catch (e: CancellationException) {

                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cargaDatosJob?.cancel()
        cargaDatosJob2?.cancel()
        _binding = null
    }


    private suspend fun cargarDatos(
        posicionService: PosicionService,
        delay: Long,
        divisionID: Int
    ) {


        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        setInteraccionUsuarioHabilitada(navView, false)

        try {

            val listaDatos = posicionService.getAllPosicion()

            withContext(Dispatchers.Main) {

                if (isActive) {
                    val posicionesAdapter = PosicionesAdapter(
                        listaDatos.posicionesPorZona,
                        bd,
                        null,
                        requireActivity(),
                        recyclerViewPosiciones,
                        bannerList
                    )
                    recyclerViewPosiciones.adapter = posicionesAdapter

                    val colorsTitle: TextView = binding.tvDescripcionColores
                    if (listaDatos.coloresDescripcion.isEmpty()) {
                        colorsTitle.visibility = View.INVISIBLE
                    }
                    val descripcionColoresAdapter = DescripcionColoresAdapter(
                        listaDatos.coloresDescripcion,
                        bd,
                        requireActivity()
                    )
                    recyclerViewDescripcionColores.adapter = descripcionColoresAdapter
                    swipeRefreshLayoutFragmentPosiciones.isRefreshing = false
                    viewModel.tablaPosiciones = listaDatos.posicionesPorZona

                    setInteraccionUsuarioHabilitada(navView, true)

                    binding.progressBar.visibility = View.GONE
                }

            }

        } catch (e: HttpException) {
            e.printStackTrace()
            swipeRefreshLayoutFragmentPosiciones.isRefreshing = false
            setInteraccionUsuarioHabilitada(navView, true)
        } catch (e: IOException) {
            e.printStackTrace()
            swipeRefreshLayoutFragmentPosiciones.isRefreshing = false
            setInteraccionUsuarioHabilitada(navView, true)
        }
    }

    private fun setInteraccionUsuarioHabilitada(
        navigationView: BottomNavigationView,
        habilitado: Boolean
    ) {
        for (i in 0 until navigationView.menu.size()) {
            navigationView.menu.getItem(i).isEnabled = habilitado
        }
    }
}