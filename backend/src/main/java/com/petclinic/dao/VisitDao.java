package com.petclinic.dao;

import com.petclinic.model.Visit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitDao {

    public List<Visit> findAll() throws SQLException {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits ORDER BY visit_date DESC";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                visits.add(mapResultSetToVisit(rs));
            }
        }
        return visits;
    }

    public Visit findById(int id) throws SQLException {
        String sql = "SELECT * FROM visits WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVisit(rs);
                }
            }
        }
        return null;
    }

    public List<Visit> findByPetId(int petId) throws SQLException {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits WHERE pet_id = ? ORDER BY visit_date DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, petId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    visits.add(mapResultSetToVisit(rs));
                }
            }
        }
        return visits;
    }

    public Visit create(Visit visit) throws SQLException {
        String sql = "INSERT INTO visits (visit_date, description, pet_id, vet_id) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(visit.getVisitDate()));
            stmt.setString(2, visit.getDescription());
            stmt.setInt(3, visit.getPetId());
            stmt.setInt(4, visit.getVetId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    visit.setId(rs.getInt("id"));
                }
            }
        }
        return visit;
    }

    public void update(Visit visit) throws SQLException {
        String sql = "UPDATE visits SET visit_date = ?, description = ?, pet_id = ?, vet_id = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(visit.getVisitDate()));
            stmt.setString(2, visit.getDescription());
            stmt.setInt(3, visit.getPetId());
            stmt.setInt(4, visit.getVetId());
            stmt.setInt(5, visit.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM visits WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Visit mapResultSetToVisit(ResultSet rs) throws SQLException {
        Visit visit = new Visit();
        visit.setId(rs.getInt("id"));
        visit.setVisitDate(rs.getDate("visit_date").toLocalDate());
        visit.setDescription(rs.getString("description"));
        visit.setPetId(rs.getInt("pet_id"));
        visit.setVetId(rs.getInt("vet_id"));
        visit.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return visit;
    }
}
