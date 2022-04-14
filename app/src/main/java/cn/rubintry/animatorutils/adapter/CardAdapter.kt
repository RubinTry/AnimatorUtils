package cn.rubintry.animatorutils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import cn.rubintry.animate.AnimateUtils
import cn.rubintry.animatorutils.R

class CardAdapter(private var dataList: MutableList<Card>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    var onItemClickListener : OnItemClickListener ?= null
    private var index = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card , parent , false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = dataList[position]
        holder.setText(R.id.tv_card_num , item.itemContent)
        holder.getView<TextView>(R.id.tv_card_num).setOnClickListener {
            onItemClickListener?.onItemClick(position , it)
        }
        if(item.canRotate){
            AnimateUtils.with(holder.getView(R.id.tv_card_num))
                .rotate(10f , true)
                .enableLoop(true)
                .start()
        }else{
            AnimateUtils.with(holder.getView(R.id.tv_card_num)).cancel()
            AnimateUtils.with(holder.getView(R.id.tv_card_num)).reset()
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(dataList: MutableList<Card>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun getData(): MutableList<Card> {
        return dataList
    }

    fun cancelNext() {
        if(index >= dataList.size){
            return
        }
        val card = dataList[index]
        card.canRotate = false
        notifyItemChanged(index)
        index ++
    }

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val viewMap = mutableMapOf<Int , View>()


        fun setText(@IdRes id : Int , content: String){
            getView<TextView>(id).text = content
        }

        fun <T : View> getView(@IdRes id : Int) : T{
            return getViewOrNull<T>(id) ?: throw IllegalStateException("Could not found view $id")
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : View> getViewOrNull(@IdRes id : Int) : T?{
            var view = viewMap[id]
            view?.let {
                return it as? T
            }
            view = itemView.findViewById(id)
            viewMap[id] = view
            return view as? T
        }
    }

    data class Card(var canRotate: Boolean, val itemContent: String)

    interface OnItemClickListener{
        fun onItemClick(position : Int , view: View)
    }
}