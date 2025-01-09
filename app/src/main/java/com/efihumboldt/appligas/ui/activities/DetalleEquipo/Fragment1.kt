package com.efihumboldt.appligas.ui.activities.DetalleEquipo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.databinding.Fragment1Binding
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.Partido
import com.efihumboldt.appligas.entidades.Posicion
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.BannerDAOImpl
import com.efihumboldt.appligas.implementaciones.EquipoSimpleDAOImpl
import com.efihumboldt.appligas.implementaciones.JugadorDAOImpl
import com.efihumboldt.appligas.implementaciones.PartidoDAOImpl
import com.efihumboldt.appligas.implementaciones.PosicionDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import com.efihumboldt.appligas.interfaces.EquipoSimpleDAO
import com.efihumboldt.appligas.interfaces.JugadorDAO
import com.efihumboldt.appligas.interfaces.PartidoDAO
import com.efihumboldt.appligas.interfaces.PosicionDAO
import com.efihumboldt.appligas.services.BannerService
import com.efihumboldt.appligas.services.EquipoSimpleService
import com.efihumboldt.appligas.services.JugadorService
import com.efihumboldt.appligas.services.PartidoService
import com.efihumboldt.appligas.services.PosicionService
import com.efihumboldt.appligas.ui.activities.DetallePartido.DetallePartidoActivity
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class Fragment1 : Fragment() {

    private lateinit var viewModel: DetalleEquipoViewModel
    private lateinit var viewModelTorneo: DetalleTorneoViewModel

    // Declarar tus vistas y adaptadores aquí
    private lateinit var recyclerViewTablaDetalleEquipo: RecyclerView
    private lateinit var headerLayout: View
    private lateinit var recyclerViewLastMatchs: RecyclerView
    private lateinit var recyclerViewPlantel : RecyclerView
    private lateinit var swipeRefreshLayoutActivityEquipo: SwipeRefreshLayout
    private lateinit var customToolbar: View
    private lateinit var binding: Fragment1Binding
    private lateinit var bannerList: List<Banner>
    private var cargaDatosJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento
        binding = Fragment1Binding.inflate(inflater, container, false) // Cambiar a tu clase de binding correspondiente
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar ViewModel
        viewModel = ViewModelProvider(this).get(DetalleEquipoViewModel::class.java)
        viewModelTorneo = ViewModelProvider(this).get(DetalleTorneoViewModel::class.java)

        // Inicializar vistas
        recyclerViewTablaDetalleEquipo = binding.frameTablaPosiciones.recyclerViewTablaDetalleEquipo
        recyclerViewLastMatchs = binding.frameUltimosPartidos.recyclerViewUltimosPartidos
        recyclerViewPlantel = binding.framePlantel.recyclerViewTablaDetalleEquipo
        swipeRefreshLayoutActivityEquipo = binding.swipeRefreshLayoutActivityEquipo

        val bd = viewModel.bd
        val torneoSeleccionado = viewModel.selectedTournament

        val apiService: ApiService = ApiServiceImpl(requireContext(), bd)

        val partidoDAO: PartidoDAO = PartidoDAOImpl(apiService.partidoApiService, bd, requireContext())
        val partidoService = PartidoService(partidoDAO)


        val posicionDAO: PosicionDAO = PosicionDAOImpl(apiService.posicionApiService, bd, torneoSeleccionado!!.divisionID, requireContext())
        val posicionService = PosicionService(posicionDAO)

        val jugadorDAO : JugadorDAO = JugadorDAOImpl(apiService.jugadorApiService, bd, requireContext())
        val jugadorService = JugadorService(jugadorDAO)

        val team = viewModel.selectedTeam

        if (team != null) {

            CoroutineScope(Dispatchers.Main).launch {

                binding.frameStatsEquipo.progressBar.visibility = View.VISIBLE
                binding.frameTablaPosiciones.progressBar.visibility = View.VISIBLE
                binding.frameUltimosPartidos.progressBar.visibility = View.VISIBLE
                binding.framePlantel.progressBar.visibility = View.VISIBLE
                binding.framePlantel.cabeceraTabla.yellowCard.visibility = View.GONE
                binding.framePlantel.cabeceraTabla.ball.visibility = View.GONE
                Glide.with(requireContext()).load(R.drawable.football_ball).into(binding.framePlantel.cabeceraTabla.redCard)


                cargarBanner(bd)
                cargarDatosTabla(posicionService, team.id, team.zona, 0)
                cargarDatosLastMatchs(partidoService, team.id)
                cargarDatosStats(partidoService, team.id)
                cargarDatosPlantel(jugadorService, team.id)
            }
        }

        swipeRefreshLayoutActivityEquipo.setOnRefreshListener {

            cargaDatosJob?.cancel()


            cargaDatosJob = CoroutineScope(Dispatchers.Main).launch {

                try {

                    if (!isDetached)
                    {
                        binding.frameStatsEquipo.progressBar.visibility = View.VISIBLE
                        binding.frameTablaPosiciones.progressBar.visibility = View.VISIBLE
                        binding.frameUltimosPartidos.progressBar.visibility = View.VISIBLE

                        cargarBanner(bd)
                        cargarDatosTabla(posicionService, team!!.id, team.zona, 2000)
                        cargarDatosLastMatchs(partidoService, team.id)
                        cargarDatosStats(partidoService, team.id)
                    }

                } catch (e : CancellationException)
                {

                }

            }
        }

    }

    private fun getReducedPositionTable(list: List<Posicion>, id: Int): List<Posicion> {
        val positionIndex = list.indexOfFirst { it.equipo == id }

        if (positionIndex == -1) {
            return list
        }

        val upperIndex = maxOf(0, positionIndex - 1)
        val lowerIndex = minOf(list.size - 1, positionIndex + 1)

        val result = mutableListOf<Posicion>()

        if (positionIndex == 0) {

            result.add(list[positionIndex])
            result.add(list[lowerIndex])
            result.add(list[minOf(list.size - 1, lowerIndex + 1)])
        } else if (positionIndex == list.size - 1) {

            result.add(list[maxOf(0, upperIndex - 1)])
            result.add(list[upperIndex])
            result.add(list[positionIndex])
        } else {

            result.add(list[upperIndex])
            result.add(list[positionIndex])
            result.add(list[lowerIndex])
        }

        return result
    }

    private suspend fun cargarDatosPlantel(jugadorService: JugadorService, id: Int) {
        try {
            val listaJugadores = jugadorService.getGoleadoresByTeamID(id)
            withContext(Dispatchers.Main) {
                val jugadoresAdapter = GoleadoresAdapter(listaJugadores, SharedDataHolder.bd, id, requireContext())
                val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                recyclerViewPlantel.layoutManager = layoutManager
                recyclerViewPlantel.adapter = jugadoresAdapter

                binding.framePlantel.progressBar.visibility = View.GONE
                swipeRefreshLayoutActivityEquipo.isRefreshing = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private suspend fun cargarDatosTabla(posicionService: PosicionService, id: Int, zonaDeseada: String, delay: Long) {
        delay(delay)
        try {
            val listaDatos = posicionService.getAllPosicion()
            val listaFiltrada = listaDatos.posicionesPorZona
                .flatMap { posicionesPorZona ->
                    val zonaActual = posicionesPorZona.zona
                    posicionesPorZona.posiciones.filter { zonaActual == "Zona $zonaDeseada"}
                }
                .toList()

            val listaReducida = getReducedPositionTable(listaFiltrada, id)

            withContext(Dispatchers.Main) {
                val posicionesAdapter = PosicionDetalleEquipoAdapter(listaReducida, SharedDataHolder.bd, id, requireContext())
                recyclerViewTablaDetalleEquipo.layoutManager = LinearLayoutManager(requireContext())
                recyclerViewTablaDetalleEquipo.adapter = posicionesAdapter

                posicionesAdapter.setOnItemClickListener(object : PosicionDetalleEquipoAdapter.OnItemClickListener {
                    override fun onItemClick(teamID: Int) {
                        // Llama a abrirActividadEquipoConItem dentro de una nueva corrutina
                        CoroutineScope(Dispatchers.Main).launch {
                            abrirActividadEquipoConItem(teamID)
                        }
                    }
                })

                binding.frameTablaPosiciones.progressBar.visibility = View.GONE


                swipeRefreshLayoutActivityEquipo.isRefreshing = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun cargarDatosLastMatchs(partidoService: PartidoService, id: Int) {
        try {
            val listaPartidos = partidoService.getLastMatchsByTeamID(id)
            withContext(Dispatchers.Main) {
                val ultimospartidosDetalleEquipoAdapter = UltimosPartidosDetalleEquipoAdapter(listaPartidos,
                    SharedDataHolder.bd, id, requireContext())
                val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                recyclerViewLastMatchs.layoutManager = layoutManager
                recyclerViewLastMatchs.adapter = ultimospartidosDetalleEquipoAdapter

                ultimospartidosDetalleEquipoAdapter.setOnItemClickListener(object : UltimosPartidosDetalleEquipoAdapter.OnItemClickListener {
                    override fun onItemClick(partido: Partido) {
                        abrirActividadPartidoConItem(partido)
                    }
                })

                binding.frameUltimosPartidos.progressBar.visibility = View.GONE
                swipeRefreshLayoutActivityEquipo.isRefreshing = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun cargarDatosStats(partidoService: PartidoService, id: Int) {
        try {
            val statsGoal = partidoService.getStatsGoalsByTeamID(id)
            withContext(Dispatchers.Main) {
                val golesHechosLocalTotal: TextView = requireView().findViewById(R.id.cantGolesLocalTotal)
                val promedioGolesHechosLocal: TextView = requireView().findViewById(R.id.cantGolesLocalProm)
                val golesHechosVisitaTotal: TextView = requireView().findViewById(R.id.cantGolesVisitaEnTotal)
                val promedioGolesHechosVisita: TextView = requireView().findViewById(R.id.cantGolesVisitaProm)
                val golesRecibidosLocalTotal: TextView = requireView().findViewById(R.id.cantGolesLocalConcebidosTotal)
                val promedioGolesRecibidosLocal: TextView = requireView().findViewById(R.id.cantGolesLocalConcebidosProm)
                val golesRecibidoVisitasTotal: TextView = requireView().findViewById(R.id.cantGolesVisitaConcebidosEnTotal)
                val promedioGolesRecibidoVisita: TextView = requireView().findViewById(R.id.cantGolesVisitaConcebidosProm)

                golesHechosLocalTotal.text = statsGoal.golesHechosLocalTotal.toString()
                promedioGolesHechosLocal.text = statsGoal.promedioGolesHechosLocal.toString()
                golesHechosVisitaTotal.text = statsGoal.golesHechosVisitaTotal.toString()
                promedioGolesHechosVisita.text = statsGoal.promedioGolesHechosVisita.toString()
                golesRecibidosLocalTotal.text = statsGoal.golesRecibidosLocalTotal.toString()
                promedioGolesRecibidosLocal.text = statsGoal.promedioGolesRecibidosLocal.toString()
                golesRecibidoVisitasTotal.text = statsGoal.golesRecibidosVisitaTotal.toString()
                promedioGolesRecibidoVisita.text = statsGoal.promedioGolesRecibidosVisita.toString()

                binding.frameStatsEquipo.progressBar.visibility = View.GONE

                swipeRefreshLayoutActivityEquipo.isRefreshing = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun cargarBanner(bd : String?){

        val apiService: ApiService = ApiServiceImpl(requireContext(), bd)
        val bannerDAO: BannerDAO = BannerDAOImpl(apiService.bannerApiService, bd, SharedDataHolder.selectedTournament!!.divisionID, requireContext())
        val bannerService = BannerService(bannerDAO)
        bannerList = bannerService.getBanners5ByDivisionID(SharedDataHolder.selectedTournament!!.divisionID, viewModel.selectedTeam!!.id)

        val banner : ImageView = binding.banner5

        if (bannerList.isNotEmpty())
        {
            Glide.with(banner.context).load("${bd}/${bannerList[0].link}").into(banner)
        }
    }

    private fun abrirActividadPartidoConItem(selectedMatch : Partido) {


        selectedMatch.let { viewModel.setSelectedMatch(it)}

        val intent = Intent(requireContext(), DetallePartidoActivity::class.java)
        startActivity(intent)


    }

    private suspend fun abrirActividadEquipoConItem(teamID : Int) {

        val apiService: ApiService = ApiServiceImpl(requireContext(), SharedDataHolder.bd)
        val equipoSimpleDAO: EquipoSimpleDAO = EquipoSimpleDAOImpl(apiService.equipoSimpleApiService, SharedDataHolder.bd, SharedDataHolder.selectedTournament!!.divisionID, requireContext())
        val equipoSimpleService = EquipoSimpleService(equipoSimpleDAO)


        try {
            val team = equipoSimpleService.getTeamByID(teamID)

            team[0].let { viewModelTorneo.setSelectedTeam(it)}

            val intent = Intent(requireContext(), DetalleEquipoActivity::class.java)
            startActivity(intent)

        } catch (e: HttpException) {
            //e.printStackTrace()
            e.response()?.code()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}