package com.example.vocabulous

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import com.example.vocabulous.databinding.ActivityGameBinding
import java.io.BufferedReader
import java.io.InputStreamReader

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // VARIABLES
        val wordEntry = binding.wordEntryTextView
        val cheerText = binding.cheerTextView
        var score = 0
        var letters = 0
        val startBundle = intent.extras
        var gems = startBundle!!.getInt("gems")
        var usedWords = arrayListOf<String>()
        val letterButtons = arrayOf(binding.aButton, binding.bButton, binding.cButton, binding.dButton, binding.eButton, binding.fButton, binding.gButton, binding.hButton,
        binding.iButton, binding.jButton, binding.kButton, binding.lButton, binding.mButton, binding.nButton, binding.oButton, binding.pButton, binding.qButton,
        binding.rButton, binding.sButton, binding.tButton, binding.uButton, binding.vButton, binding.wButton, binding.xButton, binding.yButton, binding.zButton)
        val backButton = binding.backButton
        val enterButton = binding.enterButton
        val scoreText = binding.scoreTextView
        val gemText = binding.gemsTextView
        var letterPrompt = getLetterPrompt()
        val letterPromptText = binding.letterPrompt
        val timerText = binding.timerTextView
        val heightSizing = mapOf(49 to 23, 41 to 27, 36 to 31, 32 to 35, 28 to 39, 25 to 43, 23 to 46, 21 to 50, 19 to 54 , 18 to 58, 17 to 61, 16 to 65,
        15 to 73, 14 to 77, 13 to 84, 12 to 91, 11 to 103, 10 to 118, 9 to 133, 8 to 141, 7 to 143)
        val textSizing = mapOf(49 to 28F, 41 to 33F, 36 to 39F, 32 to 44F, 28 to 50F, 25 to 55F, 23 to 61F, 21 to 66F, 19 to 72F, 18 to 77F, 17 to 83F,
            16 to 88F, 15 to 94F, 14 to 105F, 13 to 110F, 12 to 121F, 11 to 132F, 10 to 149F, 9 to 171F, 8 to 193F, 7 to 204F)

        binding.scoreTextView.text = "Score: $score"
        binding.gemsTextView.text = "Gems: $gems"

        val timer = object: CountDownTimer(9000,1000){
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = "Time Remaining: " + millisUntilFinished/1000
            }

            override fun onFinish() {


                val intent = Intent()
                intent.putExtra("gems", gems)
                intent.putExtra("score", score)
                setResult(Activity.RESULT_OK, intent)
                finish()



            }
        }
        timer.start()



        letterPromptText.text = "$letterPrompt"
        // MAKE EACH LETTER BUTTON CHANGE THE WORDENTRY TEXTVIEW
        for (button in letterButtons){
            button.setOnClickListener{
                if (letters <=50) { // the largest english word is under 50 letters
                    letters += 1
                    if (letters in heightSizing.keys){
                        wordEntry.height = heightSizing[letters]!!
                        wordEntry.textSize = textSizing[letters]!!
                    }
                    wordEntry.text = (wordEntry.text.toString() + button.text.toString()).uppercase()

                    Log.i("$letters", "${wordEntry.height}")
                }



            }
        }
        // BACKSPACE BUTTON
        backButton.setOnClickListener{
            var currentText = wordEntry.text.toString()
            if (currentText.isNotEmpty()){
                currentText = currentText.substring(0,currentText.length-1)
                wordEntry.text = currentText.uppercase()
                letters -= 1

                if (letters in heightSizing.keys){
                    wordEntry.height = heightSizing[letters]!!
                    wordEntry.textSize = textSizing[letters]!!
                }
                Log.i("$letters", "${wordEntry.height}")

            }
        }
        // ENTER BUTTON
        enterButton.setOnClickListener{
            var wordEntered = wordEntry.text.toString().lowercase()
            if (wordEntered !in usedWords && letterPrompt.lowercase() in wordEntered) { // check if the word hasn't been used and if it contains the right letters
                if (validWord(wordEntered)) { // if the word passes
                    wordEntry.text = ""
                    letterPrompt = getLetterPrompt()
                    letterPromptText.text = "$letterPrompt"
                    usedWords.add(wordEntered)
                    letters = 0
                    wordEntry.height = 143
                    wordEntry.textSize = 204F
                    timer.cancel()
                    cheerText.text = arrayOf("Nice!", "Great Job!","Awesome!").random()
                    timer.start()
                    Log.i("test", "$usedWords")

                    score += wordEntered.length
                    scoreText.text = "Score: $score"
                    if (wordEntered.length >= 5){
                        gems = gems + wordEntered.length - 5
                        gemText.text = "GEMS: $gems"
                    }

                }
                else{
                    Log.i("test", "The word is invalid!")
                    if (wordEntered in usedWords){
                        cheerText.text = "You used that word already!"
                    }
                    else {
                        cheerText.text = "Invalid word!"
                    }
                    //val toast = Toast.makeText(this, "invalid word!", Toast.LENGTH_SHORT)
                    //toast.show()
                }
            }
            else{
                Log.i("test", "The word is invalid!")
                if (wordEntered in usedWords){
                    cheerText.text = "You used that word already!"
                }
                else {
                    cheerText.text = "Invalid word!"
                }

                //val toast = Toast.makeText(this, "You used that word already!", Toast.LENGTH_SHORT)
                //toast.show()
            }
        }


    }



    // FUNCTION TO CHECK IF ENTERED WORD IS VALID
    private fun validWord(word: String): Boolean {

        val wordsMap = mapOf(
            'a' to "aWords.txt", 'b' to "bWords.txt", 'c' to "cWords.txt", 'd' to "dWords.txt", 'e' to "eWords.txt", 'f' to "fWords.txt",
            'g' to "gWords.txt", 'h' to "hWords.txt", 'i' to "iWords.txt", 'j' to "jWords.txt", 'k' to "kWords.txt", 'l' to "lWords.txt",
            'm' to "mWords.txt", 'n' to "nWords.txt", 'o' to "oWords.txt", 'p' to "pWords.txt", 'q' to "qWords.txt", 'r' to "rWords.txt",
            's' to "sWords.txt", 't' to "tWords.txt", 'u' to "uWords.txt", 'v' to "vWords.txt", 'w' to "wWords.txt", 'x' to "xWords.txt",
            'y' to "yWords.txt", 'z' to "zWords.txt")


        val firstLetter = word[0]
        val file = assets.open(wordsMap[firstLetter].toString())
        val isr = InputStreamReader(file)
        val reader = BufferedReader(isr)
        val temp = reader.readLines()
        return temp.contains(word)
    }

    private fun getLetterPrompt(): String {
        val file = assets.open("letterCombinations.txt")
        val isr = InputStreamReader(file)
        val reader = BufferedReader(isr)
        val temp = reader.readLines()
        return temp.random()
    }


}