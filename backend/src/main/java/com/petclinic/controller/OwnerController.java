package com.petclinic.controller;

import com.petclinic.dao.OwnerDao;
import com.petclinic.model.Owner;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

public class OwnerController {
    private final OwnerDao ownerDao = new OwnerDao();

    public void getAll(Context ctx) {
        try {
            List<Owner> owners = ownerDao.findAll();
            ctx.json(owners);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка вывода владельцев: " + e.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Owner owner = ownerDao.findById(id);
            if (owner != null) {
                ctx.json(owner);
            } else {
                ctx.status(404).result("Владелец не найден");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка вывода владельца: " + e.getMessage());
        }
    }

    public void create(Context ctx) {
        try {
            Owner owner = ctx.bodyAsClass(Owner.class);
            Owner created = ownerDao.create(owner);
            ctx.status(201).json(created);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка создания владельца:" + e.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Owner owner = ctx.bodyAsClass(Owner.class);
            owner.setId(id);
            ownerDao.update(owner);
            ctx.status(200).json(owner);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка редактирования владельца:" + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            ownerDao.delete(id);
            ctx.status(204);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка удаления владельца: " + e.getMessage());
        }
    }
}
