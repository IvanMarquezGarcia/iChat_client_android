package com.ivan.iChat.ichat_android.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class MessageObjectInputStream extends ObjectInputStream  {

    public MessageObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws ClassNotFoundException, IOException {
        //return super.resolveClass(desc);
        return Mensaje.class;
    }
}
