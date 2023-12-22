package com.jackcui.networkinterfacechecker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
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
}

private fun getNetworkInterfaceInfo(): MutableMap<String, String> {
    val interfaceInfo = hashMapOf<String, String>()
    for (curInterface in NetworkInterface.getNetworkInterfaces()) {
        val curInfo = StringBuilder()
        val inetAddr = curInterface.inetAddresses
        val inetAddrSB = StringBuilder()
        curInterface.interfaceAddresses[0].broadcast
        for (curAddr in inetAddr) {
            curAddr.hostAddress?.let {
                inetAddrSB.append("$it\n")
            }
        }
        if (inetAddrSB.toString().isNotBlank()) {
            curInfo.append(
                "Internet addresses:\n $inetAddrSB\n"
            )
        }
        curInfo.append(
            "MTU:\n ${curInterface.mtu}\n\n"
        )
        curInfo.append("Is up:\n ${curInterface.isUp}\n\n")
        curInfo.append("Is loopback:\n ${curInterface.isLoopback}\n\n")
        curInfo.append("Is virtual:\n ${curInterface.isVirtual}\n\n")
        curInfo.append("Is p2p:\n ${curInterface.isPointToPoint}\n\n")
        interfaceInfo[curInterface.displayName] = curInfo.toString()
    }
    return interfaceInfo
}

//fun getGatewayAddress(context: Context): String? {
//    val connectivityManager =
//        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        val network = connectivityManager.activeNetwork
//        val capabilities = connectivityManager.getNetworkCapabilities(network)
//
//        if ((capabilities != null) && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//            val linkProperties = connectivityManager.getLinkProperties(network)
//            val gateway = linkProperties?.routes?.filter { it.isDefaultRoute }
//                ?.firstNotNullOfOrNull { it.gateway }
//            return gateway?.hostAddress
//        }
//    } else {
//        // For devices with lower Android versions
//        val wifiManager =
//            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        val dhcpInfo = wifiManager.dhcpInfo
//        return intToIp(dhcpInfo.gateway)
//    }
//    return null
//}
//
//private fun intToIp(ip: Int): String {
//    return "${ip and 0xFF}.${ip shr 8 and 0xFF}.${ip shr 16 and 0xFF}.${ip shr 24 and 0xFF}"
//}
