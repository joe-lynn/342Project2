

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
    }
  }

  private void onReceive(Passenger passenger) {
    if(bodyReady) {
      bodyScan.tell(passenger, getSelf());
      bodyReady = false;
    } else {
      bodyQueue.add(passenger);
    }

    if(bagReady) {
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
