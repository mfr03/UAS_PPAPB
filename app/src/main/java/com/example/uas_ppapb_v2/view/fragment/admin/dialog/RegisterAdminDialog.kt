package com.example.uas_ppapb_v2.view.fragment.admin.dialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import com.example.uas_ppapb_v2.app.CustomApp
import com.example.uas_ppapb_v2.databinding.RegisterAdminDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import org.apache.commons.validator.routines.EmailValidator
import java.text.SimpleDateFormat
import java.util.Calendar

class RegisterAdminDialog(private val onSuccess: () -> Unit): BottomSheetDialogFragment() {

    private val binding: RegisterAdminDialogBinding by lazy {
        RegisterAdminDialogBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    @Suppress("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = (requireActivity().application as CustomApp).run { getAuthManager() }

        with(binding) {
            inputTanggalLahir.setOnTouchListener { v, event ->
                if(event.action == MotionEvent.ACTION_UP) {
                    val rightDrawable = (v as AutoCompleteTextView).compoundDrawables[2]
                    if(event.rawX >= (v.right - rightDrawable.bounds.width())) {
                        showDatePickerDialog(inputTanggalLahir)
                        return@setOnTouchListener true
                    }
                }
                false
            }

            registerButton.setOnClickListener {
                if (inputEmail.text.toString().isEmpty() || inputUsername.text.toString().isEmpty() ||inputPassword.text.toString().isEmpty()
                    || inputTanggalLahir.text.toString().isEmpty() || inputNim.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, "Mohon isi bagan yang kosong", Snackbar.LENGTH_SHORT).setAnchorView(registerButton).show()
                } else {
                    if(checkValidity(inputEmail.text.toString(), inputTanggalLahir.text.toString())) {
                        auth.registerAccount(inputEmail.text.toString(), inputPassword.text.toString(), inputUsername.text.toString(), inputTanggalLahir.text.toString(), inputNim.text.toString(),
                            bypass = true,
                            onSuccess = {
                                onSuccess()
                                dismiss()
                            },
                            onFailed = {
                                Snackbar.make(binding.root, "Register Failed : ${it.message}", Snackbar.LENGTH_SHORT).setAnchorView(registerButton).show()
                            })
                    }
                }
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

        datePickerDialog.show()
    }

    private fun checkValidity(email : String, tanggalLahir : String) : Boolean {
        if(EmailValidator.getInstance().isValid(email)) {
            if(checkAge(tanggalLahir)) {
                return true
            }
        } else {
            Snackbar.make(binding.root, "Email yang anda masukkan tidak valid", Snackbar.LENGTH_SHORT).setAnchorView(binding.registerButton).show()
        }
        return false
    }

    private fun checkAge(tanggalLahir: String) : Boolean {

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val currentDate = "$day/${month + 1}/$year"

        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = dateFormat.parse(currentDate)
        val birthCalendar = Calendar.getInstance()
        birthCalendar.time = dateFormat.parse(tanggalLahir)

        val umur = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        if(umur >= 15) {
            return true
        } else {
            Snackbar.make(binding.root, "Anda berumur kurang dari 15 tahun", Snackbar.LENGTH_SHORT).setAnchorView(binding.registerButton).show()
        }
        return false
    }
}