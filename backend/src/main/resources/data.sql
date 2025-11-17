-- Sample data for testing

-- Patients
INSERT INTO patients (id, first_name, last_name, social_security_number, birth_date, active) VALUES
(1, 'Jean', 'Dupont', '1850567890123', '1985-05-15', true),
(2, 'Marie', 'Martin', '2920789012345', '1992-07-20', true),
(3, 'Pierre', 'Bernard', '1780234567890', '1978-02-10', false),
(4, 'Sophie', 'Dubois', '1990456789012', '1990-11-25', true),
(5, 'Luc', 'Thomas', '1887123456789', '1988-07-30', true);

-- Prescriptions (using different time scenarios for testing timezone bug)
-- Prescription issued yesterday at 23:30 Paris time (should be valid today but bug causes it to fail)
INSERT INTO prescriptions (id, patient_id, issued_at, expires_at, doctor_name, left_eye_sphere, right_eye_sphere, notes) VALUES
(1, 1, CURRENT_TIMESTAMP - INTERVAL '1 day' - INTERVAL '30 minutes', CURRENT_TIMESTAMP + INTERVAL '365 days', 'Dr. Lefebvre', -2.5, -2.75, 'Standard progressive lenses'),
-- Prescription issued today at 00:15 (edge case for timezone conversion)
(2, 2, CURRENT_TIMESTAMP - INTERVAL '12 hours', CURRENT_TIMESTAMP + INTERVAL '180 days', 'Dr. Moreau', -1.5, -1.25, 'Anti-blue light coating recommended'),
-- Valid prescription issued 30 days ago
(3, 4, CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP + INTERVAL '335 days', 'Dr. Lefebvre', -3.0, -3.25, 'High index lenses'),
-- Expired prescription
(4, 3, CURRENT_TIMESTAMP - INTERVAL '400 days', CURRENT_TIMESTAMP - INTERVAL '35 days', 'Dr. Simon', -1.75, -2.0, 'Expired'),
-- Future prescription (not yet valid)
(5, 5, CURRENT_TIMESTAMP + INTERVAL '2 days', CURRENT_TIMESTAMP + INTERVAL '367 days', 'Dr. Laurent', -2.25, -2.5, 'Future dated');

-- Orders (for testing N+1 query performance)
INSERT INTO orders (id, patient_id, prescription_id, created_at, status, discount_code) VALUES
(1, 1, 1, CURRENT_TIMESTAMP - INTERVAL '5 days', 'COMPLETED', 'SUMMER2024'),
(2, 2, 2, CURRENT_TIMESTAMP - INTERVAL '3 days', 'PROCESSING', NULL),
(3, 4, 3, CURRENT_TIMESTAMP - INTERVAL '2 days', 'COMPLETED', 'VIP10'),
(4, 1, 1, CURRENT_TIMESTAMP - INTERVAL '1 day', 'PENDING', NULL),
(5, 5, NULL, CURRENT_TIMESTAMP - INTERVAL '10 days', 'COMPLETED', 'WELCOME'),
(6, 2, 2, CURRENT_TIMESTAMP - INTERVAL '20 days', 'COMPLETED', NULL),
(7, 4, 3, CURRENT_TIMESTAMP - INTERVAL '15 days', 'CANCELLED', NULL);

-- Order Items
INSERT INTO order_items (id, order_id, lens_type, quantity, unit_price) VALUES
-- Order 1
(1, 1, 'Progressive lenses', 1, 350.00),
(2, 1, 'Anti-reflective coating', 1, 75.00),
-- Order 2
(3, 2, 'Single vision lenses', 1, 150.00),
(4, 2, 'Blue light filter', 1, 50.00),
-- Order 3
(5, 3, 'Progressive lenses', 1, 380.00),
(6, 3, 'UV protection', 1, 60.00),
(7, 3, 'Scratch resistant coating', 1, 40.00),
-- Order 4
(8, 4, 'Bifocal lenses', 1, 280.00),
-- Order 5
(9, 5, 'Reading glasses', 1, 120.00),
(10, 5, 'Case and cleaning kit', 1, 25.00),
-- Order 6
(11, 6, 'Progressive lenses', 1, 350.00),
(12, 6, 'Photochromic coating', 1, 120.00),
-- Order 7
(13, 7, 'Sports lenses', 1, 200.00);

-- Reset sequences
ALTER SEQUENCE patients_id_seq RESTART WITH 6;
ALTER SEQUENCE prescriptions_id_seq RESTART WITH 6;
ALTER SEQUENCE orders_id_seq RESTART WITH 8;
ALTER SEQUENCE order_items_id_seq RESTART WITH 14;

