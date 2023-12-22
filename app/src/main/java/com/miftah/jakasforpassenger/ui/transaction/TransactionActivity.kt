package com.miftah.jakasforpassenger.ui.transaction

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.DriversItem
import com.miftah.jakasforpassenger.core.data.source.remote.dto.response.FareResponse
import com.miftah.jakasforpassenger.databinding.ActivityTransactionBinding
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_DEPARTMENT_ANGKOT
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_PRICE
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_QR_CODE
import com.miftah.jakasforpassenger.utils.Constants.EXTRA_URL_REDIRECT
import com.miftah.jakasforpassenger.utils.QrScanning
import com.miftah.jakasforpassenger.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity(), PaymentMethodeFragment.OnButtonSelectedCard {

    private lateinit var binding: ActivityTransactionBinding
    private lateinit var paymentBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var angkot: DriversItem? = null
    private var driverIdentity: QrScanning? = null
    private var price : FareResponse? = null

    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.payment_inc))
        paymentBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.paymentInc.btnFinishTransaction.setOnClickListener {
            viewModel.paymentMethode.observe(this) {
                val data = viewModel.midtransRequest()
                data?: return@observe
                viewModel.initTransaction(data, it).observe(this) { result ->
                    when (result) {
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Err", Toast.LENGTH_SHORT).show()
                        }

                        Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            result.data.actions.forEach { action ->
                                if (action.name == "deeplink-redirect") {
                                    Intent(this, PaymentActivity::class.java).let { intent ->
                                        intent.putExtra(EXTRA_URL_REDIRECT, action.url)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= 33) {
            angkot = intent.getParcelableExtra(EXTRA_DEPARTMENT_ANGKOT, DriversItem::class.java)
            driverIdentity = intent.getParcelableExtra(EXTRA_QR_CODE, QrScanning::class.java)
            price = intent.getParcelableExtra(EXTRA_PRICE, FareResponse::class.java)
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

        viewModel.price.observe(this) {
            binding.paymentInc.priceHasSelected.text = it.data.fuelPrice.toString()
        }

        viewModel.paymentMethode.observe(this) {
            binding.paymentInc.paymentHasSelected.text = it.name
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