package com.bookxpert.assignment.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.bookxpert.assignment.R
import com.bookxpert.assignment.database.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText

class InputBottomSheetFragment(private val user: User,private val listener: ProductInterface) : BottomSheetDialogFragment() {

    private lateinit var editTextInput: TextInputEditText
    private lateinit var buttonCancel: Button
    private lateinit var buttonSubmit: Button
    private lateinit var dialog_cancel: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.edit_bottom_sheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextInput = view.findViewById(R.id.outlined_edit_text)
        editTextInput.setText(user.name)
        buttonCancel = view.findViewById(R.id.button_cancel)
        buttonSubmit = view.findViewById(R.id.button_submit)
        dialog_cancel = view.findViewById(R.id.edit_dialog_cancel)

        dialog_cancel.setOnClickListener {
            listener.productDelete(user)
            dismiss() // Close the bottom sheet
        }

        buttonCancel.setOnClickListener {
            val userEdit = User(user.id,editTextInput.text.toString())
            listener.productEdit(userEdit)
            dismiss() // Close the bottom sheet
        }

        buttonSubmit.setOnClickListener {
            dismiss() // Close the bottom sheet
        }
    }
}