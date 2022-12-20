package com.dreamsoftware.artcollectibles.data.blockchain.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.2.
 */
@SuppressWarnings("rawtypes")
public class ArtMarketplaceContract extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_DEFAULT_COST_OF_PUTTING_FOR_SALE = "DEFAULT_COST_OF_PUTTING_FOR_SALE";

    public static final String FUNC_BUYITEM = "buyItem";

    public static final String FUNC_COSTOFPUTTINGFORSALE = "costOfPuttingForSale";

    public static final String FUNC_FETCHAVAILABLEMARKETITEMS = "fetchAvailableMarketItems";

    public static final String FUNC_FETCHMARKETHISTORY = "fetchMarketHistory";

    public static final String FUNC_FETCHOWNEDMARKETITEMS = "fetchOwnedMarketItems";

    public static final String FUNC_FETCHSELLINGMARKETITEMS = "fetchSellingMarketItems";

    public static final String FUNC_GETARTCOLLECTIBLEADDRESS = "getArtCollectibleAddress";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_PUTITEMFORSALE = "putItemForSale";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SETARTCOLLECTIBLEADDRESS = "setArtCollectibleAddress";

    public static final String FUNC_SETCOSTOFPUTTINGFORSALE = "setCostOfPuttingForSale";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_WITHDRAWFROMSALE = "withdrawFromSale";

    public static final Event ARTCOLLECTIBLEADDEDFORSALE_EVENT = new Event("ArtCollectibleAddedForSale", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ARTCOLLECTIBLESOLD_EVENT = new Event("ArtCollectibleSold", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ARTCOLLECTIBLEWITHDRAWNFROMSALE_EVENT = new Event("ArtCollectibleWithdrawnFromSale", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected ArtMarketplaceContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ArtMarketplaceContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ArtMarketplaceContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ArtMarketplaceContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ArtCollectibleAddedForSaleEventResponse> getArtCollectibleAddedForSaleEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ARTCOLLECTIBLEADDEDFORSALE_EVENT, transactionReceipt);
        ArrayList<ArtCollectibleAddedForSaleEventResponse> responses = new ArrayList<ArtCollectibleAddedForSaleEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ArtCollectibleAddedForSaleEventResponse typedResponse = new ArtCollectibleAddedForSaleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ArtCollectibleAddedForSaleEventResponse> artCollectibleAddedForSaleEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ArtCollectibleAddedForSaleEventResponse>() {
            @Override
            public ArtCollectibleAddedForSaleEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ARTCOLLECTIBLEADDEDFORSALE_EVENT, log);
                ArtCollectibleAddedForSaleEventResponse typedResponse = new ArtCollectibleAddedForSaleEventResponse();
                typedResponse.log = log;
                typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ArtCollectibleAddedForSaleEventResponse> artCollectibleAddedForSaleEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ARTCOLLECTIBLEADDEDFORSALE_EVENT));
        return artCollectibleAddedForSaleEventFlowable(filter);
    }

    public static List<ArtCollectibleSoldEventResponse> getArtCollectibleSoldEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ARTCOLLECTIBLESOLD_EVENT, transactionReceipt);
        ArrayList<ArtCollectibleSoldEventResponse> responses = new ArrayList<ArtCollectibleSoldEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ArtCollectibleSoldEventResponse typedResponse = new ArtCollectibleSoldEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.buyer = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ArtCollectibleSoldEventResponse> artCollectibleSoldEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ArtCollectibleSoldEventResponse>() {
            @Override
            public ArtCollectibleSoldEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ARTCOLLECTIBLESOLD_EVENT, log);
                ArtCollectibleSoldEventResponse typedResponse = new ArtCollectibleSoldEventResponse();
                typedResponse.log = log;
                typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.buyer = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.price = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ArtCollectibleSoldEventResponse> artCollectibleSoldEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ARTCOLLECTIBLESOLD_EVENT));
        return artCollectibleSoldEventFlowable(filter);
    }

    public static List<ArtCollectibleWithdrawnFromSaleEventResponse> getArtCollectibleWithdrawnFromSaleEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ARTCOLLECTIBLEWITHDRAWNFROMSALE_EVENT, transactionReceipt);
        ArrayList<ArtCollectibleWithdrawnFromSaleEventResponse> responses = new ArrayList<ArtCollectibleWithdrawnFromSaleEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ArtCollectibleWithdrawnFromSaleEventResponse typedResponse = new ArtCollectibleWithdrawnFromSaleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ArtCollectibleWithdrawnFromSaleEventResponse> artCollectibleWithdrawnFromSaleEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ArtCollectibleWithdrawnFromSaleEventResponse>() {
            @Override
            public ArtCollectibleWithdrawnFromSaleEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ARTCOLLECTIBLEWITHDRAWNFROMSALE_EVENT, log);
                ArtCollectibleWithdrawnFromSaleEventResponse typedResponse = new ArtCollectibleWithdrawnFromSaleEventResponse();
                typedResponse.log = log;
                typedResponse.tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ArtCollectibleWithdrawnFromSaleEventResponse> artCollectibleWithdrawnFromSaleEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ARTCOLLECTIBLEWITHDRAWNFROMSALE_EVENT));
        return artCollectibleWithdrawnFromSaleEventFlowable(filter);
    }

    public static List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> DEFAULT_COST_OF_PUTTING_FOR_SALE() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEFAULT_COST_OF_PUTTING_FOR_SALE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> buyItem(BigInteger tokenId, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BUYITEM, 
                Arrays.<Type>asList(new Uint256(tokenId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<BigInteger> costOfPuttingForSale() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COSTOFPUTTINGFORSALE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> fetchAvailableMarketItems() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHAVAILABLEMARKETITEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchMarketHistory() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHMARKETHISTORY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchOwnedMarketItems() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHOWNEDMARKETITEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<List> fetchSellingMarketItems() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FETCHSELLINGMARKETITEMS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<ArtCollectibleForSale>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<String> getArtCollectibleAddress() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETARTCOLLECTIBLEADDRESS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> putItemForSale(BigInteger tokenId, BigInteger price, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PUTITEMFORSALE, 
                Arrays.<Type>asList(new Uint256(tokenId),
                new Uint256(price)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setArtCollectibleAddress(String artCollectibleAddress, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETARTCOLLECTIBLEADDRESS, 
                Arrays.<Type>asList(new Address(160, artCollectibleAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> setCostOfPuttingForSale(BigInteger _costOfPuttingForSale) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETCOSTOFPUTTINGFORSALE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(_costOfPuttingForSale)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> withdrawFromSale(BigInteger tokenId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_WITHDRAWFROMSALE, 
                Arrays.<Type>asList(new Uint256(tokenId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ArtMarketplaceContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArtMarketplaceContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ArtMarketplaceContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArtMarketplaceContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ArtMarketplaceContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ArtMarketplaceContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ArtMarketplaceContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ArtMarketplaceContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class ArtCollectibleForSale extends StaticStruct {
        public BigInteger marketItemId;

        public BigInteger tokenId;

        public String creator;

        public String seller;

        public String owner;

        public BigInteger price;

        public Boolean sold;

        public Boolean canceled;

        public ArtCollectibleForSale(BigInteger marketItemId, BigInteger tokenId, String creator, String seller, String owner, BigInteger price, Boolean sold, Boolean canceled) {
            super(new Uint256(marketItemId),
                    new Uint256(tokenId),
                    new Address(160, creator),
                    new Address(160, seller),
                    new Address(160, owner),
                    new Uint256(price),
                    new Bool(sold),
                    new Bool(canceled));
            this.marketItemId = marketItemId;
            this.tokenId = tokenId;
            this.creator = creator;
            this.seller = seller;
            this.owner = owner;
            this.price = price;
            this.sold = sold;
            this.canceled = canceled;
        }

        public ArtCollectibleForSale(Uint256 marketItemId, Uint256 tokenId, Address creator, Address seller, Address owner, Uint256 price, Bool sold, Bool canceled) {
            super(marketItemId, tokenId, creator, seller, owner, price, sold, canceled);
            this.marketItemId = marketItemId.getValue();
            this.tokenId = tokenId.getValue();
            this.creator = creator.getValue();
            this.seller = seller.getValue();
            this.owner = owner.getValue();
            this.price = price.getValue();
            this.sold = sold.getValue();
            this.canceled = canceled.getValue();
        }
    }

    public static class ArtCollectibleAddedForSaleEventResponse extends BaseEventResponse {
        public BigInteger id;

        public BigInteger tokenId;

        public BigInteger price;
    }

    public static class ArtCollectibleSoldEventResponse extends BaseEventResponse {
        public BigInteger tokenId;

        public String buyer;

        public BigInteger price;
    }

    public static class ArtCollectibleWithdrawnFromSaleEventResponse extends BaseEventResponse {
        public BigInteger tokenId;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
