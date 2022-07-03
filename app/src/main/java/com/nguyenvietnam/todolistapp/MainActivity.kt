package com.nguyenvietnam.todolistapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyenvietnam.todolistapp.model.DatabaseHelper
import com.nguyenvietnam.todolistapp.model.Task
import com.nguyenvietnam.todolistapp.model.TaskAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import java.sql.Date
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var taskAdapter: TaskAdapter
    lateinit var list: MutableList<Task>
    lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(baseContext)

        datePicker()
        timePicker()

        list = mutableListOf<Task>()
        list = databaseHelper.allTask

        taskAdapter = TaskAdapter()

        main_recyclerViewTaskList.apply {
            var linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            adapter = taskAdapter
        }.run {
            taskAdapter.initList(list)
        }

        main_BTNAddTask.setOnClickListener {
            val taskTitle = main_EditTextTaskTitle.text.toString()
            val date = main_TextViewDate.text.toString()
            val time = main_TextViewTime.text.toString()

            if (taskTitle.isEmpty() || date.equals("Date") || time.equals("Time")) {
                Toast.makeText(this, "Please enter all field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var newTask = Task(taskTitle, date, time, 0)
            taskAdapter.addTask(newTask)
            list.add(newTask)
            main_EditTextTaskTitle.text.clear()
            main_TextViewDate.text = "Date"
            main_TextViewTime.text = "Time"

            databaseHelper.addTask(newTask)
        }

        taskAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                main_recyclerViewTaskList.smoothScrollToPosition(taskAdapter.itemCount - 1)
            }
        })

        itemClicked()

    }

    override fun onResume() {
        super.onResume()
        list = databaseHelper.allTask
        taskAdapter.notifyDataSetChanged()
    }

    private fun itemClicked() {
        taskAdapter.setOnItemClickListener(object : TaskAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                var intent: Intent = Intent(this@MainActivity, EditTaskActivity::class.java)
                intent.putExtra("taskTitle", list.get(position).title)
                intent.putExtra("taskDate", list.get(position).date)
                intent.putExtra("taskTime", list.get(position).time)
                intent.putExtra("taskStatus", list.get(position).status)
                intent.putExtra("taskId", list.get(position).id)

                startActivity(intent)
                Toast.makeText(this@MainActivity, list.get(position).title, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun timePicker() {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        main_BTNTimePicker.setOnClickListener {
            val timePickerDialog =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, mhour, mminute ->
                    main_TextViewTime.text = "" + mhour + ":" + mminute
                }, hour, minute, true)
            timePickerDialog.show()
        }

    }

    private fun datePicker() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        main_BTNDatePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, myear, mmonth, mdayOfMonth ->
                    main_TextViewDate.text = "" + mdayOfMonth + "/" + mmonth + "/" + myear
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
}