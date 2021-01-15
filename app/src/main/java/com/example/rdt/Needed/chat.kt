package com.example.rdt.Needed

class chat {
    private  var  sender: String? = null
    private var  receiver: String? = null
    private  var  message: String? = null
    private  var seen : Boolean? =null


    constructor(sender: String , receiver: String , message: String ) {
        this.sender = sender
        this.receiver = receiver
        this.message = message
        this.seen =seen

    }
    constructor(){}



    public fun setSender(sender: String) {
        this.sender=sender

    }
    public  fun  getSender(): String? {
        return this.sender
    }
    public fun setReceiver(receiver: String) {
        this.receiver=receiver

    }
    public  fun  getReceiver(): String? {
        return this.receiver
    }


    public fun setMessage(message : String) {
        this.message = message

    }
    public  fun  getMessage(): String? {
        return message
    }

    public fun setSeen(seen : Boolean) {
        this.seen = seen

    }
    public  fun  getSeen(): Boolean? {
        return seen
    }

}