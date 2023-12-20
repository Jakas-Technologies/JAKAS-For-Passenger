package com.miftah.jakasforpassenger.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miftah.jakasforpassenger.databinding.FragmentLoginBinding
import com.miftah.jakasforpassenger.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.editText?.text.toString()
            val pass = binding.edLoginPassword.editText?.text.toString()
            viewModel.createSave("Miftah")
            Intent(activity, MainActivity::class.java).let {
                startActivity(it)
            }
            /*viewModel.userLogin(email, pass).observe(viewLifecycleOwner) { data ->
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

                        activity?.finish()
                    }
                }
            }*/
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}