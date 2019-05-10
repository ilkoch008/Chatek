package com.example.chatek

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.registration.*

class RegisterFragment : Fragment() {
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

        val layout = inflater.inflate(R.layout.registration, container, false)
        var getPassword: EditText = layout.findViewById(R.id.editPassword) as EditText
        var getPassword1: EditText = layout.findViewById(R.id.editPassword1) as EditText
        var getNickname: EditText = layout.findViewById(R.id.editNick) as EditText
        var button: Button = layout.findViewById(R.id.button_ip) as Button
        socketThread.setContext(requireContext())

        button.setOnClickListener {
            if (getPassword.text.toString() != getPassword1.text.toString()) {
                Toast.makeText(requireContext(), "Passwords not equal", Toast.LENGTH_SHORT).show()
            } else {
                //val mainFragment = MainFragment()
                //mainFragment.set_SocketThread(socketThread)
                socketThread.setPassword(getPassword.text.toString())
                socketThread.setNickName(getNickname.text.toString())
                socketThread.setCommand(12)
                //socketThread.setNickName(nickName)
                //router.navigateTo(true, mainFragment)
            }
        }

        return layout
    }
}