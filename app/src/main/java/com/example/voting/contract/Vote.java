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
    public static final String BINARY = "60806040523480156200001157600080fd5b5060405162000e3338038062000e33833981016040819052620000349162000252565b600580546001600160a01b0319163317908190556001600160a01b0316600090815260046020908152604082206001905584516200007692918601906200011e565b5081516200008c9060019060208501906200011e565b506001600255600060038190555b8151811015620001145760066040518060400160405280848481518110620000be57fe5b6020908102919091018101518252600091810182905283546001810185559382529081902082518051939460020290910192620000ff92849201906200011e565b5060209190910151600191820155016200009a565b505050506200036a565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200016157805160ff191683800117855562000191565b8280016001018555821562000191579182015b828111156200019157825182559160200191906001019062000174565b506200019f929150620001a3565b5090565b620001c091905b808211156200019f5760008155600101620001aa565b90565b600082601f830112620001d4578081fd5b81516001600160401b03811115620001ea578182fd5b602062000200601f8301601f1916820162000343565b925081835284818386010111156200021757600080fd5b60005b82811015620002375784810182015184820183015281016200021a565b82811115620002495760008284860101525b50505092915050565b60008060006060848603121562000267578283fd5b83516001600160401b03808211156200027e578485fd5b6200028c87838801620001c3565b9450602091508186015181811115620002a3578485fd5b620002b188828901620001c3565b945050604086015181811115620002c6578384fd5b80870188601f820112620002d8578485fd5b8051915082821115620002e9578485fd5b620002f8848584020162000343565b8281528481019350818501865b8481101562000332576200031f8c888451870101620001c3565b8652948601949086019060010162000305565b505080955050505050509250925092565b6040518181016001600160401b03811182821017156200036257600080fd5b604052919050565b610ab9806200037a6000396000f3fe608060405234801561001057600080fd5b50600436106100cf5760003560e01c8063858168291161008c578063a948372811610066578063a948372814610188578063be0cda3c14610190578063e9bd438a14610198578063f60c7ffe146101a0576100cf565b8063858168291461015857806387a350021461016b5780638cb647d314610173576100cf565b80630121b93f146100d45780630494c395146100e957806317599cc51461010757806330257288146101285780635495d2aa1461013b578063793cf21814610150575b600080fd5b6100e76100e23660046108e6565b6101a8565b005b6100f1610253565b6040516100fe9190610a33565b60405180910390f35b61011a6101153660046108e6565b610259565b6040516100fe929190610977565b6100f16101363660046108e6565b61030c565b610143610334565b6040516100fe9190610949565b6100f1610343565b6100e7610166366004610846565b610349565b6100f1610414565b61017b6104ef565b6040516100fe919061095d565b61017b6105f9565b61017b610677565b61017b610768565b61017b6107c9565b33600090815260046020526040902080546101de5760405162461bcd60e51b81526004016101d590610999565b60405180910390fd5b600181015460ff16156102035760405162461bcd60e51b81526004016101d5906109c7565b6001818101805460ff19169091179055600281018290558054600680548490811061022a57fe5b600091825260209091206001600290920201810180549092019091556003805490910190555050565b60035490565b6006818154811061026657fe5b60009182526020918290206002918202018054604080516001831615610100026000190190921693909304601f8101859004850282018501909352828152909350918391908301828280156102fc5780601f106102d1576101008083540402835291602001916102fc565b820191906000526020600020905b8154815290600101906020018083116102df57829003601f168201915b5050505050908060010154905082565b60006006828154811061031b57fe5b9060005260206000209060020201600101549050919050565b6005546001600160a01b031681565b60025490565b6005546001600160a01b031633146103735760405162461bcd60e51b81526004016101d5906109ee565b60005b8151811015610410576004600083838151811061038f57fe5b60200260200101516001600160a01b03166001600160a01b0316815260200190815260200160002060000154600014156104085760028054600190810190915582516004906000908590859081106103e357fe5b6020908102919091018101516001600160a01b03168252810191909152604001600020555b600101610376565b5050565b600080805b60065481101561047657816006828154811061043157fe5b906000526020600020906002020160010154111561046e576006818154811061045657fe5b90600052602060002090600202016001015491508092505b600101610419565b5060005b6006548110156104e9576006838154811061049157fe5b906000526020600020906002020160010154600682815481106104b057fe5b9060005260206000209060020201600101541480156104cf5750828114155b156104e1575061270f91506104ec9050565b60010161047a565b50505b90565b3360009081526004602052604090206001015460609060ff1661053a5750604080518082019091526011815270165bdd48185c99481b9bdd081d9bdd1959607a1b60208201526104ec565b3360009081526004602052604090206002015460068054909190811061055c57fe5b60009182526020918290206002918202018054604080516001831615610100026000190190921693909304601f8101859004850282018501909352828152929091908301828280156105ef5780601f106105c4576101008083540402835291602001916105ef565b820191906000526020600020905b8154815290600101906020018083116105d257829003601f168201915b5050505050905090565b3360009081526004602052604090208054606091906001146106495760405180604001604052806014815260200173486173206e6f20726967687420746f20766f746560601b8152509150610673565b6040518060400160405280600e81526020016d165bdd481a185d99481c9a59da1d60921b81525091505b5090565b60606000610683610414565b90508061270f14156106bd57505060408051808201909152601081526f15da5b9b995c881b9bdd08199bdd5b9960821b60208201526104ec565b600681815481106106ca57fe5b60009182526020918290206002918202018054604080516001831615610100026000190190921693909304601f81018590048502820185019093528281529290919083018282801561075d5780601f106107325761010080835404028352916020019161075d565b820191906000526020600020905b81548152906001019060200180831161074057829003601f168201915b505050505091505090565b60008054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156105ef5780601f106105c4576101008083540402835291602001916105ef565b60018054604080516020601f600260001961010087891615020190951694909404938401819004810282018101909252828152606093909290918301828280156105ef5780601f106105c4576101008083540402835291602001916105ef565b80356001600160a01b038116811461084057600080fd5b92915050565b60006020808385031215610858578182fd5b823567ffffffffffffffff81111561086e578283fd5b80840185601f82011261087f578384fd5b8035915061089461088f83610a63565b610a3c565b82815283810190828501858502840186018910156108b0578687fd5b8693505b848410156108da576108c68982610829565b8352600193909301929185019185016108b4565b50979650505050505050565b6000602082840312156108f7578081fd5b5035919050565b60008151808452815b8181101561092357602081850181015186830182015201610907565b818111156109345782602083870101525b50601f01601f19169290920160200192915050565b6001600160a01b0391909116815260200190565b60006020825261097060208301846108fe565b9392505050565b60006040825261098a60408301856108fe565b90508260208301529392505050565b602080825260149082015273486173206e6f20726967687420746f20766f746560601b604082015260600190565b6020808252600d908201526c105b1c9958591e481d9bdd1959609a1b604082015260600190565b60208082526025908201527f4f6e6c79207365637265746172792063616e206769766520726967687420746f60408201526420766f746560d81b606082015260800190565b90815260200190565b60405181810167ffffffffffffffff81118282101715610a5b57600080fd5b604052919050565b600067ffffffffffffffff821115610a79578081fd5b506020908102019056fea2646970667358221220e702c02295d00d71c31636be1b7aef4ae06c576ad37c936400ce95f44273562e64736f6c63430006060033";

    public static final String FUNC_ANSWERS = "answers";

    public static final String FUNC_CHECKRIGHT = "checkRight";

    public static final String FUNC_GETCOUNTVOTEVARIANT = "getCountVoteVariant";

    public static final String FUNC_GETCURRENTVOTERS = "getCurrentVoters";

    public static final String FUNC_GETMYVOTE = "getMyVote";

    public static final String FUNC_GETTOTALVOTERS = "getTotalVoters";

    public static final String FUNC_GETVOTEDESCRIPTION = "getVoteDescription";

    public static final String FUNC_GETVOTENAME = "getVoteName";

    public static final String FUNC_GIVERIGHTTOVOTE = "giveRightToVote";

    public static final String FUNC_SECRETARY = "secretary";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_WINNERNAMEVARIANT = "winnerNameVariant";

    public static final String FUNC_WINNINGVARIANT = "winningVariant";

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

    public RemoteFunctionCall<String> checkRight() {
        final Function function = new Function(FUNC_CHECKRIGHT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getCountVoteVariant(BigInteger variant) {
        final Function function = new Function(FUNC_GETCOUNTVOTEVARIANT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(variant)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getCurrentVoters() {
        final Function function = new Function(FUNC_GETCURRENTVOTERS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getMyVote() {
        final Function function = new Function(FUNC_GETMYVOTE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getTotalVoters() {
        final Function function = new Function(FUNC_GETTOTALVOTERS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getVoteDescription() {
        final Function function = new Function(FUNC_GETVOTEDESCRIPTION,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getVoteName() {
        final Function function = new Function(FUNC_GETVOTENAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> giveRightToVote(List<String> voter) {
        final Function function = new Function(
                FUNC_GIVERIGHTTOVOTE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(voter, org.web3j.abi.datatypes.Address.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> secretary() {
        final Function function = new Function(FUNC_SECRETARY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger variant) {
        final Function function = new Function(
                FUNC_VOTE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(variant)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> winnerNameVariant() {
        final Function function = new Function(FUNC_WINNERNAMEVARIANT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> winningVariant() {
        final Function function = new Function(FUNC_WINNINGVARIANT,
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

    public static RemoteCall<Vote> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String nameV, String descV, List<String> answerVariant) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(nameV),
                new org.web3j.abi.datatypes.Utf8String(descV),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(answerVariant, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Vote.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Vote> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String nameV, String descV, List<String> answerVariant) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(nameV),
                new org.web3j.abi.datatypes.Utf8String(descV),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(answerVariant, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Vote.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Vote> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String nameV, String descV, List<String> answerVariant) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(nameV),
                new org.web3j.abi.datatypes.Utf8String(descV),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(answerVariant, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Vote.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Vote> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String nameV, String descV, List<String> answerVariant) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(nameV),
                new org.web3j.abi.datatypes.Utf8String(descV),
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(answerVariant, org.web3j.abi.datatypes.Utf8String.class))));
        return deployRemoteCall(Vote.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
