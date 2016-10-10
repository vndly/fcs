package com.mauriciotogneri.fcs.network;

import com.google.firebase.database.DatabaseReference;

public class NodeWriter
{
    private final DatabaseReference nodeRef;
    private boolean open = true;

    public NodeWriter(DatabaseReference nodeRef)
    {
        this.nodeRef = nodeRef;
    }

    public void write(long timestamp, Object data)
    {
        if (open)
        {
            DatabaseReference ref = nodeRef.child(String.valueOf(timestamp));
            ref.setValue(data);
        }
    }

    public void stop()
    {
        open = false;
    }
}