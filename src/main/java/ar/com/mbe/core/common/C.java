package ar.com.mbe.core.common;

/**
 * Esta clase debe ser utilizada como un punto focal para todos los manejos de textos
 * dentro y valroes constantes dentro de este proto-framework, para tener encapsuladas,
 * en una sola clase, todas las cuestiones asociadas a este tipo de valores.
 *
 * @author jmfragueiro
 * @version 20230601
 */
public abstract class C {
    /******************************************************************************************
     ** CONSTANTES GENERALES DEL SISTEMA A REALIZARSE
     ** NOTA: estas variables deben setearse para cada sistema en "resources/sistema.properties"
     ******************************************************************************************/
    public static final String SYS_CAD_UPDATE_SUCCESS = "SE HA ACTUALIZADO SATISFACTORIAMENTE";
    public static final String SYS_CAD_UPDATE_FAILURE = "NO SE HA PODIDO ACTUALIZAR";
    public static final String SYS_CAD_NULL = "";
    public static final String SYS_CAD_LOGSEP = ":";
    public static final String SYS_CAD_NUMSEP = "-";
    public static final String SYS_CAD_BARRA = "/";
    public static final String SYS_CAD_BARRAINV = "\\";
    public static final String SYS_CAD_POSITIVO = "+";
    public static final String SYS_CAD_NEGATIVO = "-";
    public static final String SYS_CAD_ALL = "*";
    public static final String SYS_CAD_URLALL = "/**";

    public static final String SYS_CAD_OPENTYPE = "[";

    public static final String SYS_CAD_CLOSETPE = "]";
    public static final String SYS_CAD_UNDERSCORE = "_";
    public static final String SYS_CAD_SPACE = " ";
    public static final String SYS_CAD_COMMA = ",";
    public static final String SYS_CAD_PUNTO = ".";
    public static final String SYS_CAD_PUNTOCOMA = ";";
    public static final String SYS_CAD_OPENREF = "<";
    public static final String SYS_CAD_CLOSEREF = ">";
    public static final String SYS_CAD_PESOS = "$";
    public static final String SYS_CAD_REFER = "->";
    public static final String SYS_CAD_REMARK = " ***** ";
    public static final String SYS_CAD_EXCLAM = "!!!!";
    public static final String SYS_CAD_PERCENT = "%";
    public static final String SYS_CAD_CONFIGURL = "/config";
    public static final String SYS_CAD_AUTHURL = "/auth";
    public static final String SYS_CAD_APIDOCURL = "/apid";
    public static final String SYS_CAD_QRURL = "/code";
    public static final String SYS_CAD_LOGINURL = SYS_CAD_AUTHURL.concat("/login");
    public static final String SYS_CAD_LOGOUTURL = SYS_CAD_AUTHURL.concat("/logout");
    public static final String SYS_CAD_UNKNOW = "UNKNOW";
    public static final String SYS_CAD_NEW = "NUEVO";
    public static final String SYS_CAD_CRLF = "\n";
    public static final String SYS_CAD_ERROR = "ERROR";
    public static final String SYS_CAD_OK = "OK";
    public static final String SYS_CAD_FILE = "ARCHIVO";
    public static final String SYS_CAD_IMAGE = "IMAGEN";
    public static final String SYS_CAD_QTY = "Cantidad";
    public static final String SYS_CAD_EXPIRED = "EXPIRADO";
    public static final String SYS_CAD_VERDADERO = "true";
    public static final String SYS_CAD_FALSO = "false";
    public static final String SYS_CAD_SEC_USUARIO = "Usuario";
    public static final String SYS_CAD_SINOBSERVACIONES  = "Sin Observaciones";
    public static final String SYS_CAD_DESA = "DESARROLLO";
    public static final String SYS_CAD_PROD = "PRODUCCION";


    /**********************************************************
     * Cadenas del proceso de conexion al Sistema
     **********************************************************/
    public static final String SYS_APP_TXTLOGIN_PASS = "Password";
    public static final String SYS_APP_TXTLOGIN_EMAIL = "Correo Electronico";
    public static final String SYS_APP_LOGIN_HTTP_AUTH = "Authorization";

    /**********************************************************
     * Cadenas asociadas a los tipos de contenido http
     **********************************************************/
    public static final String SYS_APP_MIMETYPE_JSON = "application/json";
    public static final String SYS_APP_MIMETYPE_JSCRIPT = "application/javascript";
    public static final String SYS_APP_MIMETYPE_PDF = "application/pdf";
    public static final String SYS_APP_MIMETYPE_SQL = "application/sql";
    public static final String SYS_APP_MIMETYPE_XML = "application/xml";
    public static final String SYS_APP_MIMETYPE_ZIP = "application/zip";
    public static final String SYS_APP_MIMETYPE_MPEG = "audio/mpeg";
    public static final String SYS_APP_MIMETYPE_GIF = "image/gif";
    public static final String SYS_APP_MIMETYPE_JPEG = "image/jpeg";
    public static final String SYS_APP_MIMETYPE_PNG = "image/png";
    public static final String SYS_APP_MIMETYPE_FORM = "multipart/form-data";
    public static final String SYS_APP_MIMETYPE_CSS = "text/css";
    public static final String SYS_APP_MIMETYPE_CSV = "text/csv";
    public static final String SYS_APP_MIMETYPE_HTML = "text/html";
    public static final String SYS_APP_MIMETYPE_TXT = "text/plain";
    public static final String SYS_APP_MIMETYPE_TXTXML = "text/xml";
    public static final String SYS_CAD_ACTION_ADD = "add";
    public static final String SYS_CAD_ACTION_DEL = "delete";
    public static final String SYS_CAD_ACTION_UPD = "update";

    /**********************************************************
     * Cadenas asociadas al manejo de formatos de fecha
     **********************************************************/
    public static final String SYS_APP_DATEFORMAT_VIEW = "dd/MM/yyyy";
    public static final String SYS_APP_DATEFORMAT_DB = "yyyy-MM-dd";
    public static final String SYS_APP_DATEFORMAT_LIST = "dd/MM/yyyy";
    public static final String SYS_APP_DATEFORMAT_REGEX =
            "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";

    /**********************************************************
     * Cadenas asociadas a los tipos ROLES BASICOS DEL SISTEMA
     **********************************************************/
    public static final String SYS_APP_ROLE_ADMIN = "ADMIN";
    public static final String SYS_APP_ROLE_BASIC = "BASIC";
    public static final String SYS_APP_ROLE_USER = "USER";

    /**********************************************************
     * Cadenas de cambio de password
     **********************************************************/
    public static final String SYS_APP_CHPASS_ERR_BADPASS = "LAS CONTRASEÑA ACTUAL NO ES CORRECTA";
    public static final String MSJ_APP_CHPASS_ERR_ONCHANGE =
            "ERROR AL INTENTAR MODIFICAR LA CONTRASEÑA DEL USUARIO";
    public static final String SYS_APP_CHPASS_ERR_DISTPASS = "LAS CONTRASEÑAS NUEVAS NO COINCIDEN";
    public static final String MSJ_APP_CHPASS_INF_CHANGEOK = "SE HA MODIFICADO LA CONTRASEÑA DEL USUARIO";
    public static final String SYS_APP_CHANGEEMAIL_ERR_BADEMAIL = "EL EMAIL NO ES CORRECTO";
    public static final String MSJ_APP_CHANGEEMAIL_ERR_ONCHANGE =
            "ERROR AL INTENTAR MODIFICAR EL EMAIL DEL USUARIO";
    public static final String MSJ_APP_CHANGEEMAIL_ERR_FOROHTER =
            "NO PUEDE SOLICITAR CONFIRMACIÓN DE DIRECCION DE CORREO ELECTRONICO PARA OTRO USUARIO";
    public static final String MSJ_APP_CHANGEEMAIL_ERR_NOBODY =
            "SE REQUIERE UN USUARIO PARA SOLICITAR CONFIRMACIÓN DE DIRECCION DE CORREO ELECTRONICO";

    /**********************************************************
     * Cadenas de Extensiones de archivo comunes
     **********************************************************/
    public static final String SYS_FILEXT_TXT = ".txt";
    public static final String SYS_FILEXT_IMP = ".imp";
    public static final String SYS_FILEXT_PDF = ".pdf";
    public static final String SYS_FILEXT_JPG = ".jpg";

    /**********************************************************
     * Cadenas de Mensaje asociados a la generación de Reportes
     **********************************************************/
    public static final String MSJ_REP_INF_SETBASEPATH = "ESTABLECIENDO DIRECTORIO BASE DEL REPORTE";
    public static final String MSJ_REP_INF_BASEPATH = "DIRECTORIO BASE DEL REPORTE ESTABLECIDO";
    public static final String MSJ_REP_INF_INITPARES = "INICIANDO CARGA DE ARCHVO TEMPLATE DEL REPORTE";
    public static final String MSJ_REP_INF_LOAD = "CARGANDO ARCHVO TEMPLATE DEL REPORTE";
    public static final String MSJ_REP_INF_LOADOK = "ARCHVO TEMPLATE DEL REPORTE CARGADO CORRECTAMENTE";
    public static final String MSJ_REP_INF_COMPILE = "CARGANDO ARCHVO TEMPLATE DEL REPORTE";
    public static final String MSJ_REP_INF_COMPILEOK = "ARCHVO TEMPLATE DEL REPORTE CARGADO CORRECTAMENTE";
    public static final String MSJ_REP_INF_COMPLETE = "COMBINANDO DATOS DEL REPORTE";
    public static final String MSJ_REP_INF_COMPLETEOK = "REPORTE COMBINADO CON DATOS CORRECTAMENTE";
    public static final String MSJ_REP_ERR_NOBASEPATH =
            "ERROR AL INTENTAR ESTABLECER DIRECTORIO BASE DEL REPORTE";
    public static final String MSJ_REP_ERR_CANTREADTEMPLATE =
            "ERROR AL INTENTAR LEER EL ARCHVO TEMPLATE DEL REPORTE";
    public static final String MSJ_REP_ERR_CANTLOADTEMPLATE =
            "ERROR AL INTENTAR ENCONTRAR EL ARCHVO TEMPLATE DEL REPORTE";
    public static final String MSJ_REP_ERR_COMPILETEMPLATE =
            "ERROR AL INTENTAR COMPILAR EL ARCHVO TEMPLATE DEL REPORTE";
    public static final String MSJ_REP_ERR_COMPLETE = "ERROR AL INTENTAR RELLENAR LOS DATOS EN EL REPORTE";
    public static final String MSJ_REP_ERR_NOEXPORT = "ERROR AL INTENTAR EXPORTAR EL REPORTE";
    public static final String MSJ_REP_EXPORT_PDF = "EXPORTANDO REPORTE A PDF";
    public static final String MSJ_REP_EXPORT_XLS = "EXPORTANDO REPORTE A EXCEL";
    public static final String MSJ_REP_GENERATE_PREOUT_STRING = "_";
    public static final String MSJ_REP_GENERATE_POSOUT_PDF = ".pdf";
    public static final String MSJ_REP_GENERATE_POSOUT_XLS = ".xls";

    /**********************************************************
     * Cadenas de Mensajes de Tokens de Sesiones comunes
     **********************************************************/
    public static final String MSJ_SEC_INF_BADAUTH = "SOLICITUD DE AUTENTICACION INVALIDA";
    public static final String MSJ_SEC_INF_TOKENAUTH = "SE HA GENERADO EL TOKEN AUTENTICADO";
    public static final String MSJ_SEC_INF_TOKEREPODEL = "SE HA REMOVIDO DEL REPOSITORIO UN TOKEN EXISTENTE";
    public static final String MSJ_SES_ERR_NOTOKENVALUEINFO =
            "NO SE HA PROPORCIONADO EL USUARIO PARA VALIDAR LA SESION";
    public static final String MSJ_SEC_ERR_NOTOKENKEY =
            "NO SE HA ENCONTRADO EL USUARIO PARA CREAR UNA SESION VALIDA";
    public static final String MSJ_SEC_ERR_TOKENEXIST = "YA EXISTE UNA SESION VALIDA PARA EL USUARIO";
    public static final String MSJ_SEC_ERR_TOKENEXPIRED = "LA SESION DE USUARIO HA EXPIRADO";
    public static final String MSJ_SEC_ERR_NOTOKEN = "NO SE HA ENCONTRADO UNA SESION DE USUARIO ACTIVA";
    public static final String MSJ_SEC_ERR_TOKENCHANGESTATE =
            "ERROR AL INTENTAR MODIFICAR EL ESTADO DE UNA SESION";
    public static final String MSJ_SEC_ERR_USERNOAUTH = "USUARIO NO AUTENTICADO";
    public static final String MSJ_SEC_ERR_NOUSERSERVICE = "ERROR AL INICIAR UN SERVICIO DE AUTENTICACION";
    public static final String MSJ_SEC_ERR_BADJWTSIGN = "FIRMA DE TOKEN INVALIDA";
    public static final String MSJ_SEC_ERR_BADJWT = "DATOS DE TOKEN INCORRRECTOS";
    public static final String MSJ_SEC_ERR_TOKENNOTSUP = "TOKEN NO SOPORTADO POR LA PLATAFORMA";
    public static final String MSJ_SEC_ERR_EMPTYCLAIM = "CADENA DE CONTENIDO DE TOKEN VACIA";
    public static final String MSJ_SEC_ERR_CANTEXTRACTTED = "NO SE OBTUVO NINGUN VALOR EXTRA DESDE EL TOKEN";
    public static final String MSJ_SEC_ERR_BADTOKEN = "FORMATO DE TOKEN INVALIDO";
    public static final String MSJ_SEC_ERR_TOKENUSERNOOP =
            "EL ESTADO DEL USUARIO NO PERMITE GENERAR UN TOKEN VALIDO";
    public static final String MSJ_SEC_ERR_TOKENREINIT = "ERROR AL INTENTAR REVALIDAR UN TOKEN";

    /**********************************************************
     * Cadenas de Mensajes de Seguridad y Sesiones comunes
     **********************************************************/
    public static final String MSJ_ERR_EXCEPCION = "ERROR INTERNO DEL SISTEMA";
    public static final String MSJ_ERR_BADFORMATREQUEST = "FORMATO DE REQUERIMIENTO INVALIDO";
    public static final String MSJ_ERR_UNAUTHORIZED = "Unauthorized";
    public static final String MSJ_SEC_ERR_BADREQUEST = "FORMATO DE REQUERIMIENTO DE ACCESO INVALIDO";
    public static final String MSJ_SEC_ERR_BADREQUESTVALUE = "REQUERIMIENTO DE ACCESO INVALIDO";
    public static final String MSJ_SEC_ERR_NOUSER_BY_TOKEN =
            "NO EXISTE NINGUN REGISTRO DE RECUPARACION DE CLAVE SEGUN LA SOLICITUD EFECTUADA";
    public static final String MSJ_SEC_ERR_USEREMAILNOTCHECK =
            "LA CUENTA DEL USUARIO NO TIENE CORREO ELECTRONICO VERIFICADO";
    public static final String MSJ_SEC_ERR_CANTSENDRECEMAIL =
            "NO SE PUEDE ENVIAR EMAIL PARA INICIAR EL PROCESO DE RECUPERACION";
    public static final String MSJ_SEC_ERR_USEREXPIRED = "LA CUENTA DEL USUARIO SOLICITADO ESTA EXPIRADA";
    public static final String MSJ_SEC_ERR_USERENABLED = "EL USUARIO SOLICITADO SE ENCUENTRA INHABILITADO";
    public static final String MSJ_SEC_ERR_USERLOCKED = "EL USUARIO SOLICITADO SE ENCUENTRA BLOQUEADO";
    public static final String MSJ_SES_INF_BADCREDENTIAL = "USUARIO O CONTRASEÑA INCORRECTOS";
    public static final String MSJ_SES_INF_LOGON = "HA INICIADO CORRECTAMENTE LA SESION DE USUARIO";
    public static final String MSJ_SES_INF_LOGOFF = "HA FINALIZANDO LA SESION DE USUARIO";
    public static final String MSJ_SES_ERR_GENERIC =
            "EL RECURSO NO ES ACCESIBLE O EL USUARIO NO FUE AUTENTICADO";
    public static final String MSJ_SES_ERR_LOGIN = "ERROR AL INICIAR LA SESION DE USUARIO";
    public static final String MSJ_SES_ERR_LOGOFF = "ERROR AL FINALIZAR LA SESION DE USUARIO";
    public static final String MSJ_SES_ERR_NOAUTH = "ERROR AL AUTENTICAR AL USUARIO";
    public static final String MSJ_SES_ERR_BADREQAUTH = "ERROR AL AUTENTICAR LA SOLICITUD";
    public static final String MSJ_SEC_INF_NOACCES = "ACCESO AL RECURSO O FUNCION NO PERMITIDO";
    public static final String MSJ_SEC_ERR_NOUSER = "EL USUARIO SOLICITADO NO EXISTE";
    public static final String MSJ_SEC_ERR_USERNOTENABLED = "EL USUARIO SOLICITADO SE ENCUENTRA INHABILITADO";
    public static final String MSJ_SEC_ERR_USERCANTOP = "ACCESO A OPERAR EL SISTEMA NO PERMITIDO";
    public static final String MSJ_SEC_ERR_NOGRANTS = "NO PUEDEN ESTABLECERSE LOS PERMISOS DEL USUARIO";

    /**********************************************************
     * Cadenas de Mensajes de Errores de persistencia
     **********************************************************/
    public static final String MSJ_ERR_ATSAVEDATA = "ERROR AL INTENTAR PERSISTIR UN DATO";
    public static final String MSJ_ERR_ATDELDATA = "ERROR AL INTENTAR ELIMINAR UN DATO";

    /**********************************************************
     * Cadenas de Mensajes de Errores de campos de base de datos
     **********************************************************/
    public static final String MSJ_ERR_DB_FIELD_EMPTY = "DEBEN CARGARSE DATOS PARA EL CAMPO OBLIGATORIO: ";
    public static final String MSJ_ERR_DB_FIELD_NOK = "DEBEN CARGARSE DATOS VALIDOS PARA EL CAMPO: ";
    public static final String MSJ_ERR_DB_FIELD_LONGNOK =
            "LA LONGITUD DE DATOS INGRESADOS ES INVALIDA PARA EL CAMPO: ";
    public static final String MSJ_ERR_DB_NOITEM = "NO PUDO OBTENERSE EL ITEM DESEADO";
    public static final String MSJ_ERR_DB_TOOMANY = "SE OBTUVIERON MAS ITEMS QUE LOS DESEADOS";

    /**********************************************************
     * Cadenas de Mensajes de Errores de manejo de archivos del SO
     **********************************************************/
    public static final String MSJ_ERR_FILE_CANTUPLOAD = "NO SE PUEDE HACER UPLOAD DEL ARCHIVO";
    public static final String MSJ_ERR_FILE_CANTDELETE = "NO SE PUEDE BORRAR EL ARCHIVO";
    public static final String MSJ_ERR_FILE_NODATA = "FALTAN DATOS PARA LA ACCION DE ARCHIVO";
    public static final String MSJ_ERR_FILE_NOSERVICE = "NO SE HA CONFIGURADO UN SERVICIO DE MANEJO DE ARCHIVOS";

    /******************************************************************************************
     ** VALORES GLOBALES PARA UTIIZACION EN LOS FILTROS DE BUSQUEDA
     ******************************************************************************************/
    public static final String BUS_SEPARADOR_CLAVE_VALOR = ",";
    public static final String BUS_SEPARADOR_PARES = ";";
    public static final String BUS_SEPARADOR_BETWEEN = "*";
    public static final String BUS_SEPARADOR_WSTAB_REGEX = "\\s+";

    /******************************************************************************************
     ** VALORES GLOBALES PARA EL MANEJO DEL ENVIO DE CORREOS
     ******************************************************************************************/
    public static final String SYS_EMAIL_CFG_PLANTILLA_HEAD = "cabecera";
    public static final String SYS_EMAIL_CFG_PLANTILLA_FOOT = "pie";
    public static final String SYS_EMAIL_CFG_MIMESUBTYPE = "alternative";
    public static final String SYS_EMAIL_CFG_CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String SYS_EMAIL_CFG_CONTENT_ID = "Content-ID";
    public static final String SYS_EMAIL_ERR_NOSUBJECT = "NO SE DEFINIO EL ASUNTO DEL CORREO";
    public static final String SYS_EMAIL_ERR_NOMESSAGE = "NO SE DEFINIO EL TEXTO DEL MENSAJE PARA EL CORREO";
    public static final String SYS_EMAIL_ERR_NOTO = "NO HAY DESTINATARIOS DEFINIDOS PARA ENVIAR EL CORREO";
    public static final String SYS_EMAIL_ERR_FATAL = "OCURRIO UN ERROR AL ENVIAR EL CORREO";
    public static final String SYS_EMAIL_INF_OK = "CORREO ENVIADO EXITOSAMENTE";
    public static final String SYS_EMAIL_ERR_NOUSER = "NO SE HA OBTENIDO EL USUARIO ASOCIADO AL CORREO";
    public static final String SYS_EMAIL_ERR_NODIR = "NO SE HA OBTENIDO LA DIRECCION DE CORREO DEL USUARIO";

    /**********************************************************
     * Cadenas asociadas a las opciones del Sistema
     **********************************************************/
    public static final String SYS_APP_CAD_ACCESS = "Ver";
    public static final String SYS_APP_CAD_LISTAR = "Listar";
    public static final String SYS_APP_CAD_ADD = "Agregar";
    public static final String SYS_APP_CAD_EDIT = "Editar";
    public static final String SYS_APP_CAD_DEL = "Eliminar";


    /**********************************************************
     * Cadenas de referencias para cuestiones de cliente APIREST
     **********************************************************/
    public static final String MSJ_ERR_REST_FATAL = "ERROR AL INTENTAR UNA LLAMADA DE API REST";
}
