package org.ledgers.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.architecture.OrganizationId
import org.ledgers.domain.scenario.AccountReference
import org.ledgers.domain.scenario.action.ActionId
import org.ledgers.domain.scenario.action.ActionType
import org.ledgers.dto.serializers.AccountReferenceSerializer
import org.ledgers.dto.serializers.ActionIdSerializer
import org.ledgers.dto.serializers.LedgerIdSerializer
import org.ledgers.dto.serializers.OrganizationIdSerializer

@Serializable
@SerialName("CreateAccount")
class CreateAccountActionDto(
    @Serializable(with = ActionIdSerializer::class)
    override val id: ActionId,
    @Serializable(with = LedgerIdSerializer::class)
    val ledgerId: LedgerId,
    @Serializable(with = AccountReferenceSerializer::class)
    val accountReference: AccountReference,
    @Serializable(with = OrganizationIdSerializer::class)
    val owner: OrganizationId
) : ActionDto {

    override val type: ActionType get() = ActionType.CreateAccount

}
