package com.example.tictactoe.api.models

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tictactoe.api.enums.Mode
import com.example.tictactoe.api.enums.State
import java.lang.Exception

class GameViewModel(
    private val mode: Mode,
    private val endDialog: (winner: State, newGame: () -> Unit) -> Unit,
): ViewModel()
{

    private var game = Game(mode)

    private val _gridStr = MutableLiveData<String>(game.currentGrid)
    val gridStr: LiveData<String> = _gridStr


    private fun newGame () {
        game = Game(mode)
        _gridStr.value = game.currentGrid
    }

    fun play( v: View, i: Int ) {
        try {
            game.play(i)
            _gridStr.value = game.currentGrid

            if(game.finished) endDialog(game.winner!!) { newGame() }
        }
        catch (e: Exception){
            Toast.makeText(v.context, e.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }

}