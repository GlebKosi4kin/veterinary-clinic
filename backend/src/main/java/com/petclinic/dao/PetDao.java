package com.petclinic.dao;

import com.petclinic.model.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDao {

    public List<Pet> findAll() throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets ORDER BY id";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pets.add(mapResultSetToPet(rs));
            }
        }
        return pets;
    }

    public Pet findById(int id) throws SQLException {
        String sql = "SELECT * FROM pets WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPet(rs);
                }
            }
        }
        return null;
    }

    public List<Pet> findByOwnerId(int ownerId) throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets WHERE owner_id = ? ORDER BY id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ownerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pets.add(mapResultSetToPet(rs));
                }
            }
        }
        return pets;
    }

    public Pet create(Pet pet) throws SQLException {
        String sql = "INSERT INTO pets (name, birth_date, type, owner_id) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pet.getName());
            stmt.setDate(2, Date.valueOf(pet.getBirthDate()));
            stmt.setString(3, pet.getType());
            stmt.setInt(4, pet.getOwnerId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pet.setId(rs.getInt("id"));
                }
            }
        }
        return pet;
    }

    public void update(Pet pet) throws SQLException {
        String sql = "UPDATE pets SET name = ?, birth_date = ?, type = ?, owner_id = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pet.getName());
            stmt.setDate(2, Date.valueOf(pet.getBirthDate()));
            stmt.setString(3, pet.getType());
            stmt.setInt(4, pet.getOwnerId());
            stmt.setInt(5, pet.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM pets WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Pet mapResultSetToPet(ResultSet rs) throws SQLException {
        Pet pet = new Pet();
        pet.setId(rs.getInt("id"));
        pet.setName(rs.getString("name"));
        pet.setBirthDate(rs.getDate("birth_date").toLocalDate());
        pet.setType(rs.getString("type"));
        pet.setOwnerId(rs.getInt("owner_id"));
        pet.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return pet;
    }
}
