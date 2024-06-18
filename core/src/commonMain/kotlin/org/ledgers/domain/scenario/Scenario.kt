package org.ledgers.domain.scenario

data class Scenario(
    val name: String,
    val steps: Steps
) {

    fun getPositionsAtStep(atStep: Int): Positions {
        var positions = Positions.Empty
        0.rangeTo(atStep).forEach { step ->
            positions = positions.withActionsApplied(steps.atStep(step).actions)
        }
        return positions
    }

    fun withName(name: String): Scenario {
        return copy(
            name = name
        )
    }

    companion object {
        val Empty = Scenario("", Steps.None)
    }

}
