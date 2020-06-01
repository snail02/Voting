package com.example.voting;

import com.example.voting.contract.Vote;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.ContractGasProvider;

import java.io.Serializable;

public class VotingCard implements Serializable {
    public String name;
    public String descripton;
    public String address;
    public String id;
    public boolean statusActive;

    VotingCard(){
        name = "Вопрос";
        descripton = "Описание";
        address = "адрес";
        statusActive = true;
    }

    VotingCard(String name, String descripton, String address, boolean statusActive, String id){
        this.name = name;
        this.descripton = descripton;
        this.address = address;
        this.statusActive = statusActive;
        this.id = id;
    }

    VotingCard(Vote vote) throws Exception {
        createCard(vote);
    }

    private void createCard(Vote vote) throws Exception {
        name = vote.getVoteName().send();
        descripton = vote.getVoteDescription().send();
        statusActive = true;
    }

    public String getName(){ return name; }
    public String getDescription(){ return descripton; }
    public String getAddress(){ return address; }
    public String getId(){
        return id;
    }

    public void setStatusActive(boolean statusActive) {
        this.statusActive = statusActive;
    }

    public String getStatusActive(){
        if(statusActive)
            return "Голосование открыто;";
        else
            return "Голосование закрыто";
    }

    public boolean isStatusActive() {
        return statusActive;
    }
}
