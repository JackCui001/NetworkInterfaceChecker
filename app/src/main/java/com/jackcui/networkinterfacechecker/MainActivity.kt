package com.jackcui.networkinterfacechecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import com.jackcui.networkinterfacechecker.databinding.ActivityMainBinding
import java.lang.StringBuilder
import java.net.NetworkInterface

class MainActivity : AppCompatActivity() {
    private lateinit var vb: ActivityMainBinding
    private var info = getNetworkInterfaceInfo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ViewBinding
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        val keyList = info.keys.toList()
        val adpt = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            keyList
        )
        vb.spnInterface.adapter = adpt
        vb.spnInterface.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                vb.tvInfo.text = info[keyList[position]]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getNetworkInterfaceInfo(): MutableMap<String, String> {
        val interfaceInfo = hashMapOf<String, String>()
        NetworkInterface.getNetworkInterfaces()?.let { it ->
            while (it.hasMoreElements()) {
                val curInterface = it.nextElement()
                val curInfo = StringBuilder()
                val inetAddr = curInterface.inetAddresses
                val inetAddrSB = StringBuilder()
                while (inetAddr.hasMoreElements()) {
                    val curAddr = inetAddr.nextElement()
                    curAddr.hostAddress?.let {
                        inetAddrSB.append("$it\n")
                    }
                }
                curInfo.append(
                    "Internet addresses:\n $inetAddrSB\n\n"
                )
                curInfo.append("Is up:\n ${curInterface.isUp}\n\n")
                curInfo.append("Is loopback:\n ${curInterface.isLoopback}\n\n")
                curInfo.append("Is virtual:\n ${curInterface.isVirtual}\n\n")
                curInfo.append("Is p2p:\n ${curInterface.isPointToPoint}\n\n")
                interfaceInfo[curInterface.displayName] = curInfo.toString()
            }
        }
        return interfaceInfo
    }
}
