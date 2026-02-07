package com.petclinic.controller;

import com.petclinic.dao.PetDao;
import com.petclinic.model.Pet;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

public class PetController {
    private final PetDao petDao = new PetDao();

    public void getAll(Context ctx) {
        try {
            String ownerIdParam = ctx.queryParam("ownerId");
            List<Pet> pets;

            if (ownerIdParam != null) {
                int ownerId = Integer.parseInt(ownerIdParam);
                pets = petDao.findByOwnerId(ownerId);
            } else {
                pets = petDao.findAll();
            }

            ctx.json(pets);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при выводе питомцев: " + e.getMessage());
        }
    }

    public void getById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Pet pet = petDao.findById(id);
            if (pet != null) {
                ctx.json(pet);
            } else {
                ctx.status(404).result("Питомец не найден");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при выводе питомца: " + e.getMessage());
        }
    }

    public void create(Context ctx) {
        try {
            Pet pet = ctx.bodyAsClass(Pet.class);
            Pet created = petDao.create(pet);
            ctx.status(201).json(created);
        } catch (SQLException e) {
            ctx.status(500).result("шибка при создании питомца: " + e.getMessage());
        }
    }

    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Pet pet = ctx.bodyAsClass(Pet.class);
            pet.setId(id);
            petDao.update(pet);
            ctx.status(200).json(pet);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при обовлении питомца: " + e.getMessage());
        }
    }

    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            petDao.delete(id);
            ctx.status(204);
        } catch (SQLException e) {
            ctx.status(500).result("Ошибка при удалении питомца: " + e.getMessage());
        }
    }
}
