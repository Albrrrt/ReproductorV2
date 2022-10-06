package com.albert.reproductorv2;

public class ArchivosMusica {
    private String path;
    private String titulo;
    private String artista;
    private String album;
    private String duracion;
    private String id;


    public ArchivosMusica(String path, String titulo, String artista, String album, String duracion,String id) {
        this.path = path;
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.duracion = duracion;
        this.id=id;
    }

    public String getPath() {
        return path;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuracion() {
        return duracion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
