package com.example.tictactoe.api.models

import android.util.Log
import com.example.tictactoe.api.enums.Mode
import com.example.tictactoe.api.models.Game
import java.lang.Exception
import java.util.Scanner


    fun main() {

        var game = Game(Mode.SINGLEPLAYER)
        val reader = Scanner(System.`in`)
        println("Enter a number between 0 and 8 representing the index to play at")


        while (!game.finished) {
                print("$game")
                game.play(reader.nextInt())
        }

        println("${game.winner}")
    }
