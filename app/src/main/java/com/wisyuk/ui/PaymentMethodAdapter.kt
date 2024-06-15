package com.wisyuk.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wisyuk.data.response.PaymentMethodItem
import com.wisyuk.databinding.ItemPaymentBinding
import com.wisyuk.ui.payment.PaymentActivity
import com.wisyuk.ui.paymentmethod.PaymentMethodActivity
import com.wisyuk.ui.paymentmethod.PaymentMethodActivity.Companion.RESULT_CODE

class PaymentMethodAdapter(private val activity: Activity) : ListAdapter<PaymentMethodItem, PaymentMethodAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, activity)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val paymentMethod = getItem(position)
        if (paymentMethod != null) {
            holder.bind(paymentMethod)
        }
    }

    class MyViewHolder(private val binding: ItemPaymentBinding, private val activity: Activity) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: PaymentMethodItem) {
            binding.btPayment.text = item.name
            itemView.setOnClickListener {
                val resultIntent = Intent()
                resultIntent.putExtra(PaymentMethodActivity.PAYMENT_METHOD_ID, item.id)
                resultIntent.putExtra(PaymentMethodActivity.PAYMENT_METHOD_NAME, item.name)
                activity.setResult(RESULT_CODE, resultIntent)
                activity.finish()
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PaymentMethodItem>() {
            override fun areItemsTheSame(
                oldItem: PaymentMethodItem,
                newItem: PaymentMethodItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PaymentMethodItem,
                newItem: PaymentMethodItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
