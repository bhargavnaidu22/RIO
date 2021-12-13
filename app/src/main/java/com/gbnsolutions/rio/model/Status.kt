package com.gbnsolutions.rio.model

class Status {
    private var imageUrl: String? = null
    private var timeStamp: Long = 0

    constructor() {}
    constructor(imageUrl: String?, timeStamp: Long) {
        this.imageUrl = imageUrl
        this.timeStamp = timeStamp
    }

    fun getImageUrl(): String? {
        return imageUrl
    }

    fun setImageUrl(imageUrl: String?) {
        this.imageUrl = imageUrl
    }

    fun getTimeStamp(): Long {
        return timeStamp
    }

    fun setTimeStamp(timeStamp: Long) {
        this.timeStamp = timeStamp
    }
}