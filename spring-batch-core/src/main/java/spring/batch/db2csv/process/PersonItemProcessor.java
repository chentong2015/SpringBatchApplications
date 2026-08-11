package spring.batch.db2csv.process;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import spring.batch.db2csv.bean.Person;

@Component
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person person) {
        String firstName = person.getFirstName().toUpperCase();
        String lastName = person.getLastName().toUpperCase();
        Person transformedPerson = new Person(firstName, lastName);

        System.out.println("Converting (" + person + ") into (" + transformedPerson + ")");
        return transformedPerson;
    }
}
