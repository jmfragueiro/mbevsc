package ar.com.mbe.aperos.fileman;

import org.springframework.web.multipart.MultipartFile;

public record RFileManagerRequest(MultipartFile file,
                                  String tipo,
                                  EFilemanagerSubtipo subtipo,
                                  String filename,
                                  String subcarpeta,
                                  boolean opsubcarpeta,
                                  Long id,
                                  String extra) {}
