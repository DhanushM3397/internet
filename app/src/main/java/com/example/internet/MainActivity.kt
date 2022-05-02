package com.example.internet

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import com.google.firebase.installations.FirebaseInstallations


class MainActivity : AppCompatActivity() {
    lateinit var firebaseIam: FirebaseInAppMessaging
    lateinit var fresh_instal: TextView
    lateinit var text: TextView

     var list = listOf("dhanush","yamu",null,"swamy")

    var newlist= listOf<String?>()



    lateinit var eventTriggerButton: Button

    companion object {
        private const val TAG = "FirebaseInAppMessaging"
    }

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        firebaseIam = FirebaseInAppMessaging.getInstance()
        firebaseIam.isAutomaticDataCollectionEnabled = true
        firebaseIam.setMessagesSuppressed(false)

        fresh_instal = findViewById<TextView>(R.id.textView2)
        eventTriggerButton = findViewById<Button>(R.id.eventTriggerButton)
        text = findViewById<TextView>(R.id.instanceIdText)


        eventTriggerButton.setOnClickListener { view ->
            //firebaseAnalytics.logEvent("engagement_party", Bundle())
            firebaseIam.triggerEvent("engagement_party")
            addClickListener()


        }

        // Get and display/log the Instance ID
        FirebaseInstallations.getInstance().id.addOnSuccessListener {
            fresh_instal.text = getString(R.string.instance_id_fmt, it)
            Log.d(TAG, "InstanceId: $it")
        }


        //networkConnectionStatus=findViewById(R.id.text)

        Thread(Runnable {
            while (true) {
                // This string is displayed when device is not connected
                // to either of the aforementioned states
                var conStant: String = "Not Connected"

                // Invoking the Connectivity Manager
                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                // Fetching the Network Information
                val netInfo = cm.allNetworkInfo
                cm.activeNetwork

                // Finding if Network Info typeName is WIFI or MOBILE (Constants)
                // If found, the conStant string is supplied WIFI or MOBILE DATA
                // respectively. The supplied data is a Variable
                for (ni in netInfo) {
                    if (ni.typeName.equals("WIFI", ignoreCase = true))
                        if (ni.isConnected) conStant = "WIFI"
                    if (ni.typeName.equals("MOBILE", ignoreCase = true))
                        if (ni.isConnected) conStant = "MOBILE DATA"
                }

                // To update the layout elements in real-time, use runOnUiThread method
                // We are setting the text in the TextView as the string conState
                runOnUiThread {
                    // networkConnectionStatus.text = conStant
                }
            }
        }).start() // Starting the thread

    }


    private fun addClickListener() {


        val listener = InAppMessageClickListener()
        firebaseIam.addClickListener(listener)
    }

    private fun suppressMessages() {
        firebaseIam.setMessagesSuppressed(true)
    }

    private fun enableDataCollection() {
        // Only needed if firebase_inapp_messaging_auto_data_collection_enabled is set to
        // false in AndroidManifest.xml
        firebaseIam.isAutomaticDataCollectionEnabled = true
    }

}