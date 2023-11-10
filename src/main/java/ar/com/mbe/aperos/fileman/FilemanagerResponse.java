package ar.com.mbe.aperos.fileman;

import ar.com.mbe.core.common.C;

/**
 * Esta clase es utilizada para registrar el resultado de operaciones sobre archivos,
 * por ejemplo para la devolucion de datos cuando se sube un archivo al repositorio.
 */
public class FilemanagerResponse {
    boolean ok;
    String msg;
    String nombreArchivo;

    public FilemanagerResponse() { }

    public FilemanagerResponse(boolean ok, String msg, String nombreArchivo) {
        this.ok = ok;
        this.msg = msg;
        this.nombreArchivo = nombreArchivo;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public static FilemanagerResponse cantUpload() {
        return new FilemanagerResponse(false, C.MSJ_ERR_FILE_CANTUPLOAD, C.SYS_CAD_NULL);
    }

    public static FilemanagerResponse cantDelete() {
        return new FilemanagerResponse(false, C.MSJ_ERR_FILE_CANTDELETE, C.SYS_CAD_NULL);
    }

    public static FilemanagerResponse noServiceClient() {
        return new FilemanagerResponse(false, C.MSJ_ERR_FILE_NOSERVICE, C.SYS_CAD_NULL);
    }

    public static FilemanagerResponse noDataForRequest() {
        return new FilemanagerResponse(false, C.MSJ_ERR_FILE_NODATA, C.SYS_CAD_NULL);
    }
}
