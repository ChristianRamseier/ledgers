package org.ledgers.domain.scenario

import org.ledgers.domain.architecture.Ledger
import org.ledgers.domain.architecture.Organization

data class Account(val ledger: Ledger, val owner: Organization, val operator: Ledger?) {

}
