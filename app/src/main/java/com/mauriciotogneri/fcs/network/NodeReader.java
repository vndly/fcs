package com.mauriciotogneri.fcs.network;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class NodeReader<T>
{
    private final DatabaseReference nodeRef;
    private ChildEventListener childEventListener;

    public NodeReader(DatabaseReference nodeRef)
    {
        this.nodeRef = nodeRef;
    }

    public void read(final Class<T> clazz, final OnNewData<T> listener)
    {
        childEventListener = new ChildEventListener()
        {
            @Override
            @SuppressWarnings("unchecked")
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                if (s != null)
                {
                    long timestamp = Long.parseLong(dataSnapshot.getKey());

                    listener.onNewData(timestamp, dataSnapshot.getValue(clazz));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        };

        nodeRef.addChildEventListener(childEventListener);
    }

    public void close()
    {
        nodeRef.removeEventListener(childEventListener);
    }

    public interface OnNewData<T>
    {
        void onNewData(long timestamp, T entry);
    }
}