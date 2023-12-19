package com.miftah.jakasforpassenger.ui.transaction

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.databinding.ActivityTransactionBinding
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_AMOUNT
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_DRIVER_ID
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_PRICE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity(), PaymentMethodeFragment.OnButtonSelectedCard {

    private lateinit var binding : ActivityTransactionBinding
    private lateinit var paymentBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.payment_inc))
        paymentBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.paymentInc.btnFinishTransaction.setOnClickListener {
            Intent(this, PaymentActivity::class.java).let {
                startActivity(it)
            }
        }

        val driverId = intent.getStringExtra(EXTRA_DRIVER_ID)
        val amount = intent.getIntExtra(EXTRA_AMOUNT, 0)
        val priceTotal = intent.getIntExtra(EXTRA_PRICE, 0)

        if (driverId != null) viewModel.initActivity(driverId, amount, priceTotal)

        viewModel.amount.observe(this) {
            binding.paymentInc.amountHasSelected.text = it.toString()
        }

        viewModel.priceTotal.observe(this) {
            binding.paymentInc.priceHasSelected.text = it.toString()
        }

        viewModel.driverId.observe(this) {
            binding.paymentInc.driverHasSelected.text = it.toString()
        }

        paymentBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onExpandBottomSheet() {
        if (paymentBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            paymentBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}