package org.example;

import org.example.model.Contact;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Repository
public class JdbcContactDao implements ContactDao {

    private static final String ADD_CONTACT_SQL = "" +
            "INSERT INTO CONTACTS (NAME, PHONE_NUMBER, EMAIL) " +
            "VALUES (:name, :phone_number, :email) ";

    private static final RowMapper<Contact> CONTACT_ROW_MAPPER =
            (rs, rowNum) -> new Contact(
                    rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("PHONE_NUMBER"),
                    rs.getString("EMAIL"));

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ContactService readFile = new ContactService();
    private  final Logger logger = LoggerFactory.getLogger(JdbcContactDao.class);

    public JdbcContactDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Contact> addContacts(String filePath) {
        try {
            List<Contact> contacts = readFile.contactsFromFile(filePath);

            if (contacts.isEmpty()) {
                logger.warn("No contacts found in the file: {}", filePath);
                return contacts;
            }

            var args = contacts.stream()
                    .map(contact -> new MapSqlParameterSource()
                            .addValue("name", contact.getName())
                            .addValue("phone_number", contact.getPhoneNumber())
                            .addValue("email", contact.getEmail()))
                    .toArray(MapSqlParameterSource[]::new);

            namedParameterJdbcTemplate.batchUpdate(ADD_CONTACT_SQL, args);

            return contacts;
        } catch (DataAccessException e) {
            logger.error("Database operation failed", e);
            throw new RuntimeException("Failed to add contacts to the database", e);
        }
    }
}
