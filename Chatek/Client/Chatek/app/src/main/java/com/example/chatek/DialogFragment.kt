package com.example.chatek

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout.VERTICAL
import com.example.chatek.SocketThread.*
import com.google.gson.Gson
import java.nio.charset.StandardCharsets.UTF_8
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
        socketThread.progressBar2.visibility = View.VISIBLE
        val messages : RecyclerView = layout.findViewById(R.id.messages_recycler_view) as RecyclerView
        messages.layoutManager = MyLinearLayoutManager(requireContext(), VERTICAL, true)
        val list = ArrayList<Message>()
        messages.recycledViewPool
        socketThread.setDialogRecView(messages)
        socketThread.setCompaionId(companionId)
        socketThread.setCommand(CHOOSE_COMPANION)
        mmadapter.listt_define(list)
        mmadapter.ownner_define(socketThread.id)
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

        Handler().postDelayed({
            socketThread.setCommand(RENEW_DIALOG)
        }, 100)

        return layout
    }

    override fun onResume() {
        var isSaved: Int = context!!.openFileInput("isSaved").read()
        var gson : Gson = Gson()
        var str : String = gson.toJson(socketThread.companions).toString()
        if (isSaved == 1) {
            context!!.openFileOutput("companions.txt", Context.MODE_PRIVATE)
                    .write(gson.toJson(socketThread.companions).toString().toByteArray(UTF_8))
        }
        super.onResume()
        socketThread.setCommand(CHOOSE_COMPANION)
    }

    override fun onStart() {
        var isSaved: Int = context!!.openFileInput("isSaved").read()
        var gson : Gson = Gson()
        var str : String = gson.toJson(socketThread.companions).toString()
        if (isSaved == 1) {
            context!!.openFileOutput("companions.txt", Context.MODE_PRIVATE)
                    .write(gson.toJson(socketThread.companions).toString().toByteArray(UTF_8))
        }
        super.onStart()
        socketThread.setCommand(CHOOSE_COMPANION)
    }

}