package com.petclinic.dao;

import com.petclinic.model.Owner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerDao {

    public List<Owner> findAll() throws SQLException {
        List<Owner> owners = new ArrayList<>();
        String sql = "SELECT * FROM owners ORDER BY id";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                owners.add(mapResultSetToOwner(rs));
            }
        }
        return owners;
    }

    public Owner findById(int id) throws SQLException {
        String sql = "SELECT * FROM owners WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOwner(rs);
                }
            }
        }
        return null;
    }

    public Owner create(Owner owner) throws SQLException {
        String sql = "INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, owner.getFirstName());
            stmt.setString(2, owner.getLastName());
            stmt.setString(3, owner.getAddress());
            stmt.setString(4, owner.getCity());
            stmt.setString(5, owner.getTelephone());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    owner.setId(rs.getInt("id"));
                }
            }
        }
        return owner;
    }

    public void update(Owner owner) throws SQLException {
        String sql = "UPDATE owners SET first_name = ?, last_name = ?, address = ?, city = ?, telephone = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, owner.getFirstName());
            stmt.setString(2, owner.getLastName());
            stmt.setString(3, owner.getAddress());
            stmt.setString(4, owner.getCity());
            stmt.setString(5, owner.getTelephone());
            stmt.setInt(6, owner.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM owners WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Owner mapResultSetToOwner(ResultSet rs) throws SQLException {
        Owner owner = new Owner();
        owner.setId(rs.getInt("id"));
        owner.setFirstName(rs.getString("first_name"));
        owner.setLastName(rs.getString("last_name"));
        owner.setAddress(rs.getString("address"));
        owner.setCity(rs.getString("city"));
        owner.setTelephone(rs.getString("telephone"));
        owner.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return owner;
    }
}
