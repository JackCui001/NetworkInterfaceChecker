package com.jackcui.networkinterfacechecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.net.NetworkInterface

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getNetworkInterfaceInfo(): MutableMap<String, String> {
        val interfaceInfo = hashMapOf<String, String>()
        NetworkInterface.getNetworkInterfaces()?.let {
            while (it.hasMoreElements()) {
                val curInterface = it.nextElement()
                val curInfo =
                    "Display name: ${curInterface.displayName}\nHardware address: ${curInterface.hardwareAddress}\nInternet addresses: ${
                        curInterface.inetAddresses.toList().joinToString()
                    }\nIs up: ${curInterface.isUp}\nIs loopback: ${curInterface.isLoopback}\nIs virtual: ${curInterface.isVirtual}\nIs p2p: ${curInterface.isPointToPoint}"
                interfaceInfo[curInterface.displayName] = curInfo
            }
        }
        return interfaceInfo;
    }
}
