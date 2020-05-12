package com.example.voting;

import com.example.voting.contract.Vote;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.ContractGasProvider;

public class VotingCard {
    public String name;
    public String descripton;
    public String address;
    public boolean statusActive;
    public boolean statusVoted;

    VotingCard(){
        name = "Вопрос";
        descripton = "Описание";
        address = "адрес";
        statusActive = true;
        statusVoted = true;
    }

    VotingCard(String name, String descripton, String address){
        this.name = name;
        this.descripton = descripton;
        this.address = address;
        statusActive = true;
        statusVoted = true;
    }

    VotingCard(Vote vote) throws Exception {
        createCard(vote);
    }

    private void createCard(Vote vote) throws Exception {
        name = vote.getVoteName().send();
        descripton = vote.getVoteDescription().send();
        statusActive = true;
        statusVoted = true;
    }

    public String getName(){ return name; }
    public String getDescription(){ return descripton; }
    public String getAddress(){ return address; }

    public String getStatusActive(){
        if(statusActive)
            return "Голосование открыто;";
        else
            return "Голосование закрыто";
    }
    public String getStatusVoted(){
        if(statusVoted)
            return "Вы голосовали";
        else
            return "Вы не голосовали";
    }

}
