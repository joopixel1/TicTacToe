package com.example.tictactoe.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.api.enums.State

class MyDialog(
    private val winner: State,
    private val newGame: () -> Unit
): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class because this dialog
        // has a simple UI.
        // We will use the more flexible onCreateView function
        // instead of onCreateDialog in the next AdvancedDialog
        val builder = AlertDialog.Builder(this.requireActivity())

        // An OK button that does nothing
        builder.setMessage(winner.toString())
            // An OK button that does nothing
            .setPositiveButton("New Game") { dialog, id ->
                dismiss()
                newGame()
            }
            // A "Cancel" button that does nothing
            .setNegativeButton("End") { dialog, id ->
                dismiss()
                findNavController().popBackStack()
            }

        // Create the object and return it
        return builder.create()
    }


    companion object {
        const val TAG = "JT_Game"
    }

}