package com.mauriciotogneri.fcs.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SensorLog
{
    private final BufferedWriter bufferedWriter;

    public SensorLog(File file) throws IOException
    {
        if (file.createNewFile())
        {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        }
        else
        {
            throw new IOException();
        }
    }

    public void log(Object... values)
    {
        try
        {
            StringBuilder builder = new StringBuilder();

            for (Object value : values)
            {
                if (builder.length() != 0)
                {
                    builder.append(",");
                }

                builder.append(value.toString());
            }

            builder.append("\n");

            bufferedWriter.write(builder.toString());
            bufferedWriter.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try
        {
            bufferedWriter.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}