package com.example.chatek

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MyMessagesAdapterAdapter : RecyclerView.Adapter<SearchClickableViewHolder>() {

    var listt = ArrayList<Message>()
    lateinit var req_activity: FragmentActivity
    private lateinit var router: Router

    fun listt_define(list: ArrayList<Message>){
        listt = list
    }

    fun activity_define(activity : FragmentActivity){
        req_activity = activity
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchClickableViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //tableOfItems = def_tableOfItems(tableOfItems)
        return SearchClickableViewHolder(
                inflater.inflate(R.layout.messages, parent, false),
                ::onItemClick)
    }

    override fun getItemCount(): Int = listt.size

    override fun onBindViewHolder(holder: SearchClickableViewHolder, position: Int) {
        holder.setText((listt[position]).message)
    }

    fun onItemClick(view: View, position: Int) {
        Toast.makeText(view.context, listt[position].owner, Toast.LENGTH_SHORT).show()
        //router = Router(req_activity, R.id.fragment_container)
        //val dialogfragment : DialogFragment = DialogFragment()
        //router.navigateTo(true, dialogfragment)
    }


}

class MessagesClickableViewHolder(view : View,
                                private val clickListener : (View, Int) -> Unit ) : RecyclerView.ViewHolder(view) {
    //private val title: ImageView = view.findViewById(R.id.title)
    private val text: TextView = view.findViewById(R.id.searching_text)

    init {
        view.setOnClickListener {
            clickListener(it, adapterPosition)
        }
    }

    fun setText(text: String) {
        this.text.text = text
    }
}
