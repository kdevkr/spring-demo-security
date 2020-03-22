INSERT INTO users (id, password, name, role) VALUES ('admin', '{bcrypt}$2a$12$ry/T4SyQyiNpaWbadf9sne3Cko..q92Oh2klkCMv4XB1qG6cy8iaG', 'admin', 'ADMIN');
INSERT INTO users (id, password, name, role) VALUES ('user', '{bcrypt}$2a$12$ry/T4SyQyiNpaWbadf9sne3Cko..q92Oh2klkCMv4XB1qG6cy8iaG', 'user', 'USER');

INSERT INTO users_authority (user_id, authority)
SELECT user_id, 'REPOSITORY_CREATE' FROM users where id = 'user' ON CONFLICT (user_id, authority) DO NOTHING;