package org.ledgers

/**
 * Replaces the first element in the list that matches a [predicate], or adds it to the end of the list if no element was found.
 */
internal inline fun <T> List<T>.replaceFirstOrAdd(newElement: T, predicate: (T) -> Boolean): List<T> {
    val copy = ArrayList(this)
    val index = copy.indexOfFirst(predicate)
    if(index == -1) {
        copy.add(newElement)
    } else {
        copy[index] = newElement
    }
    return copy
}
