package ar.com.mbe.aperos.fileman;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/filemanager")
public class FilemanagerController {
    private final IFilemanagerService fmService;

    @Autowired
    public FilemanagerController(List<IFilemanagerService> fileManagerServiceList,
                                 @Value("${sys.app.fileman}") String fileman) {
        this.fmService = fileManagerServiceList.stream()
                                               .filter(c -> c.getClass().getCanonicalName().equals(fileman))
                                               .findFirst()
                                               .orElse(new FilemanagerService());
    }

    /**
     * Metodo para Verificar la existencia de un archivo en el repo
     *
     * @param carpeta
     * @param archivo
     */
    @GetMapping("verificarexistencia/{carpeta}/{archivo}")
    public FilemanagerResponse verificaExistencia(HttpServletRequest request,
                                                  @PathVariable String carpeta,
                                                  @PathVariable String archivo) {
        try {
            return fmService.verificarArchivoDelRepositorio(
                    new RFileManagerRequest(null,
                                            null,
                                            null,
                                            archivo,
                                            carpeta,
                                            false,
                                            null,
                                            null)).getBody();
        } catch (Exception ex) {
            return new FilemanagerResponse(false, ex.getMessage(), archivo);
        }
    }

    /**
     * Metodo para obtener el contenido de una carpeta del repositorio
     */
    @GetMapping("contenido/{tipo}/{subtipo}/{carpeta}")
    public ResponseEntity<String> listaDeArchivos(HttpServletRequest request,
                                                  @PathVariable String tipo,
                                                  @PathVariable String subtipo,
                                                  @PathVariable String carpeta) {
        return ResponseEntity.ok()
                             .body(fmService.obtenerListaDeArchivosDelRepositorio(
                                     new RFileManagerRequest(null,
                                                             tipo,
                                                             EFilemanagerSubtipo.getFromString(subtipo),
                                                             null,
                                                             carpeta,
                                                             false,
                                                             null,
                                                             null)));
    }

    /**
     * Metodo para Descargar Archivos desde el Repósitorio
     *
     * @param carpeta
     * @param archivo
     * @return
     */
    @GetMapping("rename/{carpeta}/{archivo}/{nombrenuevo}")
    public ResponseEntity<FilemanagerResponse> rename(HttpServletRequest request,
                                                      @PathVariable String carpeta,
                                                      @PathVariable String archivo,
                                                      @PathVariable String nombrenuevo) {
        try {
            return fmService.renombrarArchivoDelRepositorio(
                    new RFileManagerRequest(null,
                                            null,
                                            null,
                                            archivo,
                                            carpeta,
                                            false,
                                            null,
                                            nombrenuevo));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new FilemanagerResponse(false, ex.getMessage(), archivo));
        }
    }

    /**
     * Metodo para Descargar Archivos desde el Repósitorio
     *
     * @param carpeta
     * @param archivo
     * @return
     */
    @GetMapping("download/{carpeta}/{archivo}/{tipo}/{subtipo}/{id}")
    public ResponseEntity<byte[]> download(HttpServletRequest request,
                                           @PathVariable String carpeta,
                                           @PathVariable String archivo,
                                           @PathVariable String tipo,
                                           @PathVariable String subtipo,
                                           @PathVariable Long id) {
        return fmService.obtenerArchivoDelRepositorio(new RFileManagerRequest(null,
                                                                              tipo,
                                                                              EFilemanagerSubtipo.getFromString(subtipo),
                                                                              archivo,
                                                                              carpeta,
                                                                              false,
                                                                              id,
                                                                              null));
    }

    /**
     * Metodo para Borrar Archivos del Repósitorio
     *
     * @param tipo
     * @param subtipo
     * @param entrarcarpeta
     * @param subcarpeta
     * @param archivo
     * @param id
     * @return
     */
    @PostMapping("delete/{tipo}/{subtipo}/{entrarcarpeta}/{subcarpeta}/{archivo}/{id}")
    public ResponseEntity<FilemanagerResponse> delete(@PathVariable String tipo,
                                                      @PathVariable String subtipo,
                                                      @PathVariable boolean entrarcarpeta,
                                                      @PathVariable String subcarpeta,
                                                      @PathVariable String archivo,
                                                      @PathVariable String id) {
        // OJO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // La variable id, recibida en los paramentros esta declarada como STRING
        // No cambiar esto, ya que en algunos casos se recibirá "null" como parámetro
        // y al leer @PathVariable String id interpreta null como un string y no como
        // una condicion de nulo. Se hace esta aclaracion porque después se usa esta
        // variable id transformandola a Long y la logica seria haber definido como
        // Long desde el principio.
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        try {
            return fmService.borrarArchivoDelRepositorio(new RFileManagerRequest(null,
                                                                                 tipo,
                                                                                 EFilemanagerSubtipo.getFromString(subtipo),
                                                                                 archivo,
                                                                                 subcarpeta,
                                                                                 entrarcarpeta,
                                                                                 id.equals("null")
                                                                                 ? -1L
                                                                                 : Long.parseLong(id),
                                                                                 null));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new FilemanagerResponse(false, ex.getMessage(), archivo));
        }
    }

    /**
     * Metodo para Cargar Archivos al Repositorio
     *
     * @param file
     * @param tipo
     * @param subtipo
     * @param nombrepropuesto
     * @param creasubcarpeta
     * @param nombresubcarpeta
     * @param id
     * @return
     */
    @PostMapping("upload/{tipo}/{subtipo}/{nombrepropuesto}/{creasubcarpeta}/{nombresubcarpeta}/{id}")
    public ResponseEntity<FilemanagerResponse> upload(@RequestParam("fileupload") MultipartFile file,
                                                      @PathVariable String tipo,
                                                      @PathVariable String subtipo,
                                                      @PathVariable String nombrepropuesto,
                                                      @PathVariable boolean creasubcarpeta,
                                                      @PathVariable String nombresubcarpeta,
                                                      @PathVariable("id") Long id) {
        try {
            return fmService.subirArchivoAlRepositorio(new RFileManagerRequest(file,
                                                                               tipo,
                                                                               EFilemanagerSubtipo.getFromString(subtipo),
                                                                               nombrepropuesto,
                                                                               nombresubcarpeta,
                                                                               creasubcarpeta,
                                                                               id,
                                                                               null));
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                                 .body(new FilemanagerResponse(false, ex.getMessage(), nombrepropuesto));
        }
    }
}


