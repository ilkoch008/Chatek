package com.example.chatek

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout.VERTICAL
import com.example.chatek.SocketThread.*
import java.util.ArrayList

class DialogFragment  : Fragment() {
    private lateinit var router : Router
    lateinit var socketThread : SocketThread
    var companionId : Int = 0

    public fun set_SocketThread(socketThread: SocketThread) {
        this.socketThread = socketThread
    }

    public fun set_CompanionId(companionId : Int){
        this.companionId = companionId
    }

    val mmadapter : MyMessagesAdapterAdapter = MyMessagesAdapterAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //router = Router(requireActivity(), R.id.fragment_container)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val layout = inflater.inflate(R.layout.dialog_fragment, container, false)

        val messages : RecyclerView = layout.findViewById(R.id.messages_recycler_view) as RecyclerView
        messages.layoutManager = MyLinearLayoutManager(requireContext(), VERTICAL, true)
        val list = ArrayList<Message>()
        messages.recycledViewPool
        socketThread.setDialogRecView(messages)
        socketThread.setCompaionId(companionId)
        socketThread.setCommand(CHOOSE_COMPANION)
        mmadapter.listt_define(list)
        mmadapter.ownner_define(nickName)
        mmadapter.activity_define(requireActivity())
        messages.adapter = mmadapter
        mmadapter.notifyDataSetChanged()
        socketThread.setDialogView(layout)
        socketThread.setMmAdapter(mmadapter)

        var messageEditText : EditText = layout.findViewById(R.id.editText2) as EditText

        var buttonSend : Button = layout.findViewById(R.id.button2) as Button

        buttonSend.setOnClickListener {
            messageEditText.text.toString()
            socketThread.setMessage(messageEditText.text.toString())
            messageEditText.setText("")
            socketThread.setCommand(SEND_MESSAGE) //send message
        }

        return layout
    }

}