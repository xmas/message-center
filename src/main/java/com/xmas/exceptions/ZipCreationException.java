package com.xmas.exceptions;

public class ZipCreationException extends NotFoundException {
    public ZipCreationException(Throwable cause) {
        super("Can't create pushPackagesZip.", cause);
    }

}
