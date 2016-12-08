

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
  private int duty;

  //Saves us from needing to recreate the message every time
  private final BagReady READY = new BagReady();

  public BaggageScanner(int line, ActorRef securityStation) {
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
    System.out.println("Bag Scan has received kill command");
    System.out.println("Sending kill command to security station");
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
