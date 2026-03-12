package com.sowmya.contactmanagementapp.ui.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import com.sowmya.contactmanagementapp.R
import com.sowmya.contactmanagementapp.databinding.FragmentDialPadBinding
import com.sowmya.contactmanagementapp.databinding.ItemDialPadButtonBinding

class DialPadFragment : Fragment() {
    private var _binding: FragmentDialPadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialPadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialPad()
        
        binding.btnBackspace.setOnClickListener {
            val current = binding.tvDialedNumber.text.toString()
            if (current.isNotEmpty()) {
                binding.tvDialedNumber.text = current.substring(0, current.length - 1)
            }
        }
        
        binding.btnCall.setOnClickListener {
            // Initiate call logic
        }
    }

    private fun setupDialPad() {
        val buttons = listOf(
            "1" to "", "2" to "ABC", "3" to "DEF",
            "4" to "GHI", "5" to "JKL", "6" to "MNO",
            "7" to "PQRS", "8" to "TUV", "9" to "WXYZ",
            "*" to "", "0" to "+", "#" to ""
        )

        buttons.forEach { (num, letters) ->
            val btnBinding = ItemDialPadButtonBinding.inflate(layoutInflater, binding.gridDialPad, false)
            btnBinding.tvNumber.text = num
            btnBinding.tvLetters.text = letters
            
            btnBinding.root.setOnClickListener {
                binding.tvDialedNumber.append(num)
            }
            
            btnBinding.root.setOnLongClickListener {
                if (num == "0") {
                    binding.tvDialedNumber.append("+")
                    true
                } else {
                    handleSpeedDial(num)
                    true
                }
            }

            val params = GridLayout.LayoutParams()
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.width = 0
            btnBinding.root.layoutParams = params
            
            binding.gridDialPad.addView(btnBinding.root)
        }
    }

    private fun handleSpeedDial(number: String) {
        android.widget.Toast.makeText(context, "Speed dial calling assigned contact for $number...", android.widget.Toast.LENGTH_SHORT).show()
        // In a real app, this would query settings and call a number
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
