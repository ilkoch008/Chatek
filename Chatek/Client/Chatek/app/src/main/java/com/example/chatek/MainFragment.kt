package com.example.chatek

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatek.SocketThread.*
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
        socketThread.setCommand(GET_COMPANIONS)
        val layout = inflater.inflate(R.layout.main_fragment, container, false)
        val companions : RecyclerView = layout.findViewById(R.id.my_recycler_view) as RecyclerView
        companions.layoutManager = MyLinearLayoutManager(requireContext())
        socketThread.setMainView(layout)
        socketThread.setMainRecView(companions)
        val list = ArrayList<Companion>()
        list.add(Companion("Server", 0, true))
        socketThread.setCommand(GET_COMPANIONS)
        mAdapter.set_SocketThread(socketThread)
        mAdapter.listt_define(list)
        mAdapter.activity_define(requireActivity())
        companions.adapter = mAdapter
        companions.recycledViewPool.clear()
        socketThread.setmAdapter(mAdapter)
        mAdapter.notifyDataSetChanged()
        //socketThread.setCommand(2)
        return layout
    }

    override fun onResume() {
        super.onResume()
        socketThread.setCommand(GET_COMPANIONS)
    }
}