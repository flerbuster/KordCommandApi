package commandApi.builder.command.arguments.arguments

import commandApi.builder.command.arguments.argument.Argument

data class Arguments(val arguments: MutableList<Argument>) {
    fun forEach(block: (Argument) -> Unit) = arguments.forEach(block)
}