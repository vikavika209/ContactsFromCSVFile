package org.example;

import org.example.model.Contact;

import java.util.List;
import java.util.Map;

public interface ContactService {
    List<Contact> addContacts(String filePath);
}
