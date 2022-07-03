package com.nguyenvietnam.todolistapp.model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.nguyenvietnam.todolistapp.R
import kotlinx.android.synthetic.main.recyclerview_model.view.*
import java.io.Serializable

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private var tasks : MutableList<Task> = mutableListOf()
    private lateinit var listener : onItemClickListener

    fun initList(data : MutableList<Task>) {
        tasks = data
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        this.listener = listener
    }

    fun addTask(task: Task) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class ViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun bind(task:Task) {
            itemView.cardview_title.text = "Task: " + task.title
            itemView.cardview_date.text = "Date: " + task.date
            itemView.cardview_time.text = "Time: " + task.time
            if(task.status==1){
                itemView.cardview_title.setTextColor(Color.parseColor("#0aad3f"))
                itemView.cardview_date.setTextColor(Color.parseColor("#0aad3f"))
                itemView.cardview_time.setTextColor(Color.parseColor("#0aad3f"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var holder = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_model, parent, false)


        return ViewHolder(holder, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}