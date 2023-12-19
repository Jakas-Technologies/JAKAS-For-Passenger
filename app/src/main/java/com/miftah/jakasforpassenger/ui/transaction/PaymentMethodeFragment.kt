package com.miftah.jakasforpassenger.ui.transaction

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.miftah.jakasforpassenger.databinding.FragmentPaymentMethodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentMethodeFragment : Fragment() {

    private lateinit var listener : OnButtonSelectedCard

    private var _binding: FragmentPaymentMethodeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentMethodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        paymentBottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.payment_inc))
        binding.cardGopayTransaction.setOnClickListener {
            listener.onExpandBottomSheet()
            viewModel.initPayment("GoPay")
        }
        binding.cardShopeepayTransaction.setOnClickListener {
            listener.onExpandBottomSheet()
            viewModel.initPayment("Shopee")
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