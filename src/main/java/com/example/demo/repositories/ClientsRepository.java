package com.example.demo.repositories;

import com.example.demo.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Client> getClients() {
        var clients = new ArrayList<Client>();

        String sql = "Select * from clients order by id DESC";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        while (rows.next()) {
            Client client = new Client();
            client.setId(rows.getInt("id"));
            client.setFirstName(rows.getString("firstname"));
            client.setLastName(rows.getString("lastname"));
            client.setEmail(rows.getString("email"));
            client.setPhone(rows.getString("phone"));
            client.setAddress(rows.getString("address"));
            client.setCreatedAt(rows.getString("created_at"));

            clients.add(client);
        }
        return clients;
    }

    public Client getClient(String id) {
        String sql = "SELECT * FROM clients WHERE id=?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql,id);

        if(rows.next()) {
            Client client = new Client();
            client.setId(rows.getInt("id"));
            client.setFirstName(rows.getString("firstname"));
            client.setLastName(rows.getString("lastname"));
            client.setEmail(rows.getString("email"));
            client.setPhone(rows.getString("phone"));
            client.setAddress(rows.getString("address"));
            client.setCreatedAt(rows.getString("created_at"));

            return client;
        }

        return null;
    }

    public Client createClient(Client client) {
        String sql = "insert into clients (firstname, lastname, email, phone, " +
                "address, created_at) values (?,?,?,?,?,?)";

        int count = jdbcTemplate.update(sql,client.getFirstName(),client.getLastName(),client.getEmail(),client.getPhone(),client.getAddress(),client.getCreatedAt());

        if(count > 0) {
            int id = jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class);
            return getClient(String.valueOf(id));
        }

        return null;
    }

     public Client updateClient(Client client) {
        String sql = "update clients set firstname=?,lastname=?,email=?, " +
                "phone=?,address=?,created_At=? where id=?";

        jdbcTemplate.update(sql, client.getFirstName(), client.getLastName(), client.getEmail(), client.getPhone(), client.getAddress(), client.getCreatedAt(), client.getId());

        return getClient(String.valueOf(client.getId()));
     }

     public void deleteClient(int id) {
        String sql = "Delete from clients where id=?";
        jdbcTemplate.update(sql,id);
     }
}
