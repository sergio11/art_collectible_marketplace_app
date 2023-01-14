package com.dreamsoftware.artcollectibles.data.blockchain.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
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
public class IFaucetContract extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_DEPOSIT = "deposit";

    public static final String FUNC_GETAMOUNT = "getAmount";

    public static final String FUNC_GETINITIALAMOUNT = "getInitialAmount";

    public static final String FUNC_REQUESTSEEDFUNDS = "requestSeedFunds";

    public static final String FUNC_SENDFUNDS = "sendFunds";

    public static final String FUNC_SETINITIALAMOUNT = "setInitialAmount";

    public static final Event ONDEPOSIT_EVENT = new Event("OnDeposit", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ONREQUESTSEEDFUNDS_EVENT = new Event("OnRequestSeedFunds", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ONSENDFUNDS_EVENT = new Event("OnSendFunds", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected IFaucetContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected IFaucetContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected IFaucetContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected IFaucetContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<OnDepositEventResponse> getOnDepositEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ONDEPOSIT_EVENT, transactionReceipt);
        ArrayList<OnDepositEventResponse> responses = new ArrayList<OnDepositEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OnDepositEventResponse typedResponse = new OnDepositEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OnDepositEventResponse> onDepositEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OnDepositEventResponse>() {
            @Override
            public OnDepositEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ONDEPOSIT_EVENT, log);
                OnDepositEventResponse typedResponse = new OnDepositEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OnDepositEventResponse> onDepositEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ONDEPOSIT_EVENT));
        return onDepositEventFlowable(filter);
    }

    public static List<OnRequestSeedFundsEventResponse> getOnRequestSeedFundsEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ONREQUESTSEEDFUNDS_EVENT, transactionReceipt);
        ArrayList<OnRequestSeedFundsEventResponse> responses = new ArrayList<OnRequestSeedFundsEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OnRequestSeedFundsEventResponse typedResponse = new OnRequestSeedFundsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OnRequestSeedFundsEventResponse> onRequestSeedFundsEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OnRequestSeedFundsEventResponse>() {
            @Override
            public OnRequestSeedFundsEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ONREQUESTSEEDFUNDS_EVENT, log);
                OnRequestSeedFundsEventResponse typedResponse = new OnRequestSeedFundsEventResponse();
                typedResponse.log = log;
                typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OnRequestSeedFundsEventResponse> onRequestSeedFundsEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ONREQUESTSEEDFUNDS_EVENT));
        return onRequestSeedFundsEventFlowable(filter);
    }

    public static List<OnSendFundsEventResponse> getOnSendFundsEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ONSENDFUNDS_EVENT, transactionReceipt);
        ArrayList<OnSendFundsEventResponse> responses = new ArrayList<OnSendFundsEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OnSendFundsEventResponse typedResponse = new OnSendFundsEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OnSendFundsEventResponse> onSendFundsEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OnSendFundsEventResponse>() {
            @Override
            public OnSendFundsEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ONSENDFUNDS_EVENT, log);
                OnSendFundsEventResponse typedResponse = new OnSendFundsEventResponse();
                typedResponse.log = log;
                typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OnSendFundsEventResponse> onSendFundsEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ONSENDFUNDS_EVENT));
        return onSendFundsEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> deposit(BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DEPOSIT, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<BigInteger> getAmount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETAMOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getInitialAmount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETINITIALAMOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> requestSeedFunds() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REQUESTSEEDFUNDS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> sendFunds(String account, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SENDFUNDS, 
                Arrays.<Type>asList(new Address(160, account),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setInitialAmount(BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETINITIALAMOUNT, 
                Arrays.<Type>asList(new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static IFaucetContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new IFaucetContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static IFaucetContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new IFaucetContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static IFaucetContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new IFaucetContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static IFaucetContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IFaucetContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class OnDepositEventResponse extends BaseEventResponse {
        public String sender;

        public BigInteger amount;
    }

    public static class OnRequestSeedFundsEventResponse extends BaseEventResponse {
        public String account;

        public BigInteger amount;
    }

    public static class OnSendFundsEventResponse extends BaseEventResponse {
        public String account;

        public BigInteger amount;
    }
}
