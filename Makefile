.PHONY: help build up down logs clean restart

help:
	@echo "Pet Clinic Management Commands:"
	@echo "  make build    - Build all Docker containers"
	@echo "  make up       - Start all services"
	@echo "  make down     - Stop all services"
	@echo "  make logs     - Show logs from all services"
	@echo "  make clean    - Stop services and remove volumes"
	@echo "  make restart  - Restart all services"

build:
	docker-compose build

up:
	docker-compose up -d
	@echo "Services started!"
	@echo "Frontend: http://localhost:3000"
	@echo "Backend API: http://localhost:7070/api"

down:
	docker-compose down

logs:
	docker-compose logs -f

clean:
	docker-compose down -v
	@echo "All services stopped and volumes removed"

restart:
	docker-compose restart
