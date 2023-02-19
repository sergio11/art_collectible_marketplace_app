package com.dreamsoftware.artcollectibles.data.database.room.dao.metadata

import androidx.room.Dao
import com.dreamsoftware.artcollectibles.data.database.room.dao.core.SupportDAO
import com.dreamsoftware.artcollectibles.data.database.room.entity.TokenMetadataEntity

@Dao
abstract class TokenMetadataDAO: SupportDAO<TokenMetadataEntity>(), ITokenMetadataDAO