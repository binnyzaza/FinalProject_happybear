package com.metehanbolat.carracegame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs
import kotlin.math.min

@SuppressLint("ViewConstructor")
class GameView(context: Context, var gameTask: GameTask): View(context) {

    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myBearPosition = 0
    private val Orange = ArrayList<HashMap<String, Any>>()
    private var mediaPlayer: MediaPlayer

    init {
        // สร้าง และเล่นเพลง
        mediaPlayer = MediaPlayer.create(context, R.raw.bg_music)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            Orange.add(map)
        }
        time += 10 + speed
        val bearWidth = viewWidth / 3
        val bearHeight = bearWidth / 1
        myPaint!!.style = Paint.Style.FILL
        val d = ResourcesCompat.getDrawable(resources, R.drawable.bear, null)

        d?.let {
            it.setBounds(
                myBearPosition * viewWidth / 3 + viewWidth / 100 + 25,
                viewHeight - bearHeight,
                myBearPosition * viewWidth / 3 + viewWidth / 50 + bearWidth - 25,
                viewHeight - 140
            )
            it.draw(canvas)
        }

        // ทำการรีเซ็ตค่าต่าง ๆ
        fun resetGame() {
            score = 0
            speed = 1
            time = 0
            // เพิ่มการรีเซ็ตสถานะอื่น ๆ ที่ต้องการ
        }

//        myPaint!!.color = Color.GREEN
//        var highScore = 0
        val disappearY = viewHeight - 50 //ระยะที่ต้องการให้ส้มหาย
        for (i in Orange.indices) {
            try {
                val rX = Orange[i]["lane"] as Int * viewWidth / 3 + viewWidth / 100
                val FallDistance = viewHeight - 10 // ระยะทางส้มตกลงมาไม่เกิน
                val rY = min(time - Orange[i]["startTime"] as Int, FallDistance)
                //เช็คว่าส้มตกตรงจุดที่กำหนก
                if (rY > disappearY) {
                    Orange.removeAt(i) //ลบส้มที่ตกลงมา
                    gameTask.closeGame(score)
                    resetGame()

                }
                //วาดส้ม
                val d2 = ResourcesCompat.getDrawable(resources, R.drawable.orange, null)
                d2?.let {
                    it.setBounds(
                        rX + 25,
                        rY - bearHeight,
                        rX + bearWidth,
                        rY
                    )
                    it.draw(canvas)
                }

                //ตรวจสอบการชนbear
                // เช็คว่าหมีชนกับส้มมั้ย
                if (Orange[i]["lane"] as Int == myBearPosition) {
                    if (rY > viewHeight - 2 - bearHeight && rY < viewHeight - 2) {
                        // หมีชนส้ม จึงเพิ่มคะแนน
                        Orange.removeAt(i)
                        score++
                        speed = 1 + abs(score / 5)
//                        if (score > highScore) highScore = score
                    }

                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score: $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed: $speed", 380f, 80f, myPaint!!)
        invalidate()

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                val laneWidth = viewWidth / 3
                if (x1 < laneWidth && myBearPosition > 0) {
                    // ย้ายทางซ้าย
                    myBearPosition--
                } else if (x1 > laneWidth * 2 && myBearPosition < 2) {
                    // ย้ายทางขวา
                    myBearPosition++
                }
                invalidate()
            }

            MotionEvent.ACTION_UP -> {

            }
        }

        return true
    }
}



