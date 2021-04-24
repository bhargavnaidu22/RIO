package com.gbnsolutions.rio.model

import android.net.Uri

class Users {
    private var uid: String = ""
    private var username: String = ""
    private var profile: String = ""
    private var email: String = ""
    private var phone: String = ""
    private var password: String = ""

    constructor()
    constructor(uid: String, username: String, profile: String , email: String , password: String) {
        this.uid = uid
        this.username = username
        this.profile = profile
        this.email = email
        this.password = password
    }
    fun getUID(): String?{
        return uid
    }
    fun setUID(uid: String){
        this.uid = uid
    }
    fun getUserName(): String?{
        return username
    }
    fun setUserName(username: String){
        this.username = username
    }
    fun getProfile(): String?{
        return profile
    }
    fun setProfile(profile: String){
        this.profile = profile
    }
    fun getEmail(): String?{
        return email
    }
    fun setEmail(email: String){
        this.email = email
    }

    fun getPhone(): String?{
        return phone
    }
    fun setPhone(phone: String){
        this.phone = phone
    }
    fun getPassword(): String?{
        return password
    }
    fun setPassword(password: String){
        this.password
    }
}