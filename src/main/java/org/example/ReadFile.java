package org.example;

import org.example.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReadFile {

    private static final Logger logger = LoggerFactory.getLogger(ReadFile.class);

    public List<Contact> contactsFromFile(String filePath) {
        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = br.readLine()) != null){
                String[] splitLine = line.split(",");
                String contactName = splitLine[0];
                String contactPhoneNumber = splitLine[1];
                String contactEmail = splitLine[2];

                if (contactName == null || contactName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Name cannot be empty or blank");
                }
                if (contactPhoneNumber == null || contactPhoneNumber.trim().isEmpty()) {
                    throw new IllegalArgumentException("Phone number cannot be empty or blank");
                }
                if (contactEmail == null || contactEmail.trim().isEmpty()) {
                    throw new IllegalArgumentException("Email cannot be empty or blank");
                }
                Contact contact = new Contact(contactName, contactPhoneNumber, contactEmail);
                contacts.add(contact);
            }
        }catch (IOException e){
            logger.error("The file can't be read", e);
        }
        return contacts;
    }
}
