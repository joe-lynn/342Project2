

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
    }
    else if(message instanceof StopMessage){
      onReceive((StopMessage)message);
    }
  }

  private void onReceive(StopMessage killCommand){
    station.tell(killCommand, getSelf());
    System.out.println(getSelf().path().name()+ " has shut down.");
    this.getContext().stop(getSelf());
  }

  public void onReceive(Passenger passenger) {
    duty -= 1;
    System.out.println(getSelf().path().name() + " has received " + passenger.getName()+"'s baggage from " + getSender().path().name());
    //Check whether the passenger passes the security check.
    boolean passed = (Math.random()*5 < 4);
    ScanReport results = new ScanReport(passenger, passed);
    station.tell(results,getSelf());

    System.out.println(getSelf().path().name() + " has scanned " + passenger.getName()+"'s baggage from " + getSender().path().name());
    //System.out.println(getSelf().path().name()+  " has " + duty);
    System.out.println(duty);
    if (duty == 0) {
      System.out.println(getSelf().path().name() + " is done processing passengers.");
      getSender().tell("BODYDONE", getSelf());
    }
    else{
      //Let the line know that the scanner is ready.
      getSender().tell(READY, getSelf());
    }
  }
}
