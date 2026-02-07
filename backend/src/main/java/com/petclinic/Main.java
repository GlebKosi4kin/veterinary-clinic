package com.petclinic;

import com.petclinic.controller.OwnerController;
import com.petclinic.controller.PetController;
import com.petclinic.controller.VetController;
import com.petclinic.controller.VisitController;
import com.petclinic.controller.PatientCardController;
import com.petclinic.dao.Database;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {
        Database.init();

        OwnerController ownerController = new OwnerController();
        PetController petController = new PetController();
        VetController vetController = new VetController();
        VisitController visitController = new VisitController();
        PatientCardController patientCardController = new PatientCardController();

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        }).start(7070);

        app.get("/api/health", ctx -> ctx.result("OK"));

        app.get("/api/owners", ownerController::getAll);
        app.get("/api/owners/{id}", ownerController::getById);
        app.post("/api/owners", ownerController::create);
        app.put("/api/owners/{id}", ownerController::update);
        app.delete("/api/owners/{id}", ownerController::delete);

        app.get("/api/pets", petController::getAll);
        app.get("/api/pets/{id}", petController::getById);
        app.post("/api/pets", petController::create);
        app.put("/api/pets/{id}", petController::update);
        app.delete("/api/pets/{id}", petController::delete);

        app.get("/api/vets", vetController::getAll);
        app.get("/api/vets/{id}", vetController::getById);
        app.post("/api/vets", vetController::create);
        app.put("/api/vets/{id}", vetController::update);
        app.delete("/api/vets/{id}", vetController::delete);

        app.get("/api/visits", visitController::getAll);
        app.get("/api/visits/{id}", visitController::getById);
        app.post("/api/visits", visitController::create);
        app.put("/api/visits/{id}", visitController::update);
        app.delete("/api/visits/{id}", visitController::delete);

        app.get("/api/patient-cards", patientCardController::getAll);
        app.get("/api/patient-cards/{id}", patientCardController::getById);
        app.post("/api/patient-cards", patientCardController::create);
        app.put("/api/patient-cards/{id}", patientCardController::update);
        app.delete("/api/patient-cards/{id}", patientCardController::delete);

        System.out.println("Ветеринарная клиника работает в http://localhost:3000");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.stop();
            Database.close();
        }));
    }
}
