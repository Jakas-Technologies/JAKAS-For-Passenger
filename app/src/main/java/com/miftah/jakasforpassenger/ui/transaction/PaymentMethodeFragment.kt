package com.miftah.jakasforpassenger.ui.transaction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miftah.jakasforpassenger.databinding.FragmentPaymentMethodeBinding
import com.miftah.jakasforpassenger.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentMethodeFragment : Fragment() {

    private lateinit var listener : OnButtonSelectedCard

    private var _binding: FragmentPaymentMethodeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardGopayTransaction.setOnClickListener {
            viewModel.choosePayment(Constants.Payment.GOPAY)
            listener.onExpandBottomSheet()
        }
        binding.cardShopeepayTransaction.setOnClickListener {
            viewModel.choosePayment(Constants.Payment.SHOPEEPAY)
            listener.onExpandBottomSheet()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnButtonSelectedCard) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnExpandBottomSheetListener")
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    interface OnButtonSelectedCard {
        fun onExpandBottomSheet()
    }
}