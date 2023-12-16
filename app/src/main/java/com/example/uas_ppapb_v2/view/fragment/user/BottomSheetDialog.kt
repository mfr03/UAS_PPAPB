package com.example.uas_ppapb_v2.view.fragment.user

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import com.example.uas_ppapb_v2.R
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.database.model.PlannedPost
import com.example.uas_ppapb_v2.database.model.Post
import com.example.uas_ppapb_v2.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

class BottomSheetDialog(private val post: Post, private val onSuccess: () -> Unit): BottomSheetDialogFragment() {

    private val binding by lazy {
        BottomSheetDialogBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    @Suppress("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roomDBManager = (requireActivity().application as CustomApp).getRoomDBManager()

        with(binding) {
            inputDate.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_UP) {
                    val rightDrawable = (v as AutoCompleteTextView).compoundDrawables[2]
                    if(event.rawX >= (v.right - rightDrawable.bounds.width())) {
                        showDatePickerDialog(inputDate)
                        return@setOnTouchListener true
                    }
                }
                false
            }
            inputTime.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_UP) {
                    val rightDrawable = (v as AutoCompleteTextView).compoundDrawables[2]
                    if(event.rawX >= (v.right - rightDrawable.bounds.width())) {
                        showTimePickerDialog(inputTime)
                        return@setOnTouchListener true
                    }
                }
                false
            }

            button2.setOnClickListener {
                roomDBManager.insertPlannedPost(
                    PlannedPost(
                        id = post.id,
                        userUID = roomDBManager.getUID(),
                        destination = post.destination,
                        startingStation = post.startingStation,
                        endStation = post.endStation,
                        overviewDescription = post.overviewDescription,
                        shortDescription = post.shortDescription,
                        price = post.price,
                        lengthOfStay = post.lengthOfStay,
                        imageURI = post.imageURI,
                        tag = post.tag,
                        plannedDate = inputDate.text.toString(),
                        plannedTime = inputTime.text.toString()
                    )
                )
                onSuccess()
                dismiss()
            }
        }
    }

    private fun showDatePickerDialog(element: AutoCompleteTextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this.requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
            element.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(element: AutoCompleteTextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                if(i2 < 10) {
                    val selectedTime = "$i:0$i2"
                    element.setText(selectedTime)
                } else {
                    val selectedTime = "$i:$i2"
                    element.setText(selectedTime)
                }

            },
            hour,
            minute,
            true // Set to true for 24-hour format, false for 12-hour format
        )
        timePickerDialog.show()
    }
}