package com.dreamsoftware.artcollectibles.data.pinata.datasource.impl

import com.dreamsoftware.artcollectibles.data.pinata.datasource.IPinataDataSource
import com.dreamsoftware.artcollectibles.data.pinata.datasource.core.SupportNetworkDataSource
import com.dreamsoftware.artcollectibles.data.pinata.service.IPinataPinningService
import com.dreamsoftware.artcollectibles.data.pinata.service.IPinataQueryFilesService

class PinataDataSourceImpl(
    private val pinataPinningService: IPinataPinningService,
    private val pinataQueryFilesService: IPinataQueryFilesService
): SupportNetworkDataSource(), IPinataDataSource {

}