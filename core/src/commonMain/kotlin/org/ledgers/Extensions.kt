package org.ledgers

import org.ledgers.domain.scenario.Step

/**
 * Replaces the first element in the list that matches a [predicate], or adds it to the end of the list if no element was found.
 */
internal inline fun <T> List<T>.replaceFirstOrAdd(newElement: T, predicate: (T) -> Boolean): List<T> {
    val copy = ArrayList(this)
    val index = copy.indexOfFirst(predicate)
    if (index == -1) {
        copy.add(newElement)
    } else {
        copy[index] = newElement
    }
    return copy
}

internal fun <T> List<T>.replaceAtIndex(index: Int, element: T, fillWith: T): List<T> {
    if (index < 0) {
        throw RuntimeException("Index must be greater than equal to 0")
    }
    val maxStep = size.coerceAtLeast(index + 1)
    val copy = ArrayList<T>(maxStep)
    for (i in 0..<maxStep) {
        copy.add(
            if (i == index) {
                element
            } else if (i < size) {
                get(i)
            } else {
                fillWith
            }
        )
    }
    return copy
}


/**
 * Copies the list and adds an element at a specified index.
 * If the index is negative, the element is added to the end of the list.
 */
internal fun <T> List<T>.add(atIndex: Int, newElement: T): List<T> {
    val copy = ArrayList(this)
    if (atIndex < 0) {
        copy.add(newElement)
    } else {
        copy.add(atIndex, newElement)
    }
    return copy
}
