package org.example;

import org.example.config.ContactServiceConfig;
import org.example.model.Contact;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ContactServiceConfig.class);
        ContactDao contactService = context.getBean(ContactDao.class);

        List<Contact> contacts= contactService.addContacts("src/main/resources/contact-list.csv");
    }
}