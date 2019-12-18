package com.bean.studio

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity() {

    private val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        cloud.setOnClickListener { startActivity(Intent(this, InfoActivity::class.java)) }

        layoutParams.gravity = Gravity.CENTER_HORIZONTAL // Setting TextView LayoutParams

        val database = FirebaseDatabase.getInstance()
        val story = database.getReference("story")

        story.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)

                if (value != null) {
                    for (i in value) {
                        ShowTextAsyncTask(i.toString()).execute()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("FirebaseDatabase", "Failed to read value.", error.toException())
            }
        })

    }

    inner class ShowTextAsyncTask(word: String): AsyncTask<Void, Void, Boolean>() {
        private val word = word

        override fun onPreExecute() {

        }
        override fun doInBackground(vararg p0: Void?): Boolean{
            SystemClock.sleep((100 + Random().nextInt(100)).toLong()) // Sleep
            return true
        }

        override fun onPostExecute(result: Boolean) {
            val text = TextView(this@MainActivity)
            text.textSize = 25F
            text.text = word
            text.typeface = ResourcesCompat.getFont(this@MainActivity, R.font.uhbee_font)

            text.setTextColor(Color.parseColor("#ffffff"))
            text.animation = AnimationUtils.loadAnimation(applicationContext, R.anim.melt)

            text.layoutParams = layoutParams
            physics_layout.addView(text)

            Handler().postDelayed({
                text.x = (-100).toFloat()
                text.y = (-100).toFloat()
                text.visibility = View.GONE

                physics_layout.clearDisappearingChildren()

            }, 20000) // Delay
        }
    }
}
