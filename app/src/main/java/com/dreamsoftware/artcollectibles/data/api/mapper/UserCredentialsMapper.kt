package com.dreamsoftware.artcollectibles.data.api.mapper

import com.dreamsoftware.artcollectibles.domain.models.UserWalletCredentials
import com.dreamsoftware.artcollectibles.utils.IMapper
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair

class UserCredentialsMapper: IMapper<Credentials, UserWalletCredentials> {

    override fun mapInToOut(input: Credentials): UserWalletCredentials = with(input) {
        UserWalletCredentials(
            address = address,
            privateKey = ecKeyPair.privateKey,
            publicKey = ecKeyPair.publicKey
        )
    }

    override fun mapInListToOutList(input: Iterable<Credentials>): Iterable<UserWalletCredentials> =
        input.map(::mapInToOut)

    override fun mapOutToIn(input: UserWalletCredentials): Credentials = with(input) {
        Credentials.create(ECKeyPair(privateKey, publicKey))
    }

    override fun mapOutListToInList(input: Iterable<UserWalletCredentials>): Iterable<Credentials> =
        input.map(::mapOutToIn)
}