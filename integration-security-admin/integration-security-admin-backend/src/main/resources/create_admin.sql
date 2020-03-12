insert into sec.permission(name, code) values('Пользователи', 'sec.admin.user') on conflict on constraint permission_pkey do nothing;
insert into sec.permission(name, code, parent_code) values('Просмотр пользователей', 'sec.admin.user.read', 'sec.admin.user') on conflict on constraint permission_pkey do nothing;
insert into sec.permission(name, code, parent_code) values('Добавление, редактирование и удаление пользователей', 'sec.admin.user.edit', 'sec.admin.user') on conflict on constraint permission_pkey do nothing;

insert into sec.permission(name, code) values('Роли', 'sec.admin.role') on conflict on constraint permission_pkey do nothing;
insert into sec.permission(name, code, parent_code) values('Просмотр ролей', 'sec.admin.role.read', 'sec.admin.role') on conflict on constraint permission_pkey do nothing;
insert into sec.permission(name, code, parent_code) values('Добавление, редактирование и удаление ролей', 'sec.admin.role.edit', 'sec.admin.role') on conflict on constraint permission_pkey do nothing;

insert into sec.permission(name, code) values('Системы', 'sec.admin.system') on conflict on constraint permission_pkey do nothing;
insert into sec.permission(name, code, parent_code) values('Просмотр систем', 'sec.admin.system.read', 'sec.admin.system') on conflict on constraint permission_pkey do nothing;
insert into sec.permission(name, code, parent_code) values('Добавление, редактирование и удаление систем', 'sec.admin.system.edit', 'sec.admin.system') on conflict on constraint permission_pkey do nothing;

insert into sec.role(name, code) select 'Администратор прав доступа', 'sec.admin' where not exists (select code from sec.role r where r.code = 'sec.admin');

insert into sec.role_permission(permission_code, role_id) values('sec.admin.user.read',(select id from sec.role where code='sec.admin')) on conflict on constraint role_permission_pk do nothing;
insert into sec.role_permission(permission_code, role_id) values('sec.admin.user.edit',(select id from sec.role where code='sec.admin')) on conflict on constraint role_permission_pk do nothing;
insert into sec.role_permission(permission_code, role_id) values('sec.admin.user',(select id from sec.role where code='sec.admin')) on conflict on constraint role_permission_pk do nothing;
insert into sec.role_permission(permission_code, role_id) values('sec.admin.role.read',(select id from sec.role where code='sec.admin')) on conflict on constraint role_permission_pk do nothing;
insert into sec.role_permission(permission_code, role_id) values('sec.admin.role.edit',(select id from sec.role where code='sec.admin')) on conflict on constraint role_permission_pk do nothing;
insert into sec.role_permission(permission_code, role_id) values('sec.admin.role',(select id from sec.role where code='sec.admin')) on conflict on constraint role_permission_pk do nothing;
insert into sec.role_permission(permission_code, role_id) values('sec.admin.system.read',(select id from sec.role where code='sec.admin')) on conflict on constraint role_permission_pk do nothing;
insert into sec.role_permission(permission_code, role_id) values('sec.admin.system.edit',(select id from sec.role where code='sec.admin')) on conflict on constraint role_permission_pk do nothing;
insert into sec.role_permission(permission_code, role_id) values('sec.admin.system',(select id from sec.role where code='sec.admin')) on conflict on constraint role_permission_pk do nothing;

INSERT INTO sec.user(id,username,name,email,is_active,password,ext_uid) VALUES (1,'admin','Администратор','aadmin@test.adm',TRUE,'$2a$10$Z.ZI8QDrgY/kkMI1NA9goeFsMid/tx0E5MI1I3vmRvUEi1elQQHMi','2ab6fe13-8f05-4c89-a6c8-bcfb9deeccf1');

INSERT INTO sec.user_role (user_id,role_id) VALUES (1,(SELECT id FROM sec.role WHERE code='sec.admin'));
