package com.xmas.exceptions;

import com.xmas.exceptions.NotFoundException;

public class ZipCreationException extends NotFoundException {
    public ZipCreationException(Throwable cause) {
        super("Can't create pushPackagesZip.", cause);
    }

}
