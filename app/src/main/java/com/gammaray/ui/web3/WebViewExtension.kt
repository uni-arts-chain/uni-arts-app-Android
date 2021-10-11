package com.gammaray.ui.web3

import com.gammaray.ui.x5.X5WebView


fun X5WebView.sendError(message: String, methodId: Long) {
    val script = "window.ethereum.sendError($methodId, \"$message\")"
    this.post {
        this.evaluateJavascript(script) {}
    }
}

fun X5WebView.sendResult(message: String, methodId: Long) {
    val script = "window.ethereum.sendResponse($methodId, \"$message\")"
    this.post {
        this.evaluateJavascript(script) {}
    }
}

fun X5WebView.sendResults(messages: List<String>, methodId: Long) {
    val message = messages.joinToString(separator = ",")
    val script = "window.ethereum.sendResponse($methodId, \"$message\")"
    this.post {
        this.evaluateJavascript(script) {}
    }
}

fun String.toHexByteArray(): ByteArray {
    return Numeric.hexStringToByteArray(this)
}
