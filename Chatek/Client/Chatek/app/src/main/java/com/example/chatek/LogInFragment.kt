package com.example.chatek

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import com.example.chatek.SocketThread.*
import java.io.FileInputStream;
import java.io.FileNotFoundException
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets.UTF_8

class LogInFragment : Fragment() {
    private lateinit var router: Router


    lateinit var socketThread : SocketThread

    public fun set_SocketThread(socketThread: SocketThread) {
        this.socketThread = socketThread
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        router = Router(requireActivity(), R.id.fragment_container)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val layout = inflater.inflate(R.layout.log_in, container, false)
        var editPassword : EditText = layout.findViewById(R.id.editPassword) as EditText
        var editNick : EditText = layout.findViewById(R.id.editNick) as EditText
        var button_enter : Button = layout.findViewById(R.id.button_enter) as Button
        var button_register : Button = layout.findViewById(R.id.button_register) as Button
        var switch : Switch = layout.findViewById(R.id.switch1) as Switch
        socketThread.setContext(requireContext())

        try {
            context!!.openFileOutput("isSaved", Context.MODE_APPEND)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        var isSaved : Int = context!!.openFileInput("isSaved").read()

        if (isSaved == 1){
            switch.setChecked(true)
            //fis = FileInputStream("password.txt")
            editPassword.setText(context!!.openFileInput("password.txt").readBytes().toString(UTF_8), TextView.BufferType.EDITABLE)
            //fis.close()
            //fis = FileInputStream("nick.txt")
            editNick.setText(context!!.openFileInput("nick.txt").readBytes().toString(UTF_8), TextView.BufferType.EDITABLE)
            //fis.close()
        }

        button_enter.setOnClickListener {
            socketThread.setNickName(editNick.text.toString())
            socketThread.setPassword(editPassword.text.toString())
            socketThread.setCommand(LOG_IN)
            context!!.openFileOutput("companions.txt", Context.MODE_APPEND)
            var x : Boolean = switch.isChecked
            if(x){
                context!!.openFileOutput("password.txt", Context.MODE_PRIVATE)
                        .write(editPassword.text.toString().toByteArray(UTF_8))
                context!!.openFileOutput("nick.txt", Context.MODE_PRIVATE)
                        .write(editNick.text.toString().toByteArray(UTF_8))
                context!!.openFileOutput("isSaved", Context.MODE_PRIVATE)
                        .write(1)
            } else {
                context!!.openFileOutput("isSaved", Context.MODE_PRIVATE)
                        .write(0)
            }
        }

        button_register.setOnClickListener {
            val registerFragment = RegisterFragment()
            registerFragment.set_SocketThread(socketThread)
            router.navigateTo(true, registerFragment)
        }

        return layout
    }
}