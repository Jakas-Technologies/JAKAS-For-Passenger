package com.miftah.jakasforpassenger.ui.transaction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.miftah.jakasforpassenger.R
import com.miftah.jakasforpassenger.databinding.ActivityTransactionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity(), PaymentMethodeFragment.OnButtonSelectedCard {

    private lateinit var binding : ActivityTransactionBinding
    private lateinit var paymentBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
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
    }

    override fun onExpandBottomSheet() {
        if (paymentBottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            paymentBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}