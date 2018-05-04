package com.truecallerdevsample

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.truecaller.android.sdk.*
import com.truecallerdevsample.models.User

/**
 * A placeholder fragment containing a simple view.
 */
class MainFragment : Fragment() ,ITrueCallback{



    val trueClient: TrueClient by lazy {
        TrueClient(context!!, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
   /*     val trueButton = view.findViewById(R.id.com_truecaller_android_sdk_truebutton) as (TrueButton)
//        trueButton.visibility = if (trueButton.isUsable) {
        trueButton.visibility =   View.VISIBLE
        *//*} else {
            View.GONE
        }*//*


        trueButton.setTrueClient(trueClient)*/
        val btnRegister= view.findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener{
            trueClient.getTruecallerUserProfile(activity!!)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (trueClient.onActivityResult(requestCode, resultCode, data)) {
            return
        } else if(requestCode == 200){
            activity!!.setResult(Activity.RESULT_OK)
            activity!!.finish()
        }
    }

    override fun onSuccesProfileShared(p0: TrueProfile) {
        val user = User()
        user.phone = p0.phoneNumber
        user.name = p0.firstName + " " + p0.lastName
        user.email = p0.email
//        user.imageUrl = p0.avatarUrl

        println(user.toString()+p0.phoneNumber+p0.firstName+p0.lastName+p0.email)
        val bundle = Bundle()
        bundle.putSerializable("data", user)
    }

    override fun onFailureProfileShared(p0: TrueError) {
        Toast.makeText(context, "error"+p0.errorType.toString(), Toast.LENGTH_SHORT).show()
        println(p0.errorType.toString()+p0.toString())
    }

}
