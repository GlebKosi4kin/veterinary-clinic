package com.petclinic.dao;

import com.petclinic.model.PatientCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientCardDao {

    public List<PatientCard> findAll() throws SQLException {
        List<PatientCard> patientCards = new ArrayList<>();
        String sql = "SELECT * FROM patient_cards ORDER BY id";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patientCards.add(mapResultSetToPatientCard(rs));
            }
        }
        return patientCards;
    }

    public PatientCard findById(int id) throws SQLException {
        String sql = "SELECT * FROM patient_cards WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatientCard(rs);
                }
            }
        }
        return null;
    }

    public PatientCard findByPetId(int petId) throws SQLException {
        String sql = "SELECT * FROM patient_cards WHERE pet_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, petId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatientCard(rs);
                }
            }
        }
        return null;
    }

    public PatientCard create(PatientCard patientCard) throws SQLException {
        String sql = "INSERT INTO patient_cards (pet_id, notes) VALUES (?, ?) RETURNING id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientCard.getPetId());
            stmt.setString(2, patientCard.getNotes());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    patientCard.setId(rs.getInt("id"));
                }
            }
        }
        return patientCard;
    }

    public void update(PatientCard patientCard) throws SQLException {
        String sql = "UPDATE patient_cards SET notes = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientCard.getNotes());
            stmt.setInt(2, patientCard.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM patient_cards WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private PatientCard mapResultSetToPatientCard(ResultSet rs) throws SQLException {
        PatientCard patientCard = new PatientCard();
        patientCard.setId(rs.getInt("id"));
        patientCard.setPetId(rs.getInt("pet_id"));
        patientCard.setNotes(rs.getString("notes"));
        patientCard.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return patientCard;
    }
}
