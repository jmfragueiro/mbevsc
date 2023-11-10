-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
drop table sg_grupo cascade constraints;

create table sg_grupo
(
  id                integer                            not null,
  name              varchar2(255 byte)                 not null,
  fechaumod         timestamp          default systimestamp not null,
  fechabaja         timestamp,
  userumod          varchar2(32 byte)                  not null
)
logging 
nocompress 
nocache;

create unique index pk_sg_grupo on sg_grupo
(id)
logging;

alter table sg_grupo add (
  constraint pk_sg_grupo
  primary key
  (id)
  using index pk_sg_grupo
  enable validate);

drop sequence seq_sg_grupo;

create sequence seq_sg_grupo
    start with 6000
    maxvalue 9999999999999999999999999999
    minvalue 1
    nocycle
    nocache
    order;

create or replace trigger
    trg_audit_sg_grupo
    before update or delete
    on sg_grupo
    for each row
declare
    l_tabla number(4) := 200;
begin
    if updating then
        insert into ut_audit
        (fuente, fecha, audtx, fechaumod, userumod)
        values (l_tabla
               , systimestamp
               , json_object('id'      is :old.id
                    ,'name'            is :old.name
                    ,'fechabaja'       is :old.fechabaja
                    absent on null)
               , :old.fechaumod
               , :old.userumod);
    else
        raise_application_error(-20001, 'NO PUEDE ELIMINARSE UN REGISTRO');
    end if;
end;

insert into sg_grupo
(id, name, fechaumod, userumod)
values
    (1, 'GENERAL', systimestamp, 'INSTALL');
insert into sg_grupo
(id, name, fechaumod, userumod)
values
    (2, 'SUPERADMIN', systimestamp, 'INSTALL');
insert into sg_grupo
(id, name, fechaumod, userumod)
values
    (1000, 'PERSONA_CONSULTA', systimestamp, 'INSTALL');
insert into sg_grupo
(id, name, fechaumod, userumod)
values
    (1001, 'PERSONA_ADMIN', systimestamp, 'INSTALL');
insert into sg_grupo
(id, name, fechaumod, userumod)
values
    (3000, 'TRAMITE_CONSULTA', systimestamp, 'INSTALL');
insert into sg_grupo
(id, name, fechaumod, userumod)
values
    (3001, 'TRAMITE_ADMIN', systimestamp, 'INSTALL');
insert into sg_grupo
(id, name, fechaumod, userumod)
values
    (3002, 'TRAMITE_GESTOR', systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
drop table sg_modulo cascade constraints;

create table sg_modulo
(
  id                integer                            not null,
  descripcion       varchar2(32 byte)                  not null,
  prefijo           varchar2(8 byte)                   not null,
  fechaumod         timestamp          default systimestamp not null,
  fechabaja         timestamp,
  userumod          varchar2(32 byte)                  not null
)
logging 
nocompress 
nocache;

create unique index pk_sg_modulo on sg_modulo
(id)
logging;

alter table sg_modulo add (
  constraint pk_sg_modulo
  primary key
  (id)
  using index pk_sg_modulo
  enable validate);

drop sequence seq_sg_modulo;

create sequence seq_sg_modulo
    start with 9
    maxvalue 9999999999999999999999999999
    minvalue 1
    nocycle
    nocache
    order;

create or replace trigger
    trg_audit_sg_modulo
    before update or delete
    on sg_modulo
    for each row
declare
    l_tabla number(4) := 201;
begin
    if updating then
        insert into ut_audit
        (fuente, fecha, audtx, fechaumod, userumod)
        values (l_tabla
               , systimestamp
               , json_object('id'      is :old.id
                    ,'descripcion'     is :old.descripcion
                    ,'prefijo'         is :old.prefijo
                    ,'fechabaja'       is :old.fechabaja
                    absent on null)
               , :old.fechaumod
               , :old.userumod);
    else
        raise_application_error(-20001, 'NO PUEDE ELIMINARSE UN REGISTRO');
    end if;
end;

insert into sg_modulo
(id, descripcion, prefijo, fechaumod, userumod)
values
    (1, 'General', 'GE', systimestamp, 'INSTALL');
insert into sg_modulo
(id, descripcion, prefijo, fechaumod, userumod)
values
    (2, 'Personas', 'PE', systimestamp, 'INSTALL');
insert into sg_modulo
(id, descripcion, prefijo, fechaumod, userumod)
values
    (3, 'Dispositivos', 'DL', systimestamp, 'INSTALL');
insert into sg_modulo
(id, descripcion, prefijo, fechaumod, userumod)
values
    (4, 'Tramites', 'TR', systimestamp, 'INSTALL');
insert into sg_modulo
(id, descripcion, prefijo, fechaumod, userumod)
values
    (5, 'Recursos Humanos', 'RH', systimestamp, 'INSTALL');
insert into sg_modulo
(id, descripcion, prefijo, fechaumod, userumod)
values
    (6, 'Organigrama', 'OR', systimestamp, 'INSTALL');
insert into sg_modulo
(id, descripcion, prefijo, fechaumod, userumod)
values
    (7, 'Seguridad', 'SG', systimestamp, 'INSTALL');
insert into sg_modulo
(id, descripcion, prefijo, fechaumod, userumod)
values
    (8, 'Patrimonio', 'PA', systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
drop table sg_permiso cascade constraints;

create table sg_permiso
(
  id                integer                            not null,
  sg_modulo_id      integer                            not null,
  descripcion       varchar2(255 byte)                 not null,
  permiso           varchar2(255 byte)                 not null,
  path              number(1),
  fechaumod         timestamp          default systimestamp not null,
  fechabaja         timestamp,
  userumod          varchar2(32 byte)                  not null
)
logging 
nocompress 
nocache;

create unique index pk_sg_permiso on sg_permiso
(id)
logging;

alter table sg_permiso add (
  constraint pk_sg_permiso
  primary key
  (id)
  using index pk_sg_permiso
  enable validate);

alter table sg_permiso add (
  constraint fk_sg_permiso_modulo
  foreign key (sg_modulo_id)
  references sg_modulo (id)
  enable validate);

drop sequence seq_sg_permiso;

create sequence seq_sg_permiso
    start with 100000
    maxvalue 9999999999999999999999999999
    minvalue 1
    nocycle
    nocache
    order;

create or replace trigger
    trg_audit_sg_permiso
    before update or delete
    on sg_permiso
    for each row
declare
    l_tabla number(4) := 202;
begin
    if updating then
        insert into ut_audit
        (fuente, fecha, audtx, fechaumod, userumod)
        values (l_tabla
               , systimestamp
               , json_object('id'     is :old.id
                    ,'sg_modulo_id'   is :old.sg_modulo_id
                    ,'descripcion'    is :old.descripcion
                    ,'permiso'        is :old.permiso
                    ,'path'           is :old.path
                    ,'fechabaja'      is :old.fechabaja
                    absent on null)
               , :old.fechaumod
               , :old.userumod);
    else
        raise_application_error(-20001, 'NO PUEDE ELIMINARSE UN REGISTRO');
    end if;
end;

insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (1, 1, 'Acerca de', '/acercade', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (2, 2, 'Consulta de Personas', '/persona', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (3, 5, 'Consulta de Agentes', '/rrhh', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4, 4, 'Consulta de Tramites', '/tramite', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (11, 1, 'Parametrizacion - Localidad / Provi / Pais', '/parametrizacion/localizacion', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (12, 1, 'Parametrizacion - Feriados', '/parametrizacion/feriado', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (13, 2, 'Parametrizacion - Nivel Acad�mico', '/parametrizacion/nivelacademico', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (20, 7, 'Consulta de Usuarios', '/usuario', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (21, 4, 'Tramites - Tipos', '/tramite/tramitetipo', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (22, 4, 'Tramites - Temas', '/tramite/tema', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (23, 4, 'Tramites - Bandeja', '/tramite/bandeja', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (24, 4, 'Tramites - Usuarios por Area', '/tramite/trareausuario', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (25, 4, 'Tramites - Permisos', '/tramite/permiso', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (26, 4, 'Tramites - Periodos', '/tramite/periodo', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (27, 4, 'Tramites - Organismos', '/tramite/organismo', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (28, 4, 'Tramites - Centros de Numeracion', '/tramite/centronumeracion', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (29, 4, 'Tramites - Reserva', '/tramite/reserva', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (30, 3, 'Dispositivo Legal - Consulta', '/dispositivolegal', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (31, 3, 'Dispositivo Legal - Temas', '/dispositivolegal/tema', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (32, 4, 'Tramites - Anexar', '/tramite/anexar', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (33, 4, 'Nuevo Tramite Alternativo', '/tramite/nuevotramitealternativo', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (43, 1, 'Manuales del Sistema', '/soporte/manuales', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (44, 2, 'Persona - Tomar Fotografia', '/gestionfoto/2', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (45, 8, 'Patrimonio - Tomar Fotografia', '/gestionfoto/8', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (46, 4, 'Tramites - Seguimiento de Favoritos', '/tramite/favorito', 1,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (2000, 2, 'Personas Nuevo', 'PERSONA_NUEVO', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (2001, 2, 'Personas Grabar', 'PERSONA_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (2002, 2, 'Visualizar Auditoria', 'PERSONA_AUDITORIA_VER', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4000, 4, 'Tramite Grabar', 'TRAMITE_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4001, 4, 'Pase Recibir', 'TRAMITE_PASE_RECIBIR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4002, 4, 'Pase Enviar', 'TRAMITE_PASE_ENVIAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4003, 4, 'Pase Grabar', 'TRAMITE_PASE_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4004, 4, 'Asignacion de Usuarios por Area', 'TRAMITE_AREAUSUARIO_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4005, 4, 'Asignacion de Permisos por Usuario', 'TRAMITE_PERMISOUSUARIO_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4006, 4, 'Configuracion de Periodos', 'TRAMITE_PERIODO_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4007, 4, 'Configuracion de Organismos', 'TRAMITE_ORGANISMO_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4008, 4, 'Configuracion de Centro de Numeracion', 'TRAMITE_CENTRO_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4009, 4, 'Configuracion de Temas', 'TRAMITE_TEMA_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4010, 4, 'Configuracion de Tipos de Tramite', 'TRAMITE_TIPO_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4011, 4, 'Reserva de Tramites', 'TRAMITE_RESERVA_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4012, 4, 'Impresion de Caratula de Expedientes', 'TRAMITE_CARATULA_IMPRIMIR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4013, 4, 'ReImpresion de Caratula de Expedientes', 'TRAMITE_CARATULA_REIMPRIMIR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4014, 4, 'Impresion de Movimiento de Tramites', 'TRAMITE_IMPRIMIR_MOVIMIENTOS', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4015, 4, 'Registracion de Nueva Anotacion', 'TRAMITE_NOTA_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4016, 4, 'Documentacion Grabar', 'TRAMITE_DOCUMENTACION_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4017, 4, 'Visualizar Auditoria', 'TRAMITE_AUDITORIA_VER', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4018, 4, 'Imprimir Resumen de Pases', 'TRAMITE_PASE_IMPRIMIR_RESUMEN', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4019, 4, 'Imprimir Resumen de Bandeja', 'TRAMITE_BANDEJA_IMPRIMIR_MOVIMIENTOS', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4020, 4, 'Imprimir Resumen de Lot de Pases', 'TRAMITE_PASE_IMPRIMIR_LOTE', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4021, 4, 'Anexar Nuevo Tramite', 'TRAMITE_ANEXAR_NUEVO', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4022, 4, 'Quitar Condicion de Anexado de un Tramite', 'TRAMITE_ANEXAR_BORRAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4023, 4, 'Tramite Grabar', 'TRAMITE_ALTERNATIVO_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (4024, 4, 'Favoritos Grabar', 'TRAMITE_FAVORITO_GRABAR', 0,
     systimestamp, 'INSTALL');
insert into sg_permiso
(id, sg_modulo_id, descripcion, permiso, path,
 fechaumod, userumod)
values
    (90000, 7, 'Usuario Grabar', 'USUARIO_GRABAR', 0,
     systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
drop table sg_user cascade constraints;

create table sg_user
(
  id                  integer                             not null,
  pe_persona_id       integer,
  username            varchar2(255 byte)                  not null,
  email               varchar2(255 byte)                  not null,
  enabled             number(1),
  password            varchar2(255 byte),
  last_login          timestamp,
  locked              number(1),
  expired             number(1),
  expires_at          date,
  tokenresetpassword  varchar2(128 byte),
  foto                varchar2(64 byte),
  emailcheck_at       timestamp,
  emailchecktoken     varchar2(128 byte),
  fechaumod           timestamp          default systimestamp not null,
  fechabaja           timestamp,
  userumod            varchar2(32 byte)                   not null
)
logging 
nocompress 
nocache;

create unique index pk_sg_user on sg_user
(id)
logging;
create unique index uk_sg_user_email on sg_user
(email)
logging;
create unique index uk_sg_user_pepersona on sg_user
(pe_persona_id)
logging;
create unique index uk_sg_user_username on sg_user
(username)
logging;

alter table sg_user add (
  constraint pk_sg_user
  primary key
  (id)
  using index pk_sg_user
  enable validate);

drop sequence seq_sg_user;

create sequence seq_sg_user
    start with 1
    maxvalue 9999999999999999999999999999
    minvalue 1
    nocycle
    nocache
    order;

create or replace trigger
    trg_audit_sg_user
    before update or delete
    on sg_user
    for each row
declare
    l_tabla number(4) := 203;
begin
    if updating then
        insert into ut_audit
        (fuente, fecha, audtx, fechaumod, userumod)
        values (l_tabla
               , systimestamp
               , json_object('id'         is :old.id
                    ,'pe_persona_id'      is :old.pe_persona_id
                    ,'username'           is :old.username
                    ,'email'              is :old.email
                    ,'enabled'            is :old.enabled
                    ,'password'           is :old.password
                    ,'last_login'         is :old.last_login
                    ,'locked'             is :old.locked
                    ,'expired'            is :old.expired
                    ,'expires_at'         is :old.expires_at
                    ,'tokenresetpassword' is :old.tokenresetpassword
                    ,'foto'               is :old.foto
                    ,'emailcheck_at'      is :old.emailcheck_at
                    ,'emailchecktoken'    is :old.emailchecktoken
                    ,'fechabaja'          is :old.fechabaja
                     absent on null)
               , :old.fechaumod
               , :old.userumod);
    else
        raise_application_error(-20001, 'NO PUEDE ELIMINARSE UN REGISTRO');
    end if;
end;

insert into sg_user
(id, username, email, enabled, password,
 last_login, locked, expired, expires_at,
 fechaumod, emailcheck_at, userumod)
values
    (seq_sg_user.nextval, 'admin', 'administrador@posadas.gov.ar', 1,
     '$2a$10$WDPZSRdLoH7pDAd6IP8pY.Dj4LBpawEgNbaqt7dIafmf788aPwgsS',
     null, 0, 0, systimestamp, systimestamp, systimestamp, 'INSTALL');
insert into sg_user
(id, username, email, enabled, password,
 last_login, locked, expired, expires_at,
 fechaumod, emailcheck_at, userumod)
values
    (seq_sg_user.nextval, 'lozano', 'lozano@gmail.com', 1,
     '$2a$10$WDPZSRdLoH7pDAd6IP8pY.Dj4LBpawEgNbaqt7dIafmf788aPwgsS',
     null, 0, 0, sysdate, systimestamp, systimestamp, 'INSTALL');
insert into sg_user
(id, username, email, enabled, password,
 last_login, locked, expired, expires_at,
 fechaumod, emailcheck_at, userumod)
values
    (seq_sg_user.nextval, 'efigureti', 'efigureti@hotmail.com', 1,
     '$2a$10$WDPZSRdLoH7pDAd6IP8pY.Dj4LBpawEgNbaqt7dIafmf788aPwgsS',
     null, 0, 1, sysdate, systimestamp, systimestamp, 'INSTALL');
insert into sg_user
(id, username, email, enabled, password,
 last_login, locked, expired, expires_at,
 fechaumod, emailcheck_at, userumod)
values
    (seq_sg_user.nextval, 'dkokoa', 'dkokoa@gmail.com', 1,
     '$2a$10$WDPZSRdLoH7pDAd6IP8pY.Dj4LBpawEgNbaqt7dIafmf788aPwgsS',
     null, 0, 1, sysdate, systimestamp, systimestamp, 'INSTALL');
insert into sg_user
(id, username, email, enabled, password,
 last_login, locked, expired, expires_at,
 fechaumod, emailcheck_at, userumod)
values
    (seq_sg_user.nextval, 'jmfragueiro', 'jmfragueiro@hotmail.com', 1,
     '$2a$10$WDPZSRdLoH7pDAd6IP8pY.Dj4LBpawEgNbaqt7dIafmf788aPwgsS',
     null, 0, 0, sysdate, systimestamp, systimestamp, 'INSTALL');
insert into sg_user
(id, username, email, enabled, password,
 last_login, locked, expired, expires_at,
 fechaumod, emailcheck_at, userumod)
values
    (seq_sg_user.nextval, 'dapereira', 'dapereira@gmail.com', 1,
     '$2a$10$WDPZSRdLoH7pDAd6IP8pY.Dj4LBpawEgNbaqt7dIafmf788aPwgsS',
     null, 0, 1, sysdate, systimestamp, systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
drop table sg_user_grupo cascade constraints;

create table sg_user_grupo
(
  id                integer                   not null,
  sg_user_id        integer,
  sg_grupo_id       integer,
  fechaumod         timestamp          default systimestamp not null,
  fechabaja         timestamp,
  userumod          varchar2(32 byte)         not null
)
logging 
nocompress 
nocache;

create unique index pk_sg_user_grupo on sg_user_grupo
(id)
logging;

create unique index uk_sg_user_grupo on sg_user_grupo
(sg_user_id, sg_grupo_id)
logging;

alter table sg_user_grupo add (
  constraint pk_sg_user_grupo
  primary key
  (id)
  using index pk_sg_user_grupo
  enable validate);

create index ix_sg_user_tokencm on sg_user
(emailchecktoken)
logging;

create index ix_sg_user_tokenrp on sg_user
(tokenresetpassword)
logging;

alter table sg_user_grupo add (
   constraint fk_sg_user_grupo_grupo
   foreign key (sg_grupo_id)
   references sg_grupo (id)
   enable validate
,  constraint fk_sg_user_grupo_user
   foreign key (sg_user_id)
   references sg_user (id)
   enable validate);

drop sequence seq_sg_user_grupo;

create sequence seq_sg_user_grupo
    start with 20
    maxvalue 9999999999999999999999999999
    minvalue 1
    nocycle
    nocache
    order;

create or replace trigger
    trg_audit_sg_user_grupo
    before update or delete
    on sg_user_grupo
    for each row
declare
    l_tabla number(4) := 204;
begin
    if updating then
        insert into ut_audit
        (fuente, fecha, audtx, fechaumod, userumod)
        values (l_tabla
               , systimestamp
               , json_object('id'  is :old.id
                    ,'sg_user_id'  is :old.sg_user_id
                    ,'sg_grupo_id' is :old.sg_grupo_id
                    ,'fechabaja'   is :old.fechabaja
                    absent on null)
               , :old.fechaumod
               , :old.userumod);
    else
        raise_application_error(-20001, 'NO PUEDE ELIMINARSE UN REGISTRO');
    end if;
end;

insert into sg_user_grupo
(id, sg_user_id, sg_grupo_id, fechaumod, userumod)
values
    (1, 1, 2, systimestamp, 'INSTALL');
insert into sg_user_grupo
(id, sg_user_id, sg_grupo_id, fechaumod, userumod)
values
    (2, 2, 2, systimestamp, 'INSTALL');
insert into sg_user_grupo
(id, sg_user_id, sg_grupo_id, fechaumod, userumod)
values
    (4, 4, 2, systimestamp, 'INSTALL');
insert into sg_user_grupo
(id, sg_user_id, sg_grupo_id, fechaumod, userumod)
values
    (5, 3, 2, systimestamp, 'INSTALL');
insert into sg_user_grupo
(id, sg_user_id, sg_grupo_id, fechaumod, userumod)
values
    (6, 5, 2, systimestamp, 'INSTALL');
insert into sg_user_grupo
(id, sg_user_id, sg_grupo_id, fechaumod, userumod)
values
    (7, 6, 2, systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
drop table sg_grupo_permiso cascade constraints;

create table sg_grupo_permiso
(
  id                integer                   not null,
  sg_grupo_id       integer,
  sg_permiso_id     integer,
  fechaumod         timestamp          default systimestamp not null,
  fechabaja         timestamp,
  userumod          varchar2(32 byte)         not null
)
logging 
nocompress 
nocache;

create unique index pk_sg_grupo_permiso on sg_grupo_permiso
(id)
logging;
create unique index uk_sg_grupo_permiso_gp on sg_grupo_permiso
(sg_grupo_id, sg_permiso_id)
logging;

alter table sg_grupo_permiso add (
  constraint pk_sg_grupo_permiso
  primary key
  (id)
  using index pk_sg_grupo_permiso
  enable validate);

alter table sg_grupo_permiso add (
  constraint fk_sg_grupo_permiso_grupo
  foreign key (sg_grupo_id)
  references sg_grupo (id)
  enable validate
, constraint fk_sg_grupo_permiso_permiso
  foreign key (sg_permiso_id) 
  references sg_permiso (id)
  enable validate);

drop sequence seq_sg_grupo_permiso;

create sequence seq_sg_grupo_permiso
  start with 50
  maxvalue 9999999999999999999999999999
  minvalue 1
  nocycle
  nocache
  order;

create or replace trigger
    trg_audit_sg_grupo_permiso
    before update or delete
    on sg_grupo_permiso
    for each row
declare
    l_tabla number(4) := 205;
begin
    if updating then
        insert into ut_audit
        (fuente, fecha, audtx, fechaumod, userumod)
        values (l_tabla
               , systimestamp
               , json_object('id'    is :old.id
                    ,'sg_grupo_id'   is :old.sg_grupo_id
                    ,'sg_permiso_id' is :old.sg_permiso_id
                    ,'fechabaja'     is :old.fechabaja
                    absent on null)
               , :old.fechaumod
               , :old.userumod);
    else
        raise_application_error(-20001, 'NO PUEDE ELIMINARSE UN REGISTRO');
    end if;
end;

insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (3, 2, 1, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (4, 2, 2, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (5, 2, 3, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (6, 2, 4, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (11, 2, 11, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (12, 2, 12, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (13, 2, 13, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (20, 2, 20, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (21, 2, 21, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (22, 2, 22, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (23, 2, 23, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (24, 2, 24, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (25, 2, 25, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (26, 2, 26, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (27, 2, 27, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (28, 2, 28, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (29, 2, 29, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (104, 1000, 2, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (105, 1001, 2, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (106, 1001, 13, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (124, 3000, 4, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (132, 3002, 23, systimestamp, 'INSTALL');
insert into sg_grupo_permiso
   (id, sg_grupo_id, sg_permiso_id, fechaumod, userumod)
 values
   (159, 3001, 4, systimestamp, 'INSTALL');
commit;

