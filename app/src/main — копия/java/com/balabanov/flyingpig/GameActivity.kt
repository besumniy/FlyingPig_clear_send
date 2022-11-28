package com.balabanov.flyingpig

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import com.balabanov.flyingpig.MainActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity :Activity() {
    lateinit var pref: SharedPreferences

    var record_value=0
    var score_value=0

    lateinit var mInterstitialAd: InterstitialAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        pref=getSharedPreferences("my settings", MODE_PRIVATE)
        game_screen.activity=this
        game_screen.setOnTouchListener { v, event ->
            val action=event.action
            when(action){
                MotionEvent.ACTION_DOWN->{}
                MotionEvent.ACTION_UP->{game_screen.ontouchevent(event.x,event.y)}
                MotionEvent.ACTION_MOVE->{}
                else-> {}
            }
            true
            }

        mInterstitialAd= InterstitialAd(this)
        mInterstitialAd.adUnitId="ca-app-pub-****************/**********"//R.string.admob_publisher_id
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener=object:AdListener(){
            override  fun onAdClosed(){
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }
        game_screen.reclam=mInterstitialAd
        }

    override fun onResume() {
        super.onResume()
        window.decorView.systemUiVisibility= (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
        if (pref.contains("record")) {
            record_value=pref.getInt("record", 0)
        }
    }

    override fun onPause() {
        super.onPause()
        val editor = pref.edit()
        editor.putInt("record", record_value)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        game_screen.onDestroy()
    }

    fun fly(v: View){
        game_screen.onclick()
    }
    fun retry(v: View){
        val intent= Intent(this,GameActivity::class.java)
        ContextCompat.startActivity(this, intent, intent.extras)
    }
    fun menu(v: View){
        var intent= Intent(this, MainActivity::class.java)
        ContextCompat.startActivity(this, intent, intent.extras)
    }

}