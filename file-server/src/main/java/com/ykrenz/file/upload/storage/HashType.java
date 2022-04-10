package com.ykrenz.file.upload.storage;

public enum HashType {
    CRC32("crc32"),
    CRC64("crc64"),
    MD5("md5");

    String value;

    HashType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
