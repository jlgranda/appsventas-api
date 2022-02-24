--insert into APP_USER(ID, PASSWORD, USERNAME) values(1, '$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G', 'svlada@gmail.com');
--insert into USER_ROLE(APP_USER_ID, ROLE) values(1, 'ADMIN');
--insert into USER_ROLE(APP_USER_ID, ROLE) values(1, 'PREMIUM_MEMBER');


-- INSERT INTO sc_comaco.usuario(
--	usr_id, usr_codigo, usr_descripcion, usr_estado, usr_reg_fecha, usr_act_fecha, usr_ip_host, usr_nombre, usr_orden, 
--	usr_prioridad, usr_tiempo_activacion, usr_tiempo_expiracion, usr_reg_usu, usr_act_usu, session_user_name, usr_uuid, 
--	usr_version, usr_eliminado, usr_fecha_eliminacion, usr_bio, unq_correo_electronico, usr_image, usr_password, usr_person_id, unq_username)
--	VALUES (1, '111111111', 'Usuario desarrollo', 11, now(), now(), '', 'Usuario desarrollo', 1,
--			1, null, null, null, null, null, 'uuid', 
--			1, false, null, 'Usuario para desarrollo', 'plataforma@comaco.gob.ec', null, '$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G', 999, 'plataforma');