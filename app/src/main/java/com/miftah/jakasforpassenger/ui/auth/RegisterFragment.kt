package com.miftah.jakasforpassenger.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.miftah.jakasforpassenger.utils.Result
import com.miftah.jakasforpassenger.databinding.FragmentRegisterBinding
import com.miftah.jakasforpassenger.ui.ViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegis.setOnClickListener {
            val username = binding.edRegisUsername.editText?.text.toString()
            val email = binding.edRegisEmail.editText?.text.toString()
            val password = binding.edRegisPassword.editText?.text.toString()
            viewModel.userRegis(username = username, email = email, password = password)
                .observe(viewLifecycleOwner) { data ->
                    when (data) {
                        is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Sukses", Toast.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                        }
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}