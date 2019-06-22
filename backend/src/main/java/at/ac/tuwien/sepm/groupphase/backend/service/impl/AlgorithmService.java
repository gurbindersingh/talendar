package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.*;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.persistence.*;
import at.ac.tuwien.sepm.groupphase.backend.service.algorithmClasses.KMeans;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.EmailException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class AlgorithmService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final RoomUseRepository roomUseRepository;
    private final Validator validator;
    private final TrainerRepository trainerRepository;
    private final ItemAffinityRepository itemAffinityRepository;
    private final CustomerRepository customerRepository;
    private KMeans kmeansRFM;
    private InfoMail infoMail;


    @Autowired
    public AlgorithmService(
        EventRepository eventRepository,
        RoomUseRepository roomUseRepository,
        Validator validator,
        TrainerRepository trainerRepository,
        ItemAffinityRepository itemAffinityRepository,
        CustomerRepository customerRepository,
        InfoMail infoMail
    ) {
        this.eventRepository = eventRepository;
        this.roomUseRepository = roomUseRepository;
        this.validator = validator;
        this.trainerRepository = trainerRepository;
        this.itemAffinityRepository = itemAffinityRepository;
        this.customerRepository = customerRepository;
        this.infoMail = infoMail;
    }

    public void algorithm() throws EmailException{
        LOGGER.info("Up/Cross Sell Algorithm Started");
        LOGGER.info("Pruning Customers");
        List<Customer> customers = customerRepository.findAll();
        List<AlgoCustomer> algoCustomers = pruneCustomers(customers);
        LOGGER.info("Updating ItemAffinities");
        updateFillItemAffinity(algoCustomers);
        LOGGER.info("Calculating Favorite Tags");
        calcMostUsed(algoCustomers);
        if(algoCustomers.size()<50){
            LOGGER.info("Small user base");
            pruneNotWilling(algoCustomers);
            simpleSet(algoCustomers);
        }else {
            LOGGER.info("RFM Model in use for " + algoCustomers.size() + " customers");
            kmeansRFM = new KMeans(algoCustomers);
            algoCustomers = kmeansRFM.algoCustomers;
            pruneNotWilling(algoCustomers);
        }
        List<Event> events = eventRepository.findByEventTypeEqualsAndDeletedFalseAndRoomUses_BeginGreaterThanEqual(EventType.Course, LocalDateTime.now());
        List<ItemAffinity> items = itemAffinityRepository.findAll();
        for(AlgoCustomer ac: algoCustomers
        ) {
            try {
                if(validator.validAlgoCustomer(ac)) {
                    sell(ac, events, items);
                }else{
                    LOGGER.info("Validation for customer " + ac.getEmail() + " failed");
                }
            }catch(EmailException e){
                throw new EmailException(e.getMessage());
            }
        }
        //eventRepository.findByEventTypeEqualsAndDeletedFalseAndRoomUses_BeginGreaterThanEqual(
        //            EventType.Course, LocalDateTime.now())

    }

    private void sell(AlgoCustomer algoCustomer, List<Event> events, List<ItemAffinity> items) throws EmailException{
        Event toMarket = null;
        boolean birthday = false;
        LocalDateTime bday = null;
        if(!algoCustomer.isBirthdayMarketed()) {
            for(Customer c : algoCustomer.getAssociated()) {
                if(birthdaySoon(c.getBirthOfChild())) {
                    birthday = true;
                    bday = c.getBirthOfChild();
                    algoCustomer.setBirthdayMarketed(true);
                    break;
                } else {
                    algoCustomer.setBirthdayMarketed(false);
                }
            }
        }
        if((algoCustomer.getFrequencyCluster() + algoCustomer.getRecencyCluster() + algoCustomer.getMoneyCluster()) <= 2 ){
            LOGGER.info("Upselling to customer " + algoCustomer.getEmail() + " with a loyalty of " + algoCustomer.getRecencyCluster()+ algoCustomer.getFrequencyCluster() + algoCustomer.getMoneyCluster());
             toMarket = findUpSell(algoCustomer, events, items);
        }else if((algoCustomer.getFrequencyCluster() + algoCustomer.getRecencyCluster() + algoCustomer.getMoneyCluster()) <= 4 ){
              if(algoCustomer.getRecencyDays() < 30){
                  LOGGER.info("Crossselling to customer " + algoCustomer.getEmail() + " with a loyalty of " + algoCustomer.getRecencyCluster()+ algoCustomer.getFrequencyCluster() + algoCustomer.getMoneyCluster());
                  toMarket = findCrossSell(algoCustomer, events, items);
              }else{
                  LOGGER.info("Upselling to customer " + algoCustomer.getEmail() + " with a loyalty of " + algoCustomer.getRecencyCluster()+ algoCustomer.getFrequencyCluster() + algoCustomer.getMoneyCluster());
                  toMarket = findUpSell(algoCustomer, events, items);
              }
        }else if((algoCustomer.getFrequencyCluster() + algoCustomer.getRecencyCluster() + algoCustomer.getMoneyCluster()) >= 5 ){
             LOGGER.info("Crossselling to customer " + algoCustomer.getEmail() + " with a loyalty of " + algoCustomer.getRecencyCluster()+ algoCustomer.getFrequencyCluster() + algoCustomer.getMoneyCluster());
             toMarket = findCrossSell(algoCustomer, events, items);
        }
        try {
            if(birthday) {
                LOGGER.info("Selling customer " + algoCustomer.getEmail() + " with a birthday on "+ bday + " on a birthday offer");
                infoMail.birthdayMail(algoCustomer, bday);
            } else if(toMarket != null) {
                infoMail.marketingMail(algoCustomer, toMarket);
            }
        }catch(EmailException e){
            throw new EmailException(e.getMessage());
        }
    }


    private void simpleSet(List<AlgoCustomer> algoCustomers){
        for(AlgoCustomer ac: algoCustomers
        ) {
            int yearsParticipated = LocalDateTime.now().getYear()-ac.getFirst().getYear();
            if(yearsParticipated == 0){
                yearsParticipated++;
            }
            ac.setFrequencyCluster(((int)(ac.getFrequency()/yearsParticipated)/2));
        }
    }

    public void updateFillItemAffinity(List<AlgoCustomer> algoCustomers){
        itemAffinityRepository.deleteAll();
        List<String> event_tags = getAllTags(algoCustomers);
        int iteration = 0;
        for(String s: event_tags
        ) {
            for(int i = 0; i < event_tags.size();i++)
            {
                if(s != event_tags.get(i)) {
                    int count = 0;
                    for(AlgoCustomer ac : algoCustomers
                    ) {
                        if(ac.getUsedTags().contains(s) && ac.getUsedTags().contains(event_tags.get(i))) {
                            //LOGGER.debug("Count for tag " + s + " for customer " + ac.getEmail() + " was increased");
                            count++;
                        }
                    }
                    double support = (double)(count)/(double)(algoCustomers.size());
                    ItemAffinity ins = new ItemAffinity(null,s,event_tags.get(i), support);
                    LOGGER.info("Tag 1: " + ins.getItem1() + " Tag 2: " + ins.getItem2() + " Support: " + ins.getSupport());
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
        for(String tag: tags

        ) {
            LOGGER.debug("Tag: " + tag  + " was used");
        }
        return tags;
    }

    public List<AlgoCustomer> pruneCustomers(List<Customer> customerList){
        List<String> customerEmails = new LinkedList<>();
        List<AlgoCustomer> pruned = new LinkedList<>();
        for(Customer c: customerList
        ) {
            if(c.getWantsEmail() != null) {
                LOGGER.info("Processing Customer " + c.getEmail());
                if(!customerEmails.contains(c.getEmail())) {
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
                    for(Event e : c.getEvents()) {
                        if(e.getPrice() != null &&
                           e.getEventTags() != null &&
                           e.getRoomUses() != null && e.getName() != null) {
                            LOGGER.info("Processing Event " +
                                        e.getName() +
                                        " for Customer " +
                                        c.getEmail());
                            events.add(e);


                            LocalDateTime tmp = mostRecentUse(e.getRoomUses());
                            if(tmp.isAfter(tmpR)) {
                                tmpR = tmp;
                            }

                            frequency++;

                            money += e.getPrice();
                            if(!tags.contains(e.getEventTags())) {
                                tags.add(e.getEventTags());
                            }


                            LocalDateTime tmp2 = firstUse(e.getRoomUses());
                            if(tmp2.isBefore(first)) {
                                first = tmp2;
                            }
                        }
                    }
                    AlgoCustomer newAlgoCustomer =
                        new AlgoCustomer(c.getEmail(), newCustomer, events, tags, tmpR, frequency,
                                         money
                        );
                    if(c.getWantsEmail()) {
                        newAlgoCustomer.setWilling(true);
                    }
                    newAlgoCustomer.setFirst(first);
                    pruned.add(newAlgoCustomer);
                } else {
                    LOGGER.debug("Customer " + c.getEmail() + " has been processed before");
                    for(AlgoCustomer ac : pruned) {
                        if(c.getEmail().equals(ac.getEmail())) {
                            ac.getAssociated().add(c);
                            if(c.getWantsEmail()) {
                                ac.setWilling(true);
                            }
                            for(Event e : c.getEvents()) {
                                if(e.getPrice() != null &&
                                   e.getEventTags() != null &&
                                   e.getRoomUses() != null && e.getName() != null) {
                                    LOGGER.info("Processing Event " +
                                                e.getName() +
                                                " for Customer " +
                                                c.getEmail());
                                    ac.getParitcipatedIn().add(e);
                                    LocalDateTime tmp = mostRecentUse(e.getRoomUses());
                                    LocalDateTime first = firstUse(e.getRoomUses());
                                    if(tmp.isAfter(ac.getRecency())) {
                                        ac.setRecency(tmp);
                                    }
                                    if(first.isBefore(ac.getFirst())) {
                                        ac.setFirst(first);
                                    }
                                    ac.setFrequency(ac.getFrequency() + 1);

                                    ac.setMoneySpent(ac.getMoneySpent() + e.getPrice());

                                    if(!ac.getUsedTags().contains(e.getEventTags())) {
                                        ac.getUsedTags().add(e.getEventTags());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
            LOGGER.info(pruned.size() + " customers available");
            for(AlgoCustomer ac : pruned
            ) {
                LOGGER.info("Customer with email " +
                            ac.getEmail() +
                            " parsed participated in " +
                            ac.getParitcipatedIn().size() +
                            " events");
                LOGGER.info("   used tags: ");
                for(String s: ac.getUsedTags()
                ) {
                    LOGGER.info("       " + s);
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

    public void calcMostUsed(List<AlgoCustomer> algoCustomers){
        for(AlgoCustomer algoCustomer: algoCustomers
        ) {
            HashMap<String, Integer> tagUsage = new HashMap<>();
            for(Event e: algoCustomer.getParitcipatedIn()
            ) {
                if(tagUsage.containsKey(e.getEventTags())){
                    int previous = tagUsage.get(e.getEventTags());
                    tagUsage.replace(e.getEventTags(), previous + 1);
                }else{
                    tagUsage.put(e.getEventTags(), 1);
                }

            }
            String best = "";
            int count = 0;
            for( Map.Entry<String, Integer> tagStat : tagUsage.entrySet()
            ) {
                if(tagStat.getValue() > count){
                    best = tagStat.getKey();
                    count = tagStat.getValue();
                }
            }
            algoCustomer.setFavoriteTag(best);
        }

    }

    private Event findUpSell(AlgoCustomer algoCustomer, List<Event> futureEvents, List<ItemAffinity> itemAffinities){
        List<Event> possibleSells = pruneSellable(algoCustomer, futureEvents);
        ArrayList<ItemAffinity> tagChoices = affineTags(itemAffinities, algoCustomer.getFavoriteTag());
        boolean found = false;
        Event ret = null;
        int iteration = -1;
        while(!found){
            LocalDateTime reference = LocalDateTime.MAX;
            for(Event e: possibleSells
            ) {
                if(e.getEventTags() != null) {
                    if(iteration == -1) {
                        if(e.getEventTags().contains(algoCustomer.getFavoriteTag()) &&
                           firstUse(e.getRoomUses()).isBefore(reference)) {
                            reference = firstUse(e.getRoomUses());
                            ret = e;
                            found = true;
                        }
                    } else {
                        if(e.getEventTags().contains(tagChoices.get(iteration).getItem2()) &&
                           firstUse(e.getRoomUses()).isBefore(reference)) {
                            reference = firstUse(e.getRoomUses());
                            ret = e;
                            found = true;
                        }
                    }
                }
            }
            if(iteration >= 1 || tagChoices == null || tagChoices.size() <= iteration){
                found = true;
            }
            iteration++;
        }
        return ret;
    }

    private Event findCrossSell(AlgoCustomer algoCustomer, List<Event> futureEvents, List<ItemAffinity> itemAffinities){
        List<Event> possibleSells = pruneSellable(algoCustomer, futureEvents);
        ArrayList<ItemAffinity> tagChoices = affineTags(itemAffinities, algoCustomer.getFavoriteTag());
        if(tagChoices == null){
            return null;
        }

        boolean found = false;
        Event ret = null;
        int iteration = 0;
        while(!found){
            LocalDateTime reference = LocalDateTime.MAX;

            for(Event e : possibleSells
            ) {
                if(e.getEventTags().contains(tagChoices.get(iteration).getItem2()) && firstUse(e.getRoomUses()).isBefore(reference)){
                    reference = firstUse(e.getRoomUses());
                    ret = e;
                    found = true;
                }
            }
            iteration++;
            if(tagChoices.size() <= iteration){
                break;
            }
            if(tagChoices.get(iteration).getSupport() < 0.2){
                break;
            }

        }
        return ret;
    }

    private ArrayList<ItemAffinity> affineTags(List<ItemAffinity> itemAffinities, String tag){
        ArrayList<ItemAffinity> ret = new ArrayList<>();
        for(ItemAffinity ia: itemAffinities
        ) {
            if(ia.getItem1() == tag){
                ret.add(ia);
            }
        }
        Collections.sort(ret, new SortBySupport());
        return ret;
    }

    private List<Event> pruneSellable(AlgoCustomer algoCustomer, List<Event> events){
        List<Event> ret = new LinkedList<>();
        for(Event e: events
        ) {
            if(firstUse(e.getRoomUses()).isBefore(LocalDateTime.now().plusMonths(3)) && LocalDateTime.now().isBefore(e.getEndOfApplication().minusDays(2))){
                if(ageExists(algoCustomer, e) &&
                   !containsName(algoCustomer.getParitcipatedIn(), e) &&
                   !algoCustomer.getMarketed().contains(e) &&
                   (e.getCustomers().size() < e.getMaxParticipants())){
                    ret.add(e);
                }
            }
        }
        return ret;
    }

    private boolean containsName(List<Event> events, Event event){
        for(Event e: events
        ) {
            if(event.getName().equals(e.getName())){
                return true;
            }
        }
        return false;
    }

    private boolean ageExists(AlgoCustomer algoCustomer, Event event){
        for(Customer c: algoCustomer.getAssociated()
        ) {
            if(c.getBirthOfChild().isBefore(LocalDateTime.now().minusYears(event.getMinAge())) && c.getBirthOfChild().isAfter(LocalDateTime.now().minusYears(
                event.getMaxAge()))){
                return true;
            }
        }
        return false;
    }

    private void pruneNotWilling(List<AlgoCustomer> algoCustomers){
        for(AlgoCustomer ac: algoCustomers
        ) {
            if(!ac.isWilling()){
                algoCustomers.remove(ac);
            }
        }
    }

    private boolean birthdaySoon(LocalDateTime localDateTime){
        int day = localDateTime.getDayOfYear();
        int dayNow = LocalDateTime.now().getDayOfYear();
        if(day < 30){
            if(((day+365) - dayNow) < 30 && ((day + 365) - dayNow) > 0){
                return true;
            }
        }else{
            if((day - dayNow) < 30 && (day - dayNow) > 0){
                return true;
            }
        }
        return false;
    }

}

class SortBySupport implements Comparator<ItemAffinity> {
    public int compare(ItemAffinity a, ItemAffinity b){
        return (int)(a.getSupport()-b.getSupport());
    }
}