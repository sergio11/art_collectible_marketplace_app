package com.dreamsoftware.artcollectibles.utils

interface ISimpleOneSideMapper<IN, OUT> {
    fun mapInToOut(input: IN): OUT
}