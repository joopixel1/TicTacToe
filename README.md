# TicTacToe
=================================

This app contains a simple tictactoe game with a singleplayer mode and multiplayer mode. I wanted to play with some algorithms here. 

I used **Minimax theorem** at first, to calculate optimal next location for the computer to play. You can read more about this theory on wikipedia

Then I noticed I was doing the same recursion everytime the computer was making a move, which was a waste of time. So I created a custom tree to save all the posssibilities and optimization value of pleying at a particular index.
But on the Other side, this latter uses more heap memory. And start time is longer.

This app demonstrates multiple fragments in an activity, a shared ViewModel across fragments,
data binding, LiveData, and the Jetpack Navigation component.


Pre-requisites
--------------
* Familiar with activities and fragments
* How to use styles and themes in the UI
* Basic understanding of Jetpack architecture components including ViewModel and LiveData
* Data binding and binding expressions
* Kotlin syntax basics


Getting Started
---------------
1. Install Android Studio, if you don't already have it.
2. Download the sample.
3. Import the sample into Android Studio.
4. Build and run the sample.
