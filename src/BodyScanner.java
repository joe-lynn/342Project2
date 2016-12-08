

import akka.actor.UntypedActor;
import akka.actor.ActorRef;

/**
* The Actor for the Body Scanner.
* Code is pretty much the same as the Baggage Scanner.
*/
public class BodyScanner extends UntypedActor {

  private final int lineNumber;
  private final ActorRef station;
  private int duty;

  //Saves us from needing to recreate the message every time
  private final BodyReady READY = new BodyReady();

  public BodyScanner(int line, ActorRef securityStation) {
    lineNumber = line;
    station = securityStation;
    duty = Driver.PASSENGERS/Driver.LINE_COUNT;
    if (Driver.PASSENGERS%Driver.LINE_COUNT > lineNumber){
      duty += 1;
    }
  }

  public void onReceive(Object message) {
    if(message instanceof Passenger) {
      onReceive((Passenger)message);
    } else if(message instanceof StopMessage){
      onReceive((StopMessage)message);
    }
  }

  private void onReceive(StopMessage killCommand){
    System.out.println(getSelf().path().name()+ " has received kill command.");
    System.out.println("Sending kill command to " + station.path().name() + " from " + self().path().name());
      station.tell(killCommand, getSelf());
      this.getContext().stop(getSelf());
  }

  public void onReceive(Passenger passenger) {

    //Check whether the passenger passes the security check.
    ScanReport results = new ScanReport(passenger, (Math.random()*5 < 4));
    station.tell(results,getSelf());

    //Let the line know that the scanner is ready.
    getSender().tell(READY, getSelf());
    duty -= 1;
    if (duty == 0) {
      getSender().tell("BODYDONE", getSelf());
    }
  }
}
