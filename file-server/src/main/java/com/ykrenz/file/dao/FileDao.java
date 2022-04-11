package com.ykrenz.file.dao;

public interface FileDao {

    String save(FileModel fileModel);

    FileModel getById(String id);

    FileModel getOneByHash(String hash);
}
