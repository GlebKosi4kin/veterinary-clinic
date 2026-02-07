package com.petclinic.dao;

import com.petclinic.model.Vet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VetDao {

    public List<Vet> findAll() throws SQLException {
        List<Vet> vets = new ArrayList<>();
        String sql = "SELECT * FROM vets ORDER BY id";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vets.add(mapResultSetToVet(rs));
            }
        }
        return vets;
    }

    public Vet findById(int id) throws SQLException {
        String sql = "SELECT * FROM vets WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVet(rs);
                }
            }
        }
        return null;
    }

    public Vet create(Vet vet) throws SQLException {
        String sql = "INSERT INTO vets (first_name, last_name, specialty) VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vet.getFirstName());
            stmt.setString(2, vet.getLastName());
            stmt.setString(3, vet.getSpecialty());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    vet.setId(rs.getInt("id"));
                }
            }
        }
        return vet;
    }

    public void update(Vet vet) throws SQLException {
        String sql = "UPDATE vets SET first_name = ?, last_name = ?, specialty = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vet.getFirstName());
            stmt.setString(2, vet.getLastName());
            stmt.setString(3, vet.getSpecialty());
            stmt.setInt(4, vet.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM vets WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Vet mapResultSetToVet(ResultSet rs) throws SQLException {
        Vet vet = new Vet();
        vet.setId(rs.getInt("id"));
        vet.setFirstName(rs.getString("first_name"));
        vet.setLastName(rs.getString("last_name"));
        vet.setSpecialty(rs.getString("specialty"));
        vet.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return vet;
    }
}
