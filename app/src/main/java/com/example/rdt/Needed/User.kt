package com.example.rdt.Needed

 class User {
     private lateinit var id :String

     private lateinit var username : String
     private var imageURl : String? = null


     constructor(id: String , username: String , imageURl: String ) {
        this.id = id
        this.username = username
        this.imageURl = imageURl

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




}
