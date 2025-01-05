package com.efihumboldt.appligas.ui.menu_inferior_torneo.noticias

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.efihumboldt.appligas.entidades.Banner
import com.efihumboldt.appligas.entidades.Noticia
import com.efihumboldt.appligas.implementaciones.ApiServiceImpl
import com.efihumboldt.appligas.implementaciones.BannerDAOImpl
import com.efihumboldt.appligas.implementaciones.NoticiaDAOImpl
import com.efihumboldt.appligas.interfaces.ApiService
import com.efihumboldt.appligas.interfaces.BannerDAO
import com.efihumboldt.appligas.interfaces.NoticiaDAO
import com.efihumboldt.appligas.R
import com.efihumboldt.appligas.services.BannerService
import com.efihumboldt.appligas.services.NoticiaService
import com.efihumboldt.appligas.ui.activities.DaetallesNoticia.DetalleNoticiaActivity
import com.efihumboldt.appligas.ui.activities.DetalleTorneo.DetalleTorneoViewModel
import com.efihumboldt.appligas.Varios.SpaceItemDecoration
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.FragmentNoticiasBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class NoticiasFragment : Fragment() {

    private var _binding: FragmentNoticiasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerViewNoticias: RecyclerView
    private lateinit var viewModel : DetalleTorneoViewModel
    private var tournamentID : Int = 0
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var cargaDatosJob: Job? = null
    private var cargaDatosJob2: Job? = null
    private lateinit var bannerList : List<Banner>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoticiasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(requireContext())

        recyclerViewNoticias = binding.recyclerViewNoticias
        recyclerViewNoticias.layoutManager = LinearLayoutManager(activity)

        swipeRefreshLayout = binding.swipeRefreshLayoutFragmentNoticias


        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        recyclerViewNoticias.layoutParams.width = screenWidth

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.espaciado_entre_noticias)
        recyclerViewNoticias.addItemDecoration(SpaceItemDecoration(spacingInPixels))


        viewModel = ViewModelProvider(requireActivity())[DetalleTorneoViewModel::class.java]
        val selectedTournament = viewModel.torneoSeleccionado
        val bd = viewModel.bd
        tournamentID = selectedTournament!!.divisionID

        val apiService: ApiService = ApiServiceImpl(requireContext(), bd)
        val noticiaDAO: NoticiaDAO = NoticiaDAOImpl(apiService.noticiaApiService, bd, selectedTournament.divisionID, requireContext())
        val noticiaService = NoticiaService(noticiaDAO)

        cargaDatosJob2 = viewLifecycleOwner.lifecycleScope.launch {
            coroutineScope {
                binding.progressBar.visibility = View.VISIBLE
                val bannerDAO: BannerDAO = BannerDAOImpl(
                    apiService.bannerApiService,
                    bd,
                    selectedTournament.divisionID,
                    requireContext()
                )
                val bannerService = BannerService(bannerDAO)
                bannerList =
                    bannerService.getBanners3ByDivisionID(viewModel.torneoSeleccionado!!.divisionID)
                cargarDatos(noticiaService, bd, 0)
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            cargaDatosJob?.cancel()
            cargaDatosJob2?.cancel()
            cargaDatosJob = viewLifecycleOwner.lifecycleScope.launch {

                try {
                    coroutineScope {
                        if (isAdded && !isRemoving) {
                            binding.progressBar.visibility = View.VISIBLE
                            cargarDatos(noticiaService, bd, 2000)
                        }
                    }
                } catch (e : CancellationException) {

                }

                }
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cargaDatosJob?.cancel()
        cargaDatosJob2?.cancel()
        _binding = null
    }

    private fun abrirActividadConItem(noticiaSeleccionada : Noticia) {
        val intent = Intent(requireContext(), DetalleNoticiaActivity::class.java)
        noticiaSeleccionada.let{viewModel.setSelectedNew(it)}
        startActivity(intent)
    }

    private suspend fun cargarDatos(noticiaService: NoticiaService, bd: String?, delay: Long) {
            val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
            setInteraccionUsuarioHabilitada(navView, false)



            try {
                val listaNoticias = noticiaService.getAllNoticias(tournamentID)
                withContext(Dispatchers.Main) {
                    if (isActive) {
                        val noticiasAdapter = NoticiasAdapter(listaNoticias, bd, requireActivity(), bannerList)
                        recyclerViewNoticias.adapter = noticiasAdapter

                        noticiasAdapter.setOnItemClickListener(object : NoticiasAdapter.OnItemClickListener {
                            override fun onItemClick(noticia: Noticia) {
                                abrirActividadConItem(noticia)
                            }
                        })

                        binding.progressBar.visibility = View.GONE

                        swipeRefreshLayout.isRefreshing = false
                        setInteraccionUsuarioHabilitada(navView, true)
                    }

                }
            } catch (e: HttpException) {
                //e.printStackTrace()
                e.response()?.code()
                swipeRefreshLayout.isRefreshing = false
                setInteraccionUsuarioHabilitada(navView, true)
            } catch (e: IOException) {
                e.printStackTrace()
                swipeRefreshLayout.isRefreshing = false
                setInteraccionUsuarioHabilitada(navView, true)
            }

    }


    private fun setInteraccionUsuarioHabilitada(navigationView: BottomNavigationView, habilitado: Boolean) {
        for (i in 0 until navigationView.menu.size()) {
            navigationView.menu.getItem(i).isEnabled = habilitado
        }
    }
}