package com.ykrenz.file.upload.manager;

import lombok.Data;

@Data
public class ListUploadParam {

    private int page = 1;

    private int limit = 500;
}
