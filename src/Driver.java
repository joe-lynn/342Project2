import akka.actor.*;
import akka.remote.EndpointManager;

import java.util.ArrayList;

public class Driver{
    private static final int LINE_COUNT = 3;
    private static final int PASSENGERS = 10;

    private static final ArrayList<ScanQueue> Queues = new ArrayList<>();
    private static final ArrayList<Passenger> Passengers = new ArrayList<>();

    public static void main(String[] args){


        final ActorSystem system = ActorSystem.create();
        //Initialize system to create ActorRef
        final ActorRef Jail =  system.actorOf(Props.create(Jail.class, LINE_COUNT), "Jail");
        //Create the jail ActorRef
        for (int i = 0; i < LINE_COUNT; i += 1){
            //Create each queue
            //TODO Disabled for compile reasons.
            //Queues.add(i, new ScanQueue(i, Jail, system));
        }
        final ActorRef DCheck = system.actorOf(Props.create(DocumentCheck.class, Queues), "DCheck");

        for (int i = 0; i < PASSENGERS; i+= 1){
            Passengers.add(new Passenger(Integer.toString(i)));
        }

        for(int i = 0; i < PASSENGERS; i += 1){
            DCheck.tell(Passengers.get(i), null);
            //print statement
            System.out.println(Passengers.get(i).getName() + " is being sent to the Document Check.");
        }

    }

}
