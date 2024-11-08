package com.outsoft.jss

import javax.persistence.criteria.Root

class CriteriaBadRequestException(
    override val message: String? = null,
    ex: Throwable? = null
) : RuntimeException(message, ex) {

    constructor(criteria: com.outsoft.jss.SearchCriteria, root: Root<*>, ex: Throwable? = null) : this(
        com.outsoft.jss.CriteriaBadRequestException.Companion.createErrorMessage(criteria, root), ex
    )

    private companion object {
        private fun createErrorMessage(criteria: com.outsoft.jss.SearchCriteria, root: Root<*>): String =
            "Incorrect search request for ${root.type().javaType}: {key=${criteria.key}, " +
                "operation=${criteria.operation}, value=${criteria.value}}"
    }
}