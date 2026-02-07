package com.petclinic.controller;

import com.petclinic.dao.PatientCardDao;
import com.petclinic.model.PatientCard;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

public class PatientCardController {
    private final PatientCardDao patientCardDao = new PatientCardDao();

    public void getAll(Context ctx) {
        try {
            List<PatientCard> patientCards = patientCardDao.findAll();
            ctx.json(patientCards);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при выводе карточек пациентов: " + e.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PatientCard patientCard = patientCardDao.findById(id);
            if (patientCard != null) {
                ctx.json(patientCard);
            } else {
                ctx.status(404).result("Карта пациента не найдена");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при выводе карточки пациента: " + e.getMessage());
        }
    }

    public void getByPetId(Context ctx) {
        try {
            int petId = Integer.parseInt(ctx.queryParam("petId"));
            PatientCard patientCard = patientCardDao.findByPetId(petId);
            if (patientCard != null) {
                ctx.json(patientCard);
            } else {
                ctx.status(404).result("Не найдена карточка пациента");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при выводе карточки пациента: " + e.getMessage());
        }
    }

    public void create(Context ctx) {
        try {
            PatientCard patientCard = ctx.bodyAsClass(PatientCard.class);
            PatientCard created = patientCardDao.create(patientCard);
            ctx.status(201).json(created);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при создании карточки пациента: " + e.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PatientCard patientCard = ctx.bodyAsClass(PatientCard.class);
            patientCard.setId(id);
            patientCardDao.update(patientCard);
            ctx.status(200).json(patientCard);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при обновлении карточки пациента: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            patientCardDao.delete(id);
            ctx.status(204);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при удалении карточки пациента: " + e.getMessage());
        }
    }
}
