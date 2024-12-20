package com.example.ikappsfcm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ik.fcm.helperfcm.IkHelperFCM
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        GlobalScope.launch {
            IkHelperFCM.startFCMService(this@MainActivity,packageName)
        }
    }
}