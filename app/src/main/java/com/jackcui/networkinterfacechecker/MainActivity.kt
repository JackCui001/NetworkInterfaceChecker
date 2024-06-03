package com.jackcui.networkinterfacechecker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.jackcui.networkinterfacechecker.databinding.ActivityMainBinding
import java.net.NetworkInterface

class MainActivity : AppCompatActivity() {
    private lateinit var vb: ActivityMainBinding
    private lateinit var info: MutableMap<String, String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
        updateInfo()
    }

    override fun onResume() {
        super.onResume()
        updateInfo()
    }

    private fun updateInfo() {
        info = getNetworkInterfaceInfo()
        val keyArr = info.keys.toTypedArray()
        (vb.actvNetworkInterface as MaterialAutoCompleteTextView).setSimpleItems(keyArr)
        vb.actvNetworkInterface.setOnItemClickListener { _, _, position, _ ->
            vb.tvInfo.text = info[keyArr[position]]
        }
    }

    private fun getNetworkInterfaceInfo(): MutableMap<String, String> {
        val interfaceInfo = hashMapOf<String, String>()
        for (curInterface in NetworkInterface.getNetworkInterfaces()) {
            val curInfo = StringBuilder()
            val inetAddr = curInterface.inetAddresses
            val inetAddrSB = StringBuilder()
            for (curAddr in inetAddr) {
                curAddr.hostAddress?.let {
                    inetAddrSB.appendLine(it)
                }
            }
            if (inetAddrSB.toString().isNotBlank()) {
                curInfo.appendLine(
                    "网络地址：\n$inetAddrSB"
                )
            }
            curInfo.appendLine(
                "MTU：${curInterface.mtu}\n"
            )
            curInfo.appendLine("已连接：${curInterface.isUp}\n")
            curInfo.appendLine("回环地址：${curInterface.isLoopback}\n")
            curInfo.appendLine("虚拟地址：${curInterface.isVirtual}\n")
            curInfo.appendLine("P2P连接：${curInterface.isPointToPoint}\n")
            interfaceInfo[curInterface.displayName] = curInfo.toString()
        }
        return interfaceInfo
    }

//    fun getGatewayAddress(context: Context): String? {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val network = connectivityManager.activeNetwork
//            val capabilities = connectivityManager.getNetworkCapabilities(network)
//
//            if ((capabilities != null) && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                val linkProperties = connectivityManager.getLinkProperties(network)
//                val gateway = linkProperties?.routes?.filter { it.isDefaultRoute }
//                    ?.firstNotNullOfOrNull { it.gateway }
//                return gateway?.hostAddress
//            }
//        } else {
//            // For devices with lower Android versions
//            val wifiManager =
//                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//            val dhcpInfo = wifiManager.dhcpInfo
//            return intToIp(dhcpInfo.gateway)
//        }
//        return null
//    }
//
//    private fun intToIp(ip: Int): String {
//        return "${ip and 0xFF}.${ip shr 8 and 0xFF}.${ip shr 16 and 0xFF}.${ip shr 24 and 0xFF}"
//    }
}