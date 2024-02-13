package de.flerbuster.commandApi.errors

import de.flerbuster.commandApi.command.arguments.type.ArgumentType
import kotlin.reflect.KClass

/**
 * Thrown when a [de.flerbuster.commandApi.command.arguments.type.ArgumentType] is not supported.
 */
class TypeNotSupportedError(type: KClass<*>) : IllegalArgumentException("class ${type.qualifiedName} is not supported")
