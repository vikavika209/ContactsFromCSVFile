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
    ContactService contactService = context.getBean(ContactService.class);

    @Test
    void addContacts() {
        List<Contact> contacts= contactService.addContacts("src/main/resources/contact-list.csv");
        Contact contact1 = new Contact("Иван Иванов", "+71234567890", "iivanov@gmail.com");
        Contact contact2 = new Contact("Петр Петров","+71233467780","petrov1992@gmail.com");
        assertEquals("Иван Иванов", contact1.getName());
        assertEquals("Петр Петров", contact2.getName());
        assertEquals("iivanov@gmail.com", contact1.getEmail());

    }
}