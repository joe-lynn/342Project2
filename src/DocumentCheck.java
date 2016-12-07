

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
        System.out.println(passenger.getName() + " has arrived at the Document Check.");
        if(Math.random()*5 < 4){
            System.out.println(passenger.getName() + " has passed the Document Check.");
            lastQueue += 1;
            if (lastQueue == queueList.size()) {
                lastQueue = 0;
            }
            ActorRef bagScan = queueList.get(lastQueue).getBagScanner();
            ActorRef bodyScan = queueList.get(lastQueue).getBodyScanner();
            bagScan.tell(passenger, getSelf());
            System.out.println(passenger.getName() + " is being sent from " + getSelf().path().name() + " to " +
                    bagScan.path().name());
            bodyScan.tell(passenger, getSelf());
            System.out.println(passenger.getName() + " is being sent from " + getSelf().path().name() + " to " +
                    bodyScan.path().name());

        }
        else{
            System.out.println(passenger.getName() + " has failed the Document Check.");
        }

    }
    @Override
    public String toString(){
        return "Document Check";
    }
}
