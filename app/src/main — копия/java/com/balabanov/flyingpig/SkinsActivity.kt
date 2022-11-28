package com.balabanov.flyingpig

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import kotlinx.android.synthetic.main.activity_skins.*


class Skins1 : AppCompatActivity(),PurchasesUpdatedListener {
    lateinit var pref: SharedPreferences
	
    private var billingClient: BillingClient?=null
    var mSkuDetailsMap=HashMap<String,SkuDetails>()
    var payed=false

    val PIGS_SIZE=7//
    var pig_i=0
    var choosen_pig=0
    var ok=0

    lateinit var soundpool: SoundPool
    var oink=0
    var oink_zombie=0
    var oink_mech=0


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skins)


        soundpool= SoundPool(8, AudioManager.STREAM_MUSIC,0)
        oink=soundpool.load(this,R.raw.jump,1)
        oink_zombie=soundpool.load(this,R.raw.jump_zombie,1)
        oink_mech=soundpool.load(this,R.raw.jump_mech,1)

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        if (pref.contains("choosen_pig")) {
            choosen_pig=pref.getInt("choosen_pig", 0)
            pig_i=choosen_pig
        }


        choose.layoutParams.height= Resources.getSystem().displayMetrics.heightPixels/6
        choose.layoutParams.width= Resources.getSystem().displayMetrics.heightPixels/3

        when(choosen_pig){
            0->choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig)!!)
            1->choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_in_hat)!!)
            2->choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_hero)!!)
            3->choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_zombie)!!)
            4->choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_wizard)!!)
            5->choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_panda)!!)
            6->choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_clown)!!)
            7->choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_mech_anim)!!)
    }
)
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
    }
    override fun onDestroy() {
        super.onDestroy()
        soundpool.unload(oink)
        soundpool.unload(oink_zombie)
        soundpool.unload(oink_mech)
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun choose(v: View){
        oink()
        if(ok==1) {
            choosen_pig =pig_i
            val editor = pref.edit()
            editor.putInt("choosen_pig", choosen_pig)
            editor.apply()
            finish()//?
        }
        else if(ok==2)startActivity(Intent(this, Game::class.java))
        else if(ok==3)
            if (billingClient!!.isReady)initiatePurchase()
            else initBilling()
            when(pig_i){
                5->launchBilling("panda")
                6->launchBilling("clown")
                7->launchBilling("mech")

        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun pre(v: View){
        pig_i--
        if(pig_i<0)pig_i=PIGS_SIZE
        oink()
        upgrade()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun next(v: View){
        pig_i++
        if(pig_i>PIGS_SIZE)pig_i=0
        oink()
        upgrade()
    }

    fun back(v: View){
        oink()
        finish()
    }
    fun oink(){
        if(pig_i==3)soundpool.play(oink_zombie,1f,1f,1,0,1f)
        else if(pig_i==7)soundpool.play(oink_mech,1f,1f,1,0,1f)
        else soundpool.play(oink,1f,1f,1,0,1f)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun upgrade(){
        //button_img=choose
        if(pig_i<5){ok=2}
                else ok=3
            //when for txt

        when(pig_i){
            0->{
                ok=1
                info.text=""
                choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig)!!)
                choose.setImageDrawable(getDrawable(R .drawable.choose))
            }
            1->{
                info.text="fly by 10 asteroids"
                choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_in_hat)!!)
                choose.setImageDrawable(getDrawable(R.drawable.getit))
                if (pref.contains("record")) {
                    if (pref.getInt("record", 0) >= 10) {
                        ok = 1
                        choose.setImageDrawable(getDrawable(R.drawable.choose))
                    }
                }
            }
            2-> {
                info.text="fly by 50 asteroids"
                choose_skin_view.setImageDrawable(getDrawable(R.drawable.pig_hero)!!)
                choose.setImageDrawable(getDrawable(R.drawable.getit))
                if (pref.contains("record")) {
                    if (pref.getInt("record", 0) >= 50) {
                        ok = 1
                        choose.setImageDrawable(getDrawable(R.drawable.choose))
                    }
                }
            }
            3->{
                info.text="fly by 100 asteroids"
                choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_zombie)!!)
                choose.setImageDrawable(getDrawable(R.drawable.getit))
                if (pref.contains("record")) {
                    if (pref.getInt("record", 0) >= 100) {
                        ok = 1
                        choose.setImageDrawable(getDrawable(R.drawable.choose))
                    }
                }
            }
            4->{
                info.text="fly by 200 asteroids"
                choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_wizard)!!)
                choose.setImageDrawable(getDrawable(R.drawable.getit))
                if (pref.contains("record")) {
                    if (pref.getInt("record", 0) >= 200) {
                        ok = 1
                        choose.setImageDrawable(getDrawable(R.drawable.choose))
                    }
                }
            }
            5->{
                info.text="1$"
                choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_panda)!!)
                choose.setImageDrawable(getDrawable(R.drawable.buyit))
                if (pref.contains("panda")) {
                    if (pref.getBoolean("panda", false)) {
                        ok = 1
                        choose.setImageDrawable(getDrawable(R.drawable.choose))
                    }
                }
            }
            6->{
                info.text="1$"
                choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_clown)!!)
                choose.setImageDrawable(getDrawable(R.drawable.buyit))
                if (pref.contains("clown")) {
                    if (pref.getBoolean("clown", false)) {
                        ok = 1
                        choose.setImageDrawable(getDrawable(R.drawable.choose))
                    }
                }
            }
            7->{
                info.text="it does not lose when leaving the area\n3$"
                //choose_skin_view.setBackgroundResource(R .drawable.pig_mech_anim)
                choose_skin_view.setImageDrawable(getDrawable(R .drawable.pig_mech_anim)!!)
                //var mech_skin=choose_skin_view.getB
                /*mech_skin.callback=choose_skin
                mech_skin.setVisible(true,true)
                choose_skin_view.post(Runnable {
                    mech_skin.start()
                })*/
                choose.setImageDrawable(getDrawable(R.drawable.buyit))
                if (pref.contains("mech")) {
                    if (pref.getBoolean("mech", false)) {
                        ok = 1
                        choose.setImageDrawable(getDrawable(R.drawable.choose))
                    }
                }
            }
    }
        if(choosen_pig==pig_i){
            ok=0
            choose.setImageDrawable(getDrawable(R .drawable.choosen))
        }
}









@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun initBilling() {
        billingClient = com.android.billingclient.api.BillingClient.newBuilder(this).setListener(this).build();
    billingClient!!.startConnection(object:BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult:BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {}
                    /*val queryPurchase = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
                    val queryPurchases: List<Purchase>? = queryPurchase.purchasesList
                    if (queryPurchases != null && queryPurchases.size > 0) {
                        handlePurchases(queryPurchases)
                    }}*/
                    //if purchase list is empty that means item is not purchased
                    //Or purchase is refunded or canceled
            else Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT)}
            override fun onBillingServiceDisconnected() {
                //here when something went wrong, e.g. no internet connection
            }
        })
    }

    fun initiatePurchase() {
        var params = SkuDetailsParams.newBuilder();
        var skuList:MutableList<String> = ArrayList<String>();
        skuList.add("clown");
        skuList.add("panda")
        skuList.add("mech")
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        //withContext(Dispatchers.IO){
        billingClient!!.querySkuDetailsAsync(params.build()){
            billingResult,skuDetailsList->
            if(billingResult.responseCode==BillingClient.BillingResponseCode.OK){
                if (skuDetailsList!=null && skuDetailsList.size>0){
                    //val flowParams=BillingFlowParams.newBuilder().setSkuDetails(skuDetailsList[0]).build()
                    //billingClient!!.launchBillingFlow(this@Skins1,flowParams)
                    //mSkuDetailsMap.put(skuDetailsList[0].getSku(), skuDetailsList[0])
                    for (skuDetails in skuDetailsList!!) {
                        mSkuDetailsMap.put(skuDetails.getSku(), skuDetails);
                    }
                }
            else Toast.makeText(applicationContext,"Error: purchase item not found",Toast.LENGTH_SHORT)
    }
        else Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT)
        }}///инфо о товарах и покупках

    /*fun queryPurchases():List<Purchase> {
        var purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
        return purchasesResult.getPurchasesList();
    }*/

    fun launchBilling(skuId:String) {
        var billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(mSkuDetailsMap.get(skuId)!!)
                .build();
        billingClient!!.launchBillingFlow(this, billingFlowParams);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun payComplete() {
        //Toast.makeText(this, getString(R.string.hello_world), Toast.LENGTH_SHORT).show();
        //payed=true
        when(pig_i){
            5-> {
                choosen_pig=5
                choose.setImageDrawable(getDrawable(R.drawable.choosen))
                val editor = pref.edit()
                editor.putBoolean("panda", true)
                editor.apply()
            }
            6->{
                choosen_pig=6
                choose.setImageDrawable(getDrawable(R.drawable.choosen))
                val editor = pref.edit()
                editor.putBoolean("clown", true)
                editor.apply()
            }
            7->{
                choosen_pig=7
                choose.setImageDrawable(getDrawable(R.drawable.choosen))
                val editor = pref.edit()
                editor.putBoolean("mech", true)
                editor.apply()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onPurchasesUpdated(responseCode: BillingResult, purchases: MutableList<Purchase>?) {
        if (responseCode.responseCode== BillingClient.BillingResponseCode.OK && purchases != null) {
        //here when purchase completed
        payComplete();
        }
    }


}