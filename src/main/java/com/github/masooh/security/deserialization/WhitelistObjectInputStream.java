package com.github.masooh.security.deserialization;

import java.io.*;
import java.util.Arrays;
import java.util.List;

class WhitelistObjectInputStream extends ObjectInputStream {
    private final List<String> allowedTypes;

    public WhitelistObjectInputStream(InputStream in, String... allowedTypes) throws IOException {
        super(in);
        assert allowedTypes.length > 0 : "At least one type must be allowed";
        this.allowedTypes = Arrays.asList(allowedTypes);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass cls) throws IOException, ClassNotFoundException {
        String className = cls.getName();
        if (!allowedTypes.contains(className)) {
            throw new InvalidClassException("Unexpected serialized class", className);
        }
        return super.resolveClass(cls);
    }
}