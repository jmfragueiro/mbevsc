-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
drop table ut_logging cascade constraints;

create table ut_logging
(
    codigo       varchar2(8)         not null,
    fecha        timestamp           not null,
    mensaje      varchar2(256 byte)  not null,
    descripcion  varchar2(4000 byte),
    sqlmsj       varchar2(2048 byte) not null
)
    logging
    nocompress
    nocache;

create or replace trigger
    trg_audit_ut_logging
    before update or delete
    on ut_logging
    for each row
begin
    raise_application_error(-20001, 'NO PUEDE MODIFICARSE UN REGISTRO DE AUDITORIA');
end;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
drop table ut_audit cascade constraints;

create table ut_audit
(
    fuente     number(4)           not null,
    fecha      timestamp           not null,
    audtx      varchar2(4000 byte) not null,
    fechaumod  timestamp           not null,
    userumod   varchar2(32 byte)   not null
)
    logging
    nocompress
    nocache;

create or replace trigger
    trg_audit_ut_audit
    before update or delete
    on ut_audit
    for each row
begin
    raise_application_error(-20001, 'NO PUEDE MODIFICARSE UN REGISTRO DE AUDITORIA');
end;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
drop table ut_parametro cascade constraints;

create table ut_parametro
(
  id                number              not null,
  tipo              number              default 0 not null,
  base              number,
  orden             number              default 0 not null,
  nombre            varchar2(64 byte)   not null,
  referencia        varchar2(16 byte),
  descripcion       varchar2(512 byte),
  valorbol          number(1)           default 0,
  valordat          date,
  valordob          float(126),
  valorint          number,
  valorstr          varchar2(255 byte),
  valorchr          varchar2(1 byte),
  valorimg          varchar2(255 byte),
  valortab          number,
  valorgeo          varchar2(21), -- ejemplo:'-27.568968,-54.684006'
  fechaumod         timestamp           default systimestamp not null,
  fechabaja         timestamp,
  userumod          varchar2(32 byte)   not null
)
logging 
nocompress 
nocache;

create unique index pk_ut_parametro on ut_parametro
(id)
logging;
create unique index uk_ut_parametro_tiponombre on ut_parametro
(tipo, nombre)
logging;
create unique index uk_ut_parametro_tipoorden on ut_parametro
(tipo, orden)
logging;
create unique index uk_ut_parametro_tiporef on ut_parametro
(tipo, referencia)
logging;

alter table ut_parametro add (
  constraint pk_ut_parametro
  primary key
  (id)
  using index pk_ut_parametro
  enable validate
,  constraint uk_ut_parametro_tiponombre
  unique (tipo, nombre)
  using index uk_ut_parametro_tiponombre
  enable validate
,  constraint uk_ut_parametro_tipoorden
  unique (tipo, orden)
  using index uk_ut_parametro_tipoorden
  enable validate
,  constraint uk_ut_parametro_tiporef
  unique (tipo, referencia)
  using index uk_ut_parametro_tiporef
  enable validate);

alter table ut_parametro add (
    constraint fk_ut_parametro_base
        foreign key (base)
            references ut_parametro (id)
                enable validate);

create or replace trigger
    trg_audit_ut_parametro
    before update or delete
    on ut_parametro
    for each row
declare
    l_tabla number(4) := 1;
begin
    if updating then
        if (:new.id != :old.id) or (:new.tipo != :old.tipo) then
            raise_application_error(-20002, 'NO PUEDE MODIFICARSE UN CAMPO DEL REGISTRO');
        end if;
        insert into ut_audit
        (fuente, fecha, audtx, fechaumod, userumod)
        values (l_tabla
               , systimestamp
               , json_object('id'      is :old.id
                    ,'tipo'            is :old.tipo
                    ,'base'            is :old.base
                    ,'nombre'          is :old.nombre
                    ,'orden'           is :old.orden
                    ,'referencia'      is :old.referencia
                    ,'descripcion'     is :old.descripcion
                    ,'valorbol'        is :old.valorbol
                    ,'valordat'        is :old.valordat
                    ,'valordob'        is :old.valordob
                    ,'valorint'        is :old.valorint
                    ,'valorstr'        is :old.valorstr
                    ,'valorchr'        is :old.valorchr
                    ,'valorimg'        is :old.valorimg
                    ,'valortab'        is :old.valortab
                    ,'valorgeo'        is :old.valorgeo
                    ,'fechabaja'       is :old.fechabaja
                     absent on null)
               , :old.fechaumod
               , :old.userumod);
    else
        raise_application_error(-20001, 'NO PUEDE ELIMINARSE UN REGISTRO');
    end if;
end;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
drop table ut_token cascade constraints;

create table ut_token
(
  id         number               not null,
  usuario    number               not null,
  token      varchar2(4000 byte)  not null,
  expired    number(1)            default 0                     not null,
  locked     number(1)            default 0                     not null,
  fechaumod  timestamp(6)         default systimestamp          not null,
  fechabaja  timestamp(6),
  userumod   varchar2(32 byte)    not null
)
logging 
nocompress 
nocache;


create unique index pk_ut_token on ut_token
(id)
logging;
create unique index uk_ut_token_token on ut_token
(token)
logging;

alter table ut_token add (
  constraint pk_ut_token
  primary key
  (id)
  using index pk_ut_token
  enable validate
,  constraint uk_ut_token_token
  unique (token)
  using index uk_ut_token_token
  enable validate);

drop sequence seq_ut_token;

create sequence seq_ut_token
  start with 1
  maxvalue 9999999999999999999999999999
  minvalue 1
  nocycle
  nocache
  order
  nokeep
  noscale
  global;

create or replace trigger
    trg_audit_ut_token
    before update or delete
    on ut_token
    for each row
declare
    l_tabla number(4) := 2;
begin
    if updating then
        insert into ut_audit
        (fuente, fecha, audtx, fechaumod, userumod)
        values (l_tabla
               , systimestamp
               , json_object('id'  is :old.id
                    ,'usuario'     is :old.usuario
                    ,'token'       is :old.token
                    ,'expired'     is :old.expired
                    ,'locked'      is :old.locked
                    absent on null)
               , :old.fechaumod
               , :old.userumod);
    else
        raise_application_error(-20001, 'NO PUEDE ELIMINARSE UN REGISTRO');
    end if;
end;

-----------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
create or replace package pk_utils as
-- #################################################################
-- ## package: pk_utils                                           ##
-- ## Este package contienen procedimientos y funciones comunes   ##
-- ## que puede ser utiles en diferentes contextos.               ##
-- ## @author: jmfragueiro                                        ##
-- ## @version: 20230728                                          ##
-- #################################################################
    --####################################################################
    --## constantes                                                     ##
    --####################################################################
    LGC_OFF	  constant varchar2(8) := 'OFF';   --No logging
    LGC_FATAL constant varchar2(8) := 'FATAL'; --The application is unusable. Action needs to be taken immediately.
    LGC_ERROR constant varchar2(8) := 'ERROR'; --An error occurred in the application.
    LGC_WARN  constant varchar2(8) := 'WARN';  --Something unexpected—though not necessarily an error—happened and needs to be watched.
    LGC_INFO  constant varchar2(8) := 'INFO';  --A normal, expected, relevant event happened.
    LGC_DEBUG constant varchar2(8) := 'DEBUG'; --Used for debugging purposes
    LGC_TRACE constant varchar2(8) := 'TRACE'; --Used for debugging purposes—includes the most detailed information

    --####################################################################
    --## procedimientos generales y comunes                             ##
    --####################################################################
    ---------------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------------
    -- procedimiento: logg                                                                     --
    -- este procedimiento se utiliza para logear inmediatamente un mensaje del sistema.        --
    -- @author: fito                                                                           --
    -- @version: 20230728                                                                      --
    -- parametros: p_codigo (varcahr2): codigo del mensaje, usar los LGC_XXXX de este pkg.     --
    --             p_msj (varcahr2): mensaje.                                                  --
    --             p_desc (varcahr2): una texto mas detallado del mensaje en caso de existir.  --
    --             p_sql (varcahr2): un texto sql en caso de existir.                          --
    ---------------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------------
    procedure logg(p_codigo varchar2, p_msj varchar2, p_desc varchar2, p_sql varchar2);

    ---------------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------------
    -- procedimiento: marklogg                                                                 --
    -- esta procedimiento se utiliza para registrar mensajes de logging pero con la posibilidad--
    -- de acumularlos sin generar una transacción, hasta que no se confirma con el parametro   --
    -- p_commit en true. Esto permite minimizar la cantidad de transacciones sobre todos en    --
    -- procesos masivos.                                                                       --
    -- @author: fito                                                                           --
    -- @version: 20230728                                                                      --
    -- parametros: p_codigo (varcahr2): codigo del mensaje, según lo definido en este package. --
    --             p_msj (varcahr2): mensaje.                                                  --
    --             p_desc (varcahr2): una texto mas detallado del mensaje en caso de existir.  --
    --             p_sql (varcahr2): un texto sql en caso de existir.                          --
    --             p_commit (boolean): si debe efectivamente insertar y comitear los mensajes. --
    ---------------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------------
    procedure marklogg(p_codigo varchar2, p_msj varchar2, p_desc varchar2, p_sql varchar2, p_commit boolean default false);
end;
/

create or replace package body pk_utils
as
    ------------------------------------------------------
    -- variables globales al package                    --
    ------------------------------------------------------
    type typ_tab_logging is
        table of ut_logging%rowtype
            index by binary_integer;

    gt_logging              typ_tab_logging;                 -- la tabla para acumular mensajes
    g_sqlerr                varchar2(512);                   -- una variable de soporte al logeo
    g_errind                binary_integer := 0;             -- el indice para la tabla de mensajes
    gc_errtot               constant binary_integer := 2000; -- maxima ctdad de mensajes sin confirmar

    g_exception             exception;

    ---------------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------------
    -- procedimiento: confirm_marklogg                                                         --
    -- este procedimiento se utiliza para descargar efectivamente los logs en la tabla logging --
    -- y posteriormente comitear. blanquea ademas la tabla de acumulacion y el reinicia indice.--
    -- @author: fito                                                                           --
    -- @version: 20230728                                                                      --
    ---------------------------------------------------------------------------------------------
    ---------------------------------------------------------------------------------------------
    procedure confirm_marklogg is
        pragma autonomous_transaction;
    begin
        -- se descarga la tabla de log primero y luego la blanquea
        forall ilog in 1..gt_logging.count insert into ut_logging values gt_logging(ilog);
        g_errind := 0;
        gt_logging.delete;
        commit;
    end;

    procedure marklogg(p_codigo varchar2, p_msj varchar2, p_desc varchar2, p_sql varchar2, p_commit boolean default false) is
    begin
        g_errind := g_errind + 1;
        gt_logging(g_errind).codigo      := p_codigo;
        gt_logging(g_errind).fecha       := systimestamp;
        gt_logging(g_errind).mensaje     := p_msj;
        gt_logging(g_errind).descripcion := p_desc;
        gt_logging(g_errind).sqlmsj      := p_sql;

        -- si debe descargar el log llama al procedimiento
        if (p_commit or (g_errind > gc_errtot)) then
            confirm_marklogg;
        end if;
    end;

    procedure logg(p_codigo varchar2, p_msj varchar2, p_desc varchar2, p_sql varchar2) is
        pragma autonomous_transaction;
    begin
        insert into ut_logging
        (codigo, fecha, mensaje, descripcion, sqlmsj)
        values (p_codigo, systimestamp, p_msj, p_desc, p_sql);
        commit;
    end;
end;
/


-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 0 - subtablas
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (1, 0, 1, 'TIPOS DE DOCUMENTO', 'tdoc', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (2, 0, 2, 'TIPOS DE CONTACTO', 'tcnt', '-- valorstr => PATRON EXPRESION REGULAR -- valorint => MAXIMA CANTIDAD (0 = ilimitada) -- valorbol => compuesto? (1=SI y tiene datos en t9)', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (3, 0, 3, 'ESTADOS CIVILES', 'esci', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (4, 0, 4, 'NIVELES DE ESTUDIO', 'estn', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (5, 0, 5, 'SUBTIPO DE CONTACTO', 'stct', '-- valorint => MAXIMA CANTIDAD (0 = ilimitada)', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (6, 0, 6, 'TIPOS DE DATO', 'tdat', '-- valorstr => PATRON EXPRESION REGULAR -- valorint => TAMAÑO MAXIMO (CARACTERES) -- valortab => subtabla con las opciones', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (7, 0, 7, 'PAISES', 'pais', '-- valorstr => Nacionalidad', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (8, 0, 8, 'PROVINCIAS', 'prov', '-- base => pais al que pertenece la provincia', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (9, 0, 9, 'DEPARTAMENTOS', 'dpto', '-- base => la provincia a la que pertenete el departamento ', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (10, 0, 10, 'LOCALIDADES', 'loca', '-- base => departamento al que pertenece -- valorbol => es municipio?', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (11, 0, 11, 'DATOS DE CONTACTO', 'dcto', '-- base => tipo de contacto al que pertenece -- valorint => tipo de dato -- valorbol => obligatorio?', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (12, 0, 12, 'TIPOS DE PERSONA JURIDICA', 'tppj', '-- valorbol => persona jurídica privada? (1=SI, 0=pública)', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (13, 0, 13, 'SUBTIPOS DE PERSONA JURIDICA', 'stpj', '-- base => tipo de pj al que pertenece', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (14, 0, 14, 'TIPOS DE FORMULARIO', 'tfrm', null, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (15, 0, 15, 'ESTADOS DE PERSONAS', 'estp', null, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, descripcion, fechaumod, userumod)
values (16, 0, 16, 'ESTADOS DE OBJETOS', 'esto', null, systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 1 - tipo documento
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (5001, 1, 1, 'Documento Nacional de Identidad', 'DNI', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (5002, 1, 2, 'Pasaporte', 'PASAPORT', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (5003, 1, 3, 'Cedula Extranjero', 'CI', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (5004, 1, 4, 'Codigo Unico de Identificacion Tributaria/Laboral', 'CUIT/CUIL', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (5005, 1, 5, 'Otro', 'OTRO', systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 2 - tipo contacto
-- valorstr => PATRON EXPRESION REGULAR
-- valorint => MAXIMA CANTIDAD (0 = ilimitada)
-- valorbol => compuesto? (1=SI y tiene datos en t9)
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, valorint, valorbol, fechaumod, userumod)
values (6001, 2, 1, 'TELEFONO FIJO', 'FIJO', '[0-9]{10}', 0, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, valorint, valorbol, fechaumod, userumod)
values (6002, 2, 2, 'CORREO ELECTRONICO', 'EMAIL', '[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$', 0, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, valorint, valorbol, fechaumod, userumod)
values (6003, 2, 3, 'CELULAR', 'CELU', '[0-9]{10}', 0, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (6004, 2, 4, 'FACEBOOK', 'FACE', 1, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (6005, 2, 5, 'TWITTER', 'TWIT', 1, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (6006, 2, 6, 'INSTAGRAM', 'INST', 1, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, valorint, valorbol, fechaumod, userumod)
values (6007, 2, 7, 'WEB', 'WEB', 'https?://.+', 0, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (6008, 2, 8, 'DOMICILIO', 'DOMI', 0, 1, systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--subtabla: 3 - estado civil
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (7001, 3, 1, 'Soltero', 'SOLT', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (7002, 3, 2, 'Casado', 'CASA', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (7003, 3, 3, 'Divorciado', 'DIVO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (7004, 3, 4, 'Viudo', 'VIUD', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (7005, 3, 5, 'Conviviente', 'CONV', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, fechaumod, userumod)
values (7006, 3, 6, 'Separado', 'SEPA', systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 4 - nivel estudio
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, fechaumod, userumod)
values (8001, 4, 1, 'Primario', 'PRIM', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, fechaumod, userumod)
values (8002, 4, 2, 'Secundario', 'SECU', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, fechaumod, userumod)
values (8003, 4, 3, 'Terciario', 'TERC', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, fechaumod, userumod)
values (8004, 4, 4, 'Universitario', 'UNIV', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, fechaumod, userumod)
values (8005, 4, 5, 'Posgrado', 'POSG', 2, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, fechaumod, userumod)
values (8006, 4, 6, 'Doctorado', 'DOCT', 2, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, fechaumod, userumod)
values (8007, 4, 7, 'Maestria', 'MAES', 2, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, fechaumod, userumod)
values (8008, 4, 8, 'Capacitaciones', 'CAPA', 99, systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 5 - subtipo_contacto
-- valorint => MAXIMA CANTIDAD (0 = ilimitada)
insert into ut_parametro
(id, tipo, orden, nombre, descripcion, referencia, valorint, fechaumod, userumod)
values (9001, 5, 1, 'COMERCIAL', 'CONTACTO COMERCIAL', 'COM', 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, descripcion, referencia, valorint, fechaumod, userumod)
values (9002, 5, 2, 'FISCAL', 'CONTACTO FISCAL', 'FIS', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, descripcion, referencia, valorint, fechaumod, userumod)
values (9003, 5, 3, 'LEGAL', 'CONTACTO LEGAL', 'LEG', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, descripcion, referencia, valorint, fechaumod, userumod)
values (9004, 5, 4, 'PARTICULAR', 'CONTACTO PAR', 'PARTICULAR', 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, descripcion, referencia, valorint, fechaumod, userumod)
values (9005, 5, 5, 'POSTAL', 'CONTACTO POSTAL', 'POS', 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, descripcion, referencia, valorint, fechaumod, userumod)
values (9006, 5, 6, 'ELECTRONICO', 'CONTACTO ELECTRONICO', 'ELE', 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, descripcion, referencia, valorint, fechaumod, userumod)
values (9007, 5, 7, 'REAL', 'CONTACTO REAL', 'REA', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, descripcion, referencia, valorint, fechaumod, userumod)
values (9008, 5, 8, 'EXTERIOR', 'CONTACTO EN EXTERIOR', 'EXT', 0, systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 6 - tipos_dato
-- valorstr => PATRON EXPRESION REGULAR
-- valorint => TAMAÑO MAXIMO (CARACTERES)
-- valortab => subtabla con las opciones
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorstr, fechaumod, userumod)
values (10001, 6, 1, 'DATO NUMERICO DECIMAL', 'NUMERICO', null, '[[:digit:]]+$', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorstr, fechaumod, userumod)
values (10002, 6, 2, 'DATO ALFANUMERICO CORTO', 'ALFANUM', 256,
        '^[a-zA-Z0-9\-#\.\(\)\/%&\s\_\*\$\;\,\[\]\{\}\'']*', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorstr, fechaumod, userumod)
values (10003, 6, 3, 'CADENA ALFANUMERICA', 'CADENA', 4000,
        '^[a-zA-Z0-9\-#\.\(\)\/%&\s\_\*\$\;\,\[\]\{\}\'']*', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorstr, fechaumod, userumod)
values (10004, 6, 4, 'DATO BOOLEANO', 'BOOLEAN', null, null, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorstr, fechaumod, userumod)
values (10005, 6, 5, 'FECHA (DD/MM/YYYY)', 'FECHA', null,
        '(([012][0-9])|([3][01]))/([0][1-9]|[1][012])/([1]([0-9]{3})|([2][0][0-9]{2}))', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorstr, fechaumod, userumod)
values (10006, 6, 6, 'FECHA Y HORA (DD/MM/RRRR HH24:MI:SS)', 'FECHAHORA', null,
        '(([012][0-9])|([3][01]))/([0][1-9]|[1][012])/([1]([0-9]{3})|([2][0][0-9]{2}))'||
        ' (([01][0-9])|([2][0-3]))(:(([0-5][0-9]))){2}',
        systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorstr, fechaumod, userumod)
values (10007, 6, 7, 'MARCA DE TIEMPO (DD/MM/RRRR HH24:MI:SS.FF)', 'TIMESTAMP', null,
        '(([012][0-9])|([3][01]))/([0][1-9]|[1][012])/([1]([0-9]{3})|([2][0][0-9]{2}))'||
        ' (([01][0-9])|([2][0-3]))(:(([0-5][0-9]))){2},[0-9]{6}',
        systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorstr, fechaumod, userumod)
values (10008, 6, 8, 'NUMERO DE COMERCIO', 'COMERCIO', null, '[0-9]{6}/[0-9]{2}', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorstr, fechaumod, userumod)
values (10009, 6, 9, 'CORREO ELECTRONICO', 'EMAIL', null,
        '[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorint, valorstr, valortab, fechaumod, userumod)
values (10010, 6, 10, 'LOCALIDAD', 'LOCAL', null, null, 9, systimestamp, 'INSTALL');
commit;


-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 7 - paises
-- valorstr => Nacionalidad
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11001, 7, 1, 'Argentina', 'AR', 'Argentina', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11002, 7, 2, 'Paraguay', 'PY', 'Paraguaya', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11003, 7, 3, 'Brasil', 'BR', 'Brasilera', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11004, 7, 4, 'Uruguay', 'UR', 'Uruguaya', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11005, 7, 5, 'Chile', 'CH', 'Chilena', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11006, 7, 6, 'Bolivia', 'BO', 'Boliviana', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11007, 7, 7, 'Alemania', 'AL', 'Alemana', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11008, 7, 8, 'Peru', 'PE', 'Paruana', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11009, 7, 9, 'Japon', 'JP', 'Japonesa', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11010, 7, 10, 'China', 'CN', 'China', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11011, 7, 11, 'España', 'ES', 'EspAñola', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11012, 7, 12, 'Francia', 'FR', 'Francesa', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11013, 7, 13, 'E.E.U.U.', 'EU', 'Estadounidense', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11014, 7, 14, 'Colombia', 'CO', 'Colombiana', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11015, 7, 15, 'Senegal', 'SG', 'Senegales', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11016, 7, 16, 'Venezuela', 'VZ', 'Venezolana', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, referencia, valorstr, fechaumod, userumod)
values (11017, 7, 17, 'Ecuador', 'EC', 'Ecuatoriana', systimestamp, 'INSTALL');
commit;


-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 8 - provincias
-- base => pais al que pertenece la provincia
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12001, 8, 11001, 1, 'Buenos Aires', 'BSAS', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12002, 8, 11001, 2, 'Ciudad de Buenos Aires', 'CABA', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12003, 8, 11001, 3, 'Catamarca', 'CATA', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12004, 8, 11001, 4, 'Chaco', 'CHAC', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12005, 8, 11001, 5, 'Chubut', 'CHUB', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12006, 8, 11001, 6, 'Cordoba', 'CBA', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12007, 8, 11001, 7, 'Corrientes', 'CTES', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12008, 8, 11001, 8, 'Entre Rios', 'ERIO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12009, 8, 11001, 9, 'Formosa', 'FORM', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12010, 8, 11001, 10, 'Jujuy', 'JUJU', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12011, 8, 11001, 11, 'La Pampa', 'LPAM', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12012, 8, 11001, 12, 'La Rioja', 'LRIO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12013, 8, 11001, 13, 'Mendoza', 'MZA', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12014, 8, 11001, 14, 'Misiones', 'MNES', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12015, 8, 11001, 15, 'Neuquen', 'NEUQ', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12016, 8, 11001, 16, 'Rio Negro', 'RNEG', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12017, 8, 11001, 17, 'Salta', 'SALT', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12018, 8, 11001, 18, 'San Juan', 'SJUA', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12019, 8, 11001, 19, 'San Luis', 'SLUI', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12020, 8, 11001, 20, 'Santa Cruz', 'SCRU', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12021, 8, 11001, 21, 'Santa Fe', 'SFE', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12022, 8, 11001, 22, 'Santiago del Estero', 'SEST', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12023, 8, 11001, 23, 'Tierra del Fuego', 'TFUE', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (12024, 8, 11001, 24, 'Tucuman', 'TUCU', systimestamp, 'INSTALL');
commit;


-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 9 - departamentos
-- base => la provincia a la que pertenete el departamento 
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13001, 9, 12014, 1, 'CAPITAL', 'CAPI', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13002, 9, 12014, 2, 'APOSTOLES', 'APOS', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13003, 9, 12014, 3, 'CANDELARIA', 'CAND', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13004, 9, 12014, 4, 'LEANDRO N.ALEM', 'LEAN', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13005, 9, 12014, 5, 'C. DE LA SIERRA', 'C.DE', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13006, 9, 12014, 6, 'SAN JAVIER', 'SANJ', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13007, 9, 12014, 7, 'SAN IGNACIO', 'SANI', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13008, 9, 12014, 8, 'OBERA', 'OBER', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13009, 9, 12014, 9, 'L.G. SAN MARTIN', 'L.G.', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13010, 9, 12014, 10, 'CAINGUAS', 'CAIN', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13011, 9, 12014, 11, '25 DE MAYO', '25DE', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13012, 9, 12014, 12, 'MONTECARLO', 'MONT', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13013, 9, 12014, 13, 'GUARANI', 'GUAR', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13014, 9, 12014, 14, 'ELDORADO', 'ELDO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13015, 9, 12014, 15, 'SAN PEDRO', 'SANP', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13016, 9, 12014, 16, 'IGUAZU', 'IGUA', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13017, 9, 12014, 17, 'G.M. BELGRANO', 'G.M.', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13018, 9, 12007, 18, 'ITUZAINGO', 'ITUZ', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (13019, 9, 12007, 19, 'SANTO TOME', 'STME', systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 10 - localidades
-- base => departamento al que pertenece
-- valorbol => es municipio?
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14001, 10, 13011, 1, 'ALBA POSSE', 'ALBA', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14002, 10, 13004, 2, 'ALMAFUERTE', 'ALMA', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14003, 10, 13006, 3, 'FLORENTINO AMEGHINO', 'FLOR', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14004, 10, 13002, 4, 'APOSTOLES', 'APOS', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14005, 10, 13010, 5, 'ARISTOBULO DEL VALLE', 'ARIS', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14006, 10, 13004, 6, 'ARROYO DEL MEDIO', 'ARRO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14007, 10, 13002, 7, 'AZARA', 'AZAR', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14008, 10, 13017, 8, 'BERNARDO DE IRIGOYEN', 'BERN', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14009, 10, 13003, 9, 'BONPLAND', 'BONP', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14010, 10, 13004, 10, 'CAA YARI', 'CAAY', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14011, 10, 13010, 11, 'CAMPO GRANDE', 'CAMP', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14012, 10, 13008, 12, 'CAMPO RAMON', 'CARM', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14013, 10, 13008, 13, 'CAMPO VIERA', 'CAVI', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14014, 10, 13003, 14, 'CANDELARIA', 'CAND', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14015, 10, 13009, 15, 'CAPIOVI', 'CAPI', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14016, 10, 13012, 16, 'CARAGUATAY', 'CARA', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14017, 10, 13004, 17, 'CERRO AZUL', 'CEAZ', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14018, 10, 13003, 18, 'CERRO CORA', 'CECO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14019, 10, 13008, 19, 'COLONIA ALBERDI', 'COAL', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14020, 10, 13011, 20, 'COLONIA AURORA', 'COAU', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14021, 10, 13014, 21, 'COLONIA DELICIA', 'CODE', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14022, 10, 13007, 22, 'COLONIA POLANA', 'COPO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14023, 10, 13014, 23, 'COLONIA VICTORIA', 'COVI', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14024, 10, 13016, 24, 'WANDA', 'WAND', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14025, 10, 13005, 25, 'CONCEPCION DE LA SIERRA', 'CONC', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14026, 10, 13007, 26, 'CORPUS', 'CORP', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14027, 10, 13004, 27, 'DOS ARROYOS', 'DOSA', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14028, 10, 13010, 28, 'DOS DE MAYO', 'DOSD', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14029, 10, 13009, 29, 'EL ALCAZAR', 'ELAL', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14030, 10, 13014, 30, 'ELDORADO', 'ELDO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14031, 10, 13013, 31, 'EL SOBERBIO', 'ELSO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14032, 10, 13016, 32, 'PUERTO ESPERANZA', 'PUER', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14033, 10, 13001, 33, 'FACHINAL', 'FACH', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14034, 10, 13009, 34, 'GARUHAPE', 'GARH', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14035, 10, 13001, 35, 'GARUPA', 'GARU', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14036, 10, 13008, 36, 'GENERAL ALVEAR', 'GEAL', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14037, 10, 13017, 37, 'GENERAL MANUEL BELGRANO', 'GEBE', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14038, 10, 13007, 38, 'GENERAL URQUIZA', 'GEUR', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14039, 10, 13004, 39, 'GOBERNADOR LOPEZ', 'GOLO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14040, 10, 13007, 40, 'GOBERNADOR ROCA', 'GORO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14041, 10, 13008, 41, 'GUARANI', 'GUAR', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14042, 10, 13007, 42, 'HIPOILTO IRIGOYEN', 'HIPO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14043, 10, 13016, 43, 'PUERTO IGUAZU', 'PUIG', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14044, 10, 13006, 44, 'ITACARUARE', 'ITAC', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14045, 10, 13007, 45, 'JARDIN AMERICA', 'JARD', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14046, 10, 13004, 46, 'LEANDRO N. ALEM', 'LEAN', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14047, 10, 13009, 47, 'PUERTO LEONI', 'PULE', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14048, 10, 13016, 48, 'PUERTO LIBERTAD', 'PULI', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14049, 10, 13003, 49, 'LORETO', 'LORE', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14050, 10, 13008, 50, 'LOS HELECHOS', 'LOSH', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14051, 10, 13003, 51, 'MARTIRES', 'MART', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14052, 10, 13006, 52, 'MOJON GRANDE', 'MOJO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14053, 10, 13012, 53, 'MONTECARLO', 'MONT', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14054, 10, 13014, 54, '9 DE JULIO', '9DEJ', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14055, 10, 13008, 55, 'OBERA', 'OBER', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14056, 10, 13004, 56, 'OLEGARIO VICTOR ANDRADE', 'OLEG', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14057, 10, 13008, 57, 'PANAMBI', 'PANA', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14058, 10, 13012, 58, 'PIRAY', 'PIRA', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14059, 10, 13001, 59, 'POSADAS', 'POSA', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14060, 10, 13003, 60, 'PROFUNDIDAD', 'PROF', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14061, 10, 13009, 61, 'PUERTO RICO', 'PURI', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14062, 10, 13009, 62, 'RUIZ DE MONTOYA', 'RUIZ', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14063, 10, 13014, 63, 'SANTIAGO DE LINIERS', 'SANT', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14064, 10, 13007, 64, 'SAN IGNACIO', 'SANI', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14065, 10, 13006, 65, 'SAN JAVIER', 'SAJA', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14066, 10, 13002, 66, 'SAN JOSE', 'SAJO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14067, 10, 13008, 67, 'SAN MARTIN', 'SANM', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14068, 10, 13015, 68, 'SAN PEDRO', 'SANP', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14069, 10, 13003, 69, 'SANTA ANA', 'SAAN', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14070, 10, 13005, 70, 'SANTA MARIA', 'SAMA', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14071, 10, 13007, 71, 'SANTO PIPO', 'SAPI', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14072, 10, 13002, 72, 'TRES CAPONES', 'TRES', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14073, 10, 13011, 73, '25 DE MAYO', '25DM', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14074, 10, 13013, 74, 'SAN VICENTE', 'SANV', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14075, 10, 13017, 75, 'COMANDANTE A. GUARCURARI', 'COGU', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14076, 10, 13002, 76, 'VILLA BONITA', 'VILL', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14077, 10, 13017, 77, 'ANDRESITO', 'ANDR', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14078, 10, 13015, 78, 'POZO AZUL', 'POZO', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (14079, 10, 13015, 79, 'FRACRAN', 'FRAC', 1, systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 11 - datos componentes de un tipo de contacto
-- base => tipo de contacto al que pertenece
-- valorint => tipo de dato
-- valorbol => obligatorio?
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (15001, 11, 8, 1, 'CALLE', 'CALL', 2, 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (15002, 11, 8, 2, 'NUMERO', 'NUME', 1, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (15003, 11, 8, 3, 'PISO', 'PISO', 1, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (15004, 11, 8, 4, 'APARTAMENTO', 'APTO', 2, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (15005, 11, 8, 5, 'SECTOR', 'SECT', 2, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (15006, 11, 8, 6, 'BARRIO', 'BARR', 2, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (15007, 11, 8, 7, 'ACLARACION EXTRA', 'EXTR', 2, 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorint, valorbol, fechaumod, userumod)
values (15008, 11, 8, 8, 'LOCALIDAD', 'LOCA', 10, 1, systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 12 - tipos de personas juridicas
-- valorbol => persona jurídica privada? (1=SI, 0=pública)
rollback;

insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16001, 12, 1, 'SOCIEDAD', 1, 'SOC', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16002, 12, 2, 'ASOCIACIÓN CIVIL', 1, 'ACIV', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16003, 12, 3, 'ASOCIACIÓN SIMPLE', 1, 'ASIM', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16004, 12, 4, 'FUNDACIÓN', 1, 'FUND', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16005, 12, 5, 'ENTIDAD RELIGIOSA', 1, 'RELIG', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16006, 12, 6, 'MUTUAL', 1, 'MUT', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16007, 12, 7, 'COOPERATIVA', 1, 'COOP', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16008, 12, 8, 'CONSORCIO PH', 1, 'CPH', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16009, 12, 9, 'ESTADO NACIONAL', 1, 'ENAC', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16010, 12, 10, 'ESTADO PROVINCIAL', 1, 'EPROV', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16011, 12, 11, 'CIUDAD AUTÓNOMA DE BUENOS AIRES', 1, 'CABA', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16012, 12, 12, 'MUNICIPIOS', 1, 'MUNI', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16013, 12, 13, 'ENTIDAD AUTÁRQUICA', 1, 'EAUT', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16014, 12, 14, 'ESTADOS EXTRANJEROS', 1, 'EXT', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, orden, nombre, valorbol, referencia, fechaumod, userumod)
values (16015, 12, 15, 'IGLESIA CATÓLICA', 1, 'IC', systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 13 - subtipos de personas juridicas
-- base => tipo de pj al que pertenece
-- valorbol => irregular? (1=SI, 0=regular)
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (17001, 13, 1, 1, 'SOCIEDAD DE HECHO', 'SH', 1, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (17002, 13, 1, 2, 'SOCIEDAD COLECTIVA', 'SC', 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (17003, 13, 1, 3, 'SOCIEDAD EN COMANDITA SIMPLE', 'SCS', 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (17004, 13, 1, 4, 'SOCIEDAD DE CAPITAL E INDUSTRIA', 'SCI', 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (17005, 13, 1, 5, 'SOCIEDAD DE RESPONSABILIDAD LIMITADA', 'SRL', 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (17006, 13, 1, 6, 'SOCIEDAD ANÓNIMA', 'SA', 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (17007, 13, 1, 7, 'SOCIEDAD EN COMANDITA POR ACCIONES', 'SCA', 0, systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, valorbol, fechaumod, userumod)
values (17008, 13, 1, 8, 'SOCIEDAD ANÓNIMA CON PARTICIPACIÓN ESTATAL MAYORITARIA', 'SAPEM', 0, systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 14 - tipos de formularios
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (18001, 14, 1, 1, 'FORMULARIO DE REGISTRACION', 'REGISTRO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (18002, 14, 1, 2, 'FORMULARIO DE CONTACTO', 'CONTACTO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (18003, 14, 1, 3, 'FORMULARIO DE INSCRIPCION', 'INSCRIPCION', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (18004, 14, 1, 4, 'FORMULARIO DE INFORMACION', 'INFORMACION', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (18005, 14, 1, 5, 'FORMULARIO DE DECLARACION JURADA', 'DDJJ', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (18006, 14, 1, 6, 'FORMULARIO DE PAGO', 'PAGO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (18007, 14, 1, 7, 'FORMULARIO DE CONVENIO DE PAGO', 'CONVENIO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (18008, 14, 1, 8, 'FORMULARIO DE TRAMITE', 'TRAMITE', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (18009, 14, 1, 9, 'FORMULARIO DE BAJA', 'BAJA', systimestamp, 'INSTALL');
commit;


-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 15 - estados de personas
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (19001, 15, 1, 1, 'SITUACION NORMAL', 'NORMAL', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (19002, 15, 1, 2, 'SITUACION DE MORA', 'MOROSO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (19003, 15, 1, 3, 'INSCRIPCION BLOQUEADA', 'BLOQUEADO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (19004, 15, 1, 4, 'SITUACION DE INHABILITACION', 'INHABILITADO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (19005, 15, 1, 5, 'SITUACION DE OMISION', 'OMISO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (19006, 15, 1, 6, 'SITUACION DE EVASION', 'EVASOR', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (19006, 15, 1, 7, 'SITUACION DE EJECUCION', 'EJECUTADO', systimestamp, 'INSTALL');
commit;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- subtabla: 16 - estados de objeto
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (20001, 16, 1, 1, 'SITUACION NORMAL', 'NORMAL', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (20002, 16, 1, 2, 'SITUACION DE SINIESTRO', 'SINIESTRADO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (20003, 16, 1, 3, 'SITUACION DE INHABILITACION', 'INHABILITADO', systimestamp, 'INSTALL');
insert into ut_parametro
(id, tipo, base, orden, nombre, referencia, fechaumod, userumod)
values (20004, 16, 1, 4, 'SITUACION DE BAJA', 'BAJA', systimestamp, 'INSTALL');
commit;