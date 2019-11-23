package com.flinect.engine

import kotlin.reflect.KClass

class Receiver {
    val handlers = HashMap<KClass<*>, MessageHandler<*>>()

    fun <T : Message> on(messageClass: KClass<T>, handler: MessageHandler<T>) {
        handlers[messageClass] = handler
    }

    inline fun <reified T : Message> getHandler(messageClass: KClass<*>): MessageHandler<T>? {
        @Suppress("UNCHECKED_CAST")
        return handlers[messageClass] as MessageHandler<T>?
    }
}
