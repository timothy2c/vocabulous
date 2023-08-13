package com.example.vocabulous

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vocabulous.databinding.GameOverFragmentBinding

class GameOverFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding = GameOverFragmentBinding.inflate(inflater, container, false)
        binding.root.bringToFront()
        val bundle = arguments
        binding.endScore.text = "Score: " +bundle!!.getString("score")
        binding.endGems.text = "GEMS: " + bundle!!.getInt("gems")



        binding.menuButton.setOnClickListener {
            val fragment = requireActivity().supportFragmentManager.findFragmentById(R.id.main_fragment_layout)
            if (fragment != null) {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.remove(fragment)
                transaction.commit()
            }


        }

        return binding.root
    }

}