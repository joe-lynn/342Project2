

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
        } else if(message instanceof StopMessage){
            onReceive((StopMessage)message);
        }
    }

    private void onReceive(StopMessage killCommand){
        System.out.println(getSelf().path().name()+ " has received kill command");

        //for(ActorRef ref : queueList){
        //    System.out.println("Sending kill command to " + ref.path().name());
        //ref.tell(killCommand, getSelf());
        //}

        this.getContext().stop(getSelf());
        System.out.println(getSelf().path().name() +" has shut off.");
    }


    private void onReceive(Passenger passenger) {
        //Print arrival statement
        System.out.println("Passenger " + passenger.getName() + " has arrived at the Document Check.");
        if(Math.random()*5 < 4){
            //Print pass statement
            System.out.println("Passenger " + passenger.getName() + " has passed the Document Check.");
            lastQueue += 1;
            if (lastQueue == queueList.size()) {
                lastQueue = 0;
            }

            queueList.get(lastQueue).tell(passenger, getSelf());

            System.out.println("Passenger " + passenger.getName() + " is being sent from " + getSelf().path().name() + " to " +
                    queueList.get(lastQueue).path().name());
        }
        else{
            //print fail statement
            System.out.println("Passenger " + passenger.getName() + " has failed the Document Check.");
        }

    }
}
