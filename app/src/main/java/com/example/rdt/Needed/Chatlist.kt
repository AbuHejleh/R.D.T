package com.example.rdt.Needed

class Chatlist {
    private lateinit var id :String
    constructor(id:String){
        this.id=id

    }
    constructor(){}
    fun getId() :String {
        return id
    }
    fun setId(id: String){
        this.id=id
    }



}