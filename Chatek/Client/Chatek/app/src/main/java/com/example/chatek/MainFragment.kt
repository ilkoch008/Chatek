package com.example.chatek

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.chatek.SocketThread.*
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.UTF_8
import java.util.ArrayList

var nickName : String = String()
var ipAddress : String = String()

class MainFragment : Fragment() {
    private lateinit var router: Router

    var mAdapter = MyAdapter()

    lateinit var socketThread : SocketThread

    public fun set_SocketThread(socketThread: SocketThread) {
        this.socketThread = socketThread
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        router = Router(requireActivity(), R.id.fragment_container)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var isSaved : Int = context!!.openFileInput("isSaved").read()

        socketThread.progressBar2.visibility = View.VISIBLE

        if (isSaved == 1){
            var compsJsonString = context!!.openFileInput("companions.txt")
                    .readBytes().toString(UTF_8)
            if(compsJsonString.isNotEmpty()) {
                var compsJson: JSONArray = JSONArray(compsJsonString)
                var companions: ArrayList<Companion> = ArrayList()
                for (i in 0 until compsJson.length()) {
                    var ii: JSONObject = compsJson.getJSONObject(i)
                    companions.add(Companion.Builder()
                            .nameIs(ii.getString("name"))
                            .idIs(ii.getInt("id"))
                            .availabilityIs(ii.getBoolean("availability"))
                            .newNumOfMessagesIs(ii.getInt("newNumberOfMessages"))
                            .oldNumOfMessagesIs(ii.getInt("oldNumberOfMessages"))
                            .build())
                }
                socketThread.companions = companions
            }
        }

        socketThread.setCommand(GET_COMPANIONS)
        socketThread.setContext(requireContext())
        val layout = inflater.inflate(R.layout.main_fragment, container, false)
        val companions : RecyclerView = layout.findViewById(R.id.my_recycler_view) as RecyclerView
        companions.layoutManager = MyLinearLayoutManager(requireContext())
        socketThread.setMainView(layout)
        socketThread.setMainRecView(companions)
        val list = ArrayList<Companion>()
        //list.add(Companion("Server", 0, true))
        socketThread.setCommand(GET_COMPANIONS)
        mAdapter.set_SocketThread(socketThread)
        mAdapter.listt_define(list)
        mAdapter.activity_define(requireActivity())
        companions.adapter = mAdapter
        companions.recycledViewPool.clear()
        socketThread.setmAdapter(mAdapter)
        mAdapter.notifyDataSetChanged()
        Handler().postDelayed({
            socketThread.setCommand(GET_COMPANIONS)
        }, 100)


//        var saveCompanions : Thread = Thread(Runnable {
//            while (true) {
//                Thread.sleep(10)
//                var isSaved: Int = context!!.openFileInput("isSaved").read()
//                var gson : Gson = Gson()
//                var str : String = gson.toJson(socketThread.companions).toString()
//                if (isSaved == 1) {
//                    context!!.openFileOutput("companions.txt", Context.MODE_PRIVATE)
//                            .write(gson.toJson(socketThread.companions).toString().toByteArray(UTF_8))
//                }
//                Thread.sleep(2000)
//            }
//        })
//        saveCompanions.start()

        return layout
    }

    override fun onResume() {
        super.onResume()
        socketThread.setCommand(GET_COMPANIONS)
    }

    override fun onStart() {
        super.onStart()

        socketThread.setCommand(GET_COMPANIONS)
    }

    override fun onStop() {
//        var isSaved: Int = context!!.openFileInput("isSaved").read()
//        var gson : Gson = Gson()
//        var str : String = gson.toJson(socketThread.companions).toString()
//        if (isSaved == 1) {
//            context!!.openFileOutput("companions.txt", Context.MODE_PRIVATE)
//                    .write(gson.toJson(socketThread.companions).toString().toByteArray(UTF_8))
//        }
        super.onStop()
    }

    override fun onPause() {
//        var isSaved: Int = context!!.openFileInput("isSaved").read()
//        var gson : Gson = Gson()
//        var str : String = gson.toJson(socketThread.companions).toString()
//        if (isSaved == 1) {
//            context!!.openFileOutput("companions.txt", Context.MODE_PRIVATE)
//                    .write(gson.toJson(socketThread.companions).toString().toByteArray(UTF_8))
//        }
        super.onPause()
    }
}