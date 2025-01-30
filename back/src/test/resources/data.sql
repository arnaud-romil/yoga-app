INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');


INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES 
('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
('UserFirstName', 'UserLastName', false, 'user@test.com', '$2a$10$P9FDjmCXZD3hEDuoM4VAcuAFjtVb2jEsyPpLOWRKLg0wLIoVzUoyC'); 

INSERT INTO `SESSIONS` (name, description, date, teacher_id)
VALUES
('My Session', 'Session description', '2025-01-25 14:00:00', 1),
('Other Session', 'Other Session description', '2025-01-25 16:00:00', 1);