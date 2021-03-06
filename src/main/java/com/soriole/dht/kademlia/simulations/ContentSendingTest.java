package com.soriole.dht.kademlia.simulations;

import java.io.IOException;
import java.util.UUID;
import com.soriole.dht.kademlia.GetParameter;
import com.soriole.dht.kademlia.JKademliaNode;
import com.soriole.dht.kademlia.KademliaStorageEntry;
import com.soriole.dht.kademlia.exceptions.ContentNotFoundException;
import com.soriole.dht.kademlia.node.KademliaId;

/**
 * Testing sending and receiving content between 2 Nodes on a network
 *
 * @author Joshua Kissoon
 * @since 20140224
 */
public class ContentSendingTest
{

    public static void main(String[] args)
    {
        try
        {
            /* Setting up 2 Kad networks */
            JKademliaNode kad1 = new JKademliaNode("JoshuaK", new KademliaId("ASF45678947584567467"), 7574);
            System.out.println("Created Node Kad 1: " + kad1.getLocalNode().getNodeId());
            JKademliaNode kad2 = new JKademliaNode("Crystal", new KademliaId("ASERTKJDHGVHERJHGFLK"), 7572);
            System.out.println("Created Node Kad 2: " + kad2.getLocalNode().getNodeId());
            kad2.bootstrap(kad1.getLocalNode());

            /**
             * Lets create the content and share it
             */
            String data = "";
            for (int i = 0; i < 500; i++)
            {
                data += UUID.randomUUID();
            }
            System.out.println(data);
            DHTContentImpl c = new DHTContentImpl(kad2.getOwnerId(), data);
            kad2.put(c);

            /**
             * Lets retrieve the content
             */
            System.out.println("Retrieving Content");
            GetParameter gp = new GetParameter(c.getKey(), DHTContentImpl.TYPE);
            gp.setOwnerId(c.getOwnerId());
            System.out.println("Get Parameter: " + gp);
            KademliaStorageEntry conte = kad2.get(gp);
            System.out.println("Content Found: " + new DHTContentImpl().fromSerializedForm(conte.getContent()));
            System.out.println("Content Metadata: " + conte.getContentMetadata());

        }
        catch (IOException | ContentNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
