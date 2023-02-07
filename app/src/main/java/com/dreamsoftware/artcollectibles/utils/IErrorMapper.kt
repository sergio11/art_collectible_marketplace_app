package com.dreamsoftware.artcollectibles.utils

interface IErrorMapper {
    fun mapToMessage(ex: Exception): String
}