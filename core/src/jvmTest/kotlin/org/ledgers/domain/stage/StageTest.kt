package org.ledgers.domain.stage

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ledgers.domain.Version
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.OrganizationId
import org.ledgers.domain.component.ComponentReference
import org.ledgers.domain.component.ComponentType

class StageTest {

    @Test
    fun givenTwoStages_whenAComponentWasAdded_theAdditionIsReturned() {
        val oldStage = Stage()
        val organizationId = LedgerId.random()
        val addedComponent = LedgerOnStage(
            box = Box(10, 10, 100, 100),
            reference = ComponentReference(ComponentType.Ledger, organizationId, Version.Zero)
        )
        val newStage = Stage(components = listOf(addedComponent))
        val changes = oldStage.getChangesThatLeadTo(newStage)
        assertThat(changes).containsExactly(Add(addedComponent))
    }

    @Test
    fun givenTwoStages_whenAComponentWasChanged_theChangeIsReturned() {
        val organizationId = LedgerId.random()
        val oldStage = Stage(
            listOf(
                LedgerOnStage(
                    box = Box(10, 10, 100, 100),
                    reference = ComponentReference(ComponentType.Ledger, organizationId, Version(1))
                )
            )
        )
        val component = LedgerOnStage(
            box = Box(10, 10, 100, 100),
            reference = ComponentReference(ComponentType.Ledger, organizationId, Version.Zero)
        )
        val newStage = Stage(components = listOf(component))
        val changes = oldStage.getChangesThatLeadTo(newStage)
        assertThat(changes).containsExactly(Change(component))
    }

    @Test
    fun givenTwoStages_whenNoComponentWasRemoved_aRemovalIsReturned() {
        val organizationId = OrganizationId.random()
        val component = LedgerOnStage(
            box = Box(10, 10, 100, 100),
            reference = ComponentReference(ComponentType.Ledger, organizationId, Version.Zero)
        )
        val oldStage = Stage(components = listOf(component))
        val newStage = Stage(components = listOf())
        val changes = oldStage.getChangesThatLeadTo(newStage)
        assertThat(changes).containsExactly(Remove(component.reference))
    }

    @Test
    fun givenTwoStages_whenNoComponentWasChanged_noChangesAreReturned() {
        val organizationId = OrganizationId.random()
        val component = LedgerOnStage(
            box = Box(10, 10, 100, 100),
            reference = ComponentReference(ComponentType.Ledger, organizationId, Version.Zero)
        )
        val oldStage = Stage(components = listOf(component))
        val newStage = Stage(components = listOf(component))
        val changes = oldStage.getChangesThatLeadTo(newStage)
        assertThat(changes).isEmpty()
    }

}
