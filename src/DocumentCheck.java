

import akka.actor.UntypedActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.List;

/**
 * The Actor for the Body Scanner.
 * Code is pretty much the same as the Baggage Scanner.
 */
public class DocumentCheck extends UntypedActor {

    private final ArrayList<ScanQueue> queueList; ;
    private int lastQueue = -1;


    public DocumentCheck(ArrayList<ScanQueue> queues) {
        queueList = queues;
    }

    public void onReceive(Object message) {
        if(message instanceof Passenger) {
            onReceive((Passenger)message);
        }
    }

    public void onReceive(Passenger passenger) {

        if(Math.random()*5 < 4){
            lastQueue += 1;
            if (lastQueue == queueList.size()) {
                lastQueue = 0;
            }
            //This is a test to see if actors are successfully being passed.
            System.out.println(passenger.getName());
            queueList.get(lastQueue).getBagScanner().tell(passenger, getSelf());
            queueList.get(lastQueue).getBodyScanner().tell(passenger, getSelf());
        }
    }
}
