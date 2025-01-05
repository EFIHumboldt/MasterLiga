package com.efihumboldt.appligas.ui.menu_inferior_torneo.partidos

import CustomArrayAdapter
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.FechaDeportiva
import com.efihumboldt.appligas.entidades.Torneo
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.BannerDAOImpl
import com.efihumboldt.appligas.implementaciones.FechaDeportivaDAOImpl
import com.efihumboldt.appligas.implementaciones.PartidoDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import com.efihumboldt.appligas.interfaces.FechaDeportivaDAO
import com.efihumboldt.appligas.interfaces.PartidoDAO
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.services.BannerService
import com.efihumboldt.appligas.services.FechaDeportivaService
import com.efihumboldt.appligas.services.PartidoService
import com.efihumboldt.appligas.ui.activities.DetallePartido.DetallePartidoViewModel
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import com.efihumboldt.appligas.Varios.SpaceItemDecoration
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.FragmentPartidosBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class PartidosFragment : Fragment() {

    private var _binding: FragmentPartidosBinding? = null
    private lateinit var viewModel: DetalleTorneoViewModel
    private lateinit var viewModelPartido: DetallePartidoViewModel
    private var listaFechasCalendario: List<FechaDeportiva> = mutableListOf()
    private var listaFechasPartido: List<FechaDeportiva> = mutableListOf()
    private lateinit var recyclerViewFechas: RecyclerView
    private lateinit var swipeRefreshLayoutFragmentPartidos : SwipeRefreshLayout
    private lateinit var adapterArray : CustomArrayAdapter
    private lateinit var spinner : Spinner
    private var cargaDatosJob : Job? = null
    private var cargaDatosJob2 : Job? = null
    private var cargaDatosJob3 : Job? = null
    private lateinit var bannerList : List<Banner>
    
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[PartidosViewModel::class.java]

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(requireContext())

        _binding = FragmentPartidosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerViewFechas = binding.recyclerViewFecha
        recyclerViewFechas.layoutManager = LinearLayoutManager(activity)

        spinner = binding.spinnerFechas

        swipeRefreshLayoutFragmentPartidos = binding.swipeRefreshLayoutFragmentPartidos

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.espaciado_entre_fechas)
        recyclerViewFechas.addItemDecoration(SpaceItemDecoration(spacingInPixels))

        viewModel = ViewModelProvider(requireActivity())[DetalleTorneoViewModel::class.java]
        viewModelPartido = ViewModelProvider(requireActivity())[DetallePartidoViewModel::class.java]

        val selectedTournament = viewModel.torneoSeleccionado
        spinner.setBackgroundColor(Color.parseColor(selectedTournament?.color))
        val bd = viewModel.bd

        val apiServiceFecha: ApiService = ApiServiceImpl(requireContext(), bd)
        val partidoDAO: PartidoDAO = PartidoDAOImpl(apiServiceFecha.partidoApiService, bd, requireContext())
        val partidoService = PartidoService(partidoDAO)

        val apiService: ApiService = ApiServiceImpl(requireContext(), bd)
        val fechaDeportivaDAO: FechaDeportivaDAO = FechaDeportivaDAOImpl(apiService.fechaDeportivaApiService, bd, selectedTournament?.divisionID, requireContext())
        val fechaDeportivaService = FechaDeportivaService(fechaDeportivaDAO)




try {
    cargaDatosJob2 = viewLifecycleOwner.lifecycleScope.launch {
        coroutineScope {
            val bannerDAO: BannerDAO = BannerDAOImpl(
                apiService.bannerApiService,
                bd,
                selectedTournament!!.divisionID,
                requireContext()
            )
            val bannerService = BannerService(bannerDAO)
            bannerList =
                bannerService.getBanners2ByDivisionID(viewModel.torneoSeleccionado!!.divisionID)

            listaFechasCalendario = fechaDeportivaService.getAllMatchdays()
            adapterArray = CustomArrayAdapter(
                requireContext(),
                R.layout.custom_spinner_item,
                selectedTournament,
                listaFechasCalendario,
                null,
                R.style.TamanioLimitadov26
            )
            spinner.adapter = adapterArray

            val selectedPosition = adapterArray.getSelectedPosition()

            // Selecciona el elemento en el Spinner
            if (selectedPosition != -1) {
                spinner.setSelection(selectedPosition)
            }
        }
    }
} catch (e: HttpException) {
    e.response()?.code()
} catch (e: IOException) {
    e.printStackTrace()
}




val textView: TextView = binding.textPartidos
homeViewModel.text.observe(viewLifecycleOwner) {
    textView.text = it
}

spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
        binding.progressBar.visibility = View.VISIBLE
        try {
            cargaDatosJob3 = viewLifecycleOwner.lifecycleScope.launch {
                coroutineScope {
                    listaFechasPartido =
                        partidoService.getMatchsByMatchdayID(listaFechasCalendario[position].id)
                    val fechasCalendarioAdapter = FechaDeportivaAdapter(
                        listaFechasPartido,
                        bd,
                        requireActivity(),
                        recyclerViewFechas,
                        viewModelPartido,
                        bannerList
                    )
                    recyclerViewFechas.adapter = fechasCalendarioAdapter
                    binding.progressBar.visibility = View.GONE
                }
            }

        } catch (e: HttpException) {
            e.response()?.code()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onNothingSelected(parentView: AdapterView<*>) {
        // Aquí puedes realizar acciones cuando no se ha seleccionado ningún ítem
    }
}

swipeRefreshLayoutFragmentPartidos.setOnRefreshListener {
    cargaDatosJob?.cancel()
    cargaDatosJob2?.cancel()
    cargaDatosJob3?.cancel()
    cargaDatosJob = viewLifecycleOwner.lifecycleScope.launch {

        try {
            coroutineScope {

                val bannerDAO: BannerDAO = BannerDAOImpl(apiService.bannerApiService, bd, selectedTournament!!.divisionID, requireContext())
                val bannerService = BannerService(bannerDAO)
                bannerList = bannerService.getBanners2ByDivisionID(viewModel.torneoSeleccionado!!.divisionID)
                cargarDatos(fechaDeportivaService,2000, selectedTournament!!)
            }

        } catch (e: CancellationException) {

        }
    }
}


return root
}

override fun onDestroyView() {
super.onDestroyView()
cargaDatosJob?.cancel()
    cargaDatosJob2?.cancel()
    cargaDatosJob3?.cancel()
_binding = null
}


private suspend fun cargarDatos(fechaDeportivaService: FechaDeportivaService, delay: Long, selectedTournament: Torneo) {
    val spinnerPosition = spinner.selectedItemPosition
    val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
    setInteraccionUsuarioHabilitada(navView, false)

    delay(delay)

    try {
        listaFechasCalendario = fechaDeportivaService.getAllMatchdays()

        withContext(Dispatchers.Main) {
            if (isActive) {
                adapterArray = CustomArrayAdapter(
                    requireContext(),
                    R.layout.custom_spinner_item,
                    selectedTournament,
                    listaFechasCalendario,
                    null,
                    R.style.TamanioLimitadov26
                )
                spinner.adapter = adapterArray
                swipeRefreshLayoutFragmentPartidos.isRefreshing = false
                setInteraccionUsuarioHabilitada(navView, true)
                spinner.setSelection(spinnerPosition)
            }


        }
    } catch (e: HttpException) {
        e.response()?.code()
        swipeRefreshLayoutFragmentPartidos.isRefreshing = false
        setInteraccionUsuarioHabilitada(navView, true)
        spinner.setSelection(spinnerPosition)
    } catch (e: IOException) {
        e.printStackTrace()
        swipeRefreshLayoutFragmentPartidos.isRefreshing = false
        setInteraccionUsuarioHabilitada(navView, true)
        spinner.setSelection(spinnerPosition)
    }

}

private fun setInteraccionUsuarioHabilitada(navigationView: BottomNavigationView, habilitado: Boolean) {
for (i in 0 until navigationView.menu.size()) {
    navigationView.menu.getItem(i).isEnabled = habilitado
}
}
}
