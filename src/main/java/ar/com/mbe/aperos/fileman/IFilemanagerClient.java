package ar.com.mbe.aperos.fileman;

public interface IFilemanagerClient {
    boolean isFmClientForObject(String tipo, EFilemanagerSubtipo subtipo, Long objectId);

    FilemanagerResponse preaction(EFilemanagerAction action, RFileManagerRequest fmrewq);

    void posaction(EFilemanagerAction action, RFileManagerRequest fmrewq, FilemanagerResponse response);
}
