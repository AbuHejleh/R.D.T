package com.example.rdt.Needed

 class User {
     private  var id :String? = null

     private  var username : String? = null
     private lateinit var imageURl : String
     private  lateinit var status :String


     constructor(id: String , username: String , imageURl: String , status:String ) {
         this.id = id
         this.username = username
         this.imageURl = imageURl
         this.status= status

    }
    constructor(){}


    public fun setUserName(username: String) {
        this.username=username

    }
    public  fun  getUserName(): String? {
        return this.username
    }
     public fun setId(id: String) {
         this.id=id

     }
     public  fun  getId(): String? {
         return this.id
     }


    public fun setImageURl(imageURl : String) {
        this.imageURl = imageURl

    }
    public  fun  getImageURl(): String? {
        return imageURl
    }

     public fun setStatus(status : String) {
         this.status = status

     }
     public  fun  getStatus(): String? {
         return status
     }




}
