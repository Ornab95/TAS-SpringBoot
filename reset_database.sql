-- Reset TAS Database for Fresh Deployment
-- This script drops all tables and recreates the database

USE tasdb;

-- Disable foreign key checks to avoid constraint errors
SET FOREIGN_KEY_CHECKS = 0;

-- Drop all tables if they exist
DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS interviews;
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS job_requisitions;
DROP TABLE IF EXISTS users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- The tables will be automatically recreated by Hibernate when the application starts
-- with the correct schema based on the JPA entities

SELECT 'Database reset complete. All tables dropped.' as Status;