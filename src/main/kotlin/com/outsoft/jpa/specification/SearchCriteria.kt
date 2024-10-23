package com.outsoft.jpa.specification

open class SearchCriteria(
    val key: String,
    val operation: Operation,
    val value: String? = null,
    val type: Type = Type.AND
) {
    enum class Operation {
        EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, NOT_LIKE, IN, NOT_IN;
    }

    enum class Type {
        AND, SECURITY, OR
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SearchCriteria) return false

        if (key != other.key) return false
        if (operation != other.operation) return false
        if (value != other.value) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + operation.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        return result
    }

    override fun toString(): String {
        return "SearchCriteria(key='$key', operation=$operation, value=$value, type=$type)"
    }

    fun copy(
        key: String = this.key,
        operation: Operation = this.operation,
        value: String? = this.value,
        type: Type = this.type
    ): SearchCriteria = SearchCriteria(key = key, operation = operation, value = value, type = type)
}
