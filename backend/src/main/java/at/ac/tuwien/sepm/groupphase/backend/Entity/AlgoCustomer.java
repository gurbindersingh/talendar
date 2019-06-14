package at.ac.tuwien.sepm.groupphase.backend.Entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class AlgoCustomer {
    private String email;
    private List<Customer> associated;
    private List<Event> paritcipatedIn;
    private List<String> usedTags;
    private boolean willing;

    private LocalDateTime recency;
    private double frequency;
    private double moneySpent;
    private double recencyDays;

    private int recencyCluster;
    private int frequencyCluster;
    private int moneyCluster;

    private LocalDateTime first;
    private double firstDays;
    private double clv;
    private double predictedClv;
    private String favoriteTag;
    private List<Event> marketed;
    private boolean birthdayMarketed;
    public AlgoCustomer(){

    }
    public AlgoCustomer(String email,
                        List<Customer> associated,
                        List<Event> paritcipatedIn,
                        List<String> usedTags
    ) {
        this.email = email;
        this.associated = associated;
        this.paritcipatedIn = paritcipatedIn;
        this.usedTags = usedTags;
    }
    public AlgoCustomer(String email,
                        List<Customer> associated,
                        List<Event> paritcipatedIn,
                        List<String> usedTags,
                        LocalDateTime recency,
                        double frequency,
                        double moneySpent
    ) {
        this.email = email;
        this.associated = associated;
        this.paritcipatedIn = paritcipatedIn;
        this.usedTags = usedTags;
        this.recency = recency;
        this.frequency = frequency;
        this.moneySpent = moneySpent;
        Duration duration = Duration.between(LocalDateTime.now(), this.recency);
        this.recencyDays = duration.toDays();
    }


    public AlgoCustomer(String email,
                        List<Customer> associated,
                        List<Event> paritcipatedIn,
                        List<String> usedTags,
                        LocalDateTime recency,
                        double frequency,
                        double moneySpent,
                        double recencyDays,
                        int recencyCluster,
                        int frequencyCluster,
                        int moneyCluster,
                        LocalDateTime first,
                        double firstDays,
                        double clv,
                        double predictedClv,
                        String favoriteTag,
                        List<Event> marketed,
                        boolean willing,
                        boolean birthdayMarketed
    ) {
        this.email = email;
        this.associated = associated;
        this.paritcipatedIn = paritcipatedIn;
        this.usedTags = usedTags;
        this.recency = recency;
        this.frequency = frequency;
        this.moneySpent = moneySpent;
        this.recencyDays = recencyDays;
        this.recencyCluster = recencyCluster;
        this.frequencyCluster = frequencyCluster;
        this.moneyCluster = moneyCluster;
        this.first = first;
        this.firstDays = firstDays;
        this.clv = clv;
        this.predictedClv = predictedClv;
        this.favoriteTag = favoriteTag;
        this.marketed = marketed;
        this.willing = willing;
        this.birthdayMarketed = birthdayMarketed;
    }


    public boolean isBirthdayMarketed() {
        return birthdayMarketed;
    }


    public void setBirthdayMarketed(boolean birthdayMarketed) {
        this.birthdayMarketed = birthdayMarketed;
    }


    public boolean isWilling() {
        return willing;
    }


    public void setWilling(boolean willing) {
        this.willing = willing;
    }


    public List<Event> getMarketed() {
        return marketed;
    }


    public void setMarketed(List<Event> marketed) {
        this.marketed = marketed;
    }


    public String getFavoriteTag() {
        return favoriteTag;
    }


    public void setFavoriteTag(String favoriteTag) {
        this.favoriteTag = favoriteTag;
    }


    public double getPredictedClv() {
        return predictedClv;
    }


    public void setPredictedClv(double predictedClv) {
        this.predictedClv = predictedClv;
    }


    public double getClv() {
        return clv;
    }


    public void setClv(double clv) {
        this.clv = clv;
    }


    public LocalDateTime getFirst() {
        return first;
    }


    public void setFirst(LocalDateTime first) {
        this.first = first;
        Duration duration = Duration.between(LocalDateTime.now(), this.first);
        this.firstDays = duration.toDays();
    }


    public double getFirstDays() {
        return firstDays;
    }


    public double getRecencyDays() {
        return recencyDays;
    }


    public void setRecencyDays(double recencyDays) {
        this.recencyDays = recencyDays;
    }


    public void setFirstDays(double firstDays) {
        this.firstDays = firstDays;
    }


    public int getRecencyCluster() {
        return recencyCluster;
    }


    public void setRecencyCluster(int recencyCluster) {
        this.recencyCluster = recencyCluster;
    }


    public int getFrequencyCluster() {
        return frequencyCluster;
    }


    public void setFrequencyCluster(int frequencyCluster) {
        this.frequencyCluster = frequencyCluster;
    }


    public int getMoneyCluster() {
        return moneyCluster;
    }


    public void setMoneyCluster(int moneyCluster) {
        this.moneyCluster = moneyCluster;
    }


    public LocalDateTime getRecency() {
        return recency;
    }


    public void setRecency(LocalDateTime recency) {
        this.recency = recency;
        Duration duration = Duration.between(LocalDateTime.now(), this.recency);
        this.recencyDays = duration.toDays();

    }


    public double getFrequency() {
        return frequency;
    }


    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }


    public double getMoneySpent() {
        return moneySpent;
    }


    public void setMoneySpent(double moneySpent) {
        this.moneySpent = moneySpent;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public List<Customer> getAssociated() {
        return associated;
    }


    public void setAssociated(List<Customer> associated
    ) {
        this.associated = associated;
    }


    public List<Event> getParitcipatedIn() {
        return paritcipatedIn;
    }


    public void setParitcipatedIn(
        List<Event> paritcipatedIn
    ) {
        this.paritcipatedIn = paritcipatedIn;
    }


    public List<String> getUsedTags() {
        return usedTags;
    }


    public void setUsedTags(List<String> usedTags) {
        this.usedTags = usedTags;
    }


    @Override
    public String toString() {
        return "AlgoCustomer{" +
               "email='" + email + '\'' +
               ", associated=" + associated +
               ", paritcipatedIn=" + paritcipatedIn +
               '}';
    }
}
