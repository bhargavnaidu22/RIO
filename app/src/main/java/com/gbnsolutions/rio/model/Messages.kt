package com.gbnsolutions.rio.model
class Messages {
    private var messageId: String? = null
    private var message: String? = null
    private var senderId: String? = null
    private var imageUrl: String? = null
    private var timestamp: Long = 0
    private var feeling = -1

    constructor() {}
    constructor(message: String?, senderId: String?, timestamp: Long) {
        this.message = message
        this.senderId = senderId
        this.timestamp = timestamp
    }

    fun getMessageId(): String? {
        return messageId
    }

    fun setMessageId(messageId: String?) {
        this.messageId = messageId
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getSenderId(): String? {
        return senderId
    }

    fun setSenderId(senderId: String?) {
        this.senderId = senderId
    }

    fun getTimestamp(): Long {
        return timestamp
    }

    fun setTimestamp(timestamp: Long) {
        this.timestamp = timestamp
    }

    fun getFeeling(): Int {
        return feeling
    }

    fun setFeeling(feeling: Int) {
        this.feeling = feeling
    }

    fun getImageUrl(): String? {
        return imageUrl
    }

    fun setImageUrl(imageUrl: String?) {
        this.imageUrl = imageUrl
    }
}