package com.example.uas_ppapb_v2.view.fragment.user.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.database.model.PlannedPost
import com.example.uas_ppapb_v2.database.model.Post
import com.example.uas_ppapb_v2.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class BottomSheetDialog(private val post: Post, private val onSuccess: (PlannedPost) -> Unit): BottomSheetDialogFragment() {

    private val binding by lazy {
        BottomSheetDialogBinding.inflate(layoutInflater)
    }
    private var selectTime: String = ""
    private var selectDate: String = ""
    private var departureDate: String = ""
    private var departureTime: String = ""

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


            setNotificationTime.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_UP) {
                    val rightDrawable = (v as AutoCompleteTextView).compoundDrawables[2]
                    if(event.rawX >= (v.right - rightDrawable.bounds.width())) {

                        if(departureDate == "" || departureTime == "") {
                            Snackbar.make(root, "Please select date and time", Snackbar.LENGTH_SHORT).show()
                        } else {
                            showDatePickerDialog(setNotificationTime, true)
                        }
                        return@setOnTouchListener true

                    }
                }
                false
            }

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
                val plannedPost = PlannedPost(
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
                    plannedTime = inputTime.text.toString(),
                    notificationTime = selectTime,
                    notificationDate = selectDate
                )
                roomDBManager.insertPlannedPost(plannedPost)
                onSuccess(plannedPost)
                dismiss()
            }
        }
    }
    private fun calculateTimeInMillis(targetDateTime: String): Long {

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        Log.d("TAG", "calculateTimeInMillis: $targetDateTime")
        try {
            val targetDate = dateFormat.parse(targetDateTime)
            return targetDate.time

        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }

    private fun showDatePickerDialog(element: AutoCompleteTextView, planned: Boolean = false) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this.requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"


            if(!planned) {
                departureDate = selectedDate
                element.setText(selectedDate)
            } else {
                selectDate = selectedDate
                showTimePickerDialog(element, true)
            }

        }, year, month, day)
        if(!planned) {
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        } else {
            Log.d("TAG", "showDatePickerDialog: ${calculateTimeInMillis("$departureDate $departureTime")}")
            datePickerDialog.datePicker.maxDate =  calculateTimeInMillis("$departureDate $departureTime")
        }
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(element: AutoCompleteTextView, planned: Boolean = false) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                var selectedTime: String = ""
                if(i < 10) {
                    if(i2 < 10) {
                        selectedTime = "0$i:0$i2"
                    } else {
                        selectedTime = "0$i:$i2"
                    }
                } else {
                    if(i2 < 10) {
                        selectedTime = "$i:0$i2"
                    } else {
                        selectedTime = "$i:$i2"
                    }
                }


                if(planned) {
                    selectTime = selectedTime
                    element.setText("$selectDate || $selectTime")
                } else {
                    departureTime = selectedTime
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