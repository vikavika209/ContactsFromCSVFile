package org.example;

import org.example.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class NamedJdbcContactService implements ContactService{

    private static final String ADD_CONTACT_SQL = "" +
            "INSERT INTO CONTACTS (NAME, PHONE_NUMBER, EMAIL) " +
            "VALUES (:name, :phone_number, :email) " +
            "RETURNING id";

    private static final String GET_ALL_CONTACT_SQL = "" +
            "SELECT " +
            "ID, " +
            "NAME, " +
            "PHONE_NUMBER, " +
            "EMAIL " +
            "FROM CONTACTS";

    private static final RowMapper<Contact> CONTACT_ROW_MAPPER =
            (rs, rowNum) -> new Contact(
                    rs.getInt("ID"),
                    rs.getString("NAME"),
                    rs.getString("PHONE_NUMBER"),
                    rs.getString("EMAIL"));

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ReadFile readFile = new ReadFile();

    public NamedJdbcContactService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Contact> addContacts(String filePath) {
        namedParameterJdbcTemplate.update("DELETE FROM CONTACTS", new HashMap<>());
        List<Contact> contacts = readFile.contactsFromFile(filePath);
        var args = contacts.stream()
                .map(contact -> new MapSqlParameterSource()
                        .addValue("name", contact.getName())
                        .addValue("phone_number", contact.getPhoneNumber())
                        .addValue("email", contact.getEmail()))
                .toArray(MapSqlParameterSource[]::new);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.batchUpdate(ADD_CONTACT_SQL, args, keyHolder, new String[]{"id"});

        List<Integer> generatedIds = keyHolder.getKeyList().stream()
                .map(key -> (Integer) key.get("id"))
                .toList();

        for (int i = 0; i < contacts.size(); i++) {
            contacts.get(i).setId(generatedIds.get(i));
        }
        return contacts;
    }
}
