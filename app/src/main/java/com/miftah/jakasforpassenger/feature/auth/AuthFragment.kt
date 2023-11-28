package com.miftah.jakasforpassenger.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.core.data.source.ViewModelFactory
import com.miftah.jakasforpassenger.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by activityViewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAuthToRegis.setOnClickListener {
            it.findNavController().navigate(R.id.action_authFragment_to_registerFragment)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}