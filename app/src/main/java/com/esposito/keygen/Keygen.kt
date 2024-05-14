package com.esposito.keygen

import kotlin.random.Random

object Keygen {

    fun generate(length: Int, shouldAddNum: Boolean, shouldUseSpecialChars: Boolean): String {
        val charPool = ('a'..'z') + ('A'..'Z') +
                (if (shouldAddNum) ('0'..'9') else emptyList()) +
                (if (shouldUseSpecialChars) listOf('!', '@', '#', '$', '%', '^', '&', '*') else emptyList())
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}