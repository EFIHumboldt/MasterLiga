package com.efihumboldt.appligas.ui.menu_inferior_torneo.principal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.efihumboldt.appligas.databinding.FragmentPrincipalSegundoBinding

class PrincipalSegundoFragment : Fragment() {
    private var _binding: FragmentPrincipalSegundoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrincipalSegundoBinding.inflate(inflater, container, false)
        return binding.root
    }
}