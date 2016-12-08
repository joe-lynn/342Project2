

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

  private boolean bagReady;
  private boolean bodyReady;
  private boolean awaitingTermination = false;

  public ScanQueue(int line, ActorRef bodyScan, ActorRef bagScan) {
    lineNumber = line;
    this.bodyScan = bodyScan;
    this.bagScan = bagScan;

    bagQueue = new LinkedList<Passenger>();
    bodyQueue = new LinkedList<Passenger>();

    bagReady = true;
    bodyReady = true;
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
    } else if(message instanceof StopMessage){
      onReceive((StopMessage)message);
    }
  }

  private void onReceive(StopMessage killCommand){
    System.out.println("Queue has received kill command");
    awaitingTermination = true;
    attemptTermination();
  }

  private void onReceive(Passenger passenger) {
    System.out.println("Passenger " + passenger.getName() + " has arrived at the Queue.");
    if(bodyReady) {
      System.out.println("Passenger " + passenger.getName() + " is being sent to body scan");
      bodyScan.tell(passenger, getSelf());
      bodyReady = false;
    } else {
      bodyQueue.add(passenger);
    }

    if(bagReady) {
      System.out.println("Passenger " + passenger.getName() + " bag is being sent to bag scan");
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

    if(awaitingTermination) {
      attemptTermination();
    }
  }

  private void onReceive(BodyReady ready) {
    if(bodyQueue.peek() != null) {
      bodyScan.tell(bodyQueue.poll(), getSelf());
      bodyReady = false;
    } else {
      bodyReady = true;
    }

    if(awaitingTermination) {
      attemptTermination();
    }
  }

  private void attemptTermination() {
    if(bagReady && bodyReady && bagQueue.isEmpty() && bodyQueue.isEmpty()) { 
      System.out.println("Sending kill command to body scan");
      bodyScan.tell(new StopMessage(), getSelf());
      System.out.println("Sending kill command to bag scan");
      bagScan.tell(new StopMessage(), getSelf());
      this.getContext().stop(getSelf());
    }
  }
}
