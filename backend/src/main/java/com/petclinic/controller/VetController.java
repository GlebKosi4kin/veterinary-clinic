package com.petclinic.controller;

import com.petclinic.dao.VetDao;
import com.petclinic.model.Vet;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

public class VetController {
    private final VetDao vetDao = new VetDao();

    public void getAll(Context ctx) {
        try {
            List<Vet> vets = vetDao.findAll();
            ctx.json(vets);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при выводе ветеринаров: " + e.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Vet vet = vetDao.findById(id);
            if (vet != null) {
                ctx.json(vet);
            } else {
                ctx.status(404).result("Ветеринар не найжен");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при выводе ветеринара: " + e.getMessage());
        }
    }

    public void create(Context ctx) {
        try {
            Vet vet = ctx.bodyAsClass(Vet.class);
            Vet created = vetDao.create(vet);
            ctx.status(201).json(created);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при создании ветеринара: " + e.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Vet vet = ctx.bodyAsClass(Vet.class);
            vet.setId(id);
            vetDao.update(vet);
            ctx.status(200).json(vet);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при обновлении ветеринара:  " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            vetDao.delete(id);
            ctx.status(204);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при удалении ветеринара:  " + e.getMessage());
        }
    }
}
