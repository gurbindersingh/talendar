package at.ac.tuwien.sepm.groupphase.backend.service.algorithmClasses;

import at.ac.tuwien.sepm.groupphase.backend.Entity.AlgoCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class KMeans {

    private static Logger LOGGER = LoggerFactory.getLogger(KMeans.class);
    public List<AlgoCustomer> algoCustomers;
    public DoubleCluster[] recencyCluster;
    public DoubleCluster[] frequencyCluster;
    public DoubleCluster[] moneyCluster;
    public DoubleCluster[] overallRanking;



    public KMeans(List<AlgoCustomer> algoCustomers){
        LOGGER.info("KMeans started");
        this.algoCustomers = algoCustomers;
        clusterCustomerKMeans(3, 100);

    }

    public void clusterCustomerKMeans(int k, int iterations){
        LOGGER.info("k = " + k + " with " + iterations + " iterations");
        recencyCluster = new DoubleCluster[k];
        frequencyCluster = new DoubleCluster[k];
        moneyCluster = new DoubleCluster[k];
        double earliest = Double.MIN_VALUE;
        double mostRecent = Double.MAX_VALUE;
        double[] clusterCentersRecency = new double[k];

        double minFrequency = Double.MAX_VALUE;
        double maxFrequency = Double.MIN_VALUE;
        double[] clusterCenterFrequency = new double[k];

        double minMoney = Double.MAX_VALUE;
        double maxMoney = Double.MIN_VALUE;
        double[] clusterCenterMoney = new double[k];


        for(AlgoCustomer ac: algoCustomers
        ) {
            if(ac.getFirstDays() > earliest){
                earliest = ac.getFirstDays();
            }
            if(ac.getRecencyDays() < mostRecent){
                mostRecent = ac.getRecencyDays();
            }
            if(ac.getFrequency() < minFrequency){
                minFrequency = ac.getFrequency();
            }
            if(ac.getFrequency() > maxFrequency){
                maxFrequency = ac.getFrequency();
            }
            if(ac.getMoneySpent() < minMoney){
                minMoney = ac.getMoneySpent();
            }
            if(ac.getMoneySpent() > maxMoney){
                maxMoney = ac.getMoneySpent();
            }
        }

        for(int i = 0; i<k; i++){
            clusterCentersRecency[i] = randomBetween(mostRecent, earliest, 1.25 );
            recencyCluster[i] = new DoubleCluster();
            recencyCluster[i].setRepresentative(clusterCentersRecency[i]);
            clusterCenterMoney[i] = randomBetween(minMoney, maxMoney, 1.25);
            moneyCluster[i] = new DoubleCluster();
            moneyCluster[i].setRepresentative(clusterCenterMoney[i]);
            clusterCenterFrequency[i] = randomBetween(minFrequency, maxFrequency, 1.25);
            frequencyCluster[i] = new DoubleCluster();
            frequencyCluster[i].setRepresentative(clusterCenterFrequency[i]);
        }

        for(int i = 0; i<iterations; i++){
            for(AlgoCustomer ac: algoCustomers
            ) {

                int closestRecency = 0;
                double closestRecencyVal = Double.MAX_VALUE;
                int closestFrequency = 0;
                double closestFrequencyVal = Double.MAX_VALUE;
                int closestMoney = 0;
                double closestMoneyVal = Double.MAX_VALUE;

                for(int index = 0; index < k; index++){
                   if(Math.abs(recencyCluster[index].getMean() - ac.getRecencyDays())< closestRecencyVal){
                       closestRecency = index;
                       closestRecencyVal = Math.abs(recencyCluster[index].getMean()-ac.getRecencyDays());
                   }
                    if(Math.abs(frequencyCluster[index].getMean() - ac.getFrequency())< closestFrequencyVal){
                        closestFrequency = index;
                        closestFrequencyVal = Math.abs(frequencyCluster[index].getMean()-ac.getFrequency());
                    }
                    if(Math.abs(moneyCluster[index].getMean() -ac.getMoneySpent())< closestMoneyVal){
                        closestMoney = index;
                        closestMoneyVal = Math.abs(moneyCluster[index].getMean() - ac.getMoneySpent());
                    }
                }

                recencyCluster[closestRecency].addMember(ac);
                frequencyCluster[closestFrequency].addMember(ac);
                moneyCluster[closestMoney].addMember(ac);
            }
            for(int index = 0; index < k; index++){
                double recencyTotal = 0;
                for(AlgoCustomer ac: recencyCluster[index].getMembers()
                ) {
                    recencyTotal+=ac.getRecencyDays();
                }
                recencyCluster[index].setRepresentative(recencyTotal/recencyCluster[index].getMembers().size());

                double frequencyTotal = 0;
                for(AlgoCustomer ac: frequencyCluster[index].getMembers()){
                    frequencyTotal += ac.getFrequency();
                }
                frequencyCluster[index].setRepresentative(frequencyTotal/frequencyCluster[index].getMembers().size());

                double moneyTotal = 0;
                for(AlgoCustomer ac: moneyCluster[index].getMembers()){
                    moneyTotal += ac.getMoneySpent();
                }
                moneyCluster[index].setRepresentative(moneyTotal/moneyCluster[index].getMembers().size());
            }

        }
        LOGGER.info("Finishing up clusters");
        sortClusters();
        assignMembers(k);
        createOverallClusters(k);
    }

    private void sortClusters(){
        Arrays.sort(recencyCluster, new SortByRecencyMean());
        Arrays.sort(frequencyCluster, new SortByMean());
        Arrays.sort(moneyCluster, new SortByMean());
    }

    private void assignMembers(int k){
        for(int i = 0; i<k; i++){
            for(AlgoCustomer ac: recencyCluster[i].getMembers()
            ) {
                ac.setRecencyCluster(i);
            }
            for(AlgoCustomer ac: frequencyCluster[i].getMembers()
            ) {
                ac.setFrequencyCluster(i);
            }
            for(AlgoCustomer ac: moneyCluster[i].getMembers()
            ) {
                ac.setMoneyCluster(i);
            }
        }
    }

    private double randomBetween(double min, double max, double modifier){
        return (max * modifier - min / modifier) * Math.random() + min/modifier;
    }

    private void createOverallClusters(int k){
        overallRanking = new DoubleCluster[(k-1)*3+1];
        for(int i = 0; i<(k-1)*3+1; i++){
            overallRanking[i] = new DoubleCluster();
            overallRanking[i].setRanking(i);
        }
        for(int i = 0; i<k; i++){

            for(AlgoCustomer ac: recencyCluster[i].getMembers()
            ) {

                overallRanking[ac.getRecencyCluster() +
                               ac.getFrequencyCluster() +
                               ac.getMoneyCluster()
                    ].addMember(ac);
            }

        }
    }
}

class SortByMean implements Comparator<DoubleCluster>{
    public int compare(DoubleCluster a, DoubleCluster b){
        return (int)(a.getMean() - b.getMean());
    }
}

class SortByRecencyMean implements Comparator<DoubleCluster>{
    public int compare(DoubleCluster a, DoubleCluster b){
        return (int)(b.getMean() - a.getMean());
    }
}

