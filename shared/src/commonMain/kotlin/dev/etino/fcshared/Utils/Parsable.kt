package dev.etino.fcshared.Utils

interface Parsable<in T, out U> {

    /**
     * Parses the provided [input] into an instance of type [Output].
     *
     * @param input The data to be parsed.
     * @return An instance of type [Output] representing the parsed data.
     * @throws IllegalArgumentException if the [input] cannot be parsed into type [Output].
     */
    fun parse(data: T): U

}