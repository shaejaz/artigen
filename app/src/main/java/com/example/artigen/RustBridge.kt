package com.example.artigen

class RustBridge {
    companion object {
        @JvmStatic
        private external fun invokeFunction(pattern: String): String
    }

    fun invokeFunctionBridge(to: String): String {
        return invokeFunction(to)
    }
}