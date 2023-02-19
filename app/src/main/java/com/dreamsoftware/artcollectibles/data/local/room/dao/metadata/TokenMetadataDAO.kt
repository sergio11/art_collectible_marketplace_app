package com.dreamsoftware.artcollectibles.data.local.room.dao.metadata

import androidx.room.Dao
import com.dreamsoftware.artcollectibles.data.local.room.dao.core.SupportDAO
import com.dreamsoftware.artcollectibles.data.local.room.entity.TokenMetadataEntity

@Dao
abstract class TokenMetadataDAO: SupportDAO<TokenMetadataEntity>(), ITokenMetadataDAO