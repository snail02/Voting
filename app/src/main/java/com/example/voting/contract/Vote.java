package com.example.voting.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
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
 * <p>Generated with web3j version 4.5.16.
 */
@SuppressWarnings("rawtypes")
public class Vote extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162000af338038062000af3833981016040819052620000349162000248565b600380546001600160a01b0319163317908190556001600160a01b03166000908152600260209081526040822060019055845162000076929186019062000114565b5081516200008c90600190602085019062000114565b5060005b81518110156200010a5760046040518060400160405280848481518110620000b457fe5b6020908102919091018101518252600091810182905283546001810185559382529081902082518051939460020290910192620000f5928492019062000114565b50602091909101516001918201550162000090565b5050505062000360565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200015757805160ff191683800117855562000187565b8280016001018555821562000187579182015b82811115620001875782518255916020019190600101906200016a565b506200019592915062000199565b5090565b620001b691905b80821115620001955760008155600101620001a0565b90565b600082601f830112620001ca578081fd5b81516001600160401b03811115620001e0578182fd5b6020620001f6601f8301601f1916820162000339565b925081835284818386010111156200020d57600080fd5b60005b828110156200022d57848101820151848201830152810162000210565b828111156200023f5760008284860101525b50505092915050565b6000806000606084860312156200025d578283fd5b83516001600160401b038082111562000274578485fd5b6200028287838801620001b9565b945060209150818601518181111562000299578485fd5b620002a788828901620001b9565b945050604086015181811115620002bc578384fd5b80870188601f820112620002ce578485fd5b8051915082821115620002df578485fd5b620002ee848584020162000339565b8281528481019350818501865b848110156200032857620003158c888451870101620001b9565b86529486019490860190600101620002fb565b505080955050505050509250925092565b6040518181016001600160401b03811182821017156200035857600080fd5b604052919050565b61078380620003706000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c80636e6aefd7116100665780636e6aefd71461010157806389c47775146101165780638cb647d31461011e5780639e7b8d6114610126578063e2ba53f01461013957610093565b80630121b93f1461009857806317599cc5146100ad5780635495d2aa146100d7578063609ff1bd146100ec575b600080fd5b6100ab6100a63660046105c3565b610141565b005b6100c06100bb3660046105c3565b6101e1565b6040516100ce92919061064d565b60405180910390f35b6100df610294565b6040516100ce9190610626565b6100f46102a3565b6040516100ce9190610744565b61010961030a565b6040516100ce919061063a565b61010961039f565b610109610400565b6100ab610134366004610595565b6104d9565b61010961057f565b33600090815260026020526040902080546101775760405162461bcd60e51b815260040161016e9061066f565b60405180910390fd5b600181015460ff161561019c5760405162461bcd60e51b815260040161016e9061069d565b6001818101805460ff1916909117905560028101829055805460048054849081106101c357fe5b60009182526020909120600160029092020101805490910190555050565b600481815481106101ee57fe5b60009182526020918290206002918202018054604080516001831615610100026000190190921693909304601f8101859004850282018501909352828152909350918391908301828280156102845780601f1061025957610100808354040283529160200191610284565b820191906000526020600020905b81548152906001019060200180831161026757829003601f168201915b5050505050908060010154905082565b6003546001600160a01b031681565b600080805b6004548110156103055781600482815481106102c057fe5b90600052602060002090600202016001015411156102fd57600481815481106102e557fe5b90600052602060002090600202016001015491508092505b6001016102a8565b505090565b60018054604080516020601f600260001961010087891615020190951694909404938401819004810282018101909252828152606093909290918301828280156103955780601f1061036a57610100808354040283529160200191610395565b820191906000526020600020905b81548152906001019060200180831161037857829003601f168201915b5050505050905090565b60008054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156103955780601f1061036a57610100808354040283529160200191610395565b3360009081526002602052604090206001015460609060ff1661044b5750604080518082019091526011815270165bdd48185c99481b9bdd081d9bdd1959607a1b60208201526104d6565b336000908152600260208190526040909120015460048054909190811061046e57fe5b60009182526020918290206002918202018054604080516001831615610100026000190190921693909304601f8101859004850282018501909352828152929091908301828280156103955780601f1061036a57610100808354040283529160200191610395565b90565b6003546001600160a01b031633146105035760405162461bcd60e51b815260040161016e906106c5565b6001600160a01b03811660009081526002602052604090206001015460ff161561053f5760405162461bcd60e51b815260040161016e9061070d565b6001600160a01b0381166000908152600260205260409020541561056257600080fd5b6001600160a01b0316600090815260026020526040902060019055565b6060600461058b6102a3565b8154811061046e57fe5b6000602082840312156105a6578081fd5b81356001600160a01b03811681146105bc578182fd5b9392505050565b6000602082840312156105d4578081fd5b5035919050565b60008151808452815b81811015610600576020818501810151868301820152016105e4565b818111156106115782602083870101525b50601f01601f19169290920160200192915050565b6001600160a01b0391909116815260200190565b6000602082526105bc60208301846105db565b60006040825261066060408301856105db565b90508260208301529392505050565b602080825260149082015273486173206e6f20726967687420746f20766f746560601b604082015260600190565b6020808252600e908201526d20b63932b0b23c903b37ba32b21760911b604082015260600190565b60208082526028908201527f4f6e6c79206368616972706572736f6e2063616e2067697665207269676874206040820152673a37903b37ba329760c11b606082015260800190565b60208082526018908201527f54686520766f74657220616c726561647920766f7465642e0000000000000000604082015260600190565b9081526020019056fea264697066735822122001929745d6d5d54b3f457114f028e11fb228ceb8c56c40c4dbd9daad533997e464736f6c63430006060033";

    public static final String FUNC_ANSWERS = "answers";

    public static final String FUNC_GETMYVOTE = "getMyVote";

    public static final String FUNC_GETQUESTIONDESCRIPTION = "getQuestionDescription";

    public static final String FUNC_GETQUESTIONNAME = "getQuestionName";

    public static final String FUNC_GIVERIGHTTOVOTE = "giveRightToVote";

    public static final String FUNC_SECRETARY = "secretary";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_WINNERNAME = "winnerName";

    public static final String FUNC_WINNINGPROPOSAL = "winningProposal";

    @Deprecated
    protected Vote(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Vote(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Vote(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Vote(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Tuple2<String, BigInteger>> answers(BigInteger param0) {
        final Function function = new Function(FUNC_ANSWERS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<String, BigInteger>>(function,
                new Callable<Tuple2<String, BigInteger>>() {
                    @Override
                    public Tuple2<String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, BigInteger>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> getMyVote() {
        final Function function = new Function(FUNC_GETMYVOTE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getQuestionDescription() {
        final Function function = new Function(FUNC_GETQUESTIONDESCRIPTION,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getQuestionName() {
        final Function function = new Function(FUNC_GETQUESTIONNAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> giveRightToVote(String voter) {
        final Function function = new Function(
                FUNC_GIVERIGHTTOVOTE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, voter)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> secretary() {
        final Function function = new Function(FUNC_SECRETARY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger proposal) {
        final Function function = new Function(
                FUNC_VOTE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(proposal)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> winnerName() {
        final Function function = new Function(FUNC_WINNERNAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> winningProposal() {
        final Function function = new Function(FUNC_WINNINGPROPOSAL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Vote load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Vote(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Vote load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Vote(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Vote load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Vote(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Vote load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Vote(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Vote> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String nameQ, String descQ, List<String> answerOption) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(nameQ),
                new org.web3j.abi.datatypes.Utf8String(descQ),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(answerOption, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Vote.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Vote> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String nameQ, String descQ, List<String> answerOption) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(nameQ),
                new org.web3j.abi.datatypes.Utf8String(descQ),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(answerOption, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Vote.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Vote> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String nameQ, String descQ, List<String> answerOption) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(nameQ),
                new org.web3j.abi.datatypes.Utf8String(descQ),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(answerOption, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Vote.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Vote> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String nameQ, String descQ, List<String> answerOption) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(nameQ),
                new org.web3j.abi.datatypes.Utf8String(descQ),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(answerOption, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Vote.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
