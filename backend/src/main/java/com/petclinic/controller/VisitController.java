package com.petclinic.controller;

import com.petclinic.dao.VisitDao;
import com.petclinic.model.Visit;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

public class VisitController {
    private final VisitDao visitDao = new VisitDao();

    public void getAll(Context ctx) {
        try {
            String petIdParam = ctx.queryParam("petId");
            List<Visit> visits;

            if (petIdParam != null) {
                int petId = Integer.parseInt(petIdParam);
                visits = visitDao.findByPetId(petId);
            } else {
                visits = visitDao.findAll();
            }

            ctx.json(visits);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при выводе визитов: " + e.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Visit visit = visitDao.findById(id);
            if (visit != null) {
                ctx.json(visit);
            } else {
                ctx.status(404).result("Визит не найден");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при выводе визита: " + e.getMessage());
        }
    }

    public void create(Context ctx) {
        try {
            Visit visit = ctx.bodyAsClass(Visit.class);
            Visit created = visitDao.create(visit);
            ctx.status(201).json(created);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при создании визита " + e.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Visit visit = ctx.bodyAsClass(Visit.class);
            visit.setId(id);
            visitDao.update(visit);
            ctx.status(200).json(visit);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при обновлении визита " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            visitDao.delete(id);
            ctx.status(204);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при удалении визита " + e.getMessage());
        }
    }
}
