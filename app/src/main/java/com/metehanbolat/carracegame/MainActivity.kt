package com.metehanbolat.carracegame

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.metehanbolat.carracegame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GameTask {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mGameView: GameView
    private lateinit var nameImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        nameImageView = findViewById(R.id.nameImageView)


        mGameView = GameView(this ,this)
        startButtonClick()

    }



    override fun closeGame(mScore: Int) {
        binding.apply {
            score.text = resources.getString(R.string.score, mScore)
            root.removeView(mGameView)
            startButton.visibility = View.VISIBLE
            score.visibility = View.VISIBLE


        }
    }

    private fun startButtonClick() {

        binding.apply {
            startButton.setOnClickListener {
                // หา ImageView ด้วย ID
                val nameImageView = findViewById<ImageView>(R.id.nameImageView)
                // ทำให้ImageView หายไป
                nameImageView.visibility = View.GONE
                mGameView.setBackgroundResource(R.drawable.sky)
                root.addView(mGameView)
                startButton.visibility = View.GONE
                score.visibility = View.GONE
            }
        }
    }

}