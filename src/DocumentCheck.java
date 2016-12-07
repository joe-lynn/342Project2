

import akka.actor.UntypedActor;
import akka.actor.ActorRef;

import java.util.ArrayList;
import java.util.List;

public class DocumentCheck extends UntypedActor {

    private final ArrayList<ActorRef> queueList; ;
    private int lastQueue = -1;


    public DocumentCheck(ArrayList<ActorRef> queues) {
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

            queueList.get(lastQueue).tell(passenger, getSelf());

            System.out.println(passenger.getName() + " is being sent from " + getSelf().path().name() + " to " +
                    queueList.get(lastQueue).path().name());
        }
        else{
            //print fail statement
            System.out.println(passenger.getName() + " has failed the Document Check.");
        }

    }
}
