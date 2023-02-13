package com.example.reactivekotlin

data class Message(
    var status: String, var message: Map<String, List<String>>
)

data class Image(
    var status: String = "success", var message: String
)


// Errors
open class Error(val status: String = "error", val message: String)

class Error500(message: String) : Error("Internal Server Error", message)
class Error404(message: String) : Error("Not Found", message)
class Error400(message: String) : Error("Bad request", message)
