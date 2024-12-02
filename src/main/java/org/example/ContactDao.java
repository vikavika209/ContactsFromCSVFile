package org.example;

import org.example.model.Contact;

import java.util.List;

public interface ContactDao {
    List<Contact> addContacts(String filePath);
}
