

import akka.actor.UntypedActor;
import akka.actor.ActorRef;

public class BodyScanner extends UntypedActor {

  private final int lineNumber;
  private final ActorRef station;

  public BodyScanner(int line, ActorRef securityStation) {
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
  }
}
