package ar.com.mbe.aperos.fileman;

import org.springframework.http.ResponseEntity;

public interface IFilemanagerService {
    IFilemanagerService addClient(IFilemanagerClient client);

    ResponseEntity<FilemanagerResponse> subirArchivoAlRepositorio(RFileManagerRequest fmreq);

    ResponseEntity<FilemanagerResponse> borrarArchivoDelRepositorio(RFileManagerRequest fmreq);

    ResponseEntity<FilemanagerResponse> verificarArchivoDelRepositorio(RFileManagerRequest fmreq);

    ResponseEntity<FilemanagerResponse> renombrarArchivoDelRepositorio(RFileManagerRequest fmreq);

    String obtenerListaDeArchivosDelRepositorio(RFileManagerRequest fmreq);

    ResponseEntity<byte[]> obtenerArchivoDelRepositorio(RFileManagerRequest fmreq);
}
