package com.truecallerdevsample

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateEncodingException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*

class MainActivity : AppCompatActivity() {

    var mainFragment= MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        getCertificateSHA1Fingerprint()
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    @SuppressLint("PackageManagerGetSignatures")
    private fun getCertificateSHA1Fingerprint() {
        val pm = packageManager
        val packageName = packageName
        val flags = PackageManager.GET_SIGNATURES
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = pm.getPackageInfo(packageName, flags)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        assert(packageInfo != null)
        val signatures = packageInfo!!.signatures
        val cert = signatures[0].toByteArray()
        val input = ByteArrayInputStream(cert)
        var cf: CertificateFactory? = null
        try {
            cf = CertificateFactory.getInstance("X509")
        } catch (e: CertificateException) {
            e.printStackTrace()
        }

        var c: X509Certificate? = null
        try {
            assert(cf != null)
            c = cf!!.generateCertificate(input) as X509Certificate
        } catch (e: CertificateException) {
            e.printStackTrace()
        }

        val hexString: String
        try {
            val md = MessageDigest.getInstance("SHA1")
            assert(c != null)
            val publicKey = md.digest(c!!.encoded)
            hexString = byte2HexFormatted(publicKey)

            println("sha1"+ hexString)
        } catch (e1: NoSuchAlgorithmException) {
            e1.printStackTrace()
        } catch (e1: CertificateEncodingException) {
            e1.printStackTrace()
        }

    }

    fun byte2HexFormatted(arr: ByteArray): String {
        val str = StringBuilder(arr.size * 2)
        for (i in arr.indices) {
            var h = Integer.toHexString(arr[i].toInt())
            val l = h.length
            if (l == 1) h = "0$h"
            if (l > 2) h = h.substring(l - 2, l)
            str.append(h.toUpperCase(Locale.getDefault()))
            if (i < arr.size - 1) str.append(':')
        }
        return str.toString()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)

            mainFragment = fragment as MainFragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (true)
            mainFragment.onActivityResult(requestCode, resultCode, data)
    }
}
