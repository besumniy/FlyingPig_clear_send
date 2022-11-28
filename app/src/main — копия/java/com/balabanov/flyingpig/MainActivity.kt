package com.balabanov.flyingpig

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.GestureDetectorCompat
import com.balabanov.flyingpig.Game
import com.balabanov.flyingpig.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_skins.*


class MainActivity :Activity(){
    lateinit var pref: SharedPreferences

    var record_value=0
    var choosen_pig=0
    lateinit var soundpool: SoundPool
    var startSound=0
    var clickSound=0
    var main_theme=0
    var main_music=0

    lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())

        soundpool= SoundPool(8, AudioManager.STREAM_MUSIC,0)
        main_theme=soundpool.load(this,R.raw.maintheme,1)

        MobileAds.initialize(this){}
        MobileAds.setRequestConfiguration(MobileAds.getRequestConfiguration().toBuilder().setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE).build())
        mAdView=findViewById( R.id.reclam)
        val adRequest=AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        start.layoutParams.height=Resources.getSystem().displayMetrics.heightPixels/6
        start.layoutParams.width=Resources.getSystem().displayMetrics.heightPixels/3

        choose_skin.layoutParams.height=Resources.getSystem().displayMetrics.heightPixels/12
        choose_skin.layoutParams.width=Resources.getSystem().displayMetrics.heightPixels/8


        flyingpig.layoutParams.height=Resources.getSystem().displayMetrics.heightPixels/6*5

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
        record.text="Record: "+record_value
        main_music=soundpool.play(main_theme,1f,1f,1,-1,1f)

        if (pref.contains("choosen_pig")) {
            choosen_pig=pref.getInt("choosen_pig", 0)
        }
        when(choosen_pig){
            0->choose_skin.setImageDrawable(getDrawable(R .drawable.pig)!!)
            1->choose_skin.setImageDrawable(getDrawable(R .drawable.pig_in_hat)!!)
            2->choose_skin.setImageDrawable(getDrawable(R .drawable.pig_hero)!!)
            3->choose_skin.setImageDrawable(getDrawable(R .drawable.pig_zombie)!!)
            4->choose_skin.setImageDrawable(getDrawable(R .drawable.pig_wizard)!!)
            5->choose_skin.setImageDrawable(getDrawable(R .drawable.pig_panda)!!)
            6->choose_skin.setImageDrawable(getDrawable(R .drawable.pig_clown)!!)
            7->choose_skin.setImageDrawable(getDrawable(R .drawable.pig_mech)!!)
        }
        if(choosen_pig==3){startSound=soundpool.load(this,R.raw.start_zombie,1)
        clickSound=soundpool.load(this,R.raw.jump_zombie, 1)}
        else if(choosen_pig==8){startSound=soundpool.load(this,R.raw.start_mech,1)
            clickSound=soundpool.load(this,R.raw.jump_mech, 1)}
        else {startSound=soundpool.load(this,R.raw.start,1)
            clickSound=soundpool.load(this,R.raw.jump, 1)}
    }

    override fun onPause() {
        super.onPause()
        val editor = pref.edit()
        editor.putInt("record", record_value)
        editor.apply()
        soundpool.stop(main_music)
    }
    override fun onDestroy(){
        super.onDestroy()
        val editor = pref.edit()
        editor.putInt("record", record_value)
        editor.apply()
        soundpool.unload(main_theme)
        soundpool.unload(startSound)
    }



    fun startGame(v: View){
        soundpool.play(startSound,1f,1f,1,0,1f)
        startActivity(Intent(this, GameActivity::class.java))
    }
    fun chooseSkin(v: View){
        soundpool.play(clickSound,1f,1f,1,0,1f)
        startActivity(Intent(this, SkinsActivity::class.java))
    }
}