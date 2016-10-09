package com.mauriciotogneri.fcs.util;

import com.google.gson.Gson;

public class JsonUtil
{
    private static final Gson GSON = new Gson();

    public static String json(Object object)
    {
        return GSON.toJson(object);
    }
}