package hu.bme.aut.android.fuelingregistry.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.fuelingregistry.R
import hu.bme.aut.android.fuelingregistry.data.FuelingData
import hu.bme.aut.android.fuelingregistry.databinding.DialogNewFuelingBinding

class NewFuelingDialogFragment : DialogFragment() {
    interface NewFuelingDialogListener {
        fun onFuelingCreated(newFueling: FuelingData)
    }

    private lateinit var listener: NewFuelingDialogListener

    private lateinit var binding: DialogNewFuelingBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewFuelingDialogListener
            ?: throw RuntimeException("Activity must implement the NewFuelingDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewFuelingBinding.inflate(LayoutInflater.from(context))

        binding.spType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.type_items))

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_fueling)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onFuelingCreated(getFueling())
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.etAmount.text.isNotEmpty()

    private fun getFueling() = FuelingData(
        amount = if (binding.etAmount.text.toString()=="") 0.0 else binding.etAmount.text.toString().toDouble(),
        type = FuelingData.Type.getByOrdinal(binding.spType.selectedItemPosition)
            ?: FuelingData.Type.stdgasoline,
        pricePerLiter = if (binding.etPricePerLiter.text.toString()=="") 0 else binding.etPricePerLiter.text.toString().toInt(),
        odometerAtFueling = if (binding.etOdometer.text.toString()=="") 0 else binding.etOdometer.text.toString().toInt(),
        isFullTank = binding.cbFullTank.isChecked,
        dateDay = binding.datePicker.dayOfMonth,
        dateMonth = binding.datePicker.month,
        dateYear = binding.datePicker.year,
    )

    companion object {
        const val TAG = "NewFuelingDialogFragment"
    }
}
