package org.ledgers.dto

import kotlinx.serialization.Serializable
import org.ledgers.domain.architecture.LedgerId
import org.ledgers.domain.scenario.AccountReference
import org.ledgers.dto.serializers.AccountReferenceSerializer
import org.ledgers.dto.serializers.LedgerIdSerializer

@Serializable
data class LedgerAndAccountReferenceDto(
    @Serializable(with = LedgerIdSerializer::class)
    val ledgerId: LedgerId,
    @Serializable(with = AccountReferenceSerializer::class)
    val accountReference: AccountReference
)
