package com.balabanov.flyingpig;

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.random.Random
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.SoundPool
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
//import android.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import com.balabanov.flyingpig.MainActivity

import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_main.*


public class GameArea : View {
    lateinit var containerSize:Array<Int>
    lateinit var soundpool: SoundPool
    lateinit var skin: Drawable
    var startSound=0
    var jumpSound=0
    var loseSound=0

    var p= Paint()

    var pigH=0
    var pigW=0
    var jump=0.0
    var b_s=0
    var go=0
    var score_value=0
    var record_value=0
    var choosen_pig=0
    var balls= mutableListOf<Ball>()
    var clouds= mutableListOf<Cloud>()

    var end_game=0
    var load_completed=true

    val pref:SharedPreferences = getDefaultSharedPreferences(getContext())

    lateinit var activity: Game
    lateinit var reclam: InterstitialAd

    lateinit var pig:Pig
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context):super(context) {
        onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet):super(context,attrs) {
        onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyle:Int):super(context, attrs, defStyle) {
        onCreate()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        //record=findViewById(R.id.record)
        //score=findViewById(R.id.score)

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onCreate(){
        containerSize=arrayOf(Resources.getSystem().displayMetrics.widthPixels,Resources.getSystem().displayMetrics.heightPixels)
        soundpool=SoundPool(8, AudioManager.STREAM_MUSIC,0)

        //pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        if (pref.contains("choosen_pig")) {
            choosen_pig=pref.getInt("choosen_pig", 0)
        }
        when(choosen_pig){
            0->{skin=context.getDrawable(R .drawable.pig)!!
                startSound=soundpool.load(context,R.raw.start,1)//
                jumpSound=soundpool.load(context,R.raw.jump, 1)
                loseSound=soundpool.load(context,R.raw.lose,1)}
            1->{skin=context.getDrawable(R .drawable.pig_in_hat)!!
                startSound=soundpool.load(context,R.raw.start,1)//
                jumpSound=soundpool.load(context,R.raw.jump, 1)
                loseSound=soundpool.load(context,R.raw.lose,1)}
            2->{skin=context.getDrawable(R .drawable.pig_hero)!!
                startSound=soundpool.load(context,R.raw.start,1)//
                jumpSound=soundpool.load(context,R.raw.jump, 1)
                loseSound=soundpool.load(context,R.raw.lose,1)}
            3->{skin=context.getDrawable(R .drawable.pig_zombie)!!
                startSound=soundpool.load(context,R.raw.start_zombie,1)//
                jumpSound=soundpool.load(context,R.raw.jump_zombie, 1)
                loseSound=soundpool.load(context,R.raw.lose_zombie,1)}
            4->{skin=context.getDrawable(R .drawable.pig_wizard)!!
                startSound=soundpool.load(context,R.raw.start,1)//
                jumpSound=soundpool.load(context,R.raw.jump, 1)
                loseSound=soundpool.load(context,R.raw.lose,1)}
            5->{skin=context.getDrawable(R .drawable.pig_panda)!!
                startSound=soundpool.load(context,R.raw.start,1)//
                jumpSound=soundpool.load(context,R.raw.jump, 1)
                loseSound=soundpool.load(context,R.raw.lose,1)}
            6->{skin=context.getDrawable(R .drawable.pig_clown)!!
                startSound=soundpool.load(context,R.raw.start,1)//
                jumpSound=soundpool.load(context,R.raw.jump, 1)
                loseSound=soundpool.load(context,R.raw.lose,1)}
            7->{skin=context.getDrawable(R .drawable.pig_mech_anim)!!
                startSound=soundpool.load(context,R.raw.start_mech,1)//
                jumpSound=soundpool.load(context,R.raw.jump_mech, 1)
                loseSound=soundpool.load(context,R.raw.lose_mech,1)}
        }



        /*soundpool.setOnLoadCompleteListener(SoundPool.OnLoadCompleteListener{ soundPool: SoundPool, sampleId: Int, status: Int ->
            if(sampleId==startSound){load_completed=true
            soundpool.play(startSound,1f,1f,1,0,1f)}
        })*/

        //soundpool.play(startSound,1f,1f,1,0,1f)
        p.setColor(Color.BLACK)
        p.setTextSize(30f);

        pigH=containerSize[1]/8
        pigW=containerSize[1]/6

        if (pref.contains("choosen_pig")) {
            choosen_pig=pref.getInt("choosen_pig", 0)
        }

        pig=Pig(skin,
            Rect(containerSize[1]/100,(containerSize[1]-pigH)/2,containerSize[1]/100+pigW,(containerSize[1]-pigH)/2+pigH),
            containerSize)

        jump=containerSize[1]/60.0
        b_s=containerSize[1]/7
        go=b_s/10

        if (pref.contains("record")) {
            record_value=pref.getInt("record", 0)
        }

//create clouds
        clouds.add(Cloud(context.getDrawable(R.drawable.cloud1)!!,Rect(0,0,containerSize[0],containerSize[0]/12),containerSize)) //up cloud
        clouds.add(Cloud(context.getDrawable(R.drawable.cloud1)!!,Rect(containerSize[0],0,containerSize[0]*2,containerSize[0]/12),containerSize)) //up cloud1
        clouds.add(Cloud(context.getDrawable(R.drawable.cloud)!!,Rect(0,containerSize[1]-containerSize[0]/12,containerSize[0],containerSize[1]),containerSize)) //down cloud
        clouds.add(Cloud(context.getDrawable(R.drawable.cloud)!!,Rect(containerSize[0],containerSize[1]-containerSize[0]/12,containerSize[0]*2,containerSize[1]),containerSize)) // down cloud1
        var h=Random.nextInt(containerSize[1])
        balls.add(
            Ball(
                context.getDrawable(R.drawable.ball)!!,
                Rect(pigW*10,h,pigW*10+b_s,h+b_s),
                Random.nextInt(-1,359),
                containerSize
            ))
        //Toast.makeText(context,balls.size,Toast.LENGTH_SHORT).show()
        create_world(containerSize[0]*4)
    }
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        if(load_completed){
        super.onDraw(canvas)
            if(end_game==0){


                //clouds
                clouds[0].update(go/4)
                clouds[1].update(go/4)
                clouds[2].update(go/8)
                clouds[3].update(go/8)

                for(cloud in clouds){
                if(cloud.rect.right<0){
                 cloud.rect.left=containerSize[0]
                cloud.rect.right=containerSize[0]*2
                 }
                cloud.draw(canvas)
                 }


                for(ball in balls){
                    ball.update(go)
                    if(ball.rect.right<0){
                        balls.remove(ball)
                        score_value++
                        break
                    }
                    if(Rect.intersects(ball.rect,pig.rectC)){
                        end_game=1}
                    if(choosen_pig==8){
                        if(pig.rect.top>containerSize[1]){if(Rect.intersects(ball.rect,Rect(pig.rectC.left,pig.rectC.top-containerSize[1],pig.rectC.right,pig.rectC.bottom-containerSize[1])))end_game=1}
                        else if(pig.rect.bottom<0){if(Rect.intersects(ball.rect,Rect(pig.rectC.left,pig.rectC.top+containerSize[1],pig.rectC.right,pig.rectC.bottom+containerSize[1])))end_game=1}
                    }

                    ball.draw(canvas)
                }
                pig.update()
                pig.draw(canvas)

                if (pig.rect.top>containerSize[1] || pig.rect.bottom<0){
                    if(choosen_pig!=7)end_game=1
                    else{
                        if(pig.rect.top>containerSize[1]){
                            if(pig.rect.bottom>containerSize[1]){
                                pig.rect.bottom-=containerSize[1]
                                pig.rect.top-=containerSize[1]
                                pig.rectC.top-=containerSize[1]
                                pig.rectC.bottom-=containerSize[1]
                            }
                            else{
                               val new_pig=skin
                                new_pig.setBounds(pig.rect.left,pig.rect.top-containerSize[1],pig.rect.right,pig.rect.bottom-containerSize[1])
                                new_pig.draw(canvas!!)
                            }

                            }
                        if(pig.rect.bottom<0){
                            if(pig.rect.top<0){
                                pig.rect.bottom+=containerSize[1]
                                pig.rect.top+=containerSize[1]
                                pig.rectC.top+=containerSize[1]
                                pig.rectC.bottom+=containerSize[1]
                            }
                            else{
                                val new_pig=skin
                                new_pig.setBounds(pig.rect.left,pig.rect.top+containerSize[1],pig.rect.right,pig.rect.bottom+containerSize[1])
                                new_pig.draw(canvas!!)
                            }

                        }
                        }
                    }
                //exit

                //score_value+=go
                //score.text="Score: "+record_value
                if(score_value>record_value){
                    record_value=score_value
                    //record.text="Record: "+record_value
                }
                if(balls.size>0){if(balls[balls.size-1].rect.left<containerSize[0]*2)create_world(containerSize[0]*4)}
                else {
                    var h=Random.nextInt(containerSize[1])
                    balls.add(
                        Ball(
                            context.getDrawable(
                                R.drawable.ball)!!,
                            Rect(containerSize[1]/6*10,h,containerSize[1]/6*10+b_s,h+b_s),
                            Random.nextInt(-1,359),
                            containerSize
                        ))
                    create_world(containerSize[0]*4)
                }

                canvas!!.drawText("Record: "+record_value, 0f, 30f, p)
                //canvas.drawText("Record: "+pig.rect.top, 500f, 30f, p)
                canvas.drawText("Score: "+score_value, 0f, 62f, p)
                postInvalidateOnAnimation()}
else{
                if(end_game==1){
    soundpool.play(loseSound,1f,1f,1,0,1f)
if(record_value==score_value){

    val editor = pref.edit()
    editor.putInt("record", record_value)
    editor.apply()
}
if(reclam.isLoaded)reclam.show()




                end_game=2}

                for(cloud in clouds)cloud.draw(canvas)
                for (ball in balls)ball.draw(canvas)
                pig.draw(canvas)
                //draw button retry
                var buttonRetry=context.getDrawable(R.drawable.buttonretry)!!
                buttonRetry.setBounds(Rect(containerSize[0]/2-containerSize[1]/8*2,containerSize[1]/4,containerSize[0]/2+containerSize[1]/8*2,containerSize[1]/2))
                buttonRetry.draw(canvas!!)
                //draw button menu
                var buttonMenu=context.getDrawable(R.drawable.buttonmenu)!!
                buttonMenu.setBounds(Rect( containerSize[0]/2-containerSize[1]/8*2,containerSize[1]/2,containerSize[0]/2+containerSize[1]/8*2,containerSize[1]/4*3))
                buttonMenu.draw(canvas!!)
                canvas!!.drawText("Record: "+record_value, 0f, 30f, p)
                canvas.drawText("Score: "+score_value, 0f, 62f, p)

}

    }
    else canvas!!.drawText("wait...", 0f, 62f, p)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun create_bubble():Ball{
        var h=Random.nextInt(containerSize[1])

    var b=Ball(context.getDrawable(R.drawable.ball)!!,

        Rect(balls[balls.size-1].rect.left+b_s*3,h,balls[balls.size-1].rect.left+b_s*4,h+b_s),
        Random.nextInt(-1,359),
    containerSize)
    return b}
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun create_world(up_bord:Int){
    while(balls[balls.size-1].rect.left<up_bord)balls.add(create_bubble())}

    fun onclick(){
        if(end_game==0 && load_completed){
        pig.vect=jump
        soundpool.play(jumpSound, 1f,1f,1,0,1f)
            }
    }
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    fun ontouchevent(x:Float, y:Float){
        if(end_game==0 ){
            pig.vect=jump
            soundpool.play(jumpSound,1f,1f,1,0,1f)
            }

        else{

            if(x>containerSize[0]/2-containerSize[1]/8*2&&y>containerSize[1]/4&&x<containerSize[0]/2+containerSize[1]/8*2&&y<containerSize[1]/2){
                soundpool.play(startSound,1f,1f,1,0,1f)
                clouds.removeAll(clouds)
                balls.removeAll(balls)
                activity.recreate()
                //val intent= Intent(context,Game::class.java)
                            //startActivity(context,intent,intent.extras)
            }
            if(x>containerSize[0]/2-containerSize[1]/8*2&&y>containerSize[1]/2&&x<containerSize[0]/2+containerSize[1]/8*2&&y<containerSize[1]/4*3) {

                activity.finish()

                //var intent= Intent(context, MainActivity::class.java)
                //startActivity(context,intent,intent.extras)}
            }
        }
    }

    fun onDestroy(){
        soundpool.unload(startSound)
        soundpool.unload(jumpSound)
        soundpool.unload(loseSound)
    }

}