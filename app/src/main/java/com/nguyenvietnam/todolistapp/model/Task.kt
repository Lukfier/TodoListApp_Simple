package com.nguyenvietnam.todolistapp.model

class Task(title: String, date: String, time: String, status: Int = 0) {

    lateinit var title:String
    lateinit var date:String
    lateinit var time:String
    var status:Int =0
    var id:Int =0

    init {
        this.title = title
        this.date = date
        this.time = time
        this.status = status
    }
}