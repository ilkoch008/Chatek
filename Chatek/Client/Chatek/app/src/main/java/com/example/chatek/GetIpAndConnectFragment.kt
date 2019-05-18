package com.example.chatek

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class GetIpAndConnectFragment : Fragment() {
    private lateinit var router: Router

    private lateinit var mainActivity: MainActivity

    lateinit var socketThread : SocketThread

    public fun set_SocketThread(socketThread: SocketThread) {
        this.socketThread = socketThread
    }

    public fun set_mainActivity(mainActivity: MainActivity){
        this.mainActivity = mainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        router = Router(requireActivity(), R.id.fragment_container)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val layout = inflater.inflate(R.layout.get_ip_and_connect, container, false)
        socketThread.setMainView(layout)
        var getIp : EditText = layout.findViewById(R.id.editIp) as EditText
        var button : Button = layout.findViewById(R.id.button_ip) as Button
        socketThread.setCommand(0)
        socketThread.start();

        button.setOnClickListener {
            ipAddress = getIp.text.toString()
            socketThread.setIp(ipAddress)
            socketThread.setCommand(1)
            button.visibility = View.GONE
            Handler().postDelayed({
                if(!socketThread.isConnected){
                    Toast.makeText(requireContext(), "Can't connect", Toast.LENGTH_SHORT).show()
                    socketThread.setCommand(0)
                    button.visibility = View.VISIBLE
                }
            }, 2000)
        }

        return layout
    }
}