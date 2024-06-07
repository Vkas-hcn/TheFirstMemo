package com.flying.dutch.myapplication.data

data class ToDoBean(
    var id:Long,
    var title:String,
    var description:String,
    var date:String,
    var isFinish:Boolean,
    var level:Int,
)
