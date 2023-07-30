package com.example.tictactoe.api.enums

enum class PlayerType(val next: Lazy<PlayerType>, val state: State) {
    X (lazy { O }, State.X_WINS),
    O (lazy { X }, State.O_WINS),
}