package com.example.chatek

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.view.animation.AlphaAnimation



class MyMessagesAdapterAdapter : RecyclerView.Adapter<SearchClickableViewHolder>() {

    var listt = ArrayList<Message>()
    lateinit var req_activity: FragmentActivity
    private lateinit var router: Router
    var owner : String = " "

    fun listt_clear(){
        listt.clear()
    }

    fun ownner_define(owner : String){
        this.owner = owner
    }

    fun listt_define(list: ArrayList<Message>){
        listt = list
    }

    fun activity_define(activity : FragmentActivity){
        req_activity = activity
    }

    fun get_listt_size() : Int {
        return listt.size
    }


    override fun getItemViewType(position: Int): Int {
        when(listt[position].owner){
            "Server" -> return 0
            owner -> return 1
            else -> return 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchClickableViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType){
            0 ->return SearchClickableViewHolder(
                    inflater.inflate(R.layout.messages, parent, false),
                    ::onItemClick)
            1 ->return SearchClickableViewHolder(
                    inflater.inflate(R.layout.my_message, parent, false),
                    ::onItemClick)
            else -> return SearchClickableViewHolder(
                    inflater.inflate(R.layout.companions_message, parent, false),
                    ::onItemClick)
        }
    }

    override fun getItemCount(): Int = listt.size

    override fun onBindViewHolder(holder: SearchClickableViewHolder, position: Int) {
        if (position < listt.size) {
            holder.setText((listt[position]).message)
            setFadeAnimation(holder.itemView)
        } else {
            holder.setText(listt[listt.size-2].message)
            setFadeAnimation(holder.itemView)
        }
    }

    fun onItemClick(view: View, position: Int) {
        Toast.makeText(view.context, listt[position].owner, Toast.LENGTH_SHORT).show()
        //router = Router(req_activity, R.id.fragment_container)
        //val dialogfragment : DialogFragment = DialogFragment()
        //router.navigateTo(true, dialogfragment)
    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 400
        view.startAnimation(anim)
    }


}

class MessagesClickableViewHolder(view : View, private val clickListener
                                        : (View, Int) -> Unit ) : RecyclerView.ViewHolder(view) {
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
