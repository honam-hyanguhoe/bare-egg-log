package com.org.egglog.client.data

class Profile(val userId: Long, val name: String, val hospital: String, val isAuth: Boolean ?= false)