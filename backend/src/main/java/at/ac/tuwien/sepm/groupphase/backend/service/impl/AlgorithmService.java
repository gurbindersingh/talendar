package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.*;
import at.ac.tuwien.sepm.groupphase.backend.persistence.*;
import at.ac.tuwien.sepm.groupphase.backend.service.algorithmClasses.KMeans;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class AlgorithmService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final RoomUseRepository roomUseRepository;
    private final Validator validator;
    private final TrainerRepository trainerRepository;
    private final ItemAffinityRepository itemAffinityRepository;
    private final CustomerRepository customerRepository;
    private KMeans kmeansRFM;


    @Autowired
    public AlgorithmService(
        EventRepository eventRepository,
        RoomUseRepository roomUseRepository,
        Validator validator,
        TrainerRepository trainerRepository,
        ItemAffinityRepository itemAffinityRepository,
        CustomerRepository customerRepository
    ) {
        this.eventRepository = eventRepository;
        this.roomUseRepository = roomUseRepository;
        this.validator = validator;
        this.trainerRepository = trainerRepository;
        this.itemAffinityRepository = itemAffinityRepository;
        this.customerRepository = customerRepository;
    }

    public void updateTables(){
        List<AlgoCustomer> algoCustomers = pruneCustomers(customerRepository.findAll());
        updateFillItemAffinity(algoCustomers);
        kmeansRFM = new KMeans(algoCustomers);

    }
    public void updateFillItemAffinity(List<AlgoCustomer> algoCustomers){
        List<String> event_tags = getAllTags(algoCustomers);
        int iteration = 0;
        for(String s: event_tags
        ) {
            for(int i = iteration; i < event_tags.size();i++)
            {
                if(s != event_tags.get(i)) {
                    int count = 0;
                    for(AlgoCustomer ac : algoCustomers
                    ) {
                        if(ac.getUsedTags().contains(s) && ac.getUsedTags().contains(event_tags.get(i))) {
                            count++;
                        }
                    }
                    double support = count/algoCustomers.size();
                    ItemAffinity ins = new ItemAffinity(null,s,event_tags.get(i), support);
                    itemAffinityRepository.save(ins);
                }
            }
            iteration++;
        }
    }

    private List<String> getAllTags(List<AlgoCustomer> algoCustomers){
        List<String> tags = new LinkedList<>();
        for(AlgoCustomer ac: algoCustomers
        ) {
            for(String s: ac.getUsedTags()
            ) {
                if(!tags.contains(s)){
                    tags.add(s);
                }
            }
        }
        return tags;
    }

    public List<AlgoCustomer> pruneCustomers(List<Customer> customerList){
        List<String> customerEmails = new LinkedList<>();
        List<AlgoCustomer> pruned = new LinkedList<>();
        for(Customer c: customerList
        ) {
            if(!customerEmails.contains(c.getEmail())){
                customerEmails.add(c.getEmail());
                List<Customer> newCustomer = new LinkedList<>();
                newCustomer.add(c);
                List<Event> events = new LinkedList<>();
                List<String> tags = new LinkedList<>();
                double frequency = 0;
                double money = 0;
                LocalDateTime first = LocalDateTime.MAX;
                LocalDateTime tmpR = LocalDateTime.MIN;
                Duration durationMin = Duration.between(LocalDateTime.now(), tmpR);
                for(Event e: c.getEvents()) {
                    events.add(e);


                    LocalDateTime tmp = mostRecentUse(e.getRoomUses());
                    if(tmp.isAfter(tmpR)) {
                        tmpR = tmp;
                    }

                    frequency++;

                    money += e.getPrice();
                    for(String s: e.getEvent_tags()
                    ) {
                        if(!tags.contains(s)){
                            tags.add(s);
                        }
                    }

                    LocalDateTime tmp2 = firstUse(e.getRoomUses());
                    if(tmp2.isBefore(first)){
                        first = tmp2;
                    }
                }
                AlgoCustomer newAlgoCustomer = new AlgoCustomer(c.getEmail(),newCustomer, events, tags, tmpR, frequency, money);
                newAlgoCustomer.setFirst(first);
                pruned.add(newAlgoCustomer);
            }else{
                for(AlgoCustomer ac: pruned) {
                    if(c.getEmail() == ac.getEmail()){
                        ac.getAssociated().add(c);
                        for(Event e: c.getEvents()) {
                            ac.getParitcipatedIn().add(e);
                            LocalDateTime tmp = mostRecentUse(e.getRoomUses());
                            LocalDateTime first = firstUse(e.getRoomUses());
                            if(tmp.isAfter(ac.getRecency())) {
                                ac.setRecency(tmp);
                            }
                            if(first.isBefore(ac.getFirst())){
                                ac.setFirst(first);
                            }
                            ac.setFrequency(ac.getFrequency()+1);

                            ac.setMoneySpent(ac.getMoneySpent() + e.getPrice());
                            for(String s: e.getEvent_tags()
                            ) {


                                if(!ac.getUsedTags().contains(s)){
                                    ac.getUsedTags().add(s);
                                }
                            }
                        }
                    }
                }
            }
        }
        return pruned;
    }


    public LocalDateTime mostRecentUse(List<RoomUse> roomUses){
        LocalDateTime cmp = roomUses.get(0).getEnd();
        for(RoomUse r: roomUses
        ) {
            if(r.getEnd().isAfter(cmp)){
                cmp = r.getEnd();
            }
        }
        return cmp;
    }

    public LocalDateTime firstUse(List<RoomUse> roomUses){
        LocalDateTime cmp = roomUses.get(0).getBegin();
        for(RoomUse r: roomUses
        ) {
            if(r.getBegin().isBefore(cmp)){
                cmp = r.getBegin();
            }
        }
        return cmp;
    }
}
