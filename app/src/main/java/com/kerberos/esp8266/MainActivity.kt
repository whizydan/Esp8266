package com.kerberos.esp8266

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val name_of_board = findViewById<TextView>(R.id.boardname)
        val project = findViewById<TextView>(R.id.projectname)
        val author = findViewById<TextView>(R.id.author)
        val date = findViewById<TextView>(R.id.DATE)
        val lives = findViewById<TextView>(R.id.lives)
        val wifi = findViewById<Button>(R.id.wifibtn)
        val reset = findViewById<Button>(R.id.reset)
        val password = findViewById<EditText>(R.id.password)
        val bssid = findViewById<EditText>(R.id.bssid)
        val off = findViewById<View>(R.id.off)
        val on = findViewById<View>(R.id.on)
        var msg = ""
        var mFirebaseDatabaseInstances= FirebaseDatabase.getInstance()
        var mFirebaseDatabase=mFirebaseDatabaseInstances.getReference("config")
        var v = ""

        mFirebaseDatabase.addValueEventListener(object  :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                val cv = snapshot.getValue(Config::class.java)
                name_of_board.text = cv?.board
                project.text = cv?.project
                author.text = cv?.author
                date.text = cv?.date
                v = cv?.live.toString()
                if (v == cv?.live.toString()){
                    //turn on off and turn off on
                    on.setBackgroundColor(Color.GRAY)
                    off.setBackgroundColor(Color.RED)
                }
                on.setBackgroundColor(Color.GREEN)
                off.setBackgroundColor(Color.GRAY)

            }

            override fun onCancelled(error: DatabaseError) {
                Toasty.error(applicationContext, "error $error occurred", Toast.LENGTH_SHORT, true).show();
                off.setBackgroundColor(Color.RED)
            }

        })
        mFirebaseDatabaseInstances.getReference("debug").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val debug = snapshot.getValue(Debug::class.java)
                val livs = debug?.lives.toString()
                val trigger = debug?.trigger.toString()
                lives.text = "Online $livs times"
                if (trigger == "0"){
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (vibrator.hasVibrator()) { // Vibrator availability checking
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)) // New vibrate method for API Level 26 or higher
                        } else {
                            vibrator.vibrate(500) // Vibrate method for below API Level 26
                        }
                    }

                    val welcome= Snackbar.make(wifi,"Alarm has been set off",Snackbar.LENGTH_LONG)
                    welcome.show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toasty.error(applicationContext, "error $error occurred", Toast.LENGTH_SHORT, true).show();
            }

        })

        wifi.setOnClickListener {
            val pass = password.getText().toString()
            val name = bssid.getText().toString()
            if ((pass.length > 8) && (name.length > 5)){
                //go to firebase set command to start hotspot
                    val mydata = Runner(name,"start hotspot","starting hotspot",pass,"start hotspot true")
                    mFirebaseDatabaseInstances.getReference("runner").setValue(mydata).addOnCompleteListener(this,
                        OnCompleteListener { task ->
                            if (task.isSuccessful){
                                Toasty.info(applicationContext, "hotspot starting", Toast.LENGTH_SHORT, true).show();
                            }else {
                                Toast.makeText(this, "Data save Failed", Toast.LENGTH_LONG).show()
                            }
                        })

            }else{
                password.setError("try something longer")
                bssid.setError("try something longer")
            }

        }
        reset.setOnClickListener {
            //go to firebase set command to reset
            val mydata = Runner("kerberos","reset","reseting board","touchdown","reset true")
            mFirebaseDatabaseInstances.getReference("runner").setValue(mydata).addOnCompleteListener(this,
                OnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toasty.info(applicationContext, "rebooting............", Toast.LENGTH_SHORT, true).show();
                    }else {
                        Toast.makeText(this, "operation failed", Toast.LENGTH_LONG).show()
                    }
                })
        }


    }
}