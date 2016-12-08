

import akka.actor.UntypedActor;
import akka.actor.ActorRef;

import java.util.Queue;
import java.util.LinkedList;

public class ScanQueue extends UntypedActor {

  private final int lineNumber;
      private final ActorRef bodyScan;
      private final ActorRef bagScan;

      private final Queue<Passenger> bagQueue;
      private final Queue<Passenger> bodyQueue;

      private boolean stoppable;
      private boolean bagReady;
      private boolean bodyReady;

  public ScanQueue(int line, ActorRef bodyScan, ActorRef bagScan, ActorRef securityStation) {
        lineNumber = line;
        this.bodyScan = bodyScan;
        this.bagScan = bagScan;

        bagQueue = new LinkedList<Passenger>();
        bodyQueue = new LinkedList<Passenger>();

        bagReady = true;
        bodyReady = true;
        stoppable = false;
      }

    public void onReceive(Object message) {
      if(message instanceof Passenger) {
        onReceive((Passenger)message);
      }

      if(message instanceof BagReady) {
        onReceive((BagReady)message);
      }

      if(message instanceof BodyReady) {
        onReceive((BodyReady)message);
      }
      if (message.equals("BAGDONE")){
        if(stoppable){
          bodyScan.tell(new StopMessage(), self());
          bagScan.tell(new StopMessage(), self());
          System.out.println(getSelf().path().name() + " has shut off.");

          getContext().stop(getSelf());
        }
        else{
          stoppable = true;
        }
      }
      if (message.equals("BODYDONE")){
        if(stoppable){
          bodyScan.tell(new StopMessage(), self());
          bagScan.tell(new StopMessage(), self());
          System.out.println(getSelf().path().name() + " has shut off.");
          getContext().stop(getSelf());
        }
        else{
          stoppable = true;
        }
      }
    }


  private void onReceive(Passenger passenger) {
    System.out.println("Passenger " + passenger.getName() + " has arrived at " + getSelf().path().name());


    if(bodyReady) {
      System.out.println("Passenger " + passenger.getName() + " is being sent to " + bodyScan.path().name());
      bodyScan.tell(passenger, getSelf());
      bodyReady = false;
    } else {
      bodyQueue.add(passenger);
    }

    if(bagReady) {
      System.out.println("Passenger " + passenger.getName() + "'s bag is being sent to " + bagScan.path().name());
      bagScan.tell(passenger, getSelf());
      bagReady = false;
    } else {
      bagQueue.add(passenger);
    }

  }

  private void onReceive(BagReady ready) {
    if(bagQueue.peek() != null) {
      bagScan.tell(bagQueue.poll(), getSelf());
      bagReady = false;
    } else {
      bagReady = true;
    }
  }

  private void onReceive(BodyReady ready) {
    if(bodyQueue.peek() != null) {
      bodyScan.tell(bodyQueue.poll(), getSelf());
      bodyReady = false;
    } else {
      bodyReady = true;
    }


  }
}
