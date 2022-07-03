package com.nguyenvietnam.todolistapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nguyenvietnam.todolistapp.model.DatabaseHelper
import com.nguyenvietnam.todolistapp.model.Task
import com.nguyenvietnam.todolistapp.model.TaskAdapter
import kotlinx.android.synthetic.main.activity_edit_task.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class EditTaskActivity : AppCompatActivity() {
    lateinit var curTask:Task
    lateinit var taskAdapter: TaskAdapter
    lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        databaseHelper = DatabaseHelper(baseContext)

        initData()


        datePicker()
        timePicker()

        edit_BTNDelete.setOnClickListener {
            databaseHelper.deleteTask(curTask)
            var intent: Intent = Intent(this@EditTaskActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        edit_BTNEdit.setOnClickListener {
            curTask.title = edit_EditTextTaskTitle.text.toString()
            curTask.date = edit_TextViewDate.text.toString()
            curTask.time = edit_TextViewTime.text.toString()
            if(edit_CheckBoxStatus.isChecked == true) {
                curTask.status = 1
            } else {
                curTask.status = 0
            }

            databaseHelper.updateTask(curTask)

            var intent: Intent = Intent(this@EditTaskActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initData() {
        var title = intent.getStringExtra("taskTitle")
        var date = intent.getStringExtra("taskDate")
        var time = intent.getStringExtra("taskTime")
        var status = intent.getIntExtra("taskStatus", 0)
        var id = intent.getIntExtra("taskId", 0)

        curTask = Task(title.toString(), date.toString(), time.toString(), status)
        curTask.id = id

        edit_EditTextTaskTitle.setText(title)
        edit_TextViewDate.text = date.toString()
        edit_TextViewTime.text = time.toString()
        if(status == 0) {
            edit_CheckBoxStatus.isChecked = false
        } else if(status == 1) {
            edit_CheckBoxStatus.isChecked = true
        }

    }

    private fun timePicker() {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        edit_BTNTimePicker.setOnClickListener {
            val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                    view, mhour, mminute ->
                edit_TextViewTime.text = "" + mhour +":"+mminute
            }, hour, minute, false)
            timePickerDialog.show()
        }

    }

    private fun datePicker() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        edit_BTNDatePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                    view, myear, mmonth, mdayOfMonth ->
                edit_TextViewDate.text = ""+ mdayOfMonth +"/"+ mmonth +"/"+ myear
            }, year, month, day)
            datePickerDialog.show()
        }
    }

}