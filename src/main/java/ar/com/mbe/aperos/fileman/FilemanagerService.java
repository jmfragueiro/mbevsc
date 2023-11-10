package ar.com.mbe.aperos.fileman;

import ar.com.mbe.aperos.apirest.RestService;
import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.F;
import ar.com.mbe.core.common.T;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FilemanagerService implements IFilemanagerService {
    private static Environment env = new AnnotationConfigApplicationContext().getEnvironment();
    private final List<IFilemanagerClient> fmclients = new ArrayList<>();

    @Override
    public IFilemanagerService addClient(IFilemanagerClient client) {
        fmclients.add(client);
        return this;
    }

    /**
     * Servicio para Subir Archivos al Repositorio
     * Los Tipos Posibles son:
     * persona
     * usuario
     * agente
     * tramite
     * dispositivo
     * patrimonio
     * <p>
     * Los Subtipos Posibles son:
     * foto
     * dni
     * doc
     * - "nombrepropuesto" indica con que nombre se debera renombar en el repositorio
     * al archivo que es enviado para su grabacion. Siempre se respeta la extension
     * del archivo enviado
     * - "creasubcartepa" envia false o true de acuerdo a si se debera crear una carpeta
     * contenedora para el archivo enviado, esta carpeta sera una subcarpeta de la que
     * tiene el mismo nombre que tipo + "/" + subtipo
     * - "nombresubcarpeta" es el nombre de la carpeta a considerar segun la necesidad
     * descripta mas arriba
     * - "id" es el id de la tabla requerida para actualizar datos en algunos casos.
     * por ejemplo en tipo=persona o tipo=usuario se graba el nombre de la imagen
     * subida al repositorio.
     * NOTA: requiere obligatoriamente encontrar un IFileManagerClient que audite
     */
    public ResponseEntity<FilemanagerResponse> subirArchivoAlRepositorio(RFileManagerRequest fmreq) {
        ResponseEntity<FilemanagerResponse> response;

        var fmc = this.getFilemanagerClient(fmreq);
        if (fmc.isPresent()) {
            var fmr = fmc.get().preaction(EFilemanagerAction.UPLOAD, fmreq);
            if (fmr.isOk()) {
                response = this.upload(fmreq);
                fmc.get().posaction(EFilemanagerAction.UPLOAD, fmreq, response.getBody());
            } else {
                response = new ResponseEntity<>(fmr, HttpStatus.OK);
            }
        } else {
            response = new ResponseEntity<>(FilemanagerResponse.noServiceClient(), HttpStatus.OK);
        }

        return response;
    }

    protected ResponseEntity<FilemanagerResponse> upload(RFileManagerRequest fmreq) {
        return (isOkDataForRequest(fmreq))
               ? RestService.postForEntity(getURLForHTTPAction(getUrlFmAccUpload(), fmreq),
                                           MediaType.MULTIPART_FORM_DATA,
                                           null,
                                           null,
                                           null,
                                           FilemanagerResponse.class,
                                           fmreq.file())
               : new ResponseEntity<>(FilemanagerResponse.noDataForRequest(), HttpStatus.OK);
    }

    /**
     * Servicio Para Borrar Archivos del Repositorio
     * NOTA: requiere obligatoriamente encontrar un IFileManagerClient que audite
     */
    public ResponseEntity<FilemanagerResponse> borrarArchivoDelRepositorio(RFileManagerRequest fmreq) {
        ResponseEntity<FilemanagerResponse> response;

        var fmc = this.getFilemanagerClient(fmreq);
        if (fmc.isPresent()) {
            var fmr = fmc.get().preaction(EFilemanagerAction.DELETE, fmreq);
            if (fmr.isOk()) {
                response = this.delete(fmreq);
                fmc.get().posaction(EFilemanagerAction.DELETE, fmreq, response.getBody());
            } else {
                response = new ResponseEntity<>(fmr, HttpStatus.OK);
            }
        } else {
            response = new ResponseEntity<>(FilemanagerResponse.noServiceClient(), HttpStatus.OK);
        }

        return response;
    }

    protected ResponseEntity<FilemanagerResponse> delete(RFileManagerRequest fmreq) {
        return (isOkDataForRequest(fmreq))
               ? RestService.postForEntity(getURLForHTTPAction(getUrlFmAccDelete(), fmreq),
                                           MediaType.MULTIPART_FORM_DATA,
                                           null,
                                           null,
                                           null,
                                           FilemanagerResponse.class,
                                           fmreq.file())
               : new ResponseEntity<>(FilemanagerResponse.cantDelete(), HttpStatus.OK);
    }

    /**
     * Servicio para Renombrar un Archivo especifico del Repositorio
     * NOTA: requiere obligatoriamente encontrar un IFileManagerClient que audite
     */
    public ResponseEntity<FilemanagerResponse> renombrarArchivoDelRepositorio(RFileManagerRequest fmreq) {
        ResponseEntity<FilemanagerResponse> response;

        var fmc = this.getFilemanagerClient(fmreq);
        if (fmc.isPresent()) {
            var fmr = fmc.get().preaction(EFilemanagerAction.RENAME, fmreq);
            if (fmr.isOk()) {
                response = this.rename(fmreq);
                fmc.get().posaction(EFilemanagerAction.RENAME, fmreq, response.getBody());
            } else {
                response = new ResponseEntity<>(fmr, HttpStatus.OK);
            }
        } else {
            response = new ResponseEntity<>(FilemanagerResponse.noServiceClient(), HttpStatus.OK);
        }

        return response;
    }

    protected ResponseEntity<FilemanagerResponse> rename(RFileManagerRequest fmreq) {
        return RestService.postForEntity(getURLForHTTPAction(getUrlFmAccRename(), fmreq),
                                         MediaType.MULTIPART_FORM_DATA,
                                         null,
                                         null,
                                         null,
                                         FilemanagerResponse.class,
                                         fmreq.file());
    }

    /**
     * Servicio para Verificar la Existencia de un archivo especifico en el Repositorio
     */
    public ResponseEntity<FilemanagerResponse> verificarArchivoDelRepositorio(RFileManagerRequest fmreq) {
        return this.verify(fmreq);
    }

    protected ResponseEntity<FilemanagerResponse> verify(RFileManagerRequest fmreq) {
        return RestService.exchange(getURLForHTTPAction(getUrlFmAccExist(), fmreq),
                                     HttpMethod.GET,
                                     MediaType.MULTIPART_FORM_DATA,
                                     null,
                                     null,
                                     null,
                                     FilemanagerResponse.class,
                                     fmreq.file());
    }

    /**
     * Servicio para Obtener un Archivo especifico del Repositorio
     */
    public ResponseEntity<byte[]> obtenerArchivoDelRepositorio(RFileManagerRequest fmreq) {
        byte[] file = null;

        var fmc = this.getFilemanagerClient(fmreq);
        if (fmc.isPresent()) {
            var fmr = fmc.get().preaction(EFilemanagerAction.UPLOAD, fmreq);
            if (fmr.isOk()) {
                file = this.download(fmreq);
                fmc.get().posaction(EFilemanagerAction.UPLOAD, fmreq,
                                    new FilemanagerResponse(true, C.SYS_CAD_OK, fmreq.filename()));
            }
        } else {
            file = this.download(fmreq);
        }

        return file != null ? ResponseEntity.ok()
                                            .header("Content-Type",
                                                    obtenerTipoMimeArchivo(fmreq.filename())
                                                                    + "; charset=UTF-8")
                                            .header("Content-Disposition",
                                                    "attachment; filename=" + fmreq.filename())
                                            .body(file)
                            : new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    protected byte[] download(RFileManagerRequest fmreq) {
        return (isOkDataForRequest(fmreq))
               ? RestService.exchange(getURLForHTTPAction(getUrlFmAccGetfiles(), fmreq),
                                      HttpMethod.GET,
                                      MediaType.MULTIPART_FORM_DATA,
                                      null,
                                      null,
                                      List.of(MediaType.APPLICATION_OCTET_STREAM),
                                      byte[].class,
                                      fmreq.file()).getBody()
               : null;
    }

    /**
     * Servicio para Obtener la Lista de Archivos de una carpeta en el Repositorio
     */
    public String obtenerListaDeArchivosDelRepositorio(RFileManagerRequest fmreq) {
        return this.list(fmreq);
    }

    protected String list(RFileManagerRequest fmreq) {
        return RestService.exchange(getURLForHTTPAction(getUrlFmAccGetfiles(), fmreq),
                                     HttpMethod.GET,
                                     MediaType.MULTIPART_FORM_DATA,
                                     null,
                                     null,
                                     null,
                                     String.class,
                                     fmreq.file()).getBody();
    }

    private Optional<IFilemanagerClient> getFilemanagerClient(RFileManagerRequest fmreq) {
        return this.fmclients.stream()
                    .filter(c -> c.isFmClientForObject(fmreq.tipo(), fmreq.subtipo(), fmreq.id()))
                    .findFirst();
    }

    private boolean isOkDataForRequest(RFileManagerRequest fmreq) {
        return (StringUtils.hasText(fmreq.tipo())
                && fmreq.subtipo() != null)
               && !StringUtils.hasText(fmreq.filename())
               && (!fmreq.opsubcarpeta() || StringUtils.hasText(fmreq.subcarpeta()));
    }

    /**
     * Este Metodo Retorna un token para consultar el repositorio de documentacion
     */
    private static String obtenerTokenParaRepositorio() {
        return T.getMd5Token(F.getNowDateAsString("d/m/y"));
    }

    public static String obtenerTipoMimeArchivo(String filename) {
        String tipo = null;
        String extension = obtenerExtensionArchivo(filename);
        if (extension.equals("doc")) {
            tipo = "application/vnd.ms-word";
        }
        if (extension.equals("docx")) {
            tipo = "application/vnd.ms-word";
        }
        if (extension.equals("xls")) {
            tipo = "application/vnd.ms-excel";
        }
        if (extension.equals("xlsx")) {
            tipo = "application/vnd.ms-excel";
        }
        if (extension.equals("pdf")) {
            tipo = "application/pdf";
        }
        if (extension.equals("png")) {
            tipo = "image/png";
        }
        if (extension.equals("jpg")) {
            tipo = "image/jpeg";
        }
        if (extension.equals("jpeg")) {
            tipo = "image/jpeg";
        }
        if (extension.equals("rtf")) {
            tipo = "application/rtf";
        }
        if (extension.equals("csv")) {
            tipo = "text/csv";
        }
        if (extension.equals("txt")) {
            tipo = "text/plain";
        }
        if (extension.equals("ppt")) {
            tipo = "application/vnd.ms-powerpoint";
        }

        return tipo;
    }

    public static String obtenerExtensionArchivo(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    public static String getURLForHTTPAction(String urlAccion, RFileManagerRequest fmreq) {
        // Tener en cuenta que el nombre propuesto no tiene extension,
        // se respeta la del archivo original.-
        String url = getBaseUrl()
                     + urlAccion
                     + "/"
                     + obtenerTokenParaRepositorio()
                     + getEntorno()
                     + "/";

        if (urlAccion.equals(getUrlFmAccUpload())) {
            url += fmreq.tipo().toLowerCase()
                    + "/"
                    + fmreq.subtipo().name().toLowerCase()
                    + "/"
                    + fmreq.filename().toLowerCase()
                    + "/"
                    + fmreq.opsubcarpeta()
                    + "/"
                    + fmreq.subcarpeta().toLowerCase()
                    + "/"
                    + fmreq.id().toString();
        } else if (urlAccion.equals(getUrlFmAccDelete())) {
            url += fmreq.tipo().toLowerCase()
                    + "/"
                    + fmreq.opsubcarpeta()
                    + "/"
                    + fmreq.subcarpeta().toLowerCase()
                    + "/"
                    + fmreq.filename().toLowerCase();
        } else if (urlAccion.equals(getUrlFmAccExist())) {
            url += fmreq.subcarpeta().toLowerCase()
                    + "/"
                    + fmreq.filename().toLowerCase();
        } else if (urlAccion.equals(getUrlFmAccRename())) {
            url += fmreq.subcarpeta().toLowerCase()
                   + "/"
                   + fmreq.filename().toLowerCase()
                   + "/"
                   + fmreq.extra().toLowerCase();
        } else if (urlAccion.equals(getUrlFmAccGetfiles())) {
            url += fmreq.tipo().toLowerCase()
                   + "/"
                   + fmreq.subtipo().name().toLowerCase()
                   + "/"
                   + fmreq.subcarpeta().toLowerCase();
        }

        return url;
    }

    public static String getEntorno() {
        return env.getProperty("${server.entorno}");
    }

    public static String getBaseUrl() {
        return env.getProperty("${sys.app.fileman.baseurl}");
    }

    public static String getUrlFmAccUpload() {
        return env.getProperty("${sys.app.fileman.acc.upload}");
    }

    public static String getUrlFmAccDelete() {
        return env.getProperty("${sys.app.fileman.acc.delete}");
    }

    public static String getUrlFmAccExist() {
        return env.getProperty("${sys.app.fileman.acc.exist}");
    }

    public static String getUrlFmAccRename() {
        return env.getProperty("${sys.app.fileman.acc.rename}");
    }

    public static String getUrlFmAccGetfiles() {
        return env.getProperty("${sys.app.fileman.acc.getfiles}");
    }
}
