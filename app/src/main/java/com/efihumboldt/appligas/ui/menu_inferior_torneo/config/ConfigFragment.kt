package com.efihumboldt.appligas.ui.menu_inferior_torneo.config
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.efihumboldt.appligas.Varios.SharedDataHolder
import com.efihumboldt.appligas.Varios.UpdateManager
import com.efihumboldt.appligas.databinding.FragmentConfigBinding


class ConfigFragment : Fragment() {

    private var _binding: FragmentConfigBinding? = null
    private lateinit var webView : WebView
    private val bd = SharedDataHolder.bd

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentConfigBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val updateManager = UpdateManager()
        updateManager.checkAndForceUpdate(requireContext())

        return root
    }

    private fun cargarXmlDesdeServidor(view: View) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}