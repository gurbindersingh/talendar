package at.ac.tuwien.sepm.groupphase.backend.service.algorithmClasses;

import at.ac.tuwien.sepm.groupphase.backend.Entity.AlgoCustomer;

import java.util.LinkedList;
import java.util.List;

public class DoubleCluster {

    private List<AlgoCustomer> members;
    private double mean;
    private int ranking;
    private double representative;

    public DoubleCluster(){
        this.members = new LinkedList<>();
    }
    public DoubleCluster(
        List<AlgoCustomer> members,
        double mean,
        int ranking,
        double representative
    ) {
        this.members = members;
        this.mean = mean;
        this.ranking = ranking;
        this.representative = representative;
    }


    public double getRepresentative() {
        return representative;
    }


    public void setRepresentative(double representative) {
        this.representative = representative;
        this.mean = this.representative;
    }


    public List<AlgoCustomer> getMembers() {
        return members;
    }


    public void setMembers(List<AlgoCustomer> members) {
        this.members = members;
    }


    public double getMean() {
        return mean;
    }


    public void setMean(double mean) {
        this.mean = mean;
    }


    public int getRanking() {
        return ranking;
    }


    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public void addMember(AlgoCustomer algoCustomer){
        this.members.add(algoCustomer);
    }



}
