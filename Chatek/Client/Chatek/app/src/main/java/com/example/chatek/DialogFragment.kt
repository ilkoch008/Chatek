package com.example.chatek

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout.VERTICAL
import java.util.ArrayList

class DialogFragment  : Fragment() {
    private lateinit var router : Router
    lateinit var socketThread : SocketThread

    public fun set_SocketThread(socketThread: SocketThread) {
        this.socketThread = socketThread
    }

    val mmadapter : MyMessagesAdapterAdapter = MyMessagesAdapterAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //router = Router(requireActivity(), R.id.fragment_container)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val layout = inflater.inflate(R.layout.dialog_fragment, container, false)

        val messages = layout.findViewById<RecyclerView>(R.id.messages_recycler_view)
        messages.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, true)
        val list = ArrayList<Message>()

        mmadapter.listt_define(list)
        mmadapter.activity_define(requireActivity())
        messages.adapter = mmadapter
        mmadapter.notifyDataSetChanged()
        socketThread.setDialogView(layout)
        socketThread.setMmAdapter(mmadapter)

        var messageEditText : EditText = layout.findViewById(R.id.editText2)

        var buttonSend : Button = layout.findViewById(R.id.button2)

        buttonSend.setOnClickListener {
            socketThread.setMessage(messageEditText.text.toString())
            messageEditText.setText("")
            socketThread.setCommand(3) //send message
        }

        return layout
    }

}