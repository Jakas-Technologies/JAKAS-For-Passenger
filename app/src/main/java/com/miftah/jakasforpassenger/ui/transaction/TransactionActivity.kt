package com.miftah.jakasforpassenger.ui.transaction

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.databinding.ActivityTransactionBinding
import com.miftah.jakasforpassenger.utils.Angkot
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_DEPARTMENT_ANGKOT
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_QR_CODE
import com.miftah.jakasforpassenger.utils.QrScanning
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity(), PaymentMethodeFragment.OnButtonSelectedCard {

    private lateinit var binding: ActivityTransactionBinding
    private lateinit var paymentBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var angkot: Angkot? = null
    private var driverIdentity: QrScanning? = null

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

        if (Build.VERSION.SDK_INT >= 33) {
            angkot = intent.getParcelableExtra(EXTRA_DEPARTMENT_ANGKOT, Angkot::class.java)
            driverIdentity = intent.getParcelableExtra(EXTRA_QR_CODE, QrScanning::class.java)
        } else {
            angkot = intent.getParcelableExtra(EXTRA_DEPARTMENT_ANGKOT)
            driverIdentity = intent.getParcelableExtra(EXTRA_QR_CODE)
        }

        if (angkot != null || driverIdentity != null) {
            viewModel.initActivity(angkot!!, driverIdentity!!)
        }

        viewModel.driverIdentity.observe(this) {
            binding.paymentInc.amountHasSelected.text = 1.toString()
            binding.paymentInc.driverHasSelected.text = it.name
        }

        viewModel.angkot.observe(this) {
            binding.paymentInc.priceHasSelected.text = it.price.toString()
        }

        viewModel.paymentMethode.observe(this) {
            binding.paymentInc.paymentHasSelected.text = it
        }

        binding.paymentInc.btnFinishTransaction.setOnClickListener {

        }

        paymentBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        paymentBottomSheetBehavior.isDraggable = false
    }

    override fun onExpandBottomSheet() {
        if (paymentBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            paymentBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}