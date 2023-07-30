package com.example.tictactoe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.tictactoe.api.enums.State
import com.example.tictactoe.api.models.GameViewModel
import com.example.tictactoe.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private val args: SecondFragmentArgs by navArgs()
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        fun callback(): (winner: State, newGame: ()->Unit)-> Unit {
            return { winner: State, newGame: () -> Unit ->
                val myDialog = MyDialog(winner, newGame)
                myDialog.show(childFragmentManager, MyDialog.TAG)
            }
        }
        binding.model = GameViewModel(args.mode, callback())
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}