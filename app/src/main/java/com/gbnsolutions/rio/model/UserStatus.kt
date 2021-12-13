package com.gbnsolutions.rio.model

import java.util.*

class UserStatus {
    private var name: String? = null
    private var profileImage: String? = null
    private var lastUpdated: Long = 0
    private var statuses: ArrayList<Status>? = null

    constructor() {}
    constructor(
        name: String?,
        profileImage: String?,
        lastUpdated: Long,
        statuses: ArrayList<Status>?
    ) {
        this.name = name
        this.profileImage = profileImage
        this.lastUpdated = lastUpdated
        this.statuses = statuses
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getProfileImage(): String? {
        return profileImage
    }

    fun setProfileImage(profileImage: String?) {
        this.profileImage = profileImage
    }

    fun getLastUpdated(): Long {
        return lastUpdated
    }

    fun setLastUpdated(lastUpdated: Long) {
        this.lastUpdated = lastUpdated
    }

    fun getStatuses(): ArrayList<Status>? {
        return statuses
    }

    fun setStatuses(statuses: ArrayList<Status>?) {
        this.statuses = statuses
    }
}