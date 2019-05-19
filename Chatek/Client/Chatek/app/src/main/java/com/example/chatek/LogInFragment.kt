package com.example.chatek

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.example.chatek.SocketThread.*

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

        button_enter.setOnClickListener {
            socketThread.setNickName(editNick.text.toString())
            socketThread.setPassword(editPassword.text.toString())
            //val mainFragment = MainFragment()
            //mainFragment.set_SocketThread(socketThread)
            socketThread.setCommand(LOG_IN)
            //socketThread.setNickName(nickName)
            //router.navigateTo(true, mainFragment)
        }

        button_register.setOnClickListener {
            val registerFragment = RegisterFragment()
            registerFragment.set_SocketThread(socketThread)
            router.navigateTo(true, registerFragment)
        }

        return layout
    }
}