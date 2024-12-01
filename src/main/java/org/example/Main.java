package org.example;

import org.example.config.ContactServiceConfig;
import org.example.model.Contact;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ContactServiceConfig.class);
        ContactService contactService = context.getBean(ContactService.class);

        List<Contact> contacts= contactService.addContacts("src/main/resources/contact-list.csv");
        System.out.println(contacts);
    }
}