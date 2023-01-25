INSERT INTO `user`(`id`, `username`, `first_name`, `last_name`, `email`, `password`, `phone_number`, `is_blocked`, `created_at`, `disabled_at`) VALUES (1, 'admin', 'first_name', 'last_name', 'admin@gmail.com', '$2a$10$PSN5G//C/8ouO5GzJbi84.uX0k7NYH65qENE2uL65MfKXRyRFYUAa', '0123456789', false, NOW(), null);
INSERT INTO `user_role`(`user_id`, `role`) VALUES (1, "ADMIN");
INSERT INTO `user_role`(`user_id`, `role`) VALUES (1, "USER");
