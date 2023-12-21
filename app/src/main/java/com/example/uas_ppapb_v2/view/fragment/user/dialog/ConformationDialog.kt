package com.example.uas_ppapb_v2.view.fragment.user.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uas_ppapb_v2.database.model.PlannedPost
import com.example.uas_ppapb_v2.databinding.ConformationDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConformationDialog(private val action: () -> Unit): BottomSheetDialogFragment() {

    private val binding by lazy {
        ConformationDialogBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            button3.setOnClickListener {
                dismiss()
            }
            button.setOnClickListener {
                action()
                dismiss()
            }
        }
    }
}