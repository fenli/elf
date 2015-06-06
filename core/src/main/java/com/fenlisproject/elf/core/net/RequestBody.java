package com.fenlisproject.elf.core.net;

import java.io.IOException;

public interface RequestBody {

    void write(HttpRequest request) throws IOException;
}
