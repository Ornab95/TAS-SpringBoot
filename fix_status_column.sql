-- Fix the status column size to accommodate longer enum values
USE tasdb;

-- Alter the status column to be larger
ALTER TABLE applications MODIFY COLUMN status VARCHAR(50);

-- Update any existing data that might be truncated
UPDATE applications SET status = 'APPLIED' WHERE status = 'APPLIE';
UPDATE applications SET status = 'REVIEWED' WHERE status = 'REVIEWE';
UPDATE applications SET status = 'SHORTLISTED' WHERE status = 'SHORTLIS';
UPDATE applications SET status = 'REJECTED' WHERE status = 'REJECTE';