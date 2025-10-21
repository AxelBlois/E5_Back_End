package com.esiee.tp3.model;

import com.esiee.tp3.domain.*;

import java.util.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Datamodel {

    private static Datamodel instance = null;

    public static synchronized Datamodel getInstance() {
        if (instance == null) {
            instance = new Datamodel();
        }
        return instance;
    }

    private final Map<Long, Person> persons = new HashMap<>();
    private final Map<Long, Civility> civilities = new HashMap<>();
    private final Map<Long, Function> functions = new HashMap<>();
    private final Map<Long, MailType> mailTypes = new HashMap<>();
    private final Map<Long, Mail> mails = new HashMap<>();

    private final AtomicLong personIdGenerator = new AtomicLong(0);
    private final AtomicLong mailIdGenerator = new AtomicLong(0);
    private final AtomicLong civilityIdGenerator = new AtomicLong(0);
    private final AtomicLong functionIdGenerator = new AtomicLong(0);
    private final AtomicLong mailTypeIdGenerator = new AtomicLong(0);

    private Datamodel() {
        initData();
    }

    private void initData() {
        Civility mr = addCivilityInternal(new Civility(), "MR", "Monsieur");
        Civility mme = addCivilityInternal(new Civility(), "MME", "Madame");

        Function dev = addFunctionInternal(new Function(), "DEV", "Développeur");
        Function pm = addFunctionInternal(new Function(), "PM", "Product Manager");

        MailType perso = addMailTypeInternal(new MailType(), "PERSO", "Personnel");
        MailType pro = addMailTypeInternal(new MailType(), "PRO", "Professionnel");

        Person person1 = new Person();
        person1.setLastname("Rozan");
        person1.setFirstname("Baptiste");
        person1.setMobilePhone("0600000001");
        person1.setLogin("rozanb");
        person1.setPassword("1234");
        person1.setCivility(mr);
        person1.setFunction(pm);
        addPersonInternal(person1); // Ajoute la personne et génère l'ID

        Mail mail1_1 = createAndAddMailInternal("br@perso.com", perso, person1);
        Mail mail1_2 = createAndAddMailInternal("baptiste.rozan@pro.com", pro, person1);
        person1.setMails(Arrays.asList(mail1_1, mail1_2)); // Mettre à jour la liste dans la personne

        Person person2 = new Person();
        person2.setLastname("Chang");
        person2.setFirstname("Charlotte");
        person2.setMobilePhone("0600000002");
        person2.setLogin("changc");
        person2.setPassword("0000");
        person2.setCivility(mme);
        person2.setFunction(dev);
        addPersonInternal(person2);

        Mail mail2_1 = createAndAddMailInternal("cc@perso.com", perso, person2);
        person2.setMails(Arrays.asList(mail2_1));
    }

    // --- Méthodes internes pour l'initialisation (gèrent l'ID et l'ajout aux maps) ---

    private Civility addCivilityInternal(Civility civility, String code, String label) {
        civility.setId(civilityIdGenerator.incrementAndGet());
        civility.setCode(code);
        civility.setLabel(label);
        civilities.put(civility.getId(), civility);
        return civility;
    }

    private Function addFunctionInternal(Function function, String code, String label) {
        function.setId(functionIdGenerator.incrementAndGet());
        function.setCode(code);
        function.setLabel(label);
        functions.put(function.getId(), function);
        return function;
    }

    private MailType addMailTypeInternal(MailType mailType, String code, String label) {
        mailType.setId(mailTypeIdGenerator.incrementAndGet());
        mailType.setCode(code);
        mailType.setLabel(label);
        mailTypes.put(mailType.getId(), mailType);
        return mailType;
    }

    private Person addPersonInternal(Person person) {
        if (person.getId() == null) {
            person.setId(personIdGenerator.incrementAndGet());
        }
        persons.put(person.getId(), person);
        return person;
    }

    private Mail createAndAddMailInternal(String address, MailType type, Person person) {
        Mail mail = new Mail();
        mail.setId(mailIdGenerator.incrementAndGet());
        mail.setAddress(address);
        mail.setType(type);
        mail.setPerson(person);
        mails.put(mail.getId(), mail);
        return mail;
    }


    // --- Getters ---

    public List<Person> getPersons() {
        return new ArrayList<>(persons.values());
    }
    public Person getPerson(Long id) {
        return persons.get(id);
    }
    public Person findPersonByLogin(String login) {
        return persons.values().stream()
                .filter(p -> login.equals(p.getLogin()))
                .findFirst()
                .orElse(null);
    }

    public List<Civility> getCivilities() {
        return new ArrayList<>(civilities.values());
    }
    public Civility getCivility(Long id) {
        return civilities.get(id);
    }

    public List<Function> getFunctions() {
        return new ArrayList<>(functions.values());
    }
    public Function getFunction(Long id) {
        return functions.get(id);
    }

    public List<MailType> getMailTypes() {
        return new ArrayList<>(mailTypes.values());
    }
    public MailType getMailType(Long id) {
        return mailTypes.get(id);
    }

    public List<Mail> getMails() {
        return new ArrayList<>(mails.values());
    }
    public Mail getMail(Long id) {
        return mails.get(id);
    }
    public List<Mail> getMailsByPersonId(Long personId) {
        Person person = persons.get(personId);
        if (person != null && person.getMails() != null) {
            return new ArrayList<>(person.getMails());
        }
        return Collections.emptyList();
    }

    // --- Méthodes CRUD ---

    public synchronized Person savePerson(Person person) {
        if (person.getId() == null) {
            person.setId(personIdGenerator.incrementAndGet());
        }
        // Assurer la cohérence des liens (simulé ici)
        if(person.getCivility() != null && person.getCivility().getId() != null) {
            person.setCivility(civilities.get(person.getCivility().getId()));
        }
        if(person.getFunction() != null && person.getFunction().getId() != null) {
            person.setFunction(functions.get(person.getFunction().getId()));
        }
        persons.put(person.getId(), person);
        return person;
    }

    public synchronized boolean deletePerson(Long id) {
        Person removedPerson = persons.remove(id);
        if (removedPerson != null) {
            List<Mail> mailsToDelete = getMailsByPersonId(id);
            mailsToDelete.forEach(mail -> mails.remove(mail.getId()));
            return true;
        }
        return false;
    }

    public synchronized Mail saveMail(Mail mail) {
        if (mail.getId() == null) {
            mail.setId(mailIdGenerator.incrementAndGet());
        }
        // Assurer la cohérence des liens
        if(mail.getPerson() == null || mail.getPerson().getId() == null) {
            throw new IllegalArgumentException("Mail must be associated with a Person");
        }
        Person person = persons.get(mail.getPerson().getId());
        if(person == null) {
            throw new IllegalArgumentException("Associated Person not found");
        }
        mail.setPerson(person); // Mettre la référence correcte

        if(mail.getType() == null || mail.getType().getId() == null) {
            throw new IllegalArgumentException("Mail must have a MailType");
        }
        MailType type = mailTypes.get(mail.getType().getId());
        if(type == null) {
            throw new IllegalArgumentException("Associated MailType not found");
        }
        mail.setType(type); // Mettre la référence correcte

        mails.put(mail.getId(), mail);

        // Mettre à jour la liste dans Person si c'est un ajout
        boolean alreadyExists = person.getMails() != null && person.getMails().stream().anyMatch(m -> m.getId().equals(mail.getId()));
        if (!alreadyExists) {
            List<Mail> personMails = new ArrayList<>(person.getMails() != null ? person.getMails() : Collections.emptyList());
            personMails.add(mail);
            person.setMails(personMails);
        }

        return mail;
    }

    public synchronized boolean deleteMail(Long mailId) {
        Mail mail = mails.remove(mailId);
        if (mail != null) {
            Person person = mail.getPerson();
            if (person != null && person.getMails() != null) {
                person.getMails().removeIf(m -> m.getId().equals(mailId));
            }
            return true;
        }
        return false;
    }

    // --- CRUD pour les Référentiels (simplifié) ---

    public synchronized Civility saveCivility(Civility civility) {
        if (civility.getId() == null) {
            civility.setId(civilityIdGenerator.incrementAndGet());
        }
        civilities.put(civility.getId(), civility);
        return civility;
    }

    public synchronized boolean deleteCivility(Long id) {
        boolean isUsed = persons.values().stream().anyMatch(p -> p.getCivility() != null && p.getCivility().getId().equals(id));
        if (isUsed) return false;
        return civilities.remove(id) != null;
    }

    public synchronized Function saveFunction(Function function) {
        if (function.getId() == null) {
            function.setId(functionIdGenerator.incrementAndGet());
        }
        functions.put(function.getId(), function);
        return function;
    }

    public synchronized boolean deleteFunction(Long id) {
        boolean isUsed = persons.values().stream().anyMatch(p -> p.getFunction() != null && p.getFunction().getId().equals(id));
        if (isUsed) return false;
        return functions.remove(id) != null;
    }

    public synchronized MailType saveMailType(MailType mailType) {
        if (mailType.getId() == null) {
            mailType.setId(mailTypeIdGenerator.incrementAndGet());
        }
        mailTypes.put(mailType.getId(), mailType);
        return mailType;
    }

    public synchronized boolean deleteMailType(Long id) {
        boolean isUsed = mails.values().stream().anyMatch(m -> m.getType() != null && m.getType().getId().equals(id));
        if (isUsed) return false;
        return mailTypes.remove(id) != null;
    }
}