package com.example.vocabulous

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.vocabulous.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val fm = supportFragmentManager
    var gems = 0
    var highScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val highScoreTextView = binding.highScoreTextView
        val gemsTextView = binding.gemsTextView
        val startButton = binding.startButton

        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val score = data?.getIntExtra("score", 123)
                    gems = data?.getIntExtra("gems", 123)!!
                    if (score != null && score > highScore) {
                        highScore = score
                    }

                    highScoreTextView.text = "High score: $highScore"
                    gemsTextView.text = "Gems: $gems"

                    val fragmentTransaction = fm.beginTransaction()
                    val gameOverFragment = GameOverFragment()
                    var gameOverBundle = Bundle()
                    gameOverBundle.putString("score", score.toString())
                    gameOverBundle.putInt("gems", gems)
                    gameOverFragment.arguments = gameOverBundle
                    fragmentTransaction.add(R.id.main_fragment_layout, gameOverFragment).addToBackStack(null).commit()


                }

            }
        startButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("gems", gems)
            startForResult.launch(intent)

        }



    }
}


