package org.ledgers.domain

data class Version(val version: Int) : Comparable<Version> {

    fun next(): Version {
        return Version(version + 1)
    }

    companion object {
        val Zero = Version(0)
    }

    override fun compareTo(other: Version): Int {
        return version.compareTo(other.version)
    }
}
