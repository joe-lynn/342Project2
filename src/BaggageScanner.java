

import akka.actor.UntypedActor;
import akka.actor.ActorRef;

/**
* The Actor for the Baggage Scanner. 
* Code is essentially the same as the Body Scanner so they could be the same
* class but aren't for modeling purposes.
*/
public class BaggageScanner extends UntypedActor {

  private final int lineNumber;
  private final ActorRef station;

  //Saves us from needing to recreate the message every time
  private final BagReady READY = new BagReady();

  public BaggageScanner(int line, ActorRef securityStation) {
    lineNumber = line;
    station = securityStation;
  }

  public void onReceive(Object message) {
    if(message instanceof Passenger) {
      onReceive((Passenger)message);
    }
  }

  public void onReceive(Passenger passenger) {
    //Check whether the passenger passes the security check.
    ScanReport results = new ScanReport(passenger, (Math.random()*5 < 1));
    station.tell(results,getSelf());

    //Let the line know that the scanner is ready.
    getSender().tell(READY, getSelf());
  }
}
