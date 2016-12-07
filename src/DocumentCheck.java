

import akka.actor.UntypedActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.List;

/**
 * The Actor for the Body Scanner.
 * Code is pretty much the same as the Baggage Scanner.
 */
public class DocumentCheck extends UntypedActor {

//for compile reasons
  public void onReceive(Object o) {}

/*
* Currently commenting out code.
* Your code doesn't match the design for the queue system so we're going to need
* to rewurite some of it.
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
        //Print arrival statement
        System.out.println(passenger.getName() + " has arrived at the Document Check.");
        if(Math.random()*5 < 4){
            //Print pass statement
            System.out.println(passenger.getName() + " has passed the Document Check.");
            lastQueue += 1;
            if (lastQueue == queueList.size()) {
                lastQueue = 0;
            }
            //Grab actor refs to reduce code size
            ActorRef bagScan = queueList.get(lastQueue).getBagScanner();
            ActorRef bodyScan = queueList.get(lastQueue).getBodyScanner();

            //Send the passenger to the bagScan
            bagScan.tell(passenger, getSelf());
            //Print a statement
            System.out.println(passenger.getName() + " is being sent from " + getSelf().path().name() + " to " +
                    bagScan.path().name());

            //Send the passenger to the bodyScan
            bodyScan.tell(passenger, getSelf());
            //Print a statement
            System.out.println(passenger.getName() + " is being sent from " + getSelf().path().name() + " to " +
                    bodyScan.path().name());

        }
        else{
            //print fail statement
            System.out.println(passenger.getName() + " has failed the Document Check.");
        }

    }
    @Override
    public String toString(){
        return "Document Check";
    }
    */
}
