

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
    station.tell(killCommand, getSelf());
    System.out.println(getSelf().path().name()+ " has shut down.");
    this.getContext().stop(getSelf());
  }

  public void onReceive(Passenger passenger) {
    duty -= 1;
    System.out.println(getSelf().path().name() + " has received "+ passenger.getName() +" from " + getSender().path().name());
    //Check whether the passenger passes the security check.
    boolean passed = (Math.random()*5 < 4);
    if (passed){
      //System.out.println("Passenger "+passenger.getName() + " has passed his body scan.");
    }
    else{
      //System.out.println("Passenger "+passenger.getName() + " has failed his body scan");
    }
    ScanReport results = new ScanReport(passenger, passed);
    station.tell(results,getSelf());


    System.out.println(getSelf().path().name() + " has scanned " + passenger.getName()+"'s baggage from " + getSender().path().name());



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
