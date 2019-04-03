package com.example.chatek

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import java.util.ArrayList

class GetPropertiesFragment : Fragment() {
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

        val layout = inflater.inflate(R.layout.get_ip_nickname, container, false)
        var getNick : EditText = layout.findViewById(R.id.editNick)
        var getIp : EditText = layout.findViewById(R.id.editIp)
        var button : Button = layout.findViewById(R.id.button_ip)

        button.setOnClickListener {
            nickName = getNick.text.toString()
            ipAddress = getIp.text.toString()
            val mainFragment = MainFragment()
            mainFragment.set_SocketThread(socketThread)
            socketThread.setCommand(1)
            socketThread.setIp(ipAddress)
            socketThread.setNickName(nickName)
            socketThread.start()
            router.navigateTo(false, mainFragment)
        }

        return layout
    }
}