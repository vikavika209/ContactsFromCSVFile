package org.example;

import org.example.config.ContactServiceConfig;
import org.example.model.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NamedJdbcContactServiceTest {

    ApplicationContext context = new AnnotationConfigApplicationContext(ContactServiceConfig.class);
    ContactDao contactService = context.getBean(ContactDao.class);

    @Test
    void addContacts() {
        List<Contact> contacts = contactService.addContacts("src/main/resources/contact-list.csv");

        Contact contact1 = contacts.get(0);
        assertEquals("Иван Иванов", contact1.getName(), "First contact's name should match");
        assertEquals("+71234567890", contact1.getPhoneNumber(), "First contact's phone number should match");
        assertEquals("iivanov@gmail.com", contact1.getEmail(), "First contact's email should match");

        Contact contact2 = contacts.get(1);
        assertEquals("Петр Петров", contact2.getName(), "Second contact's name should match");
        assertEquals("+71233467780", contact2.getPhoneNumber(), "Second contact's phone number should match");
        assertEquals("petrov1992@gmail.com", contact2.getEmail(), "Second contact's email should match");
    }
}