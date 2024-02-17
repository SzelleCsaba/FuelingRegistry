package hu.bme.aut.android.fuelingregistry.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.fuelingregistry.R
import hu.bme.aut.android.fuelingregistry.data.CarData
import hu.bme.aut.android.fuelingregistry.databinding.DialogNewCarBinding

class NewCarDialogFragment : DialogFragment() {
    interface NewCarDialogListener {
        fun onCarCreated(newCar: CarData)
    }

    private lateinit var listener: NewCarDialogListener

    private lateinit var binding: DialogNewCarBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewCarDialogListener
            ?: throw RuntimeException("Activity must implement the NewCarDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewCarBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_car)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onCarCreated(getCar())
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.etName.text.isNotEmpty()

    private fun getCar() = CarData(
        name = binding.etName.text.toString(),
        description = binding.etDescription.text.toString(),
        capacity = if (binding.etCapacity.text.toString()=="") 0 else binding.etCapacity.text.toString().toInt()
    )

    companion object {
        const val TAG = "NewCarDialogFragment"
    }
}